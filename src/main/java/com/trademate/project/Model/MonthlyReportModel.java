package com.trademate.project.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyReportModel {
    private long totalRevenue;
    private long totalExpenses;
    private long totalProfits;
    private long totalRemaining;
    private TopCustomersModel topCustomersModel;
private TopItemsModel topItemsModel;

}
