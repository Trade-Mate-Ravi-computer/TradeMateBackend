package com.trademate.project.Repository;

import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<CompanyModel,Long> {
    List<CompanyModel> findByUser(UserModel userModel);
    CompanyModel findByCompanyId(long companyId);

}
