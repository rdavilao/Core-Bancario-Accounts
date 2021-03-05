/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.accounts.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import ec.edu.espe.corebancario.accounts.exception.InsertException;
import ec.edu.espe.corebancario.accounts.exception.UpdateException;
import ec.edu.espe.corebancario.accounts.model.Account;
import ec.edu.espe.corebancario.accounts.enums.StateAccountEnum;
import ec.edu.espe.corebancario.accounts.exception.DocumentNotFoundException;
import ec.edu.espe.corebancario.accounts.repository.AccountRepository;
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
            HttpResponse<JsonNode> request = Unirest.get("http://localhost:8081/api/corebancario/client/findClientById/{id}")
                        .routeParam("id", account.getClientIdentification()).asJson();            
            if(200 == request.getStatus()){
            account.setCreationDate(new Date());            
            account.setNumber(newNumberAccount());
            account.setBalance(BigDecimal.ZERO);
            account.setStatus(StateAccountEnum.INACTIVO.getEstado());
            log.info("Creando cuenta " + account.getNumber());
            this.accountRepo.save(account);
            }else{
                log.error("Error al crear cuenta con cliente no existente: "+account.getClientIdentification());
                throw new InsertException("Account", "Error al crear cuenta con cliente no existente: " + account.toString());
            }
        } catch (Exception e) {
            throw new InsertException("Account", "Ocurrio un error al crear la cuenta: " + account.toString(), e);
        }
    }
    
    public List<Account> listAccounts(String identification) throws DocumentNotFoundException {
        try {
            List<Account> accounts = this.accountRepo.findByClientIdentification(identification);
            if(!accounts.isEmpty()){
                return this.accountRepo.findByClientIdentification(identification);
            }else{                
                throw  new DocumentNotFoundException("No existen cuentas asociadas al cliente: " + identification);
            }            
        } catch (Exception e) {
            throw  new DocumentNotFoundException("Ocurrio un error en listar las cuentas del cliente: " + identification);
        }
    }
    
    public void updateStatus(String number, String newStatus) throws UpdateException {
        try {
            Account accountUpdate = this.accountRepo.findByNumber(number);
            if (accountUpdate != null) {
                log.info("Cuenta: " + number + " se actualizo el estado a: " + newStatus);
                if (!(!accountUpdate.getBalance().equals(new BigDecimal(0)) && StateAccountEnum.INACTIVO.getEstado().equals(newStatus))){
                accountUpdate.setStatus(newStatus);
                this.accountRepo.save(accountUpdate);
                }else{
                log.error("Intento de cambio de estado a inactivo a una cuenta con balance distinto a 0.");
                throw new UpdateException("account", "Ocurrio un error al actualizar el estado de la de cuenta: " + number);
                }
            } else {
                log.error("Intento de cambio de estado a cuenta no existente: " + number);
                throw new UpdateException("account", "Ocurrio un error, no existe el numero de cuenta: " + number);
            }
        } catch (Exception e) {
            throw new UpdateException("account", "Ocurrio un error al actualizar el estado de la cuenta: " + number, e);
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
                    throw new UpdateException("account", "Ocurrio un error, no se permite actualizar el balance a una cuenta inactiva: " + number);
                }
            } else {
                log.error("Intento de cambio de balance a cuenta no existente: " + number);
                throw new UpdateException("account", "Ocurrio un error, no existe el numero de cuenta: " + number);
            }
        } catch (Exception e) {
            throw new UpdateException("account", "Ocurrio un error al actualizar el balance de la cuenta: " + number, e);
        }
    }

    public BigDecimal getBalanceAccount(String identification) throws DocumentNotFoundException {
        try {
            List<Account> accounts = this.accountRepo.findByClientIdentification(identification);
            if(!accounts.isEmpty()){
                BigDecimal balance = new BigDecimal(0);
                for (int i = 0; i < accounts.size(); i ++){
                    if (StateAccountEnum.ACTIVO.getEstado().equals(accounts.get(i).getStatus())){
                        balance = balance.add(accounts.get(i).getBalance());
                    }
                }
                return balance;
            }else{
                throw new DocumentNotFoundException("No existe cuentas para el cliente"+identification);
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Error al obtener balance de cuenta");
        }
    }
    
    public Account getAccountByNumber(String number) throws DocumentNotFoundException {
        try {
            Account account = this.accountRepo.findByNumber(number);
            if(account != null){
            return account;
            }else{
                throw new DocumentNotFoundException("Cuenta no existente");
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Error al obtener cuenta");
        }
    }
    
    public Account getAccountById(Integer id) throws DocumentNotFoundException {
        try {
            Optional<Account> accountFind = this.accountRepo.findById(id);
            if(!accountFind.isEmpty()){
            Account account = accountFind.get();
            return account;
            }else{
                throw new DocumentNotFoundException("Cuenta no existente");
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Error al obtener cuenta");
        }
    }
    
    public Account getLastAccountByIdentification(String identification) throws DocumentNotFoundException {
        try {
            List<Account> accountFind = this.accountRepo.findByClientIdentificationOrderByCreationDateDesc(identification, PageRequest.of(0, 1));
            if(!accountFind.isEmpty()){
            Account account = accountFind.get(0);
            return account;
            }else{
                throw new DocumentNotFoundException("Cuenta no existente");
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Error al obtener cuenta");
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
