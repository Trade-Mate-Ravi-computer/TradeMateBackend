package com.trademate.project.Repository;

import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.SellerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SellerRepository extends JpaRepository<SellerModel,Long> {
    SellerModel findById(long id);
    List<SellerModel> findByCompany(CompanyModel company);

}
