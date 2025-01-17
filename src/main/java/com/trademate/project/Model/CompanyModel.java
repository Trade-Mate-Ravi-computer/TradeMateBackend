package com.trademate.project.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "Company",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"companyName", "email"})
})
public class CompanyModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long companyId;
    @Column(nullable = false)
    private String companyName;
    @Column(nullable = false)
    private String companyAddress;
    private String gstIn;
    private String gstType;
    private String state;
    private String district;
    private String locality;
    private String country;
    private int pinCode;
    private long mobile;
    private  String email;
    @Lob
    @Column(name = "image", columnDefinition = "MEDIUMTEXT")
    private String image;
    private String bankName;
    private String accountNumber;
    private String ifscCode;
    @ManyToOne
    private UserModel user;
    @OneToMany(mappedBy = "company")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<SaleModel> sale;
    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<SaleBackupModel> saleBackup;
    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<StockItemModel> stockItem;
    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<CustomerModel> customers;
    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<PurchaseModel> purchases;
    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<SellerModel> seller;
    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<ExpenseModel> expense;



}
