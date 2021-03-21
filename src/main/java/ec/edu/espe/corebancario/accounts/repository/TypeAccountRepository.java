package ec.edu.espe.corebancario.accounts.repository;

import ec.edu.espe.corebancario.accounts.model.TypeAccount;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface TypeAccountRepository extends CrudRepository<TypeAccount, Integer> {

    List<TypeAccount> findByCodigo(Integer codigo);

    List<TypeAccount> findByName(String name);

    TypeAccount findByType(String type);

}
