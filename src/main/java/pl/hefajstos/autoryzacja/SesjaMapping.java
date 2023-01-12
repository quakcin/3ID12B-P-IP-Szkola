package pl.hefajstos.autoryzacja;

import com.sun.xml.bind.v2.model.annotation.Quick;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.hefajstos.QuickJSON;

@RestController
public class SesjaMapping
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/zaloguj/{sid}/{usr}/{pwd}")
    public String mappingZaloguj
    (
        @PathVariable("sid") String sid,
        @PathVariable("usr") String usr,
        @PathVariable("pwd") String pwd)
    {
        return SesjaKontroler.zaloguj(jdbcTemplate, usr, pwd).toJson();
    }

    @GetMapping("/kredytywnosc/{sid}")
    public String mappingKredytywnosc ( @PathVariable("sid") String sid )
    {
        Sesja s = SesjaKontroler.getSesjaByToken(jdbcTemplate, sid);
        return (new QuickJSON())
                .addRaw("ok", "true")
                .add("act", s.getRodzajKonta().toString())
                .add("kred", "")
                .ret();
    }

    @GetMapping("/wyloguj/{sid}")
    public String mappingWyloguj ( @PathVariable("sid") String sid )
    {
        Sesja s = SesjaKontroler.getSesjaByToken(jdbcTemplate, sid);
        s.wyloguj(jdbcTemplate);
        return (new QuickJSON())
                .addRaw("ok", "true")
                .ret();
    }
}
