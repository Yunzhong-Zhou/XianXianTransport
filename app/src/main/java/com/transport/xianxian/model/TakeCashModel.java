package com.transport.xianxian.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zyz on 2019-12-16.
 */
public class TakeCashModel implements Serializable {

    /**
     * way_list : [{"type":2,"icon":"/pay/wx_tx.png","title":"微信","sub":"2小时到账，每次体现手续费5%"},{"type":3,"icon":"/pay/zfb_tx.png","title":"支付宝","sub":"2小时到账，每次体现手续费5%"},{"type":1,"icon":"/pay/bank_tx.png","title":"银行卡","sub":"2小时到账，每次体现手续费5%"}]
     * low_money : 100
     * usable_money : 1618.00
     */

    private String low_money;
    private String usable_money;
    private List<WayListBean> way_list;

    public String getLow_money() {
        return low_money;
    }

    public void setLow_money(String low_money) {
        this.low_money = low_money;
    }

    public String getUsable_money() {
        return usable_money;
    }

    public void setUsable_money(String usable_money) {
        this.usable_money = usable_money;
    }

    public List<WayListBean> getWay_list() {
        return way_list;
    }

    public void setWay_list(List<WayListBean> way_list) {
        this.way_list = way_list;
    }

    public static class WayListBean {
        /**
         * type : 2
         * icon : /pay/wx_tx.png
         * title : 微信
         * sub : 2小时到账，每次体现手续费5%
         */

        private int type;
        private String icon;
        private String title;
        private String sub;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSub() {
            return sub;
        }

        public void setSub(String sub) {
            this.sub = sub;
        }
    }
}
