/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.accounts.service;

import ec.edu.espe.corebancario.accounts.exception.InsertException;
import ec.edu.espe.corebancario.accounts.exception.UpdateException;
import ec.edu.espe.corebancario.accounts.model.Account;
import ec.edu.espe.corebancario.accounts.enums.StateAccountEnum;
import ec.edu.espe.corebancario.accounts.exception.DocumentNotFoundException;
import ec.edu.espe.corebancario.accounts.repository.AccountRepository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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
            account.setCreationDate(new Date());
            account.setNumber(newNumberAccount());
            account.setBalance(BigDecimal.ZERO);
            account.setStatus(StateAccountEnum.INACTIVO.getEstado());
            log.info("Creando cuenta " + account.getNumber());
            this.accountRepo.save(account);
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
                log.info("Cuenta: "+number+" se actualizo el estado a: "+newStatus);
                accountUpdate.setStatus(newStatus);
                this.accountRepo.save(accountUpdate);
            } else {
                log.error("Intento de cambio de estado a cuenta no existente: "+number);
                throw new UpdateException("account", "Ocurrio un error, no existe el numero de cuenta: " + number);
            }
        } catch (Exception e) {
            throw new UpdateException("account", "Ocurrio un error al actualizar el estado de la cuenta: " + number, e);
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
