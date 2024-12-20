package com.trademate.project.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.PurchaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellerDto {

    private long id;
    private String sellerName;
    private String address;
    private String state;
    private String country;
    private int pinCode;
    private String gstIn;
    private String gstType;
    private long mobile;
}
