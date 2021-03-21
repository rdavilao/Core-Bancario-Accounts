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
