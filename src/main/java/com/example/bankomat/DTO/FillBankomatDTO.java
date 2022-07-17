package com.example.bankomat.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class FillBankomatDTO {
    private String name;
    private double sum;
}
