package ec.edu.espe.corebancario.accounts.service;

import com.google.common.hash.Hashing;
import ec.edu.espe.corebancario.accounts.api.dto.CreditCardRq;
import ec.edu.espe.corebancario.accounts.enums.StateAccountEnum;
import ec.edu.espe.corebancario.accounts.enums.TypeCreditCardEnum;
import ec.edu.espe.corebancario.accounts.exception.DocumentNotFoundException;
import ec.edu.espe.corebancario.accounts.exception.InsertException;
import ec.edu.espe.corebancario.accounts.exception.UpdateException;
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

    public CreditCardService(CreditCardRepository creditCardRepo,
            AccountRepository accountRepo, TypeAccountRepository typeAccountRepo) {
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
            throw new InsertException(CreditCardService.class.getSimpleName(),
                    "Ocurrio un error al crear la la tarjeta de credito: "
                    + creditCard.toString(), e);
        }
    }

    public CreditCard findCreditCard(String number) throws DocumentNotFoundException {
        try {
            CreditCard creditCard = this.creditCardRepo.findByNumber(number);
            if (creditCard != null) {
                return creditCard;
            } else {
                throw new DocumentNotFoundException("No existe la tarjeta de credito.");
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Ocurrio un error en listar la tarjeta de credito " + e);
        }
    }

    public List<CreditCardRq> listCreditCardActiva(String identification) throws DocumentNotFoundException {
        try {
            List<Account> accounts = this.accountRepo.findByClientIdentification(identification);
            if (!accounts.isEmpty()) {
                return sendListCreditCardRq(accounts, identification);
            } else {
                throw new DocumentNotFoundException("No existen tarjetas de credito a nombre de ese cliente: "
                        + identification);
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Ocurrio un error en listar las tarjetas de credito "
                    + e);
        }
    }

    public List<CreditCardRq> listCreditCardByType(String identification,
            Integer type) throws DocumentNotFoundException {
        try {
            List<Account> accounts = this.accountRepo.findByClientIdentificationAndType(identification, type);
            if (!accounts.isEmpty()) {
                return sendListCreditCardRq(accounts, identification);
            } else {
                throw new DocumentNotFoundException("No existen tarjetas de credito a nombre de ese cliente: "
                        + identification);
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Error en buscar tarjetas de credito de un cliente" + e);
        }
    }

    public void updateStatus(String number, String newStatus) throws UpdateException {
        try {
            CreditCard creditCardUpdate = this.creditCardRepo.findByNumber(number);
            if (creditCardUpdate != null) {
                log.info("Tarjeta de credito: " + number + " se actualizo el estado a: " + newStatus);
                creditCardUpdate.setStatus(newStatus);
                this.creditCardRepo.save(creditCardUpdate);
            } else {
                log.error("Intento de cambio de estado a una tarjeta de credito no existente");
                throw new UpdateException("credit card",
                        "Ocurrio un error al actualizar el estado de la tarjeta de credito: " + number);
            }
        } catch (Exception e) {
            throw new UpdateException("credit card",
                    "Ocurrio un error al actualizar el estado de la tarjeta de credito: " + number, e);
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

    private List<CreditCardRq> sendListCreditCardRq(List<Account> accounts, String identification)
            throws DocumentNotFoundException {
        List<CreditCardRq> creditCardsRq = new ArrayList<>();
        for (int i = 0; i < accounts.size(); i++) {
            if (4 == accounts.get(i).getType() || 5 == accounts.get(i).getType()) {
                List<CreditCard> creditCards
                        = this.creditCardRepo
                                .findByCodAccountAndStatus(
                                        accounts.get(i).getCodigo(), StateAccountEnum.ACTIVO.getEstado());
                if (!creditCards.isEmpty()) {
                    for (int j = 0; j < creditCards.size(); j++) {
                        CreditCardRq cardRq = new CreditCardRq();
                        cardRq.setNumber(creditCards.get(j).getNumber());
                        cardRq.setLimitAccount(creditCards.get(j).getLimitAccount());
                        cardRq.setExpirationDate(creditCards.get(j).getExpirationDate());
                        cardRq.setAccount(accounts.get(i).getNumber());
                        cardRq.setBalanceAccount(accounts.get(i).getBalance());
                        creditCardsRq.add(cardRq);
                    }
                } else {
                    throw new DocumentNotFoundException("No existen tarjetas de credito activas de: "
                            + identification);
                }
            }
        }
        return creditCardsRq;
    }

}
