/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.accounts.repository;

import ec.edu.espe.corebancario.accounts.model.TypeAccount;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface TypeAccountRepository extends CrudRepository<TypeAccount, Integer> {

    List<TypeAccount> findByCodigo(Integer codigo);

    List<TypeAccount> findByName(String name);

    TypeAccount findByType(String type);

}
