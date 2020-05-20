package com.transport.xianxian.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zyz on 2019-10-18.
 */
public class Auth_CheLiangZhaoPianModel implements Serializable {
    /**
     * car_type_id : 3
     * car_image_front : f.jpg
     * car_image_back : f.jpg
     * car_image_left : f.jpg
     * car_image_right : f.jpg
     * car_type_list : [{"id":1,"name":"面包车","size":"2600mm*1000mm*900mm","weight":2,"bulk":3,"image":"1.jpg"},{"id":2,"name":"4.2M","size":"4100mm*1950mm*1800mm","weight":3,"bulk":11,"image":"2.jpg"},{"id":3,"name":"5.6M","size":"5350mm*2250mm*2300mm","weight":5,"bulk":16,"image":"3.jpg"},{"id":5,"name":"7.6M","size":"7450mm*2350mm*2500mm","weight":8,"bulk":38,"image":"/upload/goods/2019-10-24/7568d47d63f3cc50c9890a80dd01047f.png"},{"id":6,"name":"9.6M","size":"9450mm*2450mm*2800mm","weight":12,"bulk":58,"image":""},{"id":7,"name":"15M","size":"12600mm*2550mm*2800mm","weight":25,"bulk":75,"image":""}]
     */

    private String car_type_id;
    private String car_image_front;
    private String car_image_back;
    private String car_image_left;
    private String car_image_right;
    private List<CarTypeListBean> car_type_list;

    public String getCar_type_id() {
        return car_type_id;
    }

    public void setCar_type_id(String car_type_id) {
        this.car_type_id = car_type_id;
    }

    public String getCar_image_front() {
        return car_image_front;
    }

    public void setCar_image_front(String car_image_front) {
        this.car_image_front = car_image_front;
    }

    public String getCar_image_back() {
        return car_image_back;
    }

    public void setCar_image_back(String car_image_back) {
        this.car_image_back = car_image_back;
    }

    public String getCar_image_left() {
        return car_image_left;
    }

    public void setCar_image_left(String car_image_left) {
        this.car_image_left = car_image_left;
    }

    public String getCar_image_right() {
        return car_image_right;
    }

    public void setCar_image_right(String car_image_right) {
        this.car_image_right = car_image_right;
    }

    public List<CarTypeListBean> getCar_type_list() {
        return car_type_list;
    }

    public void setCar_type_list(List<CarTypeListBean> car_type_list) {
        this.car_type_list = car_type_list;
    }

    public static class CarTypeListBean {
        /**
         * id : 1
         * name : 面包车
         * size : 2600mm*1000mm*900mm
         * weight : 2
         * bulk : 3
         * image : 1.jpg
         */

        private String id;
        private String name;
        private String size;
        private String weight;
        private String bulk;
        private String image;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getBulk() {
            return bulk;
        }

        public void setBulk(String bulk) {
            this.bulk = bulk;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
