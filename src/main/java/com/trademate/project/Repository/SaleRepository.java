package com.trademate.project.Repository;

import com.trademate.project.DTO.SaleDTO;
import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.CustomerModel;
import com.trademate.project.Model.SaleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface SaleRepository extends JpaRepository<SaleModel, Long> {
    @Query("select s from SaleModel s where company=?1")
    List<SaleModel> findAllBycompany(CompanyModel company);

    List<SaleModel> findByCustomer(CustomerModel customerModel);

    @Query("select sum(s.profit) as sumOfProfit,sum(s.remaining) as sumOfRemaining,sum(s.totalAmmount) as sumOfTotalAmmount from SaleModel s where month(s.date)=?1 and year(s.date)=?2 and s.company=?3")
    Map<String, Integer> sumOfRemainingByMonth(int month, int year, CompanyModel company);

    @Query("SELECT new map(SUM(s.profit) AS sumOfProfit, SUM(s.remaining) AS sumOfRemaining, SUM(s.totalAmmount) AS sumOfTotalAmmount) " +
            "FROM SaleModel s WHERE YEAR(s.date) = ?1 AND s.company = ?2")
    Map<String, Long> sumOfRemainingByYear(int year, CompanyModel company);


    @Query("SELECT SUM(s.remaining) FROM SaleModel s WHERE s.company = ?1")
    Long sumOfTotalRemainingByCompany(CompanyModel company);

    @Query("select s from SaleModel s where remaining>0")
    List<SaleModel> salesWithRemainingBalance();

    @Query("select sum(s.profit) as sumOfProfit, sum(s.remaining) as sumOfRemaining, sum(s.totalAmmount) as sumOfTotalAmmount from SaleModel s where s.company = ?1")
    Map<String, Integer> sumOfTotalRemaining(CompanyModel company);

    @Query("select s from SaleModel s where s.customer = ?1 and date = ?2")
    List<SaleModel> findByCustomerAndDate(CustomerModel customer, LocalDate date);

    @Query("select sum(s.totalAmmount) from SaleModel s where " +
            "(month(s.date) = :month1 and year(s.date) = :year and s.company = :company) or " +
            "(month(s.date) = :month2 and year(s.date) = :year and s.company = :company) or " +
            "(month(s.date) = :month3 and year(s.date) = :year and s.company = :company)")
    int getTotalAmountForQuarter(@Param("month1") int month1,
                                 @Param("month2") int month2,
                                 @Param("month3") int month3,
                                 @Param("year") int year,
                                 @Param("company") CompanyModel company);


    @Query("select sum(s.totalAmmount) as summOfTotalAmmount from SaleModel s where (month(s.date) = ?1 and year(s.date) = ?2 and company=?3)")
    int sumOfTotalAmountOfMonth(int month1, int year, CompanyModel company);

    @Query("SELECT MIN(s.date) FROM SaleModel s WHERE s.company = ?1")
    LocalDate findMinDate(CompanyModel company);

    @Query("SELECT s.customer, SUM(s.totalAmmount) AS totalSale " +
            "FROM SaleModel s " +
            "WHERE YEAR(s.date) = ?2 " +
            "  AND s.company = ?1 " +
            "  AND month(s.date) = ?3 " +
            "GROUP BY s.customer " +
            "ORDER BY totalSale DESC")
    List<Object[]> getTopFourCustomers(CompanyModel company, int year, int month);

    @Query("SELECT s.item, SUM(s.quantity) AS totalQuantity " +
            "FROM SaleModel s " +
            "WHERE YEAR(s.date) = ?2 " +
            "  AND s.company = ?1 " +
            "  AND MONTH(s.date) = ?3 " +
            "GROUP BY s.item " +
            "ORDER BY totalQuantity DESC")
    List<Object[]> getTopThreeItems(CompanyModel company, int year, int month);

    @Query("select sum(s.profit) as sumOfProfit,sum(s.remaining) as sumOfRemaining,sum(s.totalAmmount) as sumOfTotalAmmount from SaleModel s where month(s.date)=?1 and year(s.date)=?2 and s.company=?3 ")
    Map<String, Long> sumOfRemainingByMonths(int month, int year, CompanyModel company);

    @Query("SELECT SUM(s.profit) AS sumOfProfit, SUM(s.remaining) AS sumOfRemaining, SUM(s.totalAmmount) AS sumOfTotalAmmount " +
            "FROM SaleModel s WHERE MONTH(s.date) = ?1 AND YEAR(s.date) = ?2 AND s.company = ?3 AND DAY(s.date) = ?4")
    Map<String, Long> sumOfRemainingByDay(int month, int year, CompanyModel company, int day);


    List<SaleModel> findAllByCompanyOrderByDateDesc(CompanyModel company);

    List<SaleModel> findAllByCompanyAndRemainingGreaterThanOrderByDateDesc(CompanyModel company, double remaining);


    @Query("SELECT new com.trademate.project.DTO.SaleDTO(" +
            "s.id, " +
            "new com.trademate.project.DTO.StockItemDto(" +
            "   s.item.id, s.item.itemName, s.item.category, s.item.hsnCode, s.item.unit" +
            "), " +
            "s.quantity, " +
            "s.date, " +
            "s.rate, " +
            "s.receivedAmmount, " +
            "s.gstInRupee, " +
            "s.totalAmmount, " +
            "s.remaining, " +
            "s.profit, " +
            "new com.trademate.project.DTO.CustomerDto(" +
            "   s.customer.id, s.customer.customerName, s.customer.address, s.customer.email, " +
            "   s.customer.state, s.customer.country, s.customer.pinCode, s.customer.gstIn, " +
            "   s.customer.gstType, s.customer.addDate, s.customer.mobile" +
            ")) " +
            "FROM SaleModel s " +
            "WHERE s.company.id = :companyId " +
            "ORDER BY s.date DESC")
    List<SaleDTO> findAllSalesByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT new com.trademate.project.DTO.SaleDTO(" +
            "s.id, " +
            "new com.trademate.project.DTO.StockItemDto(" +
            "   s.item.id, s.item.itemName, s.item.category, s.item.hsnCode, s.item.unit" +
            "), " +
            "s.quantity, " +
            "s.date, " +
            "s.rate, " +
            "s.receivedAmmount, " +
            "s.gstInRupee, " +
            "s.totalAmmount, " +
            "s.remaining, " +
            "s.profit, " +
            "new com.trademate.project.DTO.CustomerDto(" +
            "   s.customer.id, s.customer.customerName, s.customer.address, s.customer.email, " +
            "   s.customer.state, s.customer.country, s.customer.pinCode, s.customer.gstIn, " +
            "   s.customer.gstType, s.customer.addDate, s.customer.mobile" +
            ")) " +
            "FROM SaleModel s " +
            "WHERE s.id = :saleId")
    SaleDTO findSaleById(@Param("saleId") Long saleId);

    @Query("SELECT new com.trademate.project.DTO.SaleDTO(" +
            "s.id, " +
            "new com.trademate.project.DTO.StockItemDto(" +
            "   s.item.id, s.item.itemName, s.item.category, s.item.hsnCode, s.item.unit" +
            "), " +
            "s.quantity, " +
            "s.date, " +
            "s.rate, " +
            "s.receivedAmmount, " +
            "s.gstInRupee, " +
            "s.totalAmmount, " +
            "s.remaining, " +
            "s.profit, " +
            "new com.trademate.project.DTO.CustomerDto(" +
            "   s.customer.id, s.customer.customerName, s.customer.address, s.customer.email, " +
            "   s.customer.state, s.customer.country, s.customer.pinCode, s.customer.gstIn, " +
            "   s.customer.gstType, s.customer.addDate, s.customer.mobile" +
            ")) " +
            "FROM SaleModel s " +
            "WHERE s.customer.id = :customerId AND s.date = :saleDate " +
            "ORDER BY s.date DESC")
    List<SaleDTO> findSalesByCustomerIdAndDate(
            @Param("customerId") Long customerId,
            @Param("saleDate") LocalDate saleDate);





}
