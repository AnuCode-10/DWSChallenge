"# DWSChallenge" 
#TransferRequest class is created under com.dws.challenge.domain and it is passed as request body in AccountsController. 
#transferMoney() -> Method is created in AccountsController whicj takes TransferRequest attribbutes as body and interact with transfer() method of TransferService #class(created). TransferService class is created to provide logic for a thrread safe transferv of amount between sender and receiver. ReentrantLock has been used #into TransferService class.

#On top of this, test case can be written for this.


