package com.cqut.faymong.betsoftware.entity;

/**
 * Created by fei on 2018/11/26.
 */

public class bet {
    public String account;
    public String domain;
    public String betamount;
    public boolean update;
    public boolean delete;
    public String state;

    public bet(String account, String domain, String betamount, boolean update,boolean delete,String state) {
        this.account = account;
        this.domain = domain;
        this.betamount = betamount;
        this.update = update;
        this.delete = delete;
        this.state = state;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setBetamount(String betamount) {
        this.betamount = betamount;
    }

    public String getAccount() {
        return account;
    }

    public String getDomain() {
        return domain;
    }

    public String getBetamount() {
        return betamount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
