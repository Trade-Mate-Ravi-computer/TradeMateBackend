package com.trademate.project.Repository;

import com.trademate.project.Model.SellerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SellerRepository extends JpaRepository<SellerModel,Long> {
    SellerModel findBySellerName(String name);
    @Query("select s from SellerModel s where s.sellerName = ?1 and s.companyName = ?2")
    SellerModel findBySellerNameAndCompanyName(String sellerName, String companyName);

}
