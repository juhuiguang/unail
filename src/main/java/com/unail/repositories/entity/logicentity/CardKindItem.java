package com.unail.repositories.entity.logicentity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 橘 on 2016/7/6.
 */
public class CardKindItem {
    @Column(name = "card_kind_no")
    private Long cardkindno;

    @Column(name="card_kind_type")
    private String cardkindtype;

    public String getCardkindtype() {
        return cardkindtype;
    }

    public void setCardkindtype(String cardkindtype) {
        this.cardkindtype = cardkindtype;
    }

    @Column(name = "card_kind_name", nullable = true)
    private String cardkindname;

    @Column(name = "card_kind_desc", nullable = true)
    private String cardkinddesc;

    @Column(name = "card_kind_duetime", nullable = true)
    private Date cardkindduetime;

    @Column(name = "card_kind_sales", nullable = true)
    private float cardkindsales;

    @Column(name = "if_calculate_sales", nullable = true, columnDefinition = "0")
    private int ifcalculatesales;

    @Column(name = "card_kind_useshop", nullable = true)
    private String cardkinduseshop;

    @Column(name = "card_money", nullable = true)
    private float cardmoney;

    @Column(name = "card_times", nullable = true)
    private int cardtimes;

    @Column(name = "balance_type", nullable = true)
    private String balancetype;

    @Column(name = "card_number_prefix", nullable = true)
    private String cardnumberprefix;

    @Column(name = "card_number_start", nullable = true)
    private String cardnumberstart;

    @Column(name = "card_number_end", nullable = true)
    private String cardnumberend;

    @Column(name = "cardkind_status")
    private int cardkindstatus=1;
    @Column(name="cardcount")
    private int cardcount=0;

    public Long getCardkindno() {
        return cardkindno;
    }

    public void setCardkindno(Long cardkindno) {
        this.cardkindno = cardkindno;
    }

    public String getCardkindname() {
        return cardkindname;
    }

    public void setCardkindname(String cardkindname) {
        this.cardkindname = cardkindname;
    }

    public String getCardkinddesc() {
        return cardkinddesc;
    }

    public void setCardkinddesc(String cardkinddesc) {
        this.cardkinddesc = cardkinddesc;
    }

    public Date  getCardkindduetime() {
        return cardkindduetime;
    }

    public void setCardkindduetime(Date cardkindduetime) {
        this.cardkindduetime = cardkindduetime;
    }

    public float getCardkindsales() {
        return cardkindsales;
    }

    public void setCardkindsales(float cardkindsales) {
        this.cardkindsales = cardkindsales;
    }

    public int getIfcalculatesales() {
        return ifcalculatesales;
    }

    public void setIfcalculatesales(int ifcalculatesales) {
        this.ifcalculatesales = ifcalculatesales;
    }

    public String getCardkinduseshop() {
        return cardkinduseshop;
    }

    public void setCardkinduseshop(String cardkinduseshop) {
        this.cardkinduseshop = cardkinduseshop;
    }

    public float getCardmoney() {
        return cardmoney;
    }

    public void setCardmoney(float cardmoney) {
        this.cardmoney = cardmoney;
    }

    public int getCardtimes() {
        return cardtimes;
    }

    public void setCardtimes(int cardtimes) {
        this.cardtimes = cardtimes;
    }

    public String getBalancetype() {
        return balancetype;
    }

    public void setBalancetype(String balancetype) {
        this.balancetype = balancetype;
    }

    public String getCardnumberprefix() {
        return cardnumberprefix;
    }

    public void setCardnumberprefix(String cardnumberprefix) {
        this.cardnumberprefix = cardnumberprefix;
    }

    public String getCardnumberstart() {
        return cardnumberstart;
    }

    public void setCardnumberstart(String cardnumberstart) {
        this.cardnumberstart = cardnumberstart;
    }

    public String getCardnumberend() {
        return cardnumberend;
    }

    public void setCardnumberend(String cardnumberend) {
        this.cardnumberend = cardnumberend;
    }

    public int getCardkindstatus() {
        return cardkindstatus;
    }

    public void setCardkindstatus(int cardkindstatus) {
        this.cardkindstatus = cardkindstatus;
    }

    public int getCardcount() {
        return cardcount;
    }

    public void setCardcount(int cardcount) {
        this.cardcount = cardcount;
    }
}
