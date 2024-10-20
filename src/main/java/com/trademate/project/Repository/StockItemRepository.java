package com.trademate.project.Repository;

import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.StockItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockItemRepository extends JpaRepository<StockItemModel,String> {
    public StockItemModel findByItemName(String itemName);
    public List<StockItemModel> findByCompany(CompanyModel company);
}
