package com.transport.xianxian.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zyz on 2019-10-03.
 */
public class WalletModel implements Serializable {
    /**
     * nickname : 15823629471
     * head :
     * money : 0
     * wait_money : 0
     * frozen_money : 0
     * total_money : 0
     * withdrawal_money : 0
     * tmoney_data : [{"id":1,"type":1,"out_in":2,"title":"货主付款","sn":"20191016","money":"100.00","remark":"货主付款","created_at":"2019-10-16 13:58:53"},{"id":2,"type":2,"out_in":2,"title":"货主付款","sn":"20191016","money":"1000.00","remark":"货主付款","created_at":"2019-10-16 14:08:47"}]
     */

    private String nickname;
    private String head;
    private String money;
    private String wait_money;
    private String frozen_money;
    private String total_money;
    private String withdrawal_money;
    private List<TmoneyDataBean> tmoney_data;

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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getWait_money() {
        return wait_money;
    }

    public void setWait_money(String wait_money) {
        this.wait_money = wait_money;
    }

    public String getFrozen_money() {
        return frozen_money;
    }

    public void setFrozen_money(String frozen_money) {
        this.frozen_money = frozen_money;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public String getWithdrawal_money() {
        return withdrawal_money;
    }

    public void setWithdrawal_money(String withdrawal_money) {
        this.withdrawal_money = withdrawal_money;
    }

    public List<TmoneyDataBean> getTmoney_data() {
        return tmoney_data;
    }

    public void setTmoney_data(List<TmoneyDataBean> tmoney_data) {
        this.tmoney_data = tmoney_data;
    }

    public static class TmoneyDataBean {
        /**
         * id : 1
         * type : 1
         * out_in : 2
         * title : 货主付款
         * sn : 20191016
         * money : 100.00
         * remark : 货主付款
         * created_at : 2019-10-16 13:58:53
         */

        private String id;
        private String type;
        private int out_in;
        private String title;
        private String sn;
        private String money;
        private String remark;
        private String created_at;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getOut_in() {
            return out_in;
        }

        public void setOut_in(int out_in) {
            this.out_in = out_in;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
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
    }
}
