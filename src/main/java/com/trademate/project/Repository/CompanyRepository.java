package com.trademate.project.Repository;

import com.trademate.project.Model.CompanyModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<CompanyModel,Long> {
    List<CompanyModel> findByUserId(long id);
    CompanyModel findByCompanyName(String companyName);
    CompanyModel findByCompanyId(long id);
}
