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
    private String itemName;
    private int purchasePrice;
    private String category;
    private String companyName;
    @ManyToOne
    private CompanyModel company;
    @OneToMany(mappedBy = "item")
    @JsonIgnore
    private List<PurchaseModel> purchaseList;
}
