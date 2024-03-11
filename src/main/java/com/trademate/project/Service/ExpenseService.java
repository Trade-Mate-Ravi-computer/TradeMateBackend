package com.trademate.project.Service;

import com.trademate.project.Model.DateModel;
import com.trademate.project.Model.ExpenseModel;
import com.trademate.project.Model.MonthYearModel;
import com.trademate.project.Repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;


    public ResponseEntity<ExpenseModel> add(ExpenseModel expense){
        return new ResponseEntity<ExpenseModel>(expenseRepository.save(expense), HttpStatus.CREATED);
    }
    public List<ExpenseModel> getAll(){
        return expenseRepository.findAll();
    }
    public long getByMonth(MonthYearModel monthYearModel){
        if(expenseRepository.sumOfExpenseByMonth(monthYearModel.getMonth(),monthYearModel.getYear(),monthYearModel.getCompanyName(),monthYearModel.getEmail())!=null){
            return expenseRepository.sumOfExpenseByMonth(monthYearModel.getMonth(),monthYearModel.getYear(),monthYearModel.getCompanyName(),monthYearModel.getEmail());

        }else{
            return 0;
        }
    }
    public long getByDay(DateModel monthYearModel){
        if(expenseRepository.sumOfExpenseByDay(monthYearModel.getMonth(),monthYearModel.getYear(),monthYearModel.getCompanyName(),monthYearModel.getEmail(),monthYearModel.getDay())!=null){
            return expenseRepository.sumOfExpenseByDay(monthYearModel.getMonth(),monthYearModel.getYear(),monthYearModel.getCompanyName(),monthYearModel.getEmail(),monthYearModel.getDay());

        }else{
            return 0;
        }
    }
}
