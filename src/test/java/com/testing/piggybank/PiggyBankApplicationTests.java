//package com.testing.piggybank;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
package com.testing.piggybank;

import com.testing.piggybank.model.Account;
import com.testing.piggybank.model.Direction;
import com.testing.piggybank.account.AccountRepository;
import com.testing.piggybank.account.AccountService;
import com.testing.piggybank.model.Transaction;
import com.testing.piggybank.transaction.CreateTransactionRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class PiggyBankApplicationTests {

	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	private AccountService accountService;

	@Test
	void testGetAccountWithNonexistentAccount() {
		// Mock data
		when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

		// Test the method
		Optional<Account> result = accountService.getAccount(1L);

		// Verify the result
		assertEquals(Optional.empty(), result);
	}

	@Test
	void testGetAccountsByUserIdWithNoAccounts() {
		// Mock data
		long userId = 1L;
		when(accountRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());

		// Test the method
		List<Account> result = accountService.getAccountsByUserId(userId);

		// Verify the result
		assertEquals(Collections.emptyList(), result);
	}

	@Test
	void testUpdateBalanceWithNonexistentAccount() {
		// Mock data
		long accountId = 1L;
		BigDecimal amount = new BigDecimal("100.00");
		Direction direction = Direction.CREDIT;
		when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

		// Test the method and assert exception
		assertThrows(RuntimeException.class,
				() -> accountService.updateBalance(accountId, amount, direction));
	}

	@Test
	void testUpdateBalanceWithNegativeBalance() {
		// Mock data
		long accountId = 1L;
		BigDecimal initialBalance = new BigDecimal("150.00");
		BigDecimal amount = new BigDecimal("200.00"); // Attempting to debit more than the balance
		Direction direction = Direction.DEBIT;

		Account mockAccount = new Account();
		mockAccount.setBalance(initialBalance);

		when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));
		when(accountRepository.save(Mockito.any())).thenReturn(null);

		// Test the method and expect a RuntimeException
		assertThrows(RuntimeException.class,
				() -> accountService.updateBalance(accountId, amount, direction));
	}

	@Test
	void testGetAccount() {
		// Mock data
		Account mockAccount = new Account();
		when(accountRepository.findById(anyLong())).thenReturn(Optional.of(mockAccount));

		// Test the method
		Optional<Account> result = accountService.getAccount(1L);

		// Verify the result
		assertEquals(mockAccount, result.orElse(null));
	}

	@Test
	void testGetAccountsByUserId() {
		// Mock data
		long userId = 1L;
		List<Account> mockAccounts = new ArrayList<>();
		when(accountRepository.findAllByUserId(userId)).thenReturn(mockAccounts);

		// Test the method
		List<Account> result = accountService.getAccountsByUserId(userId);

		// Verify the result
		assertEquals(mockAccounts, result);
	}

	@Test
	void testUpdateBalanceCredit() {
		// Mock data
		long accountId = 1L;
		BigDecimal amount = new BigDecimal("100.00");
		Direction direction = Direction.CREDIT;
		Account mockAccount = new Account();
		mockAccount.setBalance(new BigDecimal("500.00"));
		when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));
		when(accountRepository.save(Mockito.any())).thenReturn(null);

		// Test the method
		accountService.updateBalance(accountId, amount, direction);

		// Verify the result
		assertEquals(new BigDecimal("600.00"), mockAccount.getBalance());
	}

	@Test
	void testUpdateBalanceDebit() {
		// Mock data
		long accountId = 1L;
		BigDecimal amount = new BigDecimal("100.00");
		Direction direction = Direction.DEBIT;
		Account mockAccount = new Account();
		mockAccount.setBalance(new BigDecimal("500.00"));
		when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));
		when(accountRepository.save(Mockito.any())).thenReturn(null);

		// Test the method
		accountService.updateBalance(accountId, amount, direction);

		// Verify the result
		assertEquals(new BigDecimal("400.00"), mockAccount.getBalance());
	}

	@Test
	void testAccountTransactionIntegrationCredit() {
		// Mock data for Account
		long accountId = 1L;
		BigDecimal amount = new BigDecimal("100.00");
		Direction direction = Direction.CREDIT;
		Account mockAccount = new Account();
		mockAccount.setBalance(new BigDecimal("500.00"));
		when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));
		when(accountRepository.save(Mockito.any())).thenReturn(null);

		// Test the integration
		accountService.updateBalance(accountId, amount, direction);

		// Verify the result
		assertEquals(new BigDecimal("600.00"), mockAccount.getBalance());
	}

	@Test
	void testAccountTransactionIntegrationDebit() {
		// Mock data for Account
		long accountId = 1L;
		BigDecimal amount = new BigDecimal("100.00");
		Direction direction = Direction.DEBIT;
		Account mockAccount = new Account();
		mockAccount.setBalance(new BigDecimal("500.00"));
		when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));
		when(accountRepository.save(Mockito.any())).thenReturn(null);

		// Test the integration
		accountService.updateBalance(accountId, amount, direction);

		// Verify the result
		assertEquals(new BigDecimal("400.00"), mockAccount.getBalance());
	}

	@Test
	void testAccountTransactionIntegrationUpdateBalance() {
		// Mock data for Account
		long accountId = 1L;
		BigDecimal amount = new BigDecimal("100.00");
		Direction direction = Direction.CREDIT;
		Account mockAccount = new Account();
		mockAccount.setBalance(new BigDecimal("500.00"));
		when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));
		when(accountRepository.save(Mockito.any())).thenReturn(null);

		// Test the integration
		accountService.updateBalance(accountId, amount, direction);

		// Verify the result
		assertEquals(new BigDecimal("600.00"), mockAccount.getBalance());
	}
}

