/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.accounts.api;

import ec.edu.espe.corebancario.accounts.exception.InsertException;
import ec.edu.espe.corebancario.accounts.model.CreditCard;
import ec.edu.espe.corebancario.accounts.service.CreditCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/corebancario/creditCard")
@Slf4j
public class CreditCardController {
    
    private final CreditCardService service;

    public CreditCardController(CreditCardService service) {
        this.service = service;
    }
    
    @GetMapping("/listCreditCard/{codigo}")
    public ResponseEntity listAccounts(@PathVariable Integer codigo){
        try {
            return ResponseEntity.ok(this.service.listCreditCardActiva(codigo));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/create")
    public ResponseEntity create(@RequestBody CreditCard creditCard) {
        try {
            this.service.createCreditCard(creditCard);
            return ResponseEntity.ok().build();
        } catch (InsertException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    
}
