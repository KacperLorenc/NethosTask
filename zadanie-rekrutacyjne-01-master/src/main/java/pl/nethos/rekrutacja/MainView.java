package pl.nethos.rekrutacja;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nethos.rekrutacja.models.Result;
import pl.nethos.rekrutacja.repositories.AccountRepository;
import pl.nethos.rekrutacja.repositories.KontrahentRepository;
import pl.nethos.rekrutacja.services.ResponseService;

import java.util.Optional;


@Route
@PWA(name = "Nethos - Zadanie rekrutacyjne na stanowisko programisty", shortName = "Nethos - Rekrutacja")
@StyleSheet("/css/style.css")
public class MainView extends VerticalLayout {
    KontrahentRepository kontrahentRepository;
    AccountRepository accountRepository;
    ResponseService responseService;


    public MainView(@Autowired KontrahentRepository kontrahentRepository,
                    @Autowired AccountRepository accountRepository,
                    @Autowired ResponseService responseService) {

        this.kontrahentRepository = kontrahentRepository;
        this.accountRepository = accountRepository;
        this.responseService = responseService;

        addClassName("main-view");
        setSizeFull();
        addHeader();


        kontrahentsGrid();
        addFooter();
    }


    private void addHeader() {
        H1 header = new H1("Kontrahenci");
        header.getElement().getThemeList().add("dark");
        add(header);
    }

    private void addFooter() {
        Footer footer = new Footer();
        footer.getElement().getThemeList().add("dark");
        add(footer);
    }

    private void kontrahentsGrid() {

        Grid<Kontrahent> gridOfKontrahents = new Grid<>(Kontrahent.class);
        gridOfKontrahents.setItems(kontrahentRepository.all());
        gridOfKontrahents.removeColumnByKey("id");
        gridOfKontrahents.removeColumnByKey("accounts");
        gridOfKontrahents.setHeightByRows(true);
        gridOfKontrahents.setClassName("kontrahent-grid");
        gridOfKontrahents.addItemClickListener(event ->
            addAccountsGrid(event.getItem())
        );
        add(gridOfKontrahents);
    }

    private void addAccountsGrid(Kontrahent kontrahent) {
        Dialog dialog = new Dialog();
        Grid<Account> gridOfAccounts = new Grid<>(Account.class);
        gridOfAccounts.setItems(kontrahent.getAccounts());
        gridOfAccounts.setHeightByRows(true);
        dialog.add(gridOfAccounts);
        dialog.setHeight("300px");
        dialog.setWidth("900px");
        gridOfAccounts.addItemClickListener(event -> {

            Account account = event.getItem();
            String nip = kontrahentRepository.getById(account.getId_kontrahent()).getNip();
            String accountNumber = account.getNumer();
            Result result = responseService.getResult(nip, accountNumber);
            new Notification(result.toString(), 3000).open();
            System.out.println(accountRepository.updateEntity(account.getId(), result.getAccountAssigned(), result.getRequestDateTime()));
        });
        dialog.open();
    }
}
