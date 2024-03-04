package com.trademate.project.Controller;

import com.trademate.project.Model.ExpenseModel;
import com.trademate.project.Service.CompanyService;
import com.trademate.project.Service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = {"http://localhost:3000","https://trade-mate-pearl.vercel.app/"})
@RequestMapping("/expense")
public class ExpenseContorller {
    @Autowired
    private ExpenseService service;
@Autowired
private CompanyService companyService;
    @PostMapping("/add")
    public ResponseEntity<ExpenseModel> add(@RequestBody ExpenseModel expense){
        expense.getCompany().setCompanyId(companyService.getByName(expense.getCompanyName()).getCompanyId());
        return service.add(expense);
    }
    @GetMapping("/all")
    public List<ExpenseModel> getAll(){
        return service.getAll();
    }
}
