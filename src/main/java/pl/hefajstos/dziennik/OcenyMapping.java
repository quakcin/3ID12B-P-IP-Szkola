package pl.hefajstos.dziennik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.hefajstos.QuickJSON;
import pl.hefajstos.hefajstos.QuickJSONArray;
import pl.hefajstos.klasy.KlasyController;

@RestController
public class OcenyMapping {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/oceny/dodaj/{sid}/{prz}/{ucz}/{ocn}/{wga}/{msg}/{kat}")
    public String mappingOcenyDodajOcene
    (
        @PathVariable("sid") String sid,
        @PathVariable("prz") String przedmiot,
        @PathVariable("ucz") String uczen,
        @PathVariable("ocn") String ocena,
        @PathVariable("wga") String waga,
        @PathVariable("msg") String komentarz,
        @PathVariable("kat") String kategoria
    )
    {
        return OcenyController.addOcenaByUczenIdInPrzedmiotId(jdbcTemplate, sid, uczen, przedmiot, ocena, waga, komentarz, kategoria)
            ? QuickJSON.RESP_OK : QuickJSON.RESP_BAD;
    }
    @GetMapping("/oceny/lista/{sid}/{klasa}/{prz}")
    public String mappingKlasyUczniowyieByKlasa(@PathVariable("sid") String sid, @PathVariable("klasa") String klasa, @PathVariable("prz") String pid) {
        return QuickJSONArray.fromList
        (
    "lista",
                OcenyController.getOcenyByKlasaAndPrzedmiotId(jdbcTemplate, klasa, pid)
        );
    }

    @GetMapping("/oceny/usun/{sid}/{oid}")
    public String mappingOcenyUsunOcene
        (
            @PathVariable("sid") String sid,
            @PathVariable("oid") String oid
        )
    {
        return QuickJSONArray.fromList
            (
                "oceny", null
                //OcenyController.getOcenyByKlasaAndPrzedmiotId(jdbcTemplate, )
            );
    }
}
