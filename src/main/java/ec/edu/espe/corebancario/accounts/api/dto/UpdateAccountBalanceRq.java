package ec.edu.espe.corebancario.accounts.api.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class UpdateAccountBalanceRq {
    
    private String number;
    private BigDecimal balance;
    
}
