package ec.edu.espe.corebancario.accounts.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import ec.edu.espe.corebancario.accounts.constants.DomainConstant;
import ec.edu.espe.corebancario.accounts.enums.StateAccountEnum;
import ec.edu.espe.corebancario.accounts.exception.DocumentNotFoundException;
import ec.edu.espe.corebancario.accounts.exception.InsertException;
import ec.edu.espe.corebancario.accounts.exception.UpdateException;
import ec.edu.espe.corebancario.accounts.model.Account;
import ec.edu.espe.corebancario.accounts.repository.AccountRepository;
import ec.edu.espe.corebancario.accounts.security.Authorization;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepo;

    public AccountService(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    public void createAccount(Account account) throws InsertException {
        try {
            Authorization authorizationRq = new Authorization("rdavila", "espe123.");
            String authorization = authorizationRq.tokenAuthorizate();
            HttpResponse<JsonNode> request = Unirest.get(DomainConstant.DOMAINCLIENT + "/findClientById/{id}")
                    .header("Authorization", authorization)
                    .routeParam("id", account.getClientIdentification()).asJson();
            if (200 == request.getStatus()) {
                account.setCreationDate(new Date());
                account.setNumber(newNumberAccount());
                account.setBalance(BigDecimal.ZERO);
                account.setStatus(StateAccountEnum.INACTIVO.getEstado());
                log.info("Creando cuenta " + account.getNumber());
                this.accountRepo.save(account);
            } else {
                log.error("Error creando cuenta con cliente no existente: " + account.getClientIdentification());
                throw new InsertException(Account.class.getSimpleName(),
                        "Error al crear cuenta con cliente no existente: "
                        + account.toString());
            }
        } catch (Exception e) {
            log.error("Error al crear cuenta");
            throw new InsertException(Account.class.getSimpleName(),
                    "Ocurrio un error al crear la cuenta: " + account.toString(), e);
        }
    }

    public List<Account> listAccounts(String identification) throws DocumentNotFoundException {
        try {
            List<Account> accounts = this.accountRepo.findByClientIdentification(identification);
            if (!accounts.isEmpty()) {
                log.info("Listando cuentas por identificacion: " + identification);
                return this.accountRepo.findByClientIdentification(identification);
            } else {
                log.error("No hay cuentas a nombre del cliente: " + identification);
                throw new DocumentNotFoundException("No existen cuentas asociadas al cliente: " + identification);
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Ocurrio un error en listar las cuentas del cliente: "
                    + identification);
        }
    }

    public void updateStatus(String number, String newStatus) throws UpdateException {
        try {
            Account accountUpdate = this.accountRepo.findByNumber(number);
            if (accountUpdate != null) {
                log.info("Cuenta: " + number + " se actualizo el estado a: " + newStatus);
                if (!(!accountUpdate.getBalance().equals(new BigDecimal(0))
                        && StateAccountEnum.INACTIVO.getEstado().equals(newStatus))) {
                    accountUpdate.setStatus(newStatus);
                    this.accountRepo.save(accountUpdate);
                } else {
                    log.error("Intento de cambio de estado a inactivo a una cuenta con balance distinto a 0.");
                    throw new UpdateException(Account.class.getSimpleName(),
                            "Ocurrio un error al actualizar el estado de la de cuenta: "
                            + number);
                }
            } else {
                log.error("Intento de cambio de estado a cuenta no existente: " + number);
                throw new UpdateException(Account.class.getSimpleName(),
                        "Ocurrio un error, no existe el numero de cuenta: " + number);
            }
        } catch (Exception e) {
            throw new UpdateException(Account.class.getSimpleName(),
                    "Ocurrio un error al actualizar el estado de la cuenta: " + number, e);
        }
    }

    public void updateBalance(String number, BigDecimal balance) throws UpdateException {
        try {
            Account accountUpdate = this.accountRepo.findByNumber(number);
            if (accountUpdate != null) {
                if (StateAccountEnum.ACTIVO.getEstado().equals(accountUpdate.getStatus())) {
                    log.info("Cuenta: " + number + " se actualizo su balance: " + balance);
                    accountUpdate.setBalance(balance);
                    this.accountRepo.save(accountUpdate);
                } else {
                    log.error("Intento de cambio de balance a cuenta no activa: " + number);
                    throw new UpdateException(Account.class.getSimpleName(), "Ocurrio un error,"
                            + " no se permite actualizar el balance a una cuenta inactiva: " + number);
                }
            } else {
                log.error("Intento de cambio de balance a cuenta no existente: " + number);
                throw new UpdateException(Account.class.getSimpleName(),
                        "Ocurrio un error, no existe el numero de cuenta: " + number);
            }
        } catch (Exception e) {
            throw new UpdateException(Account.class.getSimpleName(),
                    "Ocurrio un error al actualizar el balance de la cuenta: "
                    + number, e);
        }
    }

    public BigDecimal getBalanceAccount(String identification) throws DocumentNotFoundException {
        try {
            List<Account> accounts = this.accountRepo.findByClientIdentification(identification);
            if (!accounts.isEmpty()) {
                BigDecimal balance = new BigDecimal(0);
                for (int i = 0; i < accounts.size(); i++) {
                    if (StateAccountEnum.ACTIVO.getEstado().equals(accounts.get(i).getStatus())) {
                        balance = balance.add(accounts.get(i).getBalance());
                    }
                }
                log.info("Obteniendo balance del cliente: " + identification);
                return balance;
            } else {
                log.info("No existen cuentas o no existen cuenta activas del cliente: " + identification);
                throw new DocumentNotFoundException("No existe cuentas para el cliente" + identification);
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Error al obtener balance de cuenta");
        }
    }

    public Account getAccountByNumber(String number) throws DocumentNotFoundException {
        try {
            Account account = this.accountRepo.findByNumber(number);
            if (account != null) {
                log.info("Obteniendo cuenta por su numero: " + number);
                return account;
            } else {
                log.info("Cuenta con numero no existente: " + number);
                throw new DocumentNotFoundException("Cuenta buscada por numero no existente");
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Error al buscar por numero de cuenta");
        }
    }

    public Account getAccountById(Integer id) throws DocumentNotFoundException {
        try {
            Optional<Account> accountFind = this.accountRepo.findById(id);
            if (!accountFind.isEmpty()) {
                log.info("Obteniendo cuenta por su id: " + id);
                return accountFind.get();
            } else {
                log.error("Cuenta con id no existente: " + id);
                throw new DocumentNotFoundException("Cuenta buscada por ID no existente");
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Error al buscar cuenta por codigo");
        }
    }

    public Account getLastAccountByIdentification(String identification) throws DocumentNotFoundException {
        try {
            List<Account> accountFind
                    = this.accountRepo
                            .findByClientIdentificationOrderByCreationDateDesc(identification, PageRequest.of(0, 1));
            if (!accountFind.isEmpty()) {
                log.info("Obteniendo ultima cuenta creada de un cliente: " + identification);
                return accountFind.get(0);
            } else {
                log.error("Cliente no tiene cuentas registradas: " + identification);
                throw new DocumentNotFoundException("Ultima cuenta no existente");
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Error al obtener ultima cuenta");
        }
    }

    private String newNumberAccount() throws DocumentNotFoundException {
        String numberAccount;
        try {
            Account account = this.accountRepo.findFirstByOrderByNumberDesc();
            if (account != null) {
                BigDecimal number = new BigDecimal(account.getNumber());
                number = number.add(new BigDecimal(1));
                numberAccount = number.toString();
            } else {
                numberAccount = "270000000001";
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Error en asignacion de nuevo numero de cuenta" + e);
        }
        return numberAccount;
    }

}
