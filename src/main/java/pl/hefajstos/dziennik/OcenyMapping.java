package pl.hefajstos.dziennik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.hefajstos.QuickJSONArray;
import pl.hefajstos.klasy.KlasyController;

@RestController
public class OcenyMapping {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/oceny/lista/{sid}/{klasa}/{prz}")
    public String mappingKlasyUczniowyieByKlasa(@PathVariable("sid") String sid, @PathVariable("klasa") String klasa, @PathVariable("prz") String pid) {
        return QuickJSONArray.fromList
        (
    "oceny",
                OcenyController.getOcenyByKlasaAndPrzedmiotId(jdbcTemplate, klasa, pid)
        );
    }
}
