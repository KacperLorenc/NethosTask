package pl.nethos.rekrutacja.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.nethos.rekrutacja.entities.Account;
import pl.nethos.rekrutacja.entities.Kontrahent;
import pl.nethos.rekrutacja.models.Result;
import pl.nethos.rekrutacja.repositories.AccountRepository;
import pl.nethos.rekrutacja.repositories.KontrahentRepository;
import pl.nethos.rekrutacja.services.AccountNotificationHandler;
import pl.nethos.rekrutacja.services.ResponseService;


//layout inside the dialog
public class DialogView extends VerticalLayout {

    //variables

    private final KontrahentRepository kontrahentRepository;
    private final AccountRepository accountRepository;
    private final ResponseService responseService;
    private final Kontrahent kontrahent;
    private final Grid<Account> gridOfAccounts;

    //constructor

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

    //methods

    //sets size of grid, its items, adds columns and formats them, sets on click listener
    private void initAccountsGrid(Grid<Account> gridOfAccounts, Kontrahent kontrahent) {

        gridOfAccounts.setItems(kontrahent.getAccounts());
        gridOfAccounts.removeAllColumns();

        //column numer
        gridOfAccounts
                .addColumn(account -> {
                    if (account.getNumer() != null)
                        return "xx xxxx xxxx xxxx xxxx xxxx xxxx";

                    else
                        return "brak numeru";
                })
                .setHeader("Numer")
                .setWidth("200px");

        //column aktywne
        gridOfAccounts
                .addColumn(account -> {
                    if (account.getAktywne() == 1)
                        return "Tak";
                    else
                        return "Nie";
                })
                .setHeader("Aktywne")
                .setWidth("60px");

        //column domyslne
        gridOfAccounts
                .addColumn(account -> {
                    if (account.getDomyslne() == 1)
                        return "Tak";
                    else
                        return "Nie";
                })
                .setHeader("Domyślne")
                .setWidth("60px");

        //column wirtualne
        gridOfAccounts
                .addColumn(account -> {
                    if (account.getWirtualne() == 1)
                        return "Tak";
                    else
                        return "Nie";
                })
                .setHeader("Wirtualne")
                .setWidth("60px");



        //column stan weryfikacji
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

        //size of grid
        gridOfAccounts.setHeightByRows(true);

        //calls verifyAndUpdate on every click
        gridOfAccounts.addItemClickListener(this::showNotificationAndUpdate);

        //grid added to view
        add(gridOfAccounts);

    }

    //adds label to a dialog
    private void addLabel() {
        Label text = new Label("Kliknij na wybrane konto aby je zweryfikować.");
        text.setClassName("kliknijLabel");
        add(text);
    }

    //sets size of view
    private void setViewSize() {
        int length = kontrahent.getAccounts().size() * 45 + 100;
        setHeight(length < 175 ? "175px" : Integer.toString(length) + "px");
        setMaxHeight("45%");
        setWidth("900px");
    }

    //gets result from resource service, creates notification,
    //updates database, set of accounts in chosen kontrahent and the grid itself
    private void showNotificationAndUpdate(ItemClickEvent<Account> event) {

        //creates handler that helps creating notification, takes account in constructor
        AccountNotificationHandler handler = new AccountNotificationHandler(event.getItem());

        //handler produces string by taking date of previous check from database
        new Notification("Ostatnio weryfikowano: " + handler.getNotificationText(accountRepository), 3000).open();


        String nip = handler.getNip(kontrahentRepository);  //gets nip of chosen kontrahent from repository
        String accountNumber = handler.getNumer();          //gets account number from kontrahent
        long id = handler.getId();                          //gets accounts id

        //response service creates a response from external api by passing nip and account number
        Result result = responseService.getResult(nip, accountNumber);

        //if response from external api isn't null, program updates database and grid,
        //else program shows error notification
        if (!update(id, result))
            new Notification("Nie można połączyć z https://wl-api.mf.gov.pl/api",3000).open();
    }

    private boolean update(long id, Result result){
        if (result==null) {
            return false;
        } else {
            accountRepository.updateEntity(id, result.getAccountAssigned(), result.getRequestDateTime());
            kontrahent.updateAccounts(id, accountRepository.getById(id));
            gridOfAccounts.setItems(kontrahent.getAccounts());
            return true;
        }
    }

}
