package com.transport.xianxian.model;

import java.util.List;

/**
 * Created by zyz on 2019-09-30.
 */
public class Fragment1Model {
    /**
     * nickname : 15823629471
     * head :
     * mobile : 15823629471
     * online_time :
     * indent_count :
     * comment_score :
     * money :
     * is_work :
     * lat :
     * lng :
     * addr :
     * indent_use_type : 0
     * distance :
     * temperature :
     * time_start :
     * time_end :
     * indent_use_type_list : [{"key":1,"val":"专车"},{"key":2,"val":"顺风车"},{"key":3,"val":"快递"}]
     * distance_list : [{"key":1,"val":"不限"},{"key":2,"val":"市内"},{"key":3,"val":"长途"}]
     * temperature_list : [{"key":1,"val":"不限"},{"key":2,"val":"-18°到-2°"},{"key":3,"val":"+2°到-18°"},{"key":4,"val":"+15°到-20°"}]
     * time_start_list : [{"key":1,"val":"不限"},{"key":2,"val":"11:00"},{"key":3,"val":"12:00"},{"key":4,"val":"13:00"}]
     * time_end_list : [{"key":1,"val":"不限"},{"key":2,"val":"21:00"},{"key":3,"val":"22:00"}]
     * notice_list : [{"title":"消息","notice_id":1},{"title":"消息","notice_id":1}]
     * fresh_second : 100
     * wait_tindent : [{"id":"1","sn":"20190917","now_state":"8月8日 12：00 ","now_state_action":"装货","send_addr":"广东省创业元10-1","send_name":"法务科技","send_distance":"10KM","next_recive_addr":"重庆南坪","next_recive_name":"黄发","next_recive_distance":"2km","tag":[{"key":1,"val":"专车"},{"key":2,"val":"6吨"},{"key":3,"val":"恒温"},{"key":4,"val":"5.5方"}],"remark":"装车时间有限","created_at":"2019-10-17 15:00","price":"1800"}]
     */

    private String nickname;
    private String head;
    private String mobile;
    private Long online_time;
    private String indent_count;
    private String comment_score;
    private String money;
    private String today_money;
    private String is_work;
    private String lat;
    private String lng;
    private String addr;
    private String indent_use_type;
    private String distance;
    private String temperature;
    private String time_start;
    private String time_end;
    private int fresh_second;
    private List<IndentUseTypeListBean> indent_use_type_list;
    private List<DistanceListBean> distance_list;
    private List<TemperatureListBean> temperature_list;
    private List<TimeStartListBean> time_start_list;
    private List<TimeEndListBean> time_end_list;
    private List<NoticeListBean> notice_list;
    private List<WaitTindentBean> wait_tindent;

    public String getToday_money() {
        return today_money;
    }

