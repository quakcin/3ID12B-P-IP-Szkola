package pl.hefajstos.nauczyciele;

import com.sun.xml.bind.v2.model.annotation.Quick;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.autoryzacja.Sesja;
import pl.hefajstos.autoryzacja.SesjaController;
import pl.hefajstos.hefajstos.QuickJSON;
import pl.hefajstos.hefajstos.QuickJSONArray;
import pl.hefajstos.autoryzacja.RodzajKonta;



import java.sql.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class NauczycieleMapping
{
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @GetMapping("/nauczyciel/info/{sid}/{id}")
    public String mappingNauczycielInfo (@PathVariable("sid") String sid, @PathVariable("id") String id)
    {
        Nauczyciel n = NauczycieleController.getNauczycielById(jdbcTemplate, id);
        return n != null
            ? n.toJson()
            : QuickJSON.RESP_BAD;
    }

    @GetMapping("/nauczyciel/lista/{sid}")
    public String mappingNauczycielLista (@PathVariable("sid") String sid)
    {
        return QuickJSONArray.fromList("nauczyciele",
                NauczycieleController.getListaNauczycieli(jdbcTemplate));
    }

    @GetMapping("/nauczyciel/usun/{sid}/{id}")
    public String mappingNauczycielUsun (@PathVariable("sid") String sid, @PathVariable("id") String id)
    {
        return NauczycieleController.usunNauczyciela(jdbcTemplate, id)
            ? QuickJSON.RESP_OK
            : QuickJSON.RESP_BAD;

    }

    @GetMapping("/nauczyciel/dodaj/{sid}/{imie}/{nazw}/{zatr}/{zaw}/{wych}/{prz}")
    public String mappingNauczycielDodaj
    (
            @PathVariable("sid") String sid,
            @PathVariable("imie") String imie,
            @PathVariable("nazw") String nazw,
            @PathVariable("zatr") Date zatr,
            @PathVariable("zaw") String zaw,
            @PathVariable("wych") String wych,
            @PathVariable("prz") String prz
    )
    {
        Nauczyciel nowyNauczyciel = new Nauczyciel();
        nowyNauczyciel.setNauczycielId(null);
        nowyNauczyciel.setImie(imie);
        nowyNauczyciel.setNazwisko(nazw);
        nowyNauczyciel.setDataZatrudnienia(zatr);
        nowyNauczyciel.setKlasaId(wych);
        nowyNauczyciel.setStopienZawodowy(zaw);
        UUID id = NauczycieleController.dodajNauczycielaDoBazy(jdbcTemplate, nowyNauczyciel, prz);
        return id == null
            ? QuickJSON.RESP_BAD
            : (new QuickJSON()).addRaw("ok", "true")
                .add("uid", id.toString())
                .ret();
    }

    @GetMapping("/nauczyciel/edytuj/{sid}/{imie}/{nazw}/{zatr}/{zaw}/{wych}/{prz}/{id}")
    public String mappingNauczycielEdytuj
    (
        @PathVariable("sid") String sid,
        @PathVariable("imie") String imie,
        @PathVariable("nazw") String nazw,
        @PathVariable("zatr") Date zatr,
        @PathVariable("zaw") String zaw,
        @PathVariable("wych") String wych,
        @PathVariable("prz") String prz,
        @PathVariable("id") String id
    )
    {
        Nauczyciel nowyNauczyciel = new Nauczyciel();
        nowyNauczyciel.setImie(imie);
        nowyNauczyciel.setNazwisko(nazw);
        nowyNauczyciel.setDataZatrudnienia(zatr);
        nowyNauczyciel.setKlasaId(wych);
        nowyNauczyciel.setStopienZawodowy(zaw);
        nowyNauczyciel.setNauczycielId(id);

        return NauczycieleController.aktualizujNauczycielaWBazie(jdbcTemplate, nowyNauczyciel, prz)
                ? QuickJSON.RESP_OK : QuickJSON.RESP_BAD;
    }

    @GetMapping("/nauczyciel/lekcje/{sid}")
    public String mappingNauczycielLekcje (@PathVariable("sid") String sid)
    {
        List<LekcjaNauczyciela> lekcjaNauczyciele = NauczycieleController.getLekcjeNauczycielaBySession(jdbcTemplate, sid);
        return QuickJSONArray.fromList("lekcje", lekcjaNauczyciele);
    }
}
