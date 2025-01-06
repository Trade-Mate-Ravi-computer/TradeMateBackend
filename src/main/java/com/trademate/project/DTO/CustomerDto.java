package com.trademate.project.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.SaleModel;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerDto {
    private long id;
    private String customerName;
    private String address;
    private String email;
    private String state;
    private String country;
    private int pinCode;
    private String gstIn;
    private String gstType;
    private LocalDate addDate;
    private long mobile;

}
