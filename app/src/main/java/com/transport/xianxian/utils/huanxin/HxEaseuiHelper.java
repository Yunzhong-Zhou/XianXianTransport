package com.transport.xianxian.utils.huanxin;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.text.TextUtils;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.transport.xianxian.utils.LocalUserInfo;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.transport.xianxian.net.OkHttpClientManager.HOST;

/**
 * Created by Administrator on 2017/1/12.
 */

public class HxEaseuiHelper {

    private static HxEaseuiHelper instance = null;
    protected EMMessageListener messageListener = null;
    private Context appContext;
    private String username;
    private EaseUI easeUI;
    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;
    private Map<String, EaseUser> contactList;
    private Map<String, RobotUser> robotList;
    private DemoModel demoModel = null;

    private String TAG="HxEaseuiHelper";


    Context mContext;
    // 记录是否已经初始化
    private boolean isInit = false;

    public synchronized static HxEaseuiHelper getInstance() {
        if (instance == null) {
            instance = new HxEaseuiHelper();
        }
        return instance;
    }


    public void init(Context context) {
        mContext = context;
        demoModel = new DemoModel(context);

        // 获取当前进程 id 并取得进程名
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(context,pid);
        /**
         * 如果app启用了远程的service，此application:onCreate会被调用2次
         * 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
         * 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回*/

        if (processAppName == null || !processAppName.equalsIgnoreCase(context.getPackageName())) {
            // 则此application的onCreate 是被service 调用的，直接返回
            return;
        }
        if (isInit) {
            return;
        }
        /**
         * SDK初始化的一些配置
         * 关于 EMOptions 可以参考官方的 API 文档
         * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1chat_1_1_e_m_options.html*/

        EMOptions options = new EMOptions();
        // 设置Appkey，如果配置文件已经配置，这里可以不用设置
        // options.setAppKey("lzan13#hxsdkdemo");
        // 设置自动登录
        options.setAutoLogin(true);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执，TODO 这个暂时有bug，上层收不到发送回执
        options.setRequireDeliveryAck(true);
        // 设置是否需要服务器收到消息确认
//        options.setRequireServerAck(true);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(false);
        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
        options.setDeleteMessagesAsExitGroup(false);
        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
        options.allowChatroomOwnerLeave(true);


        //use default options if options is null
        if (EaseUI.getInstance().init(context, options)) {
            appContext = context;

            //获取easeui实例
            easeUI = EaseUI.getInstance();
            //初始化easeui
            easeUI.init(appContext,options);
            //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
            EMClient.getInstance().setDebugMode(true);
            setEaseUIProviders();
            //设置全局监听
            setGlobalListeners();

//            broadcastManager = LocalBroadcastManager.getInstance(appContext);
            initDbDao();
        }
    }
    /**
     * 根据Pid获取当前进程的名字，一般就是当前app的包名
     *
     * @param pid 进程的id
     * @return 返回进程的名字
     */
    private String getAppName(Context context,int pid) {
        String processName = null;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List list = activityManager.getRunningAppProcesses();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    // 根据进程的信息获取当前进程的名字
                    processName = info.processName;
                    // 返回当前进程名
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 没有匹配的项，返回为null
        return null;
    }

    protected void setEaseUIProviders() {
        // set profile provider if you want easeUI to handle avatar and nickname
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {

            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });
    }

    private EaseUser getUserInfo(String username){
        //获取 EaseUser实例, 这里从内存中读取
        //如果你是从服务器中读读取到的，最好在本地进行缓存
        EaseUser user = null;
        //如果用户是本人，就设置自己的头像
        if(username.equals(EMClient.getInstance().getCurrentUser())){
            user=new EaseUser(username);
            user.setAvatar((String) SharedPreferencesUtils.getParam(appContext, APPConfig.USER_HEAD_IMG,
                    HOST+ LocalUserInfo.getInstance(mContext).getUserImage()));
            user.setNickname((String)SharedPreferencesUtils.getParam(appContext, APPConfig.USER_NAME,
                    LocalUserInfo.getInstance(mContext).getNickname()));
            return user;
        }
//        if (user==null && getRobotList()!=null){
//            user=getRobotList().get(username);
//        }

        //收到别人的消息，设置别人的头像
        if (contactList!=null && contactList.containsKey(username)){
            user=contactList.get(username);
        }else { //如果内存中没有，则将本地数据库中的取出到内存中
            contactList=getContactList();
            user=contactList.get(username);
        }

        //如果用户不是你的联系人，则进行初始化
        if(user == null){
            user = new EaseUser(username);
            EaseCommonUtils.setUserInitialLetter(user);
        }else {
            if (TextUtils.isEmpty(user.getNickname())){//如果名字为空，则显示环信号码
                user.setNickname(user.getUsername());
            }
        }
        //Log.i("zcb","头像："+user.getAvatar());

        return user;
    }

    private void initDbDao() {
        inviteMessgeDao = new InviteMessgeDao(appContext);
        userDao = new UserDao(appContext);
    }

    public Map<String, RobotUser> getRobotList() {
        if (isLoggedIn() && robotList == null) {
            robotList = demoModel.getRobotList();
        }
        return robotList;
    }

    /**
     * get current user's id
     */
    public String getCurrentUsernName(){
        if(username == null){
            username = (String)SharedPreferencesUtils.getParam(appContext, Constant.HX_CURRENT_USER_ID,"");
        }
        return username;
    }

    /**
     *获取所有的联系人信息
     *
     * @return
     */
    public Map<String, EaseUser> getContactList() {
        if (isLoggedIn() && contactList == null) {
            contactList = demoModel.getContactList();
        }

        // return a empty non-null object to avoid app crash
        if(contactList == null){
            return new Hashtable<String, EaseUser>();
        }

        return contactList;
    }
    /**
     * if ever logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    /**
     * set global listener
     */
    protected void setGlobalListeners(){
        registerMessageListener();
    }

    /**
     * Global listener
     * If this event already handled by an activity, you don't need handle it again
     * activityList.size() <= 0 means all activities already in background or not in Activity Stack
     */
    protected void registerMessageListener() {
        messageListener = new EMMessageListener() {
            private BroadcastReceiver broadCastReceiver = null;

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());

                    //接收并处理扩展消息
                    String userName=message.getStringAttribute(Constant.USER_NAME,"");
                    String userId=message.getStringAttribute(Constant.USER_ID,"");
                    String userPic=message.getStringAttribute(Constant.HEAD_IMAGE_URL,"");
                    String hxIdFrom=message.getFrom();
                    System.out.println("helper接收到的用户名："+userName+"helper接收到的id："+userId+"helper头像："+userPic);
                    EaseUser easeUser=new EaseUser(hxIdFrom);
                    easeUser.setAvatar(userPic);
                    easeUser.setNickname(userName);

                    //存入内存
                    getContactList();
                    contactList.put(hxIdFrom,easeUser);

                    //存入db
                    UserDao dao=new UserDao(appContext);
                    List<EaseUser> users=new ArrayList<EaseUser>();
                    users.add(easeUser);
                    dao.saveContactList(users);


                    // in background, do not refresh UI, notify it in notification bar
                    //设置本地消息推送通知
                    if(!easeUI.hasForegroundActivies()){

                        getNotifier().notify(message);
                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "receive command message");
                    //get message body
                    //end of red packet code
                    //获取扩展属性 此处省略
                    //maybe you need get extension of your message
                    //message.getStringAttribute("");
                }
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages) {

            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {

            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }
    public EaseNotifier getNotifier(){
        return easeUI.getNotifier();
    }

}
