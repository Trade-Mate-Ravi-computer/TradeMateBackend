package com.trademate.project.Service;

import com.trademate.project.Model.ExpenseModel;
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
}
