package com.trademate.project.Repository;

import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.PurchaseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<PurchaseModel,Long> {
//@Query("select p from PurchaseModel where companyName=?1")
    List<PurchaseModel> findAllByCompany(CompanyModel company);
    PurchaseModel findById(long id);
}
