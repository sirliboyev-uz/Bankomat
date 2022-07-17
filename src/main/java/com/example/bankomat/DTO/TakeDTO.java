package com.example.bankomat.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class TakeDTO {
    private String bankomatNum;
    private String cardNumber;
    private double sum;
    private String cardType;
}
