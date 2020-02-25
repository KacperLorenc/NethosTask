package pl.nethos.rekrutacja;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nethos.rekrutacja.repositories.AccountRepository;
import pl.nethos.rekrutacja.repositories.KontrahentRepository;


@Route
@PWA(name = "Nethos - Zadanie rekrutacyjne na stanowisko programisty", shortName = "Nethos - Rekrutacja")
@StyleSheet("/css/style.css")
public class MainView extends VerticalLayout {
    KontrahentRepository kontrahentRepository;
    AccountRepository accountRepository;


    public MainView(@Autowired KontrahentRepository kontrahentRepository,
                    @Autowired AccountRepository accountRepository) {

        this.kontrahentRepository = kontrahentRepository;
        this.accountRepository = accountRepository;

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
    private void addFooter(){
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
        gridOfKontrahents.addItemClickListener(event -> {
                addAccountsGrid(event.getItem());
            System.out.println(kontrahentRepository.getById(event.getItem().getId()));
        });
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
        gridOfAccounts.addItemClickListener(event ->{
            System.out.println(accountRepository.getById(event.getItem().getId()));
        });
        dialog.open();
    }
}
