package com.trademate.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductSalePurchaseDto {
    private String productName;
    private Long salesCount;
    private Long purchaseCount;
}

