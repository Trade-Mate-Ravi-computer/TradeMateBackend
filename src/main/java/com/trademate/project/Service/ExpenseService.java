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


}