    public void setToday_money(String today_money) {
        this.today_money = today_money;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getOnline_time() {
        return online_time;
    }

    public void setOnline_time(Long online_time) {
        this.online_time = online_time;
    }

    public String getIndent_count() {
        return indent_count;
    }

    public void setIndent_count(String indent_count) {
        this.indent_count = indent_count;
    }

    public String getComment_score() {
        return comment_score;
    }

    public void setComment_score(String comment_score) {
        this.comment_score = comment_score;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getIs_work() {
        return is_work;
    }

    public void setIs_work(String is_work) {
        this.is_work = is_work;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getIndent_use_type() {
        return indent_use_type;
    }

    public void setIndent_use_type(String indent_use_type) {
        this.indent_use_type = indent_use_type;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public int getFresh_second() {
        return fresh_second;
    }

    public void setFresh_second(int fresh_second) {
        this.fresh_second = fresh_second;
    }

    public List<IndentUseTypeListBean> getIndent_use_type_list() {
        return indent_use_type_list;
    }

    public void setIndent_use_type_list(List<IndentUseTypeListBean> indent_use_type_list) {
        this.indent_use_type_list = indent_use_type_list;
    }

    public List<DistanceListBean> getDistance_list() {
        return distance_list;
    }

    public void setDistance_list(List<DistanceListBean> distance_list) {
        this.distance_list = distance_list;
    }

    public List<TemperatureListBean> getTemperature_list() {
        return temperature_list;
    }

    public void setTemperature_list(List<TemperatureListBean> temperature_list) {
        this.temperature_list = temperature_list;
    }

    public List<TimeStartListBean> getTime_start_list() {
        return time_start_list;
    }

    public void setTime_start_list(List<TimeStartListBean> time_start_list) {
        this.time_start_list = time_start_list;
    }

    public List<TimeEndListBean> getTime_end_list() {
        return time_end_list;
    }

    public void setTime_end_list(List<TimeEndListBean> time_end_list) {
        this.time_end_list = time_end_list;
    }

    public List<NoticeListBean> getNotice_list() {
        return notice_list;
    }

    public void setNotice_list(List<NoticeListBean> notice_list) {
        this.notice_list = notice_list;
    }

    public List<WaitTindentBean> getWait_tindent() {
        return wait_tindent;
    }

    public void setWait_tindent(List<WaitTindentBean> wait_tindent) {
        this.wait_tindent = wait_tindent;
    }

    public static class IndentUseTypeListBean {
        /**
         * key : 1
         * val : 专车
         */

        private int key;
        private String val;

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }

    public static class DistanceListBean {
        /**
         * key : 1
         * val : 不限
         */

        private int key;
        private String val;

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }

    public static class TemperatureListBean {
        /**
         * key : 1
         * val : 不限
         */

        private int key;
        private String val;

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }

    public static class TimeStartListBean {
        /**
         * key : 1
         * val : 不限
         */

        private int key;
        private String val;

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }

    public static class TimeEndListBean {
        /**
         * key : 1
         * val : 不限
         */

        private int key;
        private String val;

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }

    public static class NoticeListBean {
        /**
         * title : 消息
         * notice_id : 1
         */

        private String title;
        private int notice_id;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getNotice_id() {
            return notice_id;
        }

        public void setNotice_id(int notice_id) {
            this.notice_id = notice_id;
        }
    }

    public static class WaitTindentBean {
        /**
         * id : 1
         * sn : 20190917
         * now_state : 8月8日 12：00
         * now_state_action : 装货
         * send_addr : 广东省创业元10-1
         * send_name : 法务科技
         * send_distance : 10KM
         * next_recive_addr : 重庆南坪
         * next_recive_name : 黄发
         * next_recive_distance : 2km
         * tag : [{"key":1,"val":"专车"},{"key":2,"val":"6吨"},{"key":3,"val":"恒温"},{"key":4,"val":"5.5方"}]
         * remark : 装车时间有限
         * created_at : 2019-10-17 15:00
         * price : 1800
         */

        private String id;
        private String sn;
        private String now_state;
        private String now_state_action;
        private String send_addr;
        private String send_name;
        private String send_distance;
        private String next_recive_addr;
        private String next_recive_name;
        private String next_recive_distance;
        private String remark;
        private String created_at;
        private String price;
        private List<TagBean> tag;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getNow_state() {
            return now_state;
        }

        public void setNow_state(String now_state) {
            this.now_state = now_state;
        }

        public String getNow_state_action() {
            return now_state_action;
        }

        public void setNow_state_action(String now_state_action) {
            this.now_state_action = now_state_action;
        }

        public String getSend_addr() {
            return send_addr;
        }

        public void setSend_addr(String send_addr) {
            this.send_addr = send_addr;
        }

        public String getSend_name() {
            return send_name;
        }

        public void setSend_name(String send_name) {
            this.send_name = send_name;
        }

        public String getSend_distance() {
            return send_distance;
        }

        public void setSend_distance(String send_distance) {
            this.send_distance = send_distance;
        }

        public String getNext_recive_addr() {
            return next_recive_addr;
        }

        public void setNext_recive_addr(String next_recive_addr) {
            this.next_recive_addr = next_recive_addr;
        }

        public String getNext_recive_name() {
            return next_recive_name;
        }

        public void setNext_recive_name(String next_recive_name) {
            this.next_recive_name = next_recive_name;
        }

        public String getNext_recive_distance() {
            return next_recive_distance;
        }

        public void setNext_recive_distance(String next_recive_distance) {
            this.next_recive_distance = next_recive_distance;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public List<TagBean> getTag() {
            return tag;
        }

        public void setTag(List<TagBean> tag) {
            this.tag = tag;
        }

        public static class TagBean {
            /**
             * key : 1
             * val : 专车
             */

            private int key;
            private String val;

            public int getKey() {
                return key;
            }

            public void setKey(int key) {
                this.key = key;
            }

            public String getVal() {
                return val;
            }

            public void setVal(String val) {
                this.val = val;
            }
        }
    }
}
