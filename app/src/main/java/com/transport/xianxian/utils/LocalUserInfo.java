package com.transport.xianxian.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;


public class LocalUserInfo {
    /**
     * 保存Preference的name
     */
    public static final String PREFERENCE_NAME = "local_userinfo";
    public static final String USERPHOTO_FILENAME = "userphotoname";//头像图片文件名

    public static final String Time= "time";//时间戳
    public static final String Token = "token";//token
    public static final String UserType = "userType";//用户类型
    public static final String Version = "version";//版本号
    public static final String USERNAME = "userName";//用户名
    public static final String NICKNAME = "nickname";//昵称
    public static final String USERID = "userID";//用户id
    public static final String USERJOB = "userJob";//用户职位
    public static final String BELONGID = "belong_id";//所属id
    public static final String USERIMAGE= "userImage";//用户头像
    public static final String Language_Type= "language_type";//语言
    public static final String Mobile_State_Code= "mobile_state_code";//国家代码
    public static final String Country_IMG= "country_img";//国家代码
    public static final String Pay= "pay";//是否开通支付(1:否，2：是)
    public static final String Merchant= "merchant";//是否为商户(1:否，2：是)
    public static final String Gather= "gather";//是否开通收款(1:否，2：是)
    public static final String InvuteCode = "invite_code";//邀请码

    public static final String CITYNAME = "cityName";//选择的城市

    public static final String WALLETADDR = "wallet_addr";//钱包地址
    public static final String EMAIL = "email";//邮箱
    public static final String PHONENUMBER = "phoneNumber";//电话
    public static final String LEVEL = "level";//等级
    public static final String USERCAPTION = "userCaption";//用户简介
    public static final String ISREALNAMEVALIDATED = "isRealNameValidated";//用户是否实名验证
    public static final String USERREALNAME = "userRealName";//用户真实姓名

    public static final String HXID = "hx_id";//环信id
    public static final String WINNUM1 = "winNum1";//中奖信息
    public static final String LOSENUM = "loseNum";//未中奖信息
    public static final String LOSENUM1 = "loseNum1";//未中奖信息

    public static final String ISNEWCOMER = "isNewcomer";//是否为新用户

    private static SharedPreferences mSharedPreferences;
    private static LocalUserInfo localUserInfo;
    private static Editor editor;

    private LocalUserInfo(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
    }

    /**
     * 单例模式，获取instance实例
     *
     * @param cxt
     * @return
     */
    public static LocalUserInfo getInstance(Context cxt) {
        if (localUserInfo == null) {
            localUserInfo = new LocalUserInfo(cxt);
        }
        editor = mSharedPreferences.edit();
        return localUserInfo;
    }

    //设置时间戳
    public void setTime(String string) {
        editor.putString(Time, string);
        editor.commit();
    }
    //设置token
    public void setToken(String string) {
        editor.putString(Token, string);
        editor.commit();
    }
    //设置用户类型
    public void setUserType(String string) {
        editor.putString(UserType, string);
        editor.commit();
    }
    //设置版本号
    public void setVersion(String string) {
        editor.putString(Version, string);
        editor.commit();
    }
    //设置用户名
    public void setUserName(String string) {
        editor.putString(USERNAME, string);
        editor.commit();
    }
    //设置昵称
    public void setNickname(String string) {
        editor.putString(NICKNAME, string);
        editor.commit();
    }
    //设置用户id
    public void setUserId(String string) {
        editor.putString(USERID, string);
        editor.commit();
    }
    //设置用户职位
    public void setUserJob(String string) {
        editor.putString(USERJOB, string);
        editor.commit();
    }
    //设置用户所属id
    public void setBelongid(String string) {
        editor.putString(BELONGID, string);
        editor.commit();
    }
    //设置用户头像
    public void setUserImage(String string) {
        editor.putString(USERIMAGE, string);
        editor.commit();
    }
    //设置语言
    public void setLanguage_Type(String string) {
        editor.putString(Language_Type, string);
        editor.commit();
    }
    //设置国家图片
    public void setCountry_IMG(String string) {
        editor.putString(Country_IMG, string);
        editor.commit();
    }
    //是否开通支付
    public void setPay(String string) {
        editor.putString(Pay, string);
        editor.commit();
    }
    //是否开通收款
    public void setGather(String string) {
        editor.putString(Gather, string);
        editor.commit();
    }
    //是否为商户
    public void setMerchant(String string) {
        editor.putString(Merchant, string);
        editor.commit();
    }
    //设置本地存储的头像文件名
    public void setUserPhotoName(String photoName) {
        editor.putString(USERPHOTO_FILENAME, photoName);
        editor.commit();
    }
    //保存个人简介
    public void setUserCaption(String userCaption) {
        editor.putString(USERCAPTION, userCaption);
        editor.commit();
    }
    //保存手机号码
    public void setPhoneNumber(String phoneNumber) {
        editor.putString(PHONENUMBER, phoneNumber);
        editor.commit();
    }
    //保存邮箱
    public void setEmail(String email) {
        editor.putString(EMAIL, email);
        editor.commit();
    }
    //保存钱包地址
    public void setWalletaddr(String string) {
        editor.putString(WALLETADDR, string);
        editor.commit();
    }
    //保存邀请码
    public void setInvuteCode(String string) {
        editor.putString(InvuteCode, string);
        editor.commit();
    }
    //设置实名认证状态
    public void setIsVerified(String string) {
        editor.putString(ISREALNAMEVALIDATED, string);
        editor.commit();
    }
    //设置实名认证状态
    public void setMobile_State_Code(String string) {
        editor.putString(Mobile_State_Code, string);
        editor.commit();
    }
    //设置是否为新人
    public void setIsnewcomer(String string) {
        editor.putString(ISNEWCOMER, string);
        editor.commit();
    }
    //保存城市
    public void setCityname(String string) {
        editor.putString(CITYNAME, string);
        editor.commit();
    }

