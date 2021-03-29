package ec.edu.espe.corebancario.accounts.service;

import ec.edu.espe.corebancario.accounts.exception.DocumentNotFoundException;
import ec.edu.espe.corebancario.accounts.exception.UpdateException;
import ec.edu.espe.corebancario.accounts.model.Account;
import ec.edu.espe.corebancario.accounts.repository.AccountRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class AccountServiceUnitTest {

    @Mock
    private AccountRepository repository;

    @InjectMocks
    Account account;

    @BeforeEach
    public void setUp() {
        account = new Account();
    }
    
    @Test
    public void givenIdentificationReturnListOfAccounts() {
        String identification = "1725456055";
        List<Account> accounts
                = repository.findByClientIdentification(identification);
        AccountService service = new AccountService(repository);
        try {
            Assertions.assertEquals(accounts, service.listAccounts(identification));
        } catch (DocumentNotFoundException ex) {
            Logger.getLogger(AccountServiceUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void givenNumberReturnAccount() {
        String number = "270000000001";
        Account account
                = repository.findByNumber(number);
        AccountService service = new AccountService(repository);
        try {
            Assertions.assertEquals(account, service.getAccountByNumber(number));
        } catch (DocumentNotFoundException ex) {
            Logger.getLogger(AccountServiceUnitTest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void givenIdReturnAccount() {
        Integer id = 1;
        Optional<Account> account
                = repository.findById(id);
        AccountService service = new AccountService(repository);
        try {
            Assertions.assertEquals(account, service.getAccountById(id));
        } catch (DocumentNotFoundException ex) {
            Logger.getLogger(AccountServiceUnitTest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    @Test
    public void givenIdentificationReturnBalanceAccountOfThis(){
        
    }
    
    @Test
    public void givenIdentificationReturnLastAccount() {
        String identification = "1725456055";
        List<Account> accountFind
                = repository.findByClientIdentificationOrderByCreationDateDesc(identification, PageRequest.of(0, 1));
        AccountService service = new AccountService(repository);
        try {
            Assertions.assertEquals(accountFind.get(0), service.getLastAccountByIdentification(identification));
        } catch (DocumentNotFoundException ex) {
            Logger.getLogger(AccountServiceUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    @Test
    public void givenNullIdentificationThrowDocumentNotFoundException() {
        String identification = null;
        AccountService service = new AccountService(repository);
        Assertions.assertThrows(DocumentNotFoundException.class, () -> service.listAccounts(identification));
        Assertions.assertThrows(DocumentNotFoundException.class, () -> service.getBalanceAccount(identification));
    }

    @Test
    public void givenNullNumberThrowDocumentNotFoundException() {
        String number = null;
        AccountService service = new AccountService(repository);
        Assertions.assertThrows(DocumentNotFoundException.class, () -> service.getAccountByNumber(number));
    }

    @Test
    public void givenNullIdThrowDocumentNotFoundException() {
        Integer id = null;
        AccountService service = new AccountService(repository);
        Assertions.assertThrows(DocumentNotFoundException.class, () -> service.getAccountById(id));
    }

    @Test
    public void givenNullIdentificationOnLastAccountThrowDocumentNotFoundException() {
        String identification = null;
        AccountService service = new AccountService(repository);
        Assertions.assertThrows(DocumentNotFoundException.class, () -> service.getLastAccountByIdentification(identification));
    }

    @Test
    public void givenNullNumberAndStatusThrowUpdateException() {
        String number = null;
        String status = "INA";
        AccountService service = new AccountService(repository);
        Assertions.assertThrows(UpdateException.class, () -> service.updateStatus(number, status));
    }
    
    @Test
    public void givenNullNumberAndBalanceThrowUpdateException() {
        String number = null;
        BigDecimal balance = new BigDecimal('0');
        AccountService service = new AccountService(repository);
        Assertions.assertThrows(UpdateException.class, () -> service.updateBalance(number, balance));
    }
    
    
}
