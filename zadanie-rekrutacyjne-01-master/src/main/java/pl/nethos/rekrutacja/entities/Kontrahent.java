package pl.nethos.rekrutacja.entities;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Kontrahent {

    //variables
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kontrahent_gen")
    @SequenceGenerator(name = "kontrahent_gen", sequenceName = "kontrahent_seq", allocationSize = 1)
    private long id;
    private String nazwa;
    private String nip;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_kontrahent")
    private Set<Account> accounts;

    //getters

    public Set<Account> getAccounts() {
        return accounts;
    }

    public long getId() {
        return id;
    }

    public String getNazwa() {
        return nazwa;
    }


    public String getNip() {
        return nip;
    }

    //methods

    //updates set of accounts
    public void updateAccounts(long accountId, Account account) {
        if (account != null) {
            this.accounts = accounts
                    .stream()
                    .map(x -> {
                        if (x.getId() == accountId) {
                            return account;
                        }
                        return x;
                    })
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public String toString() {
        return "Kontrahent{" +
                "id=" + id +
                ", nazwa='" + nazwa + '\'' +
                ", nip='" + nip + '\'' +

                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kontrahent that = (Kontrahent) o;
        return id == that.id &&
                nazwa.equals(that.nazwa) &&
                nip.equals(that.nip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nazwa, nip);
    }
}
