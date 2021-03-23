package ec.edu.espe.corebancario.accounts.api;

import ec.edu.espe.corebancario.accounts.api.dto.CreditCardRq;
import ec.edu.espe.corebancario.accounts.api.dto.UpdateAccountStatusRq;
import ec.edu.espe.corebancario.accounts.exception.InsertException;
import ec.edu.espe.corebancario.accounts.exception.UpdateException;
import ec.edu.espe.corebancario.accounts.model.CreditCard;
import ec.edu.espe.corebancario.accounts.service.CreditCardService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/corebancario/creditCard")
@Slf4j
public class CreditCardController {

    private final CreditCardService service;

    public CreditCardController(CreditCardService service) {
        this.service = service;
    }

    @GetMapping("/findCreditCard/{number}")
    @ApiOperation(value = "Busqueda de tarjeta de credito por su numero", 
            notes = "Una numero de tarjeta de credito es unico para cada tarjeta")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tarjeta de credito encontrada"),
            @ApiResponse(code = 404, message = "No existen tarjeta de credito")
    })
    public ResponseEntity<CreditCard> findCreditCard(@PathVariable String number) {
        try {
            return ResponseEntity.ok(this.service.findCreditCard(number));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/listCreditCard/{identification}")
    @ApiOperation(value = "Busqueda de tarjetas de credito activas asociadas a una cuenta", 
            notes = "Una cuenta puede tener asociada varias tarjetas de credito")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tarjetas de credito activas encontradas"),
            @ApiResponse(code = 404, message = "No existen tarjetas de credito activas")
    })
    public ResponseEntity<List<CreditCardRq>> listAccounts(@PathVariable String identification) {
        try {
            return ResponseEntity.ok(this.service.listCreditCardActiva(identification));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/listCreditCardClient")
    @ApiOperation(value = "Busqueda de tarjetas de credito activas asociadas a una cuenta",
            notes = "Una cuenta puede tener asociada varias tarjetas de credito")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tarjetas de credito activas encontradas"),
            @ApiResponse(code = 404, message = "No existen tarjetas de credito activas")
    })
    public ResponseEntity<List<CreditCardRq>> listCreditCardClient(@RequestParam String identification,
            @RequestParam Integer type) {
        try {
            return ResponseEntity.ok(this.service.listCreditCardByType(identification, type));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateStatus")
    @ApiOperation(value = "Actualizar el estado de una tarjeta de credito",
            notes = "Actualiza el estado de una tarjeta de credito")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Estado de tarjeta de credito actualizada"),
            @ApiResponse(code = 400, message = "Error al actualizar el tarjeta de credito cuenta")
    })
    public ResponseEntity updateStatus(@RequestBody UpdateAccountStatusRq updateAccount) {
        ResponseEntity<?> response;
        try {
            this.service.updateStatus(updateAccount.getNumber(), updateAccount.getState());
            response = ResponseEntity.ok().build();
        } catch (UpdateException ex) {
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @PostMapping("/create")
    @ApiOperation(value = "Crea una tarjeta de credito",
            notes = "Crea una tarjeta de credito.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tarjeta de credito creada"),
            @ApiResponse(code = 400, message = "Error al crear una tarjeta de credito")
    })
    public ResponseEntity create(@RequestBody CreditCard creditCard) {
        ResponseEntity<?> response;
        try {
            this.service.createCreditCard(creditCard);
            response = ResponseEntity.ok().build();
        } catch (InsertException ex) {
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }
}
