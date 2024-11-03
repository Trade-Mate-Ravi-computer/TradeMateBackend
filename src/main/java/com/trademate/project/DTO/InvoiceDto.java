package com.trademate.project.DTO;

import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.CustomerModel;
import com.trademate.project.Model.SaleModel;
import com.trademate.project.Model.StockItemModel;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceDto {
    private CustomerModel customerModel;
    private List<SaleModel> sales;
    private double totalAmount;

}
