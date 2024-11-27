package com.trademate.project.Repository;

import com.trademate.project.DTO.MonthlySalesPurchasesDto;
import com.trademate.project.DTO.ProductSalePurchaseDto;
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

    @Query("""
    SELECT new com.trademate.project.DTO.ProductSalePurchaseDto(
        s.item.itemName,
        SUM(s.quantity), 
        (SELECT SUM(pur.quantity) 
         FROM PurchaseModel pur 
         WHERE pur.company.companyId = :companyId AND pur.item.itemName = s.item.itemName)
    )
    FROM SaleModel s
    WHERE s.company.companyId = :companyId
    GROUP BY s.item.itemName
""")
    List<ProductSalePurchaseDto> findDistinctProductSalesAndPurchases(Long companyId);

    @Query("""
    SELECT new com.trademate.project.DTO.MonthlySalesPurchasesDto(
        yearMonth.year, 
        yearMonth.month,
        COALESCE(s.totalSalesAmount, 0) AS totalSalesAmount, 
        COALESCE(p.totalPurchaseAmount, 0) AS totalPurchaseAmount
    )
    FROM (
        SELECT DISTINCT YEAR(date) AS year, MONTH(date) AS month
        FROM SaleModel
        WHERE company.companyId = :companyId
        UNION
        SELECT DISTINCT YEAR(date) AS year, MONTH(date) AS month
        FROM PurchaseModel
        WHERE company.companyId = :companyId
    ) yearMonth
    LEFT JOIN (
        SELECT YEAR(s.date) AS year, MONTH(s.date) AS month, SUM(s.totalAmmount) AS totalSalesAmount
        FROM SaleModel s
        WHERE s.company.companyId = :companyId
        GROUP BY YEAR(s.date), MONTH(s.date)
    ) s ON yearMonth.year = s.year AND yearMonth.month = s.month
    LEFT JOIN (
        SELECT YEAR(p.date) AS year, MONTH(p.date) AS month, SUM(p.totalAmount) AS totalPurchaseAmount
        FROM PurchaseModel p
        WHERE p.company.companyId = :companyId
        GROUP BY YEAR(p.date), MONTH(p.date)
    ) p ON yearMonth.year = p.year AND yearMonth.month = p.month
    ORDER BY yearMonth.year, yearMonth.month
""")
    List<MonthlySalesPurchasesDto> findMonthlySalesAndPurchases(Long companyId);

}
