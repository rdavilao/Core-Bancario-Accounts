package ec.edu.espe.corebancario.accounts.api.dto;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class CreditCardRq {

    private String number;
    private Integer limitAccount;
    private Date expirationDate;
    private String account;
    private BigDecimal balanceAccount;
}
