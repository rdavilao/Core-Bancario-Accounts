package ec.edu.espe.corebancario.accounts.api;

import ec.edu.espe.corebancario.accounts.api.dto.CreditCardRq;
import ec.edu.espe.corebancario.accounts.exception.DocumentNotFoundException;
import ec.edu.espe.corebancario.accounts.model.CreditCard;
import ec.edu.espe.corebancario.accounts.service.CreditCardService;
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
public class CreditCardControllerUnitTest {
 
    @Mock
    private CreditCardService service;
    
    @InjectMocks
    CreditCard creditCard;
    
    @BeforeEach
    public void setUp() {
        creditCard = new CreditCard();
    }
    
    @Test
    public void givenNumberReturnCreditCard(){
        String number = "4240000000000001";
        CreditCardController controller = new CreditCardController(service);
        CreditCard result = new CreditCard();
        try {
            when (service.findCreditCard(number))
                    .thenReturn(result);
        } catch (DocumentNotFoundException ex) {
            
        }
        ResponseEntity response = ResponseEntity.ok(result);
        Assertions.assertEquals(response, controller.findCreditCard(number));
    }
    
    /*
    @Test
    public void givenIdentificationReturnListOfCreditCardRq(){
        String identification = "1725456055";
        CreditCardController controller = new CreditCardController(service);
        List<CreditCardRq> result = new ArrayList<CreditCardRq>();
        try {
            when (service.listCreditCardActiva(identification))
                    .thenReturn(result);
        } catch (DocumentNotFoundException ex) {
            
        }
        ResponseEntity response = ResponseEntity.ok(result);
        Assertions.assertEquals(response, controller.findCreditCard(identification));
    }*/
    
    @Test
    public void givenNullNumberReturnNotFound(){
        String number = null;
        CreditCardController controller = new CreditCardController(service);
        try {
            Mockito.doThrow(DocumentNotFoundException.class)
                    .when (service)
                    .findCreditCard(number);
        } catch (DocumentNotFoundException ex) {
           Logger.getLogger(CreditCardControllerUnitTest.class.getName()).log(Level.SEVERE, null, ex); 
        }
        ResponseEntity response = ResponseEntity.notFound().build();
         Assertions.assertEquals(response, controller.findCreditCard(number));
    }
    
    @Test
    public void givenNullIdentificationReturnNotFound(){
        String identification = null;
        CreditCardController controller = new CreditCardController(service);
        try {
            Mockito.doThrow(DocumentNotFoundException.class)
                    .when (service)
                    .listCreditCardActiva(identification);
        } catch (DocumentNotFoundException ex) {
           Logger.getLogger(CreditCardControllerUnitTest.class.getName()).log(Level.SEVERE, null, ex); 
        }
        ResponseEntity response = ResponseEntity.notFound().build();
         Assertions.assertEquals(response, controller.listAccounts(identification));
    }
}
