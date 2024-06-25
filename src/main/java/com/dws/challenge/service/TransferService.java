package com.dws.challenge.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.dws.challenge.domain.Account;
import com.dws.challenge.repository.AccountsRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferService {

    @Autowired
    private AccountsRepository accountRepository;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public void transfer(Long accountSenderId, Long accountReceiverId, BigDecimal amount) {
    	//load accounts
        Account accountSender = accountRepository.findById(accountSenderId)
                                              .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountSenderId));
        Account accountReceiver = accountRepository.findById(accountReceiverId)
                                            .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountReceiverId));

        // validate amount
         
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }

//        if (accountSender.getBalance().compareTo(amount) < 0) {
//            throw new IllegalArgumentException("Insufficient balance.");
//        }
        
        // Ensure sufficient balance in accountSender
        synchronized (this) {
            BigDecimal balanceFrom = accountSender.getBalance();
            if (balanceFrom.compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient balance in account: " + accountSenderId);
            }

        // Perform the transfer
        accountSender.setBalance(accountSender.getBalance().subtract(amount));
        accountReceiver.setBalance(accountReceiver.getBalance().add(amount));

        accountRepository.save(accountSender);
        accountRepository.save(accountReceiver);
        
       

        // Send notifications
        notificationService.notify(accountSender.getAccountId(), "Transfer to " + accountReceiverId + ": " + amount);
        notificationService.notify(accountReceiver.getAccountId(), "Transfer from " + accountSenderId + ": " + amount);
    }
}
    }
