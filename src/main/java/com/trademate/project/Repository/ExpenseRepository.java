package com.trademate.project.Repository;

import com.trademate.project.Model.ExpenseModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<ExpenseModel,Long> {
}
