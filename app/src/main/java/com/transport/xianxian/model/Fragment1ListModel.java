package com.transport.xianxian.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zyz on 2019-10-19.
 */
public class Fragment1ListModel implements Serializable {
    /**
     * id : 7
     * sn : T201910307
     * now_state : 2019-10-30 18:30:14
     * now_state_action : -73324
     * addr_list : [{"type":1,"number":1,"addr":"重庆市沙坪坝井口镇","addr_detail":"阳光家园27--13-1","name":"黄实名","mobile":"15823629473","lat":"29.643795","lng":"106.447151","mileage":"10000"},{"type":1,"number":2,"addr":"重庆南岸","addr_detail":"天福克拉","name":"徐小平","mobile":"15823232323","lat":"29.529205","lng":"106.56385","mileage":"10000"},{"type":1,"number":3,"addr":"重庆市渝中","addr_detail":"重庆天地","name":"黄市","mobile":"15834343","lat":"29.55033","lng":"106.508395","mileage":"10000"}]
     * tag : ["专车"]
     * remark :
     * created_at : 2019-10-30 18:30:14
     * price : 500
     */

    private String id;
    private String sn;
    private int use_type;
    private String use_type_text;
    private String now_state;
    private long now_state_action;
    private String remark;
    private String created_at;
    private String price;
    private List<AddrListBean> addr_list;
    private List<String> tag;

    public int getUse_type() {
        return use_type;
    }

    public void setUse_type(int use_type) {
        this.use_type = use_type;
    }

    public String getUse_type_text() {
        return use_type_text;
    }

    public void setUse_type_text(String use_type_text) {
        this.use_type_text = use_type_text;
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

    public String getNow_state() {
        return now_state;
    }

    public void setNow_state(String now_state) {
        this.now_state = now_state;
    }

    public long getNow_state_action() {
        return now_state_action;
    }

    public void setNow_state_action(long now_state_action) {
        this.now_state_action = now_state_action;
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

    public static class AddrListBean {
        /**
         * type : 1
         * number : 1
         * addr : 重庆市沙坪坝井口镇
         * addr_detail : 阳光家园27--13-1
         * name : 黄实名
         * mobile : 15823629473
         * lat : 29.643795
         * lng : 106.447151
         * mileage : 10000
         */

        private int type;
        private int number;
        private String addr;
        private String addr_detail;
        private String name;
        private String mobile;
        private String lat;
        private String lng;
        private String mileage;

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

        public String getMileage() {
            return mileage;
        }

        public void setMileage(String mileage) {
            this.mileage = mileage;
        }
    }
}
