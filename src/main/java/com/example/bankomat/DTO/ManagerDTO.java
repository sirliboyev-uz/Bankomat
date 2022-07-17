package com.example.bankomat.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ManagerDTO {
    private String fullName;
    private String username;
    private String password;
    private String bankomat;
}
