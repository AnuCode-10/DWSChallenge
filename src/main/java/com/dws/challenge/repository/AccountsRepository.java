package com.dws.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.DuplicateAccountIdException;

public interface AccountsRepository   {

  void createAccount(Account account) throws DuplicateAccountIdException;

  Account getAccount(String accountId);
  
  Account save(Account account);

  void clearAccounts();


}

