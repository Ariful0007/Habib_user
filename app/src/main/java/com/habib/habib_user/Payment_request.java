package com.habib.habib_user;

public class Payment_request {
    String useremail,paymentmethode,paymentnumber,ammount,date,status,uuid,username;

    public Payment_request() {
    }

    public Payment_request(String useremail, String paymentmethode,
                           String paymentnumber, String ammount, String date, String status, String uuid, String username) {
        this.useremail = useremail;
        this.paymentmethode = paymentmethode;
        this.paymentnumber = paymentnumber;
        this.ammount = ammount;
        this.date = date;
        this.status = status;
        this.uuid = uuid;
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getPaymentmethode() {
        return paymentmethode;
    }

    public void setPaymentmethode(String paymentmethode) {
        this.paymentmethode = paymentmethode;
    }

    public String getPaymentnumber() {
        return paymentnumber;
    }

    public void setPaymentnumber(String paymentnumber) {
        this.paymentnumber = paymentnumber;
    }

    public String getAmmount() {
        return ammount;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}