package pl.hefajstos.klasy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.autoryzacja.RodzajKonta;
import pl.hefajstos.autoryzacja.SesjaController;
import pl.hefajstos.hefajstos.QuickJSON;
import pl.hefajstos.hefajstos.QuickJSONArray;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

@RestController
public class KlasyMapping
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/klasy/uczniowie/{sid}/{klasa}")
    public String mappingKlasyUczniowyieByKlasa (@PathVariable("sid") String sid, @PathVariable("klasa") String klasa)
    {
        RodzajKonta konto = SesjaController.getRodzajKontaBySesjaId(jdbcTemplate, sid);
        if (konto.equals(RodzajKonta.Dyrektor) == false && konto.equals(RodzajKonta.Nauczyciel) == false)
            return QuickJSON.RESP_BAD;

        return QuickJSONArray.fromList
        (
            "lista",
            KlasyController.getListaUczniowByKlasa(jdbcTemplate, klasa)
        );
    }

    @GetMapping("/klasy/lista/{sid}")
    public String mappingKlasyLista (@PathVariable("sid") String sid)
    {
        RodzajKonta konto = SesjaController.getRodzajKontaBySesjaId(jdbcTemplate, sid);
        if (konto.equals(RodzajKonta.Dyrektor) == false && konto.equals(RodzajKonta.Nauczyciel) == false)
            return QuickJSON.RESP_BAD;

        return QuickJSONArray
                .fromList("klasy", KlasyController.getListaKlas(jdbcTemplate));
    }

    @GetMapping("/klasy/lista_bez_wychowawcow/{sid}")
    public String mappingKlasyBezWychowawcowLista (@PathVariable("sid") String sid)
    {
        if (SesjaController.getRodzajKontaBySesjaId(jdbcTemplate, sid).equals(RodzajKonta.Dyrektor) == false)
            return QuickJSON.RESP_BAD;

        return QuickJSONArray
                .fromList("klasy", KlasyController.getListaBezWychowawcow(jdbcTemplate));
    }

}
