/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.accounts.api;

import ec.edu.espe.corebancario.accounts.api.dto.UpdateAccountRQ;
import ec.edu.espe.corebancario.accounts.exception.InsertException;
import ec.edu.espe.corebancario.accounts.exception.UpdateException;
import ec.edu.espe.corebancario.accounts.model.Account;
import ec.edu.espe.corebancario.accounts.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController

@RequestMapping("/api/corebancario/account")
@Slf4j

public class AccountController {
    
    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }
    
    @PostMapping("/create")
    public ResponseEntity create(@RequestBody Account account) {
        try {
            this.service.createAccount(account);
            return ResponseEntity.ok().build();
        } catch (InsertException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/update")
    public ResponseEntity update(@RequestBody UpdateAccountRQ updateAccount) {
        try {
            log.info(updateAccount.getNumber()+" - "+updateAccount.getState());
            this.service.updateStatus(updateAccount.getNumber(),updateAccount.getState());
            return ResponseEntity.ok().build();
        } catch (UpdateException ex) {
            return ResponseEntity.badRequest().build();
        }
    }   
}
