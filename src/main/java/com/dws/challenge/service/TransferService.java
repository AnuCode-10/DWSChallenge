package com.dws.challenge.service;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dws.challenge.domain.Account;
import com.dws.challenge.repository.AccountsRepository;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TransferService {

	@Autowired
	private AccountsRepository accountRepository;

	@Autowired
	private NotificationService notificationService;
	
	//ReentrantLock for Multiple Transfers 
	
	private final Lock lock = new ReentrantLock();

	@Transactional
	public void transfer(String accountSenderId, String accountReceiverId, BigDecimal amount) {

		// load accounts
		

			Account accountSender = accountRepository.getAccount(accountSenderId);

			Account accountReceiver = accountRepository.getAccount(accountReceiverId);

			// validate amount

			if (amount.compareTo(BigDecimal.ZERO) <= 0) {
				throw new IllegalArgumentException("Amount must be positive.");
			}

			// Ensure sufficient balance in accountSender
			
			try {
				// Acquire the lock
				lock.lock();

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

			notificationService.notifyAboutTransfer(accountSender, "your account is debited with amount " + amount);
			notificationService.notifyAboutTransfer(accountReceiver, "your account is credited with amount " + amount);

		}
			finally {
				// release the lock
				lock.unlock();
			}
	}
	

}
