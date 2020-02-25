package pl.nethos.rekrutacja.repositories;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nethos.rekrutacja.Account;
import pl.nethos.rekrutacja.Kontrahent;

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
    public Account getById(long id){
        return em.createQuery("SELECT a FROM Account a WHERE a.id = ?1", Account.class).setParameter(1,id).getSingleResult();
    }
}
