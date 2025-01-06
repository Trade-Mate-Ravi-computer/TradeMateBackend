package com.trademate.project.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.CustomerModel;
import com.trademate.project.Model.StockItemModel;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SaleDTO {

    private long id;
    private StockItemDto item;
    private int quantity;
    private LocalDate date;
    private int rate;
    private int receivedAmmount;
    private double gstInRupee;
    private int totalAmmount;
    private int remaining;
    private int profit;
    private CustomerDto customer;

}