    //保存环信ID
    public void setHxid(String string) {
        editor.putString(HXID, string);
        editor.commit();
    }
    //保存中奖信息
    public void setWinnum1(String string) {
        editor.putString(WINNUM1, string);
        editor.commit();
    }
    //保存未中奖信息
    public void setLosenum(String string) {
        editor.putString(LOSENUM, string);
        editor.commit();
    }
    //保存未中奖信息
    public void setLosenum1(String string) {
        editor.putString(LOSENUM1, string);
        editor.commit();
    }

    public String getUserPhotoName() {
        return mSharedPreferences.getString(USERPHOTO_FILENAME, "");
    }

//	public void setUserInfo(String str_name, String str_value) {
//        editor.putString(str_name, str_value);
//        editor.commit();
//    }

    public void deleteUserInfo() {
        editor.clear();
        editor.commit();
    }

    public String getUserInfo(String str_name) {
        return mSharedPreferences.getString(str_name, "");
    }

    public boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

    /**
     * 保存键为key的值为vlaue
     *
     * @param key
     * @param vlaue
     */
    public void put(String key, int vlaue) {
        editor.putInt(key, vlaue);
        editor.commit();
    }

    /**
     * 保存键为key的值为vlaue
     *
     * @param key
     * @param vlaue
     */
    public void put(String key, String vlaue) {
        editor.putString(key, vlaue);
        editor.commit();
    }

    /**
     * 保存键为key的值为vlaue
     *
     * @param key
     * @param vlaue
     */
    public void put(String key, boolean vlaue) {
        editor.putBoolean(key, vlaue);
        editor.commit();
    }

    /**
     * 保存键为key的值为vlaue
     *
     * @param key
     * @param vlaue
     */
    public void put(String key, long vlaue) {
        editor.putLong(key, vlaue);
        editor.commit();
    }

    /**
     * 保存键为key的值为vlaue
     *
     * @param key
     * @param vlaue
     */
    public void put(String key, float vlaue) {
        editor.putFloat(key, vlaue);
        editor.commit();
    }

    /**
     * key对应的整型值叠加1
     *
     * @param key
     */
    public void superposition(String key) {
        int vlaue = mSharedPreferences.getInt(key, 0);
        Editor editor = mSharedPreferences.edit();
        vlaue++;
        editor.putFloat(key, vlaue);
        editor.commit();
    }

    public int getInt(String key, int defult) {
        return mSharedPreferences.getInt(key, defult);
    }

    public int getInt(String key) {
        return mSharedPreferences.getInt(key, 0);
    }

    public boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, true);
    }

    public boolean getBoolean(String key, boolean isTrue) {
        return mSharedPreferences.getBoolean(key, isTrue);
    }

    public String getString(String key) {
        return mSharedPreferences.getString(key, null);
    }

    public String getString(String key, String defult) {
        return mSharedPreferences.getString(key, defult);
    }

    public long getLong(String key, long defult) {
        return mSharedPreferences.getLong(key, defult);
    }

    public long getLong(String key) {
        return mSharedPreferences.getLong(key, 0);
    }

    public void remove(String... keys) {
        Editor editor = mSharedPreferences.edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.commit();
    }

	/**
	 * 保存用户信息
	 * @param
	 */
//	public void putUser(UserInfo userInfo) {
//        MyLogger.i(">>>>>>>>>>保存用户信息" + userInfo.toString());
        /*put(USERNAME, userInfo.getUserName());
		put(USERID, userInfo.getId());
        put(LEVEL,userInfo.getLevel()+"");
		put(EMAIL, userInfo.getEmail());
		put(PHONENUMBER, userInfo.getPhoneNumber());
        put(USERCAPTION,userInfo.getUserCaption()+"");
        put(ISREALNAMEVALIDATED,userInfo.getIsRealNameValidated()+"");
		put(USERREALNAME, userInfo.getUserRealName()+"");*/
