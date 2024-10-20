package com.trademate.project.Repository;

import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<CustomerModel,Long> {
    List<CustomerModel> findByCompany(CompanyModel company);
}