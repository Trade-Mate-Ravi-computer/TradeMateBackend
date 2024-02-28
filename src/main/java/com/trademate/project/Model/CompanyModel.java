package com.trademate.project.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Company")
public class CompanyModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long companyId;
    @Column(unique = true,nullable = false)
    private String companyName;
    @Column(nullable = false)
    private String companyAddress;
    private String gstIn;
    private String gstType;
    private int pinCode;
    private long mobile;
    private  String email;
    @ManyToOne
    private UserModel user;
    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<SaleModel> sale;
    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<StockItemModel> stockItem;
    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<PurchaseModel> purchases;

}
