package com.trademate.project.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Customers",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"customerName","companyName"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String customerName;
    private String address;
    private String companyName;
    private String email;
    private String state;
    private String country;
    private int pinCode;
    private String gstIn;
    private String gstType;
    private long mobile;
    @ManyToOne
    private CompanyModel company;
    @OneToMany(mappedBy ="customer")
    @JsonIgnore
    private List<SaleModel> sales;
}
