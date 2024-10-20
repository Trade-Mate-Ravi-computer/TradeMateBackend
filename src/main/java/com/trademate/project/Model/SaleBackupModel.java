package com.trademate.project.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "SaleBackup")
public class SaleBackupModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private StockItemModel item;
    private int quantity;
    private LocalDate date;
    private int rate;
    private int receivedAmmount;
    private double gstInRupee;
    private int totalAmmount;
    private int remaining;
    private int profit;
    private long customerMobile;
    @ManyToOne
    private CompanyModel company;
    @ManyToOne
    private CustomerModel customer;

}
