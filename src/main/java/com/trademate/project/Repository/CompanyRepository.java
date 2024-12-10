package com.trademate.project.Repository;

import com.trademate.project.DTO.MonthlySalesPurchasesDto;
import com.trademate.project.DTO.ProductSalePurchaseDto;
import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

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

    // Sales Reports
    @Query("SELECT s.date AS date, SUM(s.totalAmmount) AS totalSales FROM SaleModel s WHERE s.company.companyId = :companyId GROUP BY s.date")
    List<Map<String, Object>> getDailySalesReport(Long companyId);

    @Query("SELECT DATE(s.date) AS date, " +
            "SUM(s.totalAmmount) AS totalSales, " +
            "SUM(s.profit) AS totalProfit, " +
            "SUM(s.remaining) AS totalRemaining " +
            "FROM SaleModel s " +
            "WHERE s.company.companyId = :companyId " +
            "GROUP BY DATE(s.date)")
    List<Map<String, Object>> getDailySalesReportWithProfitAndRemaining(Long companyId);



    @Query("SELECT MONTH(s.date) AS month, YEAR(s.date) AS year, SUM(s.totalAmmount) AS totalSales FROM SaleModel s WHERE s.company.companyId = :companyId GROUP BY YEAR(s.date), MONTH(s.date)")
    List<Map<String, Object>> getMonthlySalesReport(Long companyId);

//    @Query("SELECT c.customerName, SUM(s.totalAmmount) AS totalSpent FROM SaleModel s JOIN s.customer c WHERE s.company.companyId = :companyId GROUP BY c.customerName")
//    List<Map<String, Object>> getCustomerWiseSalesReport(Long companyId);
    @Query("SELECT COALESCE(c.customerName, 'Unknown') AS customerName, SUM(s.totalAmmount) AS totalSpent FROM SaleModel s JOIN s.customer c WHERE s.company.companyId = :companyId GROUP BY c.customerName")
    List<Map<String, Object>> getCustomerWiseSalesReport(Long companyId);


    // Purchase Reports
    @Query("SELECT p.date AS date, SUM(p.totalAmount) AS totalPurchases FROM PurchaseModel p WHERE p.company.companyId = :companyId GROUP BY p.date")
    List<Map<String, Object>> getDailyPurchaseReport(Long companyId);

    @Query("SELECT MONTH(p.date) AS month, YEAR(p.date) AS year, SUM(p.totalAmount) AS totalPurchases FROM PurchaseModel p WHERE p.company.companyId = :companyId GROUP BY YEAR(p.date), MONTH(p.date)")
    List<Map<String, Object>> getMonthlyPurchaseReport(Long companyId);

    @Query("SELECT COALESCE(v.sellerName, 'Unknown') AS sellerName, SUM(p.totalAmount) AS totalSpent FROM PurchaseModel p JOIN p.seller v WHERE p.company.companyId = :companyId GROUP BY v.sellerName")
    List<Map<String, Object>> getSellerWisePurchaseReport(Long companyId);

    // Inventory Reports
    @Query("SELECT si.itemName, si.quantity AS stockLeft, si.purchasePrice AS stockValue FROM StockItemModel si WHERE si.company.companyId = :companyId")
    List<Object[]> getCurrentStockReport(Long companyId);

    @Query("SELECT si.itemName, SUM(si.quantity) AS totalSold FROM StockItemModel si WHERE si.company.companyId = :companyId GROUP BY si.itemName")
    List<Object[]> getProductWiseSalesReport(Long companyId);

    // Financial Reports
    @Query("SELECT SUM(s.totalAmmount) AS totalSales, SUM(p.totalAmount) AS totalPurchases, (SUM(s.totalAmmount) - SUM(p.totalAmount)) AS profit FROM SaleModel s, PurchaseModel p WHERE s.company.companyId = :companyId AND p.company.companyId = :companyId")
    Map<String, Object> getProfitAndLossReport(Long companyId);

    // Pending Payments Report
    @Query("SELECT c.customerName, SUM(s.totalAmmount - s.receivedAmmount) AS pendingAmount FROM SaleModel s JOIN s.customer c WHERE s.company.companyId = :companyId AND (s.totalAmmount > s.receivedAmmount) GROUP BY c.customerName")
    List<Object[]> getPendingPaymentsFromCustomers(Long companyId);

    @Query("SELECT v.sellerName, SUM(p.totalAmount - p.paidAmount) AS pendingAmount FROM PurchaseModel p JOIN p.seller v WHERE p.company.companyId = :companyId AND (p.totalAmount > p.paidAmount) GROUP BY v.sellerName")
    List<Map<String, Object>> getPendingPaymentsToVendors(Long companyId);

    // Expense Reports
    @Query("SELECT e.date AS date, SUM(e.amount) AS totalExpenses FROM ExpenseModel e WHERE e.company.companyId = :companyId GROUP BY e.date")
    List<Map<String, Object>> getDailyExpenseReport(Long companyId);

    @Query("SELECT MONTH(e.date) AS month, YEAR(e.date) AS year, SUM(e.amount) AS totalExpenses FROM ExpenseModel e WHERE e.company.companyId = :companyId GROUP BY YEAR(e.date), MONTH(e.date)")
    List<Map<String, Object>> getMonthlyExpenseReport(Long companyId);

}
