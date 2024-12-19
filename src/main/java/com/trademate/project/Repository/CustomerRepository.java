package com.trademate.project.Repository;

import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<CustomerModel,Long> {
    List<CustomerModel> findByCompany(CompanyModel company);
    @Query("SELECT c.customerName AS name, SUM(s.totalAmmount) AS totalPurchases " +
            "FROM CustomerModel c JOIN c.sales s " +
            "WHERE c.company.id = :companyId " +
            "GROUP BY c.customerName " +
            "ORDER BY totalPurchases DESC")
    List<Object[]> findTop5CustomersByCompany(@Param("companyId") Long companyId);


}