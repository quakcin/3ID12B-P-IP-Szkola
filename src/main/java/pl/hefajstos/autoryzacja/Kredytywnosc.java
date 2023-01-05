package pl.hefajstos.autoryzacja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.hefajstos.QuickJSON;
import pl.hefajstos.uczen.Uczen;

import java.util.List;

@RestController
public class Kredytywnosc
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SesjaKontroler sesjaKontroler;

    @GetMapping("/kredytywnosc/{sid}")
    public String getPing (@PathVariable("sid") String sid)
    {
        Sesja sesja = sesjaKontroler.getRodzajKonta(sid);

        if (sesja.getRodzajKonta().equals(RodzajKonta.Uczen))
        {
            String sql ="SELECT Imie, Nazwisko FROM Uczen WHERE Id = '" + sesja.getKlucz() + "'";
            List<Uczen> uczniowie = jdbcTemplate.query(sql,
                    BeanPropertyRowMapper.newInstance(Uczen.class));
            for (Uczen u : uczniowie)
                return (new QuickJSON()).add("kred", u.getImie() + " " + u.getNazwisko()).ret();
        }

        return (new QuickJSON()).add("kred","").add("act", sesja.getRodzajKonta().name()).ret();
    }
}
