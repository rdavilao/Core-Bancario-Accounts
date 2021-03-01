/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.accounts.service;

import com.google.common.hash.Hashing;
import ec.edu.espe.corebancario.accounts.api.dto.CreditCardRQ;
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
import java.util.ArrayList;
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
                    .hashString(String.valueOf(numberRandom.nextInt(899) + 100),
                             StandardCharsets.UTF_8).toString()
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
            List<CreditCard> creditCards = this.creditCardRepo.findByCodAccountAndStatus(codAccount, StateAccountEnum.ACTIVO.getEstado());
            if (!creditCards.isEmpty()) {
                return creditCards;
            } else {
                throw new DocumentNotFoundException("No existen tarjetas de credito activas del cliente.");
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Ocurrio un error en listar las tarjetas de credito");
        }
    }

    public List<CreditCardRQ> listCreditCardByType(String identification, Integer type) throws DocumentNotFoundException {
        try {
            List<Account> accounts = this.accountRepo.findByClientIdentificationAndType(identification, type);
            if (!accounts.isEmpty()) {
                List<CreditCardRQ> creditCardsRQ = new ArrayList<>();
                for (int i = 0; i < accounts.size(); i++) {
                    List<CreditCard> creditCards = this.creditCardRepo.findByCodAccountAndStatus(accounts.get(i).getCodigo(), StateAccountEnum.ACTIVO.getEstado());
                    if (!creditCards.isEmpty()) {
                        for (int j = 0; j < creditCards.size(); j++) {
                            CreditCardRQ cardRQ = new CreditCardRQ();
                            cardRQ.setNumber(creditCards.get(j).getNumber());
                            cardRQ.setLimitAccount(creditCards.get(j).getLimitAccount());
                            cardRQ.setExpirationDate(creditCards.get(j).getExpirationDate());
                            cardRQ.setAccount(accounts.get(i).getNumber());
                            creditCardsRQ.add(cardRQ);
                        }
                    } else {
                        throw new DocumentNotFoundException("No existen tarjetas de credito activas a nombre de ese cliente: " + identification);
                    }
                }
                return creditCardsRQ;
            } else {
                throw new DocumentNotFoundException("No existen tarjetas de credito a nombre de ese cliente: " + identification);
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Error en buscar tarjetas de credito de un cliente" + e);
        }

    }

    private String newNumberCreditCard(Integer codAccount, Random random) throws DocumentNotFoundException {
        StringBuilder numberCreditCard = new StringBuilder();
        try {
            Optional<Account> accountRes = this.accountRepo.findById(codAccount);
            if (accountRes.isPresent()) {
                Account account = accountRes.get();
                Optional<TypeAccount> typeAccountRes = this.typeAccountRepo.findById(account.getType());
                if (typeAccountRes.isPresent()) {
                    TypeAccount typeAccount = typeAccountRes.get();
                    if (TypeCreditCardEnum.VISA.gettype().equals(typeAccount.getType())) {
                        numberCreditCard.append(TypeCreditCardEnum.VISA.getIdentificator());
                    } else {
                        numberCreditCard.append(TypeCreditCardEnum.MASTERCARD.getIdentificator());
                    }
                }
                numberCreditCard.append(random.nextInt(89999) + 10000);
                numberCreditCard.append(account.getNumber());
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Error en asignacion de nuevo numero de cuenta" + e);
        }
        return numberCreditCard.toString();
    }

    private Date expirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, 5);
        return calendar.getTime();
    }

}
