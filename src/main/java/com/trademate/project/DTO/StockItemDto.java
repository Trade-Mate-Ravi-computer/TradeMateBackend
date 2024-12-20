package com.trademate.project.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.PurchaseModel;
import com.trademate.project.Model.SaleModel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StockItemDto {

    private long itemId;

    @Column(unique = true, nullable = false)
    private String itemName;


    private String category;

    private String hsnCode;

    private String unit;

}
