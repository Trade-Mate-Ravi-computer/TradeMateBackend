package com.trademate.project.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "StockItems")
public class StockItemModel {
    @Id
    @Column(unique = true,nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long itemId;
    @Column(unique = true,nullable = false)
    private String itemName;
    private int purchasePrice;
    private String category;
    private int gstInPercent;
    private int quantity;
    private String sku;
    @ManyToOne
    private CompanyModel company;
    @OneToMany(mappedBy = "item")
    @JsonIgnore
    private List<PurchaseModel> purchaseList;
    @OneToMany(mappedBy = "item")
    @JsonIgnore
    private List<SaleModel> saleList;
}
