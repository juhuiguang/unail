package com.unail.repositories.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by 橘 on 2016/7/30.
 */
@Entity
@Table(name="balance_card")
public class BalanceCard implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long balanceid;
    @ManyToOne
    @JoinColumn(name="balance_id")
    private ProductBalance balance;

    @ManyToOne
    @JoinColumn(name="card_detail_id")
    private CardUseDetail carddetail;

    public Long getBalanceid() {
        return balanceid;
    }

    public void setBalanceid(Long balanceid) {
        this.balanceid = balanceid;
    }

    public ProductBalance getBalance() {
        return balance;
    }

    public void setBalance(ProductBalance balance) {
        this.balance = balance;
    }

    public CardUseDetail getCarddetail() {
        return carddetail;
    }

    public void setCarddetail(CardUseDetail carddetail) {
        this.carddetail = carddetail;
    }
}
