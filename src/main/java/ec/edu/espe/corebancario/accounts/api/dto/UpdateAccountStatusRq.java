package ec.edu.espe.corebancario.accounts.api.dto;

import lombok.Data;

@Data
public class UpdateAccountStatusRq {
    
    private String number;
    private String state;
    
}
