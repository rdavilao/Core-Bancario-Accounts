/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.accounts.api.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data

public class UpdateAccountBalanceRQ {
    
    private String number;
    private BigDecimal balance;
    
}
