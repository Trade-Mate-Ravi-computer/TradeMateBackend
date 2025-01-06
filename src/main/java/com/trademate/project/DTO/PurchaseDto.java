package com.trademate.project.DTO;

import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.SellerModel;
import com.trademate.project.Model.StockItemModel;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDto {

    private long id;
    private int price;
    private LocalDate date;
    private int quantity;
    private int totalAmount;
    private int paidAmount;
    private double gstInRupee;
    private int remaining;
    private StockItemDto item;
    private SellerDto seller;
}
