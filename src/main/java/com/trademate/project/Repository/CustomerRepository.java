package com.trademate.project.Repository;

import com.trademate.project.Model.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerModel,Long> {
}
