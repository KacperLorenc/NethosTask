package pl.nethos.rekrutacja.views;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nethos.rekrutacja.entities.Account;
import pl.nethos.rekrutacja.entities.Kontrahent;
import pl.nethos.rekrutacja.repositories.AccountRepository;
import pl.nethos.rekrutacja.repositories.KontrahentRepository;
import pl.nethos.rekrutacja.services.ResponseService;


@Route
@PWA(name = "Nethos - Zadanie rekrutacyjne na stanowisko programisty", shortName = "Nethos - Rekrutacja")
@StyleSheet("/css/style.css")
public class MainView extends VerticalLayout {

    //variables

    private final KontrahentRepository kontrahentRepository;
    private final AccountRepository accountRepository;
    private final ResponseService responseService;

    //constructor

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

    }

    //methods

    private void addHeader() {
        H1 header = new H1("Kontrahenci");
        header.getElement().getThemeList().add("dark");
        add(header);
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
        gridOfKontrahents.getColumnByKey("nip").setHeader("NIP");
        gridOfKontrahents.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_WRAP_CELL_CONTENT);
        add(gridOfKontrahents);
    }

    private void addAccountsGrid(Kontrahent kontrahent) {

        Grid<Account> gridOfAccounts = new Grid<>(Account.class);
        DialogView dialogView = new DialogView(kontrahentRepository, accountRepository, responseService, gridOfAccounts, kontrahent);
        Dialog dialog = new Dialog(dialogView);

        dialog.open();
    }


}
