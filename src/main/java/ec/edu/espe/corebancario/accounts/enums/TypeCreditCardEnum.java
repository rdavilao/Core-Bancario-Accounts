/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.accounts.enums;


public enum TypeCreditCardEnum {
    VISA("VISA", "4"),
    MASTERCARD("MASTERCARD", "5");
    
    private final String type;
    private final String identificator;

    private TypeCreditCardEnum(String type, String identificator) {
        this.type = type;
        this.identificator = identificator;
    }

    public String gettype() {
        return type;
    }

    public String getIdentificator() {
        return identificator;
    }
}
