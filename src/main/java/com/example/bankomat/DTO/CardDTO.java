package com.example.bankomat.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CardDTO {
    private String cardNumber;
    private String bankName;
    private String fullName;
    private String expDate;
    private String cardPassword;
    private double balance;
}
