package com.trademate.project.Repository;

import com.trademate.project.Model.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<CustomerModel,Long> {
    CustomerModel findByCustomerName(String name);
    @Query("SELECT c FROM CustomerModel c WHERE c.customerName = ?1 AND c.companyName = ?2 and c.email=?3")
    CustomerModel findByCustomerNameAndCompanyName(String customerName, String companyName,String email);
}
