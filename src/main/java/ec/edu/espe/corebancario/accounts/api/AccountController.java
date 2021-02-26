/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.accounts.api;

import ec.edu.espe.corebancario.accounts.api.dto.UpdateAccountStatusRQ;
import ec.edu.espe.corebancario.accounts.api.dto.UpdateAccountBalanceRQ;
import ec.edu.espe.corebancario.accounts.exception.DocumentNotFoundException;
import ec.edu.espe.corebancario.accounts.exception.InsertException;
import ec.edu.espe.corebancario.accounts.exception.UpdateException;
import ec.edu.espe.corebancario.accounts.model.Account;
import ec.edu.espe.corebancario.accounts.service.AccountService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/listAccount/{id}")
    @ApiOperation(value = "Busqueda de cuenta/as por número de identificacion del cliente", notes = "Busqueda de cuenta/as por número de identificacion del cliente. Un cliente puede tener varias cuentas o al menos una.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Cuenta/as encontrada"),
        @ApiResponse(code = 404, message = "Cuenta/as no encontrada")
    })
    public ResponseEntity listAccounts(@PathVariable String id) {
        try {
            return ResponseEntity.ok(this.service.listAccounts(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/balanceClient/{identification}")
    @ApiOperation(value = "Balance de cuenta total del cliente", notes = "Balance de cuenta total del cliente. El balance de cuenta total de un cliente es la suma de balances de todas las cuentas de un mismo cliente")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Cuenta/as encontrada"),
        @ApiResponse(code = 404, message = "Cuenta/as no encontrada")
    })
    public ResponseEntity balanceClient(@PathVariable String identification) {
        try {
            return ResponseEntity.ok(this.service.getBalanceAccount(identification));
        } catch (DocumentNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    @ApiOperation(value = "Crea una cuenta", notes = "Crea una cuenta del cliente. Las cuentas permiten realizar transacciones.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Cuenta creada"),
        @ApiResponse(code = 400, message = "Error al crear cuenta")
    })
    public ResponseEntity create(@RequestBody Account account) {
        try {
            this.service.createAccount(account);
            return ResponseEntity.ok().build();
        } catch (InsertException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/updateStatus")
    @ApiOperation(value = "Actualizar el estado de una cuenta", notes = "Actualiza el estado de una cuenta")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Estado de cuenta actualizada"),
        @ApiResponse(code = 400, message = "Error al actualizar el estado cuenta")
    })
    public ResponseEntity updateStatus(@RequestBody UpdateAccountStatusRQ updateAccount) {
        try {
            this.service.updateStatus(updateAccount.getNumber(), updateAccount.getState());
            return ResponseEntity.ok().build();
        } catch (UpdateException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/updateBalance")
    @ApiOperation(value = "Actualizar el balance de una cuenta", notes = "Actualiza el balance de una cuenta")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Balance de cuenta actualizada"),
        @ApiResponse(code = 400, message = "Error al actualizar el balance de cuenta")
    })
    public ResponseEntity updateBalance(@RequestBody UpdateAccountBalanceRQ updateAccount) {
        try {
            this.service.updateBalance(updateAccount.getNumber(), updateAccount.getBalance());
            return ResponseEntity.ok().build();
        } catch (UpdateException ex) {
            return ResponseEntity.badRequest().build();
        }
    }    
}