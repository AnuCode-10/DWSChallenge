package com.dws.challenge.domain;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransferRequest {
 
	private String accountSenderId;
	private String accountReceiverId;
	 private BigDecimal amount;
}
