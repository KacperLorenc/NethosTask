package pl.nethos.rekrutacja;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nethos.rekrutacja.models.Account;
import pl.nethos.rekrutacja.repositories.AccountRepository;
import pl.nethos.rekrutacja.models.Kontrahent;
import pl.nethos.rekrutacja.repositories.KontrahentRepository;

@Route
@PWA(name = "Nethos - Zadanie rekrutacyjne na stanowisko programisty", shortName = "Nethos - Rekrutacja")
public class MainView extends VerticalLayout {

    public MainView(@Autowired KontrahentRepository kontrahentRepository,
                    @Autowired AccountRepository accountRepository) {
        setSizeFull();

        wyswietl(kontrahentRepository, accountRepository);
    }

    private void wyswietl(KontrahentRepository kontrahentRepository, AccountRepository accountRepository) {
        for (Kontrahent kontrahent : kontrahentRepository.all()) {
            add(new Label(kontrahent.toString()));
            if(kontrahent.getAccounts() == null){
              add( new Label("Empty"));
            }
        }
        for (Account account : accountRepository.all()) {
            add(new Label(account.toString()));
        }
    }
}
