package pl.hefajstos.klasy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.nauczyciele.Nauczyciel;
import pl.hefajstos.hefajstos.QuickJSONArray;
import pl.hefajstos.uczen.Uczen;
import pl.hefajstos.uczen.UczenController;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@RestController
public class KlasyController
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static List<Uczen> getListaUczniowByKlasa (JdbcTemplate jdbcTemplate, String klasa)
    {
        return jdbcTemplate.query
        (
        "SELECT * FROM Uczen WHERE Klasa = ? ORDER BY Nazwisko, Imie",
            BeanPropertyRowMapper.newInstance(Uczen.class), klasa
        );
    }

    public static KlasyView getKlasaByName (JdbcTemplate jdbcTemplate, String nazwa)
    {
        List<KlasyView> listaKlas = KlasyController.getListaKlas(jdbcTemplate);
        for (KlasyView k : listaKlas)
            if (k.getNazwa().equals(nazwa))
                return k;
        return null;
    }

    public static List<KlasyView> getListaKlas (JdbcTemplate jdbcTemplate)
    {
        return jdbcTemplate.query(
                "SELECT * FROM KlasyView WHERE NAZWA != '0'",
                BeanPropertyRowMapper.newInstance(KlasyView.class)
        );
    }

    public static void przypiszUczniaDoKlasy (JdbcTemplate jdbcTemplate, KlasyView klasa, Uczen uczen)
    {
        /* znalezienie numeru dla ucznia */
        Integer maxNumerWDzienniku = 0;
        List<Uczen> uczniowieWKlasie = KlasyController.getListaUczniowByKlasa(jdbcTemplate, klasa.getNazwa());
        for (Uczen u : uczniowieWKlasie)
            if (u.getNumer() > maxNumerWDzienniku)
                maxNumerWDzienniku = u.getNumer();

        uczen.setNumer(maxNumerWDzienniku + 1);
        uczen.setKlasa(klasa.getNazwa());
        UczenController.zapiszUczniaWBazie(jdbcTemplate, uczen);
    }

    public static List<KlasyView> getListaBezWychowawcow (JdbcTemplate jdbcTemplate)
    {
        List<KlasyView> klasy = getListaKlas(jdbcTemplate);
        ArrayList<KlasyView> bezWychowawcow = new ArrayList<>();
        for (KlasyView k : klasy)
            if (k.getWychowawca().equals(" "))
                bezWychowawcow.add(k);
        return bezWychowawcow;
    }
}
