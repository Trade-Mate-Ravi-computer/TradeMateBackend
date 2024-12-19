package com.trademate.project.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrdersModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String orderId;
    private String amount;
    private String receipt;
    private String status;
    @ManyToOne
    private UserModel user;
    private String createDate;
    private String currency;
    private  String razorpay_payment_id;
    private String razorpay_signature;
    private String orderEmail;
    private int numberOfAttempt;
    private int durationInMonths;


}
