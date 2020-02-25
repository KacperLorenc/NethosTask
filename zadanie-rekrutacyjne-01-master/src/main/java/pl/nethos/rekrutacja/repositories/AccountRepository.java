package pl.nethos.rekrutacja.repositories;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nethos.rekrutacja.Account;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AccountRepository {
    @PersistenceContext
    private EntityManager em;

    public List<Account> all() {
        return em.createQuery("SELECT a FROM Account a", Account.class).getResultList();
    }

    public Account getById(long id) {
        return em.createQuery("SELECT a FROM Account a WHERE a.id = ?1", Account.class).setParameter(1, id).getSingleResult();
    }

    @Transactional
    public Account updateEntity(long id, String status, String time) {
        Account account = getById(id);
        account.setStan_weryfikacji(status.equals("NIE") ? "0" : "1");
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
            Date parsedDate = dateFormat.parse(time);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            account.setData_weryfikacji(timestamp);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        em.persist(account);

        return account;
    }

}
