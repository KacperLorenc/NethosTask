package pl.nethos.rekrutacja.services;

import pl.nethos.rekrutacja.Account;
import pl.nethos.rekrutacja.repositories.AccountRepository;
import pl.nethos.rekrutacja.repositories.KontrahentRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class AccountNotificationHandler {
    Account account;

    public AccountNotificationHandler(Account account) {
        this.account = account;
    }
    public String getNip(KontrahentRepository repository){
       return repository.getById(this.account.getId_kontrahent()).getNip();
    }
    public String getNumer(){
        return this.account.getNumer();
    }
    private Optional<Timestamp> getDataWeryfikacji(AccountRepository repository){
       return Optional.ofNullable(repository.getById(account.getId()).getData_weryfikacji());
    }
    public String getNotificationText(AccountRepository repository){
      Timestamp ts = getDataWeryfikacji(repository).orElse(Timestamp.valueOf(LocalDateTime.now()));
      LocalDateTime time = ts.toLocalDateTime();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy hh:mm:ss");
      return time.format(formatter);
    }
}
