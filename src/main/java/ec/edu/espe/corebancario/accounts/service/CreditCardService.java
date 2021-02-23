/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.accounts.service;

import com.google.common.hash.Hashing;
import ec.edu.espe.corebancario.accounts.enums.StateAccountEnum;
import ec.edu.espe.corebancario.accounts.enums.TypeCreditCardEnum;
import ec.edu.espe.corebancario.accounts.exception.DocumentNotFoundException;
import ec.edu.espe.corebancario.accounts.exception.InsertException;
import ec.edu.espe.corebancario.accounts.model.Account;
import ec.edu.espe.corebancario.accounts.model.CreditCard;
import ec.edu.espe.corebancario.accounts.model.TypeAccount;
import ec.edu.espe.corebancario.accounts.repository.AccountRepository;
import ec.edu.espe.corebancario.accounts.repository.CreditCardRepository;
import ec.edu.espe.corebancario.accounts.repository.TypeAccountRepository;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CreditCardService {
    
    private final CreditCardRepository creditCardRepo;
    private final AccountRepository accountRepo;
    private final TypeAccountRepository typeAccountRepo;
    
    public CreditCardService(CreditCardRepository creditCardRepo, AccountRepository accountRepo, TypeAccountRepository typeAccountRepo) {
        this.creditCardRepo = creditCardRepo;
        this.accountRepo = accountRepo;
        this.typeAccountRepo = typeAccountRepo;
    }
    
    public void createCreditCard(CreditCard creditCard) throws InsertException {
        try {
            Random numberRandom = new Random();
            creditCard.setNumber(newNumberCreditCard(creditCard.getCodAccount(), numberRandom));
            creditCard.setCvv(Hashing.sha256()
                    .hashString(String.valueOf(numberRandom.nextInt(899)+100)
                    ,StandardCharsets.UTF_8).toString()
            );
            creditCard.setCreationDate(new Date());
            creditCard.setExpirationDate(expirationDate());
            creditCard.setStatus(StateAccountEnum.INACTIVO.getEstado());
            this.creditCardRepo.save(creditCard);
        } catch (Exception e) {
            throw new InsertException("CreditCard", "Ocurrio un error al crear la la tarjeta de credito: " + creditCard.toString(), e);
        }
    }
    
    public List<CreditCard> listCreditCardActiva(Integer codAccount) throws DocumentNotFoundException {
        try {
            List<CreditCard> creditCards = this.creditCardRepo.findByCodAccountAndStatus(codAccount,StateAccountEnum.ACTIVO.getEstado());
            if(!creditCards.isEmpty()){
                return creditCards;
            }else{                
                throw  new DocumentNotFoundException("No existen tarjetas de credito activas del cliente.");
            }            
        } catch (Exception e) {
            throw  new DocumentNotFoundException("Ocurrio un error en listar las tarjetas de credito");
        }
    }
    
    private String newNumberCreditCard(Integer codAccount, Random random) throws DocumentNotFoundException  {
        StringBuilder numberCreditCard = new StringBuilder();
        try {
            Optional<Account> accountRes = this.accountRepo.findById(codAccount);
            if(accountRes.isPresent()){
                Account account = accountRes.get();
                Optional<TypeAccount> typeAccountRes = this.typeAccountRepo.findById(account.getType());
                if(typeAccountRes.isPresent()){
                    TypeAccount typeAccount = typeAccountRes.get();
                    if(TypeCreditCardEnum.VISA.gettype().equals(typeAccount.getType())){
                        numberCreditCard.append(TypeCreditCardEnum.VISA.getIdentificator());
                    }else{
                        numberCreditCard.append(TypeCreditCardEnum.MASTERCARD.getIdentificator());
                    }
                }
                numberCreditCard.append(random.nextInt(89999)+10000);
                numberCreditCard.append(account.getNumber());
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Error en asignacion de nuevo numero de cuenta" + e);
        }
        return numberCreditCard.toString();
    }
    
    private Date expirationDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR,5);
        return calendar.getTime();
    }
    
}
