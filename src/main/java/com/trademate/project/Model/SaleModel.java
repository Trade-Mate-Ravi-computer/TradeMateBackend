package com.trademate.project.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Sales Detail")
public class SaleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private StockItemModel item;
    private String customerName;
    private int quantity;
    private LocalDate date;
    private int rate;
    private int receivedAmmount;
    private double gstInRupee;
    private int totalAmmount;
    private int remaining;
    private int profit;
    private String itemName;
    private String email;
    private String companyName;
    @ManyToOne
    private CompanyModel company;
    @ManyToOne
    private CustomerModel customer;


}