//		put(Constant.INT_SHOPID, user.getShopId());//shopId
//		MyLogger.i("保存用户信息:"+Constant.INT_SHOPID);
//		put(Constant.PRE_LOGIN_USERNAME,
//				user.getPhoneNumber());
//		put(Constant.PRE_LOGIN_LASTTIME,
//				System.currentTimeMillis());
//		put(Constant.PRE_LOGIN_STATE, true);
//		put("payed", user.isPayed());

//	}

	/**
	 * 获取当前用户信息
	 * @return
	 */
//	public UserInfo getUser(){
//        if (!TextUtils.isEmpty(getString(USERNAME))) {
//            UserInfo info = new UserInfo(getInt(Constant.PRE_USER_ID, Integer.MIN_VALUE),
//                    getInt(Constant.PRE_USER_LOGINTYPE, Integer.MIN_VALUE),
//                    getString(Constant.PRE_USER_NICKNAME),
//                    getString(Constant.PRE_USER_PHONENUMBER),
//                    getString(USERPHOTO_FILENAME), getString(Constant.INT_SHOPID), getBoolean("payed", false));
//            return info;
//        }
//        return null;
//	}
    public String getTime(){
        String string=getString(Time);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "";
    }
    public String getToken(){
        String string=getString(Token);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "";
    }
    public String getUserType(){
        String string=getString(UserType);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "";
    }
    public String getVersion(){
        String string=getString(Version);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "";
    }
    public String getUserName(){
        String string=getString(USERNAME);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "";
    }
    public String getNickname(){
        String string=getString(NICKNAME);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "";
    }
    public String getWalletaddr(){
        String string=getString(WALLETADDR);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "";
    }
    public String getUserId(){
        String useid=getString(USERID);
        if (!TextUtils.isEmpty(useid)) {
            return useid;
        }
        return "";
    }
    public String getUserJob(){
        String string=getString(USERJOB);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "";
    }
    public String getBelongid(){
        String string=getString(BELONGID);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "";
    }
    public String getUserImage(){
        String string=getString(USERIMAGE);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "";
    }
    public String getCountry_IMG(){
        String string=getString(Country_IMG);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "";
    }
    public String getLanguage_Type(){
        String string=getString(Language_Type);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "zh";
    }
    public String getUserRealName(){
        String userRealName=getString(USERREALNAME);
        if (!TextUtils.isEmpty(userRealName)) {
            return userRealName;
        }
        return "";
    }
    public String getPhonenumber(){
        String phoneNumber=getString(PHONENUMBER);
        if (!TextUtils.isEmpty(phoneNumber)) {
            return phoneNumber;
        }
        return "";
    }
    public String getEmail(){
        String email=getString(EMAIL);
        if (!TextUtils.isEmpty(email)) {
            return email;
        }
        return "";
    }
    public String getInvuteCode(){
        String string=getString(InvuteCode);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "";
    }
    public String getlevel(){
        String level=getString(LEVEL);
        if (!TextUtils.isEmpty(level)&&!"null".equals(level)) {
            return level;
        }
        return "0";
    }
    public String getuserCaption(){
        String userCaption=getString(USERCAPTION);
        if (!TextUtils.isEmpty(userCaption)&&!"null".equals(userCaption)) {
            return userCaption;
        }
        return "无";
    }
    public String getIsVerified(){
        String isrealnamevalidated=getString(ISREALNAMEVALIDATED);
        if (!TextUtils.isEmpty(isrealnamevalidated)) {
            return isrealnamevalidated;
        }
        return "2";//1 认证 2 未认证
    }
    public String getMobile_State_Code(){
        String string=getString(Mobile_State_Code);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "86";
    }
    public String getPay(){
        String string=getString(Pay);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "1";
    }
    public String getGather(){
        String string=getString(Gather);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "1";
    }
    public String getMerchant(){
        String string=getString(Merchant);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "1";
    }
    //获取城市
    public String getCityname() {
        String string = getString(CITYNAME);
        if (!TextUtils.isEmpty(string) && !"null".equals(string)) {
            return string;
        }
        return "";
    }

    //获取环信ID
    public String getHxid() {
        String string = getString(HXID);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "";
    }
    //获取中奖信息
    public String getWinnum1() {
        String string = getString(WINNUM1);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "";
    }
    //获取未中奖信息
    public String getLosenum() {
        String string = getString(LOSENUM);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "";
    }
    //获取未中奖信息
    public String getLosenum1() {
        String string = getString(LOSENUM1);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "";
    }
    public String getIsnewcomer(){
        String string=getString(ISNEWCOMER);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return "";
    }


    public boolean isLogin(){
        return !TextUtils.isEmpty(getString(USERID));
    }
}
