package pl.nethos.rekrutacja;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nethos.rekrutacja.models.Kontrahent;
import pl.nethos.rekrutacja.repositories.KontrahentRepository;

@Route("/kontrahents")
public class KontrahentsGridGui extends VerticalLayout {
   private KontrahentRepository kontrahentRepository;

   @Autowired
    public KontrahentsGridGui(KontrahentRepository kontrahentRepository) {
        this.kontrahentRepository = kontrahentRepository;

        Grid <Kontrahent> gridOfKontrahents = new Grid<>(Kontrahent.class);
        gridOfKontrahents.setItems(kontrahentRepository.all());
        gridOfKontrahents.removeColumnByKey("id");
        add(gridOfKontrahents);
    }
}
