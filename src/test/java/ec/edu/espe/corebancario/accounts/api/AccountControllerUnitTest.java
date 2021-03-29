package ec.edu.espe.corebancario.accounts.api;

import ec.edu.espe.corebancario.accounts.api.dto.UpdateAccountStatusRq;
import ec.edu.espe.corebancario.accounts.exception.DocumentNotFoundException;
import ec.edu.espe.corebancario.accounts.model.Account;
import ec.edu.espe.corebancario.accounts.service.AccountService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class AccountControllerUnitTest {
    
    @Mock
    private AccountService service;
    
    @InjectMocks
    Account account;
    
    @BeforeEach
    public void setUp() {
        account = new Account();
    }
    
    @Test
    public void givenIdentificationReturnListOfAccounts(){
        String identification = "1725456055";
        AccountController controller = new AccountController(service);
        List<Account> result = new ArrayList<Account>();
        try {
            when (service.listAccounts(identification))
                    .thenReturn(result);
        } catch (DocumentNotFoundException ex) {
            
        }
        ResponseEntity response = ResponseEntity.ok(result);
        Assertions.assertEquals(response, controller.listAccounts(identification));        
    }
    
    @Test
    public void givenNumberReturnAccount(){
        String number = "270000000001";
        AccountController controller = new AccountController(service);
        Account result = new Account();
        try {
            when (service.getAccountByNumber(number))
                    .thenReturn(result);
        } catch (DocumentNotFoundException ex) {
            
        }
        ResponseEntity response = ResponseEntity.ok(result);
        Assertions.assertEquals(response, controller.findAccountByNumber(number));        
    }
    
    @Test
    public void givenIdReturnAccount(){
        Integer id = 1;
        AccountController controller = new AccountController(service);
        Account result = new Account();
        try {
            when (service.getAccountById(id))
                    .thenReturn(result);
        } catch (DocumentNotFoundException ex) {
            
        }
        ResponseEntity response = ResponseEntity.ok(result);
        Assertions.assertEquals(response, controller.findAccountById(id));        
    }
    
    @Test
    public void givenIdentificationReturnLastAccountCreated(){
        String identification = "1725456055";
        AccountController controller = new AccountController(service);
        Account result = new Account();
        try {
            when (service.getLastAccountByIdentification(identification))
                    .thenReturn(result);
        } catch (DocumentNotFoundException ex) {
            
        }
        ResponseEntity response = ResponseEntity.ok(result);
        Assertions.assertEquals(response, controller.findLastAccountByIdentification(identification));        
    }
    
    @Test
    public void givenIdentificationReturnBalanceOfClient(){
        String identification = "1725456055";
        AccountController controller = new AccountController(service);
        BigDecimal result = new BigDecimal("0");
        try {
            when (service.getBalanceAccount(identification))
                    .thenReturn(result);
        } catch (DocumentNotFoundException ex) {
            
        }
        ResponseEntity response = ResponseEntity.ok(result);
        Assertions.assertEquals(response, controller.balanceClient(identification));        
    }
    
    @Test
    public void givenUpdateAccountStatusRqReturnOk(){
        UpdateAccountStatusRq updateAccount = new UpdateAccountStatusRq();
        AccountController controller = new AccountController(service);        
        ResponseEntity response = ResponseEntity.ok().build();
        Assertions.assertEquals(response, controller.updateStatus(updateAccount));        
    }
    
    @Test
    public void givenNullIdentificationReturnNotFound(){
        String identification = null;
        AccountController controller = new AccountController(service);
        try {
            Mockito.doThrow(DocumentNotFoundException.class)
                    .when (service)
                    .listAccounts(identification);
            Mockito.doThrow(DocumentNotFoundException.class)
                    .when (service)
                    .getLastAccountByIdentification(identification);
        } catch (DocumentNotFoundException ex) {
           Logger.getLogger(AccountControllerUnitTest.class.getName()).log(Level.SEVERE, null, ex); 
        }        
        ResponseEntity response = ResponseEntity.notFound().build();
        Assertions.assertEquals(response, controller.listAccounts(identification));        
        Assertions.assertEquals(response, controller.findLastAccountByIdentification(identification));        
    }
    
    @Test
    public void givenNullNumberReturnNotFound(){
        String number = null;
        AccountController controller = new AccountController(service);
        try {
            Mockito.doThrow(DocumentNotFoundException.class)
                    .when (service)
                    .getAccountByNumber(number);
        } catch (DocumentNotFoundException ex) {
           Logger.getLogger(AccountControllerUnitTest.class.getName()).log(Level.SEVERE, null, ex); 
        }
        ResponseEntity response = ResponseEntity.notFound().build();
        Assertions.assertEquals(response, controller.findAccountByNumber(number));              
    }
    
    @Test
    public void givenNullIdReturnNotFound(){
        Integer id = null;
        AccountController controller = new AccountController(service);
        try {
            Mockito.doThrow(DocumentNotFoundException.class)
                    .when (service)
                    .getAccountById(id);
        } catch (DocumentNotFoundException ex) {
           Logger.getLogger(AccountControllerUnitTest.class.getName()).log(Level.SEVERE, null, ex); 
        }
        ResponseEntity response = ResponseEntity.notFound().build();
        Assertions.assertEquals(response, controller.findAccountById(id));              
    }
}
