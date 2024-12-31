package com.trademate.project.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "expense")
public class ExpenseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // Basic Expense Details
    private String name; // Name of the expense (e.g., "Office Supplies")
    private String expenseOn; // What the expense was used for (e.g., "Office Rent")
    private LocalDate date; // Date of the expense
    private int amount; // Amount spent

    // Company Details
    private String companyName; // Name of the associated company
    private String email; // Contact email for the expense (e.g., vendor or admin email)

    // Additional Expense Details
    private String expenseType; // Type of expense (e.g., "Operational", "Capital", etc.)
    private String paymentMethod; // Payment method (e.g., "Credit Card", "Bank Transfer")
    private String notes; // Additional notes or descriptions of the expense

    // Association with Company
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyModel company; // Reference to the associated company
}
