package pl.nethos.rekrutacja.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.nethos.rekrutacja.Account;
import pl.nethos.rekrutacja.Kontrahent;
import pl.nethos.rekrutacja.models.Result;
import pl.nethos.rekrutacja.repositories.AccountRepository;
import pl.nethos.rekrutacja.repositories.KontrahentRepository;
import pl.nethos.rekrutacja.services.AccountNotificationHandler;
import pl.nethos.rekrutacja.services.ResponseService;

public class DialogView extends VerticalLayout {

    private final KontrahentRepository kontrahentRepository;
    private final AccountRepository accountRepository;
    private final ResponseService responseService;
    private final Kontrahent kontrahent;
    Grid<Account> gridOfAccounts;

    public DialogView(KontrahentRepository kontrahentRepository, AccountRepository accountRepository, ResponseService responseService, Grid<Account> gridOfAccounts, Kontrahent kontrahent) {
        this.kontrahentRepository = kontrahentRepository;
        this.accountRepository = accountRepository;
        this.responseService = responseService;
        this.kontrahent = kontrahent;
        this.gridOfAccounts = gridOfAccounts;

        setViewSize();
        addLabel();
        initAccountsGrid(gridOfAccounts, kontrahent);

    }

    private void initAccountsGrid(Grid<Account> gridOfAccounts, Kontrahent kontrahent) {

        gridOfAccounts.setItems(kontrahent.getAccounts());
        gridOfAccounts.removeAllColumns();

        gridOfAccounts
                .addColumn(account -> {
                    if (account.getAktywne() == 1)
                        return "Tak";
                    else
                        return "Nie";
                })
                .setHeader("Aktywne")
                .setWidth("60px");
        gridOfAccounts
                .addColumn(account -> {
                    if (account.getDomyslne() == 1)
                        return "Tak";
                    else
                        return "Nie";
                })
                .setHeader("Domyślne")
                .setWidth("60px");
        gridOfAccounts
                .addColumn(account -> {
                    if (account.getWirtualne() == 1)
                        return "Tak";
                    else
                        return "Nie";
                })
                .setHeader("Wirtualne")
                .setWidth("60px");


        gridOfAccounts
                .addColumn(account -> {
                    if (account.getNumer() != null)
                        return "xx xxxx xxxx xxxx xxxx xxxx xxxx";

                    else
                        return "brak numeru";
                })
                .setHeader("Numer")
                .setWidth("200px");

        gridOfAccounts
                .addColumn(account -> {
                    if (account.getStan_weryfikacji() == null) {
                        return "nieokreślony";
                    } else if (account.getStan_weryfikacji().equals("0")) {
                        return "błędne konto";
                    } else {
                        return "zweryfikowany";
                    }
                })
                .setHeader("Stan weryfikacji");

        gridOfAccounts.setHeightByRows(true);

        gridOfAccounts.addItemClickListener(this::verify);

        add(gridOfAccounts);

    }

    private void addLabel() {
        Label text = new Label("Kliknij rekord konta aby je zweryfikować.");
        text.setClassName("kliknijLabel");
        add(text);
    }

    private void setViewSize() {
        int length = kontrahent.getAccounts().size() * 45 + 100;
        setHeight(length < 175 ? "175px" : Integer.toString(length) + "px");
        setMaxHeight("45%");
        setWidth("900px");
    }

    private void verify(ItemClickEvent<Account> event) {
        AccountNotificationHandler handler = new AccountNotificationHandler(event.getItem());

        String nip = handler.getNip(kontrahentRepository);
        String accountNumber = handler.getNumer();
        long id = event.getItem().getId();
        Result result = responseService.getResult(nip, accountNumber);

        new Notification("Ostatnio weryfikowano: " + handler.getNotificationText(accountRepository), 3000).open();

        accountRepository.updateEntity(id, result.getAccountAssigned(), result.getRequestDateTime());
        kontrahent.updateAccounts(id, accountRepository.getById(id));
        gridOfAccounts.setItems(kontrahent.getAccounts());
    }
}
