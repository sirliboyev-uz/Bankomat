package com.example.bankomat.Controller;

import com.example.bankomat.DTO.*;
import com.example.bankomat.Repository.DatasRepository;
import com.example.bankomat.Service.FullService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")

public class FullController {
    @Autowired
    FullService fullService;
    @Autowired
    DatasRepository datasRepository;

    @PostMapping("/addCard")
    public HttpEntity<?> createCard(@RequestBody CardDTO cardDTO){
        ApiResponse apiResponse= fullService.addCard(cardDTO);
        return ResponseEntity.status(apiResponse.getType()?200:409).body(apiResponse.getMessage());
    }
    @PostMapping("/addBankomat")
    public HttpEntity<?> createBankomat(@RequestBody BankomatDTO bankomatDTO){
        ApiResponse apiResponse= fullService.addBankomat(bankomatDTO);
        return ResponseEntity.status(apiResponse.getType()?201:409).body(apiResponse.getMessage());
    }
    @PostMapping("/addManager")
    public HttpEntity<?> createManager(@RequestBody ManagerDTO managerDTO){
        ApiResponse apiResponse= fullService.addManager(managerDTO);
        return ResponseEntity.status(apiResponse.getType()?200:409).body(apiResponse.getMessage());
    }
    @PostMapping("/takeMoney")
    public HttpEntity<?> takeMoney(@RequestBody TakeDTO takeDTO){
        ApiResponse apiResponse= fullService.takeSumCard(takeDTO);
        return ResponseEntity.status(apiResponse.getType()?200:409).body(apiResponse.getMessage());
    }
    @PostMapping("/fillBalanceCard")
    public HttpEntity<?> fillBalanceCard(@RequestBody TakeDTO takeDTO){
        ApiResponse apiResponse= fullService.fillSumCard(takeDTO);
        return ResponseEntity.status(apiResponse.getType()?200:409).body(apiResponse.getMessage());
    }

    @PostMapping("/fillBalanceBankomat")
    public HttpEntity<?> fillBalanceBankomat(@RequestBody FillBankomatDTO fillBankomatDTO){
        ApiResponse apiResponse= fullService.fillBalanceBankomat(fillBankomatDTO);
        return ResponseEntity.status(apiResponse.getType()?200:409).body(apiResponse.getMessage());
    }

    @GetMapping("/getDatas")
    public HttpEntity<?> getDatas(){
        return ResponseEntity.ok(datasRepository.findAll());
    }
}
