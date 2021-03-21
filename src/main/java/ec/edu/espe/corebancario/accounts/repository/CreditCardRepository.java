package ec.edu.espe.corebancario.accounts.repository;

import ec.edu.espe.corebancario.accounts.model.CreditCard;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CreditCardRepository extends CrudRepository<CreditCard, Integer> {

    CreditCard findByNumber(String number);

    List<CreditCard> findByCodAccountAndStatus(Integer codAccount, String status);

    CreditCard findFirstByOrderByNumberDesc();
}
