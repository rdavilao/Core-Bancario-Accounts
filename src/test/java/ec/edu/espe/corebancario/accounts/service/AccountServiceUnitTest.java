package ec.edu.espe.corebancario.accounts.service;

import ec.edu.espe.corebancario.accounts.enums.StateAccountEnum;
import ec.edu.espe.corebancario.accounts.exception.DocumentNotFoundException;
import ec.edu.espe.corebancario.accounts.exception.InsertException;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountServiceUnitTest {

    @Mock
    private AccountRepository repository;

    @InjectMocks
    private AccountService service;
    private Account account;

    @BeforeEach
    public void setUp() {
        account = new Account();
    }

    @Test
    public void givenAccountCreateAccount() {
        account.setClientIdentification("1725456048");
        account.setType(1);
        try {
            service.createAccount(account);
        } catch (InsertException ex) {
            Logger.getLogger(AccountServiceUnitTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AccountServiceUnitTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void givenAccountReturnInsertExeption() {
        account.setClientIdentification("172545604878987");
        account.setType(0);
        try {
            service.createAccount(account);
            verify(repository, times(1)).save(account);
            service.createAccount(account);
        } catch (InsertException ex) {
            Logger.getLogger(AccountServiceUnitTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void givenNumberAndStatusUpdateAccountStatus() {
        String number = "270000000003";
        String status = StateAccountEnum.INACTIVO.getEstado();
        try {
            service.updateStatus(number, status);
            verify(repository, times(1)).save(account);
        } catch (UpdateException ex) {
            Logger.getLogger(AccountServiceUnitTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void givenIdentificationReturnListOfAccounts() {
        String identification = "1725456055";
        List<Account> accounts
                = repository.findByClientIdentification(identification);
        try {
            Assertions.assertEquals(accounts, service.listAccounts(identification));
        } catch (DocumentNotFoundException ex) {
            Logger.getLogger(AccountServiceUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void givenNumberReturnAccount() {
        String number = "270000000001";
        account = repository.findByNumber(number);
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
        try {
            Assertions.assertEquals(account, service.getAccountById(id));
        } catch (DocumentNotFoundException ex) {
            Logger.getLogger(AccountServiceUnitTest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void givenNullIdentificationThrowDocumentNotFoundException() {
        String identification = null;
        Assertions.assertThrows(DocumentNotFoundException.class, () -> service.listAccounts(identification));
        Assertions.assertThrows(DocumentNotFoundException.class, () -> service.getBalanceAccount(identification));
    }

    @Test
    public void givenNullNumberThrowDocumentNotFoundException() {
        String number = null;
        Assertions.assertThrows(DocumentNotFoundException.class, () -> service.getAccountByNumber(number));
    }

    @Test
    public void givenNullIdThrowDocumentNotFoundException() {
        Integer id = null;
        Assertions.assertThrows(DocumentNotFoundException.class, () -> service.getAccountById(id));
    }

    @Test
    public void givenNullIdentificationOnLastAccountThrowDocumentNotFoundException() {
        String identification = null;
        Assertions.assertThrows(DocumentNotFoundException.class, () -> service.getLastAccountByIdentification(identification));
    }

    @Test
    public void givenNullNumberAndStatusThrowUpdateException() {
        String number = null;
        String status = "INA";
        Assertions.assertThrows(UpdateException.class, () -> service.updateStatus(number, status));
    }

    @Test
    public void givenNullNumberAndBalanceThrowUpdateException() {
        String number = null;
        BigDecimal balance = new BigDecimal('0');
        Assertions.assertThrows(UpdateException.class, () -> service.updateBalance(number, balance));
    }
}
