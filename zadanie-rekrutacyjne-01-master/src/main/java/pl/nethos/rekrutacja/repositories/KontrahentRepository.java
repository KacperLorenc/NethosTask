package pl.nethos.rekrutacja.repositories;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nethos.rekrutacja.entities.Kontrahent;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional
public class KontrahentRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Kontrahent> all() {
        return em.createQuery("SELECT k FROM Kontrahent k", Kontrahent.class).getResultList();
    }
    public Kontrahent getById(long id){
        return em.createQuery("SELECT k FROM Kontrahent k WHERE k.id = ?1",Kontrahent.class).setParameter(1,id).getSingleResult();
    }

}
