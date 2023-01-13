package pl.hefajstos.klasy.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneratorMapping
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/klasy/generuj/agresywnie/{sid}/{min}/{max}")
    public String mappingKlasyGenerowanie (@PathVariable String sid, @PathVariable Integer min, @PathVariable Integer max)
    {
        Parametry parametry = new Parametry();
        parametry.setMaxUczniowWKlasie(max);
        parametry.setMinUczniowWKlasie(min);
        RaportAgresywny r = Agresywny.generuj(jdbcTemplate, parametry);
        return r.toJson();
    }
}
