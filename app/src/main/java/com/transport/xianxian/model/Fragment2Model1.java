package com.transport.xianxian.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zyz on 2019-09-30.
 */
public class Fragment2Model1 implements Serializable {

    private List<TindentListBean> tindent_list;

    public List<TindentListBean> getTindent_list() {
        return tindent_list;
    }

    public void setTindent_list(List<TindentListBean> tindent_list) {
        this.tindent_list = tindent_list;
    }

    public static class TindentListBean implements Serializable {
        /**
         * id : 127
         * sn : T20200228127
         * created_at : 2020-02-28 15:43
         * use_type : 专车
         * use_type_id : 1
         * car_type : 面包车
         * status : 6
         * status_text : 转单
         * temperature : 冷藏 -0°到10°
         * remark :
         * price : 83
         * is_plan : 2
         * plan_time : 2020-02-28 15:43
         * addr_list : [{"addr":"重庆市沙坪坝区井口街道美丽·阳光家园H组团美丽·阳光家园"},{"addr":"重庆市沙坪坝区井口街道井源路2号美丽·阳光家园"}]
         */

        private String id;
        private String sn;
        private String created_at;
        private String use_type;
        private int use_type_id;
        private String car_type;
        private int status;
        private String status_text;
        private String temperature;
        private String remark;
        private String price;
        private int is_plan;
        private String plan_time;
        private List<AddrListBean> addr_list;
        private String terminal_id;
        private String track_id;
        private Long take_time;
        private Long end_time;

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

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
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

        public String getCar_type() {
            return car_type;
        }

        public void setCar_type(String car_type) {
            this.car_type = car_type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getStatus_text() {
            return status_text;
        }

        public void setStatus_text(String status_text) {
            this.status_text = status_text;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
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

        public int getIs_plan() {
            return is_plan;
        }

        public void setIs_plan(int is_plan) {
            this.is_plan = is_plan;
        }

        public String getPlan_time() {
            return plan_time;
        }

        public void setPlan_time(String plan_time) {
            this.plan_time = plan_time;
        }

        public List<AddrListBean> getAddr_list() {
            return addr_list;
        }

        public void setAddr_list(List<AddrListBean> addr_list) {
            this.addr_list = addr_list;
        }

        public static class AddrListBean implements Serializable {
            /**
             * addr : 重庆市沙坪坝区井口街道美丽·阳光家园H组团美丽·阳光家园
             */

            private String addr;

            public String getAddr() {
                return addr;
            }

            public void setAddr(String addr) {
                this.addr = addr;
            }
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
    }
}
