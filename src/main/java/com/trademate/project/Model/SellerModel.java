package com.trademate.project.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "sellers",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sellerName","companyName"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String sellerName;
    private String address;
    private String companyName;
    private String state;
    private String country;
    private int pinCode;
    private String gstIn;
    private String gstType;
    private long mobile;
    @ManyToOne
    private CompanyModel company;
    @OneToMany(mappedBy ="seller")
    @JsonIgnore
    private List<PurchaseModel> purchase;
}
