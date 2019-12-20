package com.transport.xianxian.model;

import java.io.Serializable;

/**
 * Created by zyz on 2019-10-17.
 */
public class Auth_CheZhuModel implements Serializable {
    private int is_certification;

    public int getIs_certification() {
        return is_certification;
    }

    public void setIs_certification(int is_certification) {
        this.is_certification = is_certification;
    }

    /**
     * is_identity : 1
     * is_license : 1
     * is_face : 1
     * is_bank : 1
     * is_car_insurance : 1
     * is_nvq : 1
     * is_depend_on : 1
     * is_car_number : 1
     * is_car_image : 1
     */


    private int is_identity;
    private int is_license;
    private int is_face;
    private int is_bank;
    private int is_car_insurance;
    private int is_nvq;
    private int is_depend_on;
    private int is_car_number;
    private int is_car_image;

    public int getIs_identity() {
        return is_identity;
    }

    public void setIs_identity(int is_identity) {
        this.is_identity = is_identity;
    }

    public int getIs_license() {
        return is_license;
    }

    public void setIs_license(int is_license) {
        this.is_license = is_license;
    }

    public int getIs_face() {
        return is_face;
    }

    public void setIs_face(int is_face) {
        this.is_face = is_face;
    }

    public int getIs_bank() {
        return is_bank;
    }

    public void setIs_bank(int is_bank) {
        this.is_bank = is_bank;
    }

    public int getIs_car_insurance() {
        return is_car_insurance;
    }

    public void setIs_car_insurance(int is_car_insurance) {
        this.is_car_insurance = is_car_insurance;
    }

    public int getIs_nvq() {
        return is_nvq;
    }

    public void setIs_nvq(int is_nvq) {
        this.is_nvq = is_nvq;
    }

    public int getIs_depend_on() {
        return is_depend_on;
    }

    public void setIs_depend_on(int is_depend_on) {
        this.is_depend_on = is_depend_on;
    }

    public int getIs_car_number() {
        return is_car_number;
    }

    public void setIs_car_number(int is_car_number) {
        this.is_car_number = is_car_number;
    }

    public int getIs_car_image() {
        return is_car_image;
    }

    public void setIs_car_image(int is_car_image) {
        this.is_car_image = is_car_image;
    }
}
