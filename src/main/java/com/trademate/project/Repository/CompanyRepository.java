package com.trademate.project.Repository;

import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyRepository extends JpaRepository<CompanyModel,Long> {
    List<CompanyModel> findByUser(UserModel userModel);
    CompanyModel findByCompanyId(long companyId);

    @Query(value = "SELECT c.* FROM company c JOIN sales_detail s ON c.company_id = s.company_company_id WHERE s.date >= DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY) AND c.company_id = :companyId", nativeQuery = true)
    List<CompanyModel> findWeeklyReport(@Param("companyId") long companyId);

    @Query(value = "SELECT  s.*, p.* FROM company c " +
            "LEFT JOIN sales_detail s ON c.company_id = s.company_company_id " +
            "LEFT JOIN purchase p ON c.company_id = p.company_company_id " +
            "WHERE c.company_id = :companyId", nativeQuery = true)
    List<Object[]> findAllSalesAndPurchases(@Param("companyId") long companyId);



}
