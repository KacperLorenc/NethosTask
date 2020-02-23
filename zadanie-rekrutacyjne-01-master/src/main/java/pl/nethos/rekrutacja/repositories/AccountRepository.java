package pl.nethos.rekrutacja.repositories;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nethos.rekrutacja.models.Account;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional
public class AccountRepository {
    @PersistenceContext
    private EntityManager em;

    public List<Account> all() {
        return em.createQuery("SELECT a FROM Account a", Account.class).getResultList();
    }
}
