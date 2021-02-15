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
import ec.edu.espe.corebancario.accounts.repository.AccountRepository;
import java.math.BigDecimal;
import java.util.Date;
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
            account.setStatus(StateAccountEnum.INACTIVO.getEstado());
            log.info("Creando cuenta "+account.getStatus());
            account.setCreationDate(new Date());
            account.setCurrentBalance(BigDecimal.ZERO);
            log.info("Creando cuenta "+account.getStatus());
            account.setNumber(newNumberAccount());
            log.info("Creando cuenta "+account.getNumber());
            this.accountRepo.save(account);
        } catch (Exception e) {
            throw new InsertException("Account", "Ocurrio un error al crear la cuenta: " + account.toString(), e);
        }
    }

    public void updateStatus(String number, String newStatus) throws UpdateException {
        try {
            Account accountUpdate = this.accountRepo.findByNumber(number);
            if (accountUpdate != null) {
                accountUpdate.setStatus(newStatus);
                this.accountRepo.save(accountUpdate);
            } else {
                throw new UpdateException("account", "Ocurrio un error, no existe el numero de cuenta: " + number);
            }
        } catch (Exception e) {
            throw new UpdateException("account", "Ocurrio un error al actualizar el estado de la cuenta: " + number, e);
        }
    }

    private String newNumberAccount() {
        Account account = this.accountRepo.findFirstByOrderByNumberDesc();
        log.info("NewNUMBER:  "+account.toString());
        String number = "";
        if (account != null) {
           number = Integer.toString(Integer.parseInt(account.getNumber())+1);           
        } else {
           number = "270000000001";
        }
        return number;
    }

}
