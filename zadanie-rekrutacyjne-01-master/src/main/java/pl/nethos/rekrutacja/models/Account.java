package pl.nethos.rekrutacja.models;

import javax.persistence.*;
import java.sql.Timestamp;

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


    public long getId() {
        return id;
    }

    public long getId_kontrahent() {
        return id_kontrahent;
    }

    public void setId_kontrahent(long id_kontrahent) {
        this.id_kontrahent = id_kontrahent;
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

}
