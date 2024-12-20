package com.trademate.project.Repository;

import com.trademate.project.DTO.PurchaseDto;
import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.PurchaseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<PurchaseModel,Long> {
//@Query("select p from PurchaseModel where companyName=?1")
    List<PurchaseModel> findAllByCompany(CompanyModel company);
    PurchaseModel findById(long id);

    @Query("SELECT new com.trademate.project.DTO.PurchaseDto(" +
            "p.id, " +
            "p.price, " +
            "p.date, " +
            "p.quantity, " +
            "p.totalAmount, " +
            "p.paidAmount, " +
            "p.gstInRupee, " +
            "p.remaining, " +
            "new com.trademate.project.DTO.StockItemDto(" +
            "   p.item.itemId, p.item.itemName, p.item.category, p.item.hsnCode, p.item.unit" +
            "), " +
            "new com.trademate.project.DTO.SellerDto(" +
            "   p.seller.id, p.seller.sellerName, p.seller.address, p.seller.state, p.seller.country, " +
            "   p.seller.pinCode, p.seller.gstIn, p.seller.gstType, p.seller.mobile" +
            ")) " +
            "FROM PurchaseModel p " +
            "WHERE p.company.id = :companyId")
    List<PurchaseDto> findAllPurchasesByCompanyId(@Param("companyId") Long companyId);

}
