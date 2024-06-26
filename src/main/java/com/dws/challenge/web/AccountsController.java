package com.dws.challenge.web;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.service.TransferService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {

  private final AccountsService accountsService;

  @Autowired
  public AccountsController(AccountsService accountsService) {
    this.accountsService = accountsService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
    log.info("Creating account {}", account);

    try {
    this.accountsService.createAccount(account);
    } catch (DuplicateAccountIdException daie) {
      return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
    }
catch (Exception e) {
		// TODO: handle exception
    	log.error("Acount creation failed");
	}
    
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping(path = "/{accountId}")
  public Account getAccount(@PathVariable String accountId) {
    log.info("Retrieving account for id {}", accountId);
    return this.accountsService.getAccount(accountId);
  }
  @Autowired
  private TransferService transferService;
  
  @PostMapping("/transfer")
  public ResponseEntity<String> transferMoney(@RequestParam Long accountSenderId,
                                              @RequestParam Long accountReceiverId,
                                              @RequestParam BigDecimal amount) {
      transferService.transfer(accountSenderId, accountReceiverId, amount);
      return ResponseEntity.ok("Transfer successful");
  }

  
  
  
//  @PostMapping("/transfer")
//  public ResponseEntity<String> transferMoney(@RequestBody TransferRequest transferRequest) {
//      transferService.transfer(transferRequest.getAccountFrom(), 
//                               transferRequest.getAccountTo(), 
//                               transferRequest.getAmount());
//      return ResponseEntity.ok("Transfer successful.");
//  }
}


