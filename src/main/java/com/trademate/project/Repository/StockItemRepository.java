package com.trademate.project.Repository;

import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.StockItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockItemRepository extends JpaRepository<StockItemModel,String> {
    StockItemModel findByItemName(String itemName);
    StockItemModel findByItemId(long itemId);
    List<StockItemModel> findByCompany(CompanyModel company);

    @Query("SELECT si.itemName AS name, SUM(s.quantity) AS totalQuantity " +
            "FROM StockItemModel si JOIN si.saleList s " +
            "WHERE si.company.id = :companyId " +
            "GROUP BY si.itemName " +
            "ORDER BY totalQuantity DESC")
    List<Object[]> findTop5SoldItemsByCompany(@Param("companyId") Long companyId);


}
