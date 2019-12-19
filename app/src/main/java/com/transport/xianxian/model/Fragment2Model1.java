package com.transport.xianxian.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zyz on 2019-09-30.
 */
public class Fragment2Model1 implements Serializable {
    /**
     * send_time :
     * compare_time :
     * now_state :
     * now_state_action :
     * now_state_sub_action :
     * id : 5
     * sn : T201911045
     * status : 1
     * created_at : 2019-11-04 17:49
     * is_attach_fee : 2
     * user_type : 专车
     * send_name : 测试
     * send_mobile : 15823629481
     * send_head : /upload/head/2019-10-20/ff660c5fefd51b5a5b3d9e8ccb7304c6.png
     * industry : 餐饮
     * hx_username : 15823629481
     * addr_list : [{"type":1,"number":1,"addr":"重庆南岸","addr_detail":"天福克拉","name":"徐小平","mobile":"15823232323","lat":"29.529205","lng":"106.56385","arrive_time":1573013253,"leave_time":1573013386,"mileage":0,"pre_time":"","status":"2","status_text":"已接单 前往装货","other":"","is_show":2},{"type":2,"number":2,"addr":"重庆南岸","addr_detail":"天福克拉","name":"徐小平","mobile":"15823232323","lat":"29.529205","lng":"106.56385","arrive_time":0,"leave_time":0,"mileage":0,"pre_time":"","status":"6","status_text":"","other":"","is_show":2},{"type":2,"number":3,"addr":"重庆南岸","addr_detail":"天福克拉","name":"徐小平","mobile":"15823232323","lat":"29.529205","lng":"106.56385","arrive_time":0,"leave_time":0,"mileage":0,"pre_time":"","status":"5","status_text":"","other":"","is_show":2}]
     * tag : ["专车"]
     * goods_desc : ["冰鲜","总总量","体积5.5方"]
     * remark :
     * price : null
     * price_detail : [{"title":"里程费","price":"1206.00"},{"title":"加急费","price":null},{"title":"温层费","price":null},{"title":"货主附加费","price":null},{"title":"司机附加费","price":null},{"title":"其他费用","price":null}]
     */
    private String terminal_id;
    private String track_id;
    private Long take_time;
    private Long end_time;

    public Long getTake_time() {
        return take_time;
    }

    public void setTake_time(Long take_time) {
        this.take_time = take_time;
    }

    public Long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Long end_time) {
        this.end_time = end_time;
    }

    public String getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(String terminal_id) {
        this.terminal_id = terminal_id;
    }

    public String getTrack_id() {
        return track_id;
    }

    public void setTrack_id(String track_id) {
        this.track_id = track_id;
    }

    private String send_time;
    private String compare_time;
    private String now_state;
    private String now_state_action;
    private String now_state_sub_action;
    private String id;
    private String sn;
    private int status;
    private String created_at;
    private int is_attach_fee;
    private String use_type;
    private int use_type_id;
    private String send_name;
    private String send_mobile;
    private String send_head;
    private String industry;
    private String hx_username;
    private String remark;
    private String price;
    private List<AddrListBean> addr_list;
    private List<String> tag;
    private List<String> goods_desc;
    private List<PriceDetailBean> price_detail;

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getCompare_time() {
        return compare_time;
    }

    public void setCompare_time(String compare_time) {
        this.compare_time = compare_time;
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

    public String getNow_state_sub_action() {
        return now_state_sub_action;
    }

    public void setNow_state_sub_action(String now_state_sub_action) {
        this.now_state_sub_action = now_state_sub_action;
    }

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getIs_attach_fee() {
        return is_attach_fee;
    }

    public void setIs_attach_fee(int is_attach_fee) {
        this.is_attach_fee = is_attach_fee;
    }

    public String getUse_type() {
        return use_type;
    }

    public void setUse_type(String use_type) {
        this.use_type = use_type;
    }

    public int getUse_type_id() {
        return use_type_id;
    }

    public void setUse_type_id(int use_type_id) {
        this.use_type_id = use_type_id;
    }

    public String getSend_name() {
        return send_name;
    }

    public void setSend_name(String send_name) {
        this.send_name = send_name;
    }

    public String getSend_mobile() {
        return send_mobile;
    }

    public void setSend_mobile(String send_mobile) {
        this.send_mobile = send_mobile;
    }

    public String getSend_head() {
        return send_head;
    }

    public void setSend_head(String send_head) {
        this.send_head = send_head;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getHx_username() {
        return hx_username;
    }

    public void setHx_username(String hx_username) {
        this.hx_username = hx_username;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<AddrListBean> getAddr_list() {
        return addr_list;
    }

    public void setAddr_list(List<AddrListBean> addr_list) {
        this.addr_list = addr_list;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public List<String> getGoods_desc() {
        return goods_desc;
    }

    public void setGoods_desc(List<String> goods_desc) {
        this.goods_desc = goods_desc;
    }

    public List<PriceDetailBean> getPrice_detail() {
        return price_detail;
    }

    public void setPrice_detail(List<PriceDetailBean> price_detail) {
        this.price_detail = price_detail;
    }

    public static class AddrListBean implements Serializable{
        /**
         * type : 1
         * number : 1
         * addr : 重庆南岸
         * addr_detail : 天福克拉
         * name : 徐小平
         * mobile : 15823232323
         * lat : 29.529205
         * lng : 106.56385
         * arrive_time : 1573013253
         * leave_time : 1573013386
         * mileage : 0
         * pre_time :
         * status : 2
         * status_text : 已接单 前往装货
         * other :
         * is_show : 2
         */

        private int type;
        private int number;
        private String addr;
        private String addr_detail;
        private String name;
        private String mobile;
        private String lat;
        private String lng;
        private String arrive_time;
        private String leave_time;
        private int mileage;
        private String pre_time;
        private String status;
        private String status_text;
        private String other;
        private int is_show;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getAddr_detail() {
            return addr_detail;
        }

        public void setAddr_detail(String addr_detail) {
            this.addr_detail = addr_detail;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
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

        public String getArrive_time() {
            return arrive_time;
        }

        public void setArrive_time(String arrive_time) {
            this.arrive_time = arrive_time;
        }

        public String getLeave_time() {
            return leave_time;
        }

        public void setLeave_time(String leave_time) {
            this.leave_time = leave_time;
        }

        public int getMileage() {
            return mileage;
        }

        public void setMileage(int mileage) {
            this.mileage = mileage;
        }

        public String getPre_time() {
            return pre_time;
        }

        public void setPre_time(String pre_time) {
            this.pre_time = pre_time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus_text() {
            return status_text;
        }

        public void setStatus_text(String status_text) {
            this.status_text = status_text;
        }

        public String getOther() {
            return other;
        }

        public void setOther(String other) {
            this.other = other;
        }

        public int getIs_show() {
            return is_show;
        }

        public void setIs_show(int is_show) {
            this.is_show = is_show;
        }
    }

    public static class PriceDetailBean implements Serializable{
        /**
         * title : 里程费
         * price : 1206.00
         */

        private String title;
        private String price;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
