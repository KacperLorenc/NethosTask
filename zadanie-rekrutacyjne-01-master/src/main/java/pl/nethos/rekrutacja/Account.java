package pl.nethos.rekrutacja;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_gen")
    @SequenceGenerator(name="account_gen", sequenceName = "account_seq", allocationSize = 1)
    private long id;


    private long id_kontrahent;
    private String numer;
    private int aktywne;
    private int domyslne;
    private int wirtualne;
    private String stan_weryfikacji;
    private Timestamp data_weryfikacji;

    public String getNumer() {
        return numer;
    }

    public int getAktywne() {
        return aktywne;
    }

    public int getDomyslne() {
        return domyslne;
    }

    public int getWirtualne() {
        return wirtualne;
    }

    public String getStan_weryfikacji() {
        return stan_weryfikacji;
    }

    public Timestamp getData_weryfikacji() {
        return data_weryfikacji;
    }

    public long getId() {
        return id;
    }

    public long getId_kontrahent() {
        return id_kontrahent;
    }

    public void setStan_weryfikacji(String stan_weryfikacji) {
        this.stan_weryfikacji = stan_weryfikacji;
    }

    public void setData_weryfikacji(Timestamp data_weryfikacji) {
        this.data_weryfikacji = data_weryfikacji;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        builder.append("Account{" +
                "id=" + id +
                ", id_kontrahent=" + id_kontrahent +
                ", numer='" + numer + '\'' +
                ", aktywne=" + aktywne +
                ", domyslne=" + domyslne +
                ", wirtualne=" + wirtualne);
        if (stan_weryfikacji!= null)
            builder.append(", stan weryfikacji=" + stan_weryfikacji);
        else
            builder.append(", stan weryfikacji=brak danych ");
        if (data_weryfikacji != null)
            builder.append(", data weryfikacji=" + data_weryfikacji + "}");
        else
            builder.append(", data weryfikacji=brak danych}");

        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id &&
                id_kontrahent == account.id_kontrahent &&
                aktywne == account.aktywne &&
                numer.equals(account.numer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, id_kontrahent, numer, aktywne);
    }
}
