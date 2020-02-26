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
import pl.nethos.rekrutacja.services.AccountNotificationHandler;
import pl.nethos.rekrutacja.services.ResponseService;


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
        addKontrahentsGrid();
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

    private void addKontrahentsGrid() {

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

        Grid<Account> gridOfAccounts = new Grid<>(Account.class);
        initAccountsGrid(gridOfAccounts, kontrahent);

        Dialog dialog = addDialog(gridOfAccounts, kontrahent);

        dialog.open();
    }

    private Dialog addDialog(Grid<Account> gridOfAccounts, Kontrahent kontrahent) {

        Dialog dialog = new Dialog();
        dialog.add(gridOfAccounts);

        int length = kontrahent.getAccounts().size() * 50;
        dialog.setHeight(length < 100 ? "100px" : Integer.toString(length) + "px");
        dialog.setWidth("900px");

        return dialog;
    }

    private void initAccountsGrid(Grid<Account> gridOfAccounts, Kontrahent kontrahent) {
        gridOfAccounts.setItems(kontrahent.getAccounts());
        gridOfAccounts.removeColumnByKey("stan_weryfikacji");
        gridOfAccounts.removeColumnByKey("data_weryfikacji");
        gridOfAccounts.removeColumnByKey("id");
        gridOfAccounts.removeColumnByKey("id_kontrahent");
        gridOfAccounts.removeColumnByKey("wirtualne");
        gridOfAccounts.removeColumnByKey("aktywne");
        gridOfAccounts.removeColumnByKey("domyslne");
        gridOfAccounts.removeColumnByKey("numer");

        gridOfAccounts
                .addColumn(account -> {
                    if (account.getAktywne() == 1)
                        return "Tak";
                    else
                        return "Nie";
                })
                .setHeader("Aktywne");
        gridOfAccounts
                .addColumn(account -> {
                    if (account.getDomyslne() == 1)
                        return "Tak";
                    else
                        return "Nie";
                })
                .setHeader("Domyślne");
        gridOfAccounts
                .addColumn(account -> {
                    if (account.getWirtualne() == 1)
                        return "Tak";
                    else
                        return "Nie";
                })
                .setHeader("Wirtualne");

        gridOfAccounts.setSizeFull();
        gridOfAccounts
                .addColumn(account -> {
                    if (account.getNumer() != null)
                        return "xx xxxx xxxx xxxx xxxx xxxx xxxx";

                    else
                        return "brak numeru";
                })
                .setHeader("Numer")
                .setKey("numer");

        gridOfAccounts.getColumnByKey("numer").setWidth("200px");

        gridOfAccounts
                .addColumn(account -> {
                    if (account.getStan_weryfikacji() == null)
                        return "nieokreślony";
                    else if (account.getStan_weryfikacji().equals("0"))
                        return "błędne konto";
                    else
                        return "zweryfikowany";
                })
                .setHeader("Stan weryfikacji");

        gridOfAccounts.setSizeFull();

        gridOfAccounts.addItemClickListener(event -> {

            AccountNotificationHandler handler = new AccountNotificationHandler(event.getItem());
            String nip = handler.getNip(kontrahentRepository);
            String accountNumber = handler.getNumer();
            Result result = responseService.getResult(nip, accountNumber);

            new Notification("Ostatnio weryfikowano: " + handler.getNotificationText(accountRepository), 3000).open();
            accountRepository.updateEntity(event.getItem().getId(), result.getAccountAssigned(), result.getRequestDateTime());
            kontrahent.updateAccounts(event.getItem().getId(), accountRepository.getById(event.getItem().getId()));
            gridOfAccounts.setItems(kontrahent.getAccounts());

        });
    }
}
