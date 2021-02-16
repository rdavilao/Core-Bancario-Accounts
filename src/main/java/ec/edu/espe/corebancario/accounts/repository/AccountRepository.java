/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.accounts.repository;

import ec.edu.espe.corebancario.accounts.model.Account;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository <Account, Integer> {
    
    List<Account> findByClientIdentification(String clientIdentification);
    Account findByNumber(String number);
    Account findFirstByOrderByNumberDesc();
    Account findByClientIdentificationAndType(String clientIdentification,String type);
}
