package ec.edu.espe.corebancario.accounts.service;

import ec.edu.espe.corebancario.accounts.api.dto.CreditCardRq;
import ec.edu.espe.corebancario.accounts.enums.StateAccountEnum;
import ec.edu.espe.corebancario.accounts.exception.DocumentNotFoundException;
import ec.edu.espe.corebancario.accounts.exception.InsertException;
import ec.edu.espe.corebancario.accounts.exception.UpdateException;
import ec.edu.espe.corebancario.accounts.model.CreditCard;
import ec.edu.espe.corebancario.accounts.repository.AccountRepository;
import ec.edu.espe.corebancario.accounts.repository.CreditCardRepository;
import ec.edu.espe.corebancario.accounts.repository.TypeAccountRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreditCardServiceUnitTest {

    @Mock
    private CreditCardRepository creditCardRepository;
    private AccountRepository accountRepository;
    private TypeAccountRepository typeAccountRepository;

    @InjectMocks
    private CreditCard creditCard;
    private CreditCardService service;

    @BeforeEach
    public void setUp() {
        creditCard = new CreditCard();
    }

    @Test
    public void givenCreditCardCreateCreditCard() {
        creditCard.setCodAccount(1);
        creditCard.setLimitAccount(100);
        try {
            service.createCreditCard(creditCard);
        } catch (InsertException ex) {
            Logger.getLogger(CreditCardServiceUnitTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CreditCardServiceUnitTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void givenNumberAndStatsUpdateCreditCardStatus(){
        String number = "4245000000000001";
        String status = StateAccountEnum.INACTIVO.getEstado();
        try {
            service.updateStatus(number,status);
        } catch (UpdateException ex) {
            Logger.getLogger(CreditCardServiceUnitTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CreditCardServiceUnitTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    @Test
    public void givenIdentificationReturnListOfCreditCardEnable(){
        String identification = "1725456055";
        List<Account> accounts = accountRepository.findByClientIdentification(identification);        
        CreditCardService service = new CreditCardService(creditCardRepository,accountRepository,typeAccountRepository);
        try {
            Assertions.assertEquals(accounts, service.listCreditCardActiva(identification));
        } catch (DocumentNotFoundException ex) {
            Logger.getLogger(AccountServiceUnitTest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void givenNullCreditCardThrowInsertException(){
        CreditCard creditCard = null; 
        CreditCardService service = new CreditCardService(creditCardRepository,accountRepository,typeAccountRepository);
        Assertions.assertThrows(InsertException.class, () -> service.createCreditCard(creditCard));
    }
    @Test
    public void givenOneCreditCardCreateThis() {
        CreditCard creditCard = new CreditCard();
        CreditCardService service = new CreditCardService(creditCardRepository, accountRepository, typeAccountRepository); 
        try {
            service.createCreditCard(creditCard);
        } catch (InsertException ex) {
            Logger.getLogger(CreditCardServiceUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Test
    public void GivenCreditCardAndProducedErrorCreatingCreditCardThrowInsertException() {
        when(creditCardRepository.save(any())).thenThrow(InsertException.class);
        Assertions.assertThrows(InsertException.class, () -> service.createCreditCard(creditCard));
    }
     */    
    @Test
    public void givenIdentificationReturnListOfCreditCardEnable() {
        String identification = "1725456055";
        List<CreditCardRq> creditCardsRq = new ArrayList<CreditCardRq>();
        CreditCardService service = new CreditCardService(creditCardRepository, accountRepository, typeAccountRepository);
        try {
            Assertions.assertEquals(creditCardsRq, service.listCreditCardActiva(identification));
        } catch (DocumentNotFoundException ex) {
            Logger.getLogger(CreditCardServiceUnitTest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void givenNumberReturnCreditCard() {
        String number = "4245000000000001";
        CreditCard creditCard = creditCardRepository.findByNumber(number);
        CreditCardService service = new CreditCardService(creditCardRepository, accountRepository, typeAccountRepository);
        try {
            Assertions.assertEquals(creditCard, service.findCreditCard(number));
        } catch (DocumentNotFoundException ex) {
            Logger.getLogger(CreditCardServiceUnitTest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void givenNullIdentificationThrowDocumentNotFoundException() {
        String identification = null;
        CreditCardService service = new CreditCardService(creditCardRepository, accountRepository, typeAccountRepository);
        Assertions.assertThrows(DocumentNotFoundException.class, () -> service.listCreditCardActiva(identification));
        Assertions.assertThrows(DocumentNotFoundException.class, () -> service.findCreditCard(identification));
    }

    @Test
    public void givenNullIdentificationAndStatusThrowUpdateException() {
        String identification = null;
        String status = "ACT";
        CreditCardService service = new CreditCardService(creditCardRepository, accountRepository, typeAccountRepository);
        Assertions.assertThrows(UpdateException.class, () -> service.updateStatus(identification, status));
    }

    @Test
    public void givenNullIdentificationAndTypeThrowDocumentNotFoundException() {
        String identification = null;
        Integer type = 4;
        CreditCardService service = new CreditCardService(creditCardRepository, accountRepository, typeAccountRepository);
        Assertions.assertThrows(DocumentNotFoundException.class, () -> service.listCreditCardByType(identification, type));
    }

    @Test
    public void givenEmptyCreditCardThrowInsertException() {
        CreditCard creditCard = new CreditCard();
        CreditCardService service = new CreditCardService(creditCardRepository, accountRepository, typeAccountRepository);
        Assertions.assertThrows(InsertException.class, () -> service.createCreditCard(creditCard));
    }
}
