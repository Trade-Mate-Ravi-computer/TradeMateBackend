package com.trademate.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlySalesPurchasesDto {
    private int year;
    private int month;
    private long totalSalesAmount;
    private long totalPurchaseAmount;
}
