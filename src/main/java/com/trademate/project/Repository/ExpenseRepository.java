package com.trademate.project.Repository;

import com.trademate.project.Model.ExpenseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExpenseRepository extends JpaRepository<ExpenseModel,Long> {
    @Query("select sum(amount) from ExpenseModel e where month(e.date)=?1 and year(e.date)=?2 and companyName=?3 and email=?4")
    Long sumOfExpenseByMonth(int month,int year,String companyName,String email);

    @Query("select sum(amount) from ExpenseModel e where month(e.date)=?1 and year(e.date)=?2 and companyName=?3 and email=?4 and day(e.date)=?5")
    Long sumOfExpenseByDay(int month,int year,String companyName,String email,int day);
}
