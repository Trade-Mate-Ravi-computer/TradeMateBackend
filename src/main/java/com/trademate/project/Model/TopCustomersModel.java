package com.trademate.project.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopCustomersModel {
    private List<CustomerSaleModel> customerSaleModels;

}
