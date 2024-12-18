package com.trademate.project.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Customers",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"customerName","company_id"})
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
    private String email;
    private String state;
    private String country;
    private int pinCode;
    private String gstIn;
    private String gstType;
    private LocalDate addDate;
    private long mobile;
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyModel company;
    @OneToMany(mappedBy ="customer")
    @JsonIgnore
    private List<SaleModel> sales;
}
