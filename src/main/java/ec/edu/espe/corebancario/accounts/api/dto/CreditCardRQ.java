/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.accounts.api.dto;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data

public class CreditCardRQ {

    private String number;
    private Integer limitAccount;
    private Date expirationDate;
    private String account;
    private BigDecimal balanceAccount;
}
