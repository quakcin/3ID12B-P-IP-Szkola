package pl.hefajstos.hefajstos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import pl.hefajstos.nauczyciele.Nauczyciel;
import pl.hefajstos.uczen.Uczen;
import pl.hefajstos.uczen.UczenSpecjalny;
import pl.hefajstos.uczen.UczenZAspergerem;

import java.sql.Date;

@SpringBootTest
@AutoConfigureMockMvc
public class UczniowieSpecjalni
{
    @Test
    public void powinnoPoprawnieUtworzycNowegoUczniaSpecjalnegoISprawdzicJegoWlasciwosci () throws Exception {
        Uczen u = new Uczen();
        u.setId("testoweId");
        u.setImie("Imie");
        u.setNazwisko("Nazwisko");
        u.setPesel("12345678987");
        u.setMiejsceUrodzenia("Kielce");
        u.setKlasa("0");
        UczenZAspergerem uczenZAspergerem = new UczenZAspergerem();
        uczenZAspergerem.setUczen(u);
        uczenZAspergerem.setNauczycielWspomagajacy(new Nauczyciel("nauczycielskieId", "Imie", "Nazwisko", new Date(15), "Doktor", "1", "Bez"));
        assert (uczenZAspergerem instanceof UczenZAspergerem);
        assert (uczenZAspergerem instanceof UczenSpecjalny);

    }
}
