package com.trademate.project.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "StockItems")
public class StockItemModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private long itemId;

    @Column(unique = true, nullable = false)
    private String itemName;

    private String description;

    @Column(nullable = false)
    private double purchasePrice;

    @Column(nullable = false)
    private double sellingPrice;

    private String category;

    private int gstPercentage;

    private String hsnCode;

    @Column(nullable = false)
    private double quantity;

    private String unit;

    private String sku;

    private boolean isActive = true;

    private String barcode;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private CompanyModel company;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PurchaseModel> purchaseList;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SaleModel> saleList;

    @Transient
    public double getStockValue() {
        return quantity * sellingPrice;
    }
}
