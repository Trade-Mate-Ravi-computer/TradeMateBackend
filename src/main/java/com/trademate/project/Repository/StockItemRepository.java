package com.trademate.project.Repository;

import com.trademate.project.Model.StockItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockItemRepository extends JpaRepository<StockItemModel,String> {
    public StockItemModel findByItemName(String itemName);
    @Query("select s from StockItemModel s where companyName=?1 and email=?2")
    public List<StockItemModel> fingByCompanyName(String companyName,String email);
}
