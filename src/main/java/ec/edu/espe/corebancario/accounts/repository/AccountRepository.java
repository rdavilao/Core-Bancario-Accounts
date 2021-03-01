/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.accounts.repository;

import ec.edu.espe.corebancario.accounts.model.Account;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Integer> {

    List<Account> findByClientIdentification(String clientIdentification);

    Account findByNumber(String number);

    Account findFirstByOrderByNumberDesc();

    List<Account> findByClientIdentificationAndType(String clientIdentification, Integer type);

    List<Account> findByClientIdentificationOrderByCreationDateDesc(String clientIdentification, Pageable limit);

}
