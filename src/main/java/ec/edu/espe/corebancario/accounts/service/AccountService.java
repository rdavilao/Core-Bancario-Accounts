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

public class AccountService {
    
    private final AccountRepository accountRepo;
    
    public AccountService(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }
    
    public void createClient(Account account) throws InsertException {
        try {
            account.setStatus(StateAccountEnum.INACTIVO.getEstado());
            account.setCreationDate(new Date());
            account.setCurrentBalance(BigDecimal.ZERO);
            this.accountRepo.save(account);
        } catch (Exception e) {
            throw new InsertException("Account", "Ocurrio un error al crear la cuenta: " + account.toString(), e);
        }
    }
    
    public void updateStatus(String number, String newStatus) throws UpdateException{
        try {
            Account accountUpdate = this.accountRepo.findByNumber(number);
            if(accountUpdate != null) {              
                accountUpdate.setStatus(newStatus);
                this.accountRepo.save(accountUpdate);
            }else{
                throw new UpdateException("account", "Ocurrio un error, no existe el numero de cuenta: " + number);
            }
        } catch (Exception e) {
            throw new UpdateException("account", "Ocurrio un error al actualizar el estado de la cuenta: " + number, e);
        }
    }
    
}
