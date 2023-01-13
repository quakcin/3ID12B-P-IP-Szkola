package pl.hefajstos.klasy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.hefajstos.Jsonable;
import pl.hefajstos.hefajstos.QuickJSON;
import pl.hefajstos.hefajstos.QuickJSONArray;
import pl.hefajstos.klasy.generator.Agresywny;
import pl.hefajstos.klasy.generator.Parametry;
import pl.hefajstos.klasy.generator.Raport;

import java.util.List;

@RestController
public class KlasyMapping
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/klasy/uczniowie/{sid}/{klasa}")
    public String mappingKlasyUczniowyieByKlasa (@PathVariable("sid") String sid, @PathVariable("klasa") String klasa)
    {
        return QuickJSONArray.fromList
        (
            "lista",
            KlasyController.getListaUczniowByKlasa(jdbcTemplate, klasa)
        );
    }

    @GetMapping("/klasy/lista/{sid}")
    public String mappingKlasyLista (@PathVariable("sid") String sid)
    {
        return QuickJSONArray
                .fromList("klasy", KlasyController.getListaKlas(jdbcTemplate));
    }

    @GetMapping("/klasy/lista_bez_wychowawcow/{sid}")
    public String mappingKlasyBezWychowawcowLista (@PathVariable("sid") String sid)
    {
        return QuickJSONArray
                .fromList("klasy", KlasyController.getListaBezWychowawcow(jdbcTemplate));
    }




}
