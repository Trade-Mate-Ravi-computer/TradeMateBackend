package com.trademate.project.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DateModel {
    private int year;
    private int month;
    private int day;
    private String companyName;
    private String email;
}
