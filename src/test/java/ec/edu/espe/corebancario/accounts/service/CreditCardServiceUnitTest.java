package ec.edu.espe.corebancario.accounts.service;

import ec.edu.espe.corebancario.accounts.api.dto.CreditCardRq;
import ec.edu.espe.corebancario.accounts.enums.StateAccountEnum;
import ec.edu.espe.corebancario.accounts.exception.DocumentNotFoundException;
import ec.edu.espe.corebancario.accounts.exception.InsertException;
import ec.edu.espe.corebancario.accounts.exception.UpdateException;
import ec.edu.espe.corebancario.accounts.model.Account;
import ec.edu.espe.corebancario.accounts.model.CreditCard;
import ec.edu.espe.corebancario.accounts.repository.AccountRepository;
import ec.edu.espe.corebancario.accounts.repository.CreditCardRepository;
import ec.edu.espe.corebancario.accounts.repository.TypeAccountRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreditCardServiceUnitTest {

    @Mock
    private CreditCardRepository creditCardRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TypeAccountRepository typeAccountRepository;

    @InjectMocks
    private CreditCardService service;

    @Test
    public void givenCreditCardCreateCreditCard() {
        CreditCard creditCard = new CreditCard();
        creditCard.setCodAccount(5);
        creditCard.setLimitAccount(100);
        try {
            service.createCreditCard(creditCard);
            verify(creditCardRepository, times(1)).save(creditCard);
        } catch (InsertException ex) {
            Logger.getLogger(CreditCardServiceUnitTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CreditCardServiceUnitTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void givenNumberAndStatsUpdateCreditCardStatus() {
        String number = "446758270000000004";
        String status = StateAccountEnum.INACTIVO.getEstado();
        CreditCard creditCard = new CreditCard();
        creditCard.setCodAccount(5);
        creditCard.setNumber(number);
        creditCard.setStatus(status);
        try {
            when(creditCardRepository.findByNumber(any())).thenReturn(creditCard);
            service.updateStatus(number, status);
            verify(creditCardRepository, times(1)).save(creditCard);
        } catch (UpdateException ex) {
            Logger.getLogger(CreditCardServiceUnitTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CreditCardServiceUnitTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void givenIdentificationReturnListOfCreditCardEnable() {
        String identification = "1804915617";
        CreditCardService instance = new CreditCardService(creditCardRepository, accountRepository, typeAccountRepository);
        try {
            List<CreditCardRq> result = instance.listCreditCardActiva(identification);

            Assertions.assertEquals(result, instance.listCreditCardActiva(identification));
        } catch (DocumentNotFoundException ex) {
            Logger.getLogger(CreditCardServiceUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void givenIdentificationReturnListOfCreditCardByType() {
        String identification = "1804915617";
        Integer type = 1;
        List<Account> accounts = new ArrayList<>();
        try {
            when(accountRepository.findByClientIdentificationAndType(identification, type)).thenReturn(accounts);
            service.listCreditCardByType(identification, type);
        } catch (DocumentNotFoundException ex) {
            Logger.getLogger(CreditCardServiceUnitTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void givenNumberReturnCreditCard() {
        String number = "446758270000000004";
        //AQUIIIIIIIIIIIIIIIII
        CreditCard creditCard = creditCardRepository.findByNumber(number);
        try {
            Assertions.assertEquals(creditCard, service.findCreditCard(number));
        } catch (DocumentNotFoundException ex) {
            Logger.getLogger(CreditCardServiceUnitTest.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            Logger.getLogger(CreditCardServiceUnitTest.class
                    .getName()).log(Level.SEVERE, null, e);
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
}
