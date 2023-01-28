package pl.hefajstos.autoryzacja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.nauczyciele.NauczycieleController;

import static pl.hefajstos.autoryzacja.RodzajKonta.*;
import pl.hefajstos.nauczyciele.Nauczyciel;
import pl.hefajstos.uczen.UczenController;
import pl.hefajstos.uczen.Uczen;

@RestController
public class SesjaController
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * @param jdbcTemplate 
     * @param nazwaUzytkownika
     * @param haslo
     * @return Aktywna sesja po zalogowaniu, Nie Aktywna jeśli błąd
     */
    public static Sesja zaloguj (JdbcTemplate jdbcTemplate, String nazwaUzytkownika, String haslo)
    {
        return new Sesja(jdbcTemplate, nazwaUzytkownika, haslo);
    }

    public static String getKredytywnoscBySesjaId (JdbcTemplate jdbcTemplate, String sid)
    {
        Sesja s = SesjaController.getSesjaByToken(jdbcTemplate, sid);
        switch (s.getRodzajKonta())
        {
            case Dyrektor:
                return "Adam Krechowicz";

            case Nauczyciel:
                Nauczyciel n = NauczycieleController.getNauczycielById(jdbcTemplate, s.getKlucz());
                return n.getImie() + " " + n.getNazwisko();

            case Uczen:
                Uczen u = UczenController.getUczenById(jdbcTemplate, s.getKlucz());
                return u.getImie() + " " + u.getNazwisko() + " " + u.getKlasa();

            default:
                return "Błąd";
        }
    }

    /**
     * @param sid - token istniejącej sesji
     * @return Sztuczna (NIEAKTYWNA) sesja z dopiskami do istniejącej sesji pod (sid)
     */
    public static Sesja getSesjaByToken (JdbcTemplate jdbcTemplate, String sid)
    {
        Sesja nowaSesja = new Sesja();

        String sql = "SELECT KONTO.NICKNAME, KONTO.HASLO, KONTO.TYP, KONTO.ID " +
                "FROM KONTO INNER JOIN SESJA ON SESJA.ID = KONTO.ID " +
                "WHERE SESJA.TOKEN = ?";

        KontoBaza konto = jdbcTemplate.queryForObject(
                sql, BeanPropertyRowMapper.newInstance(KontoBaza.class), sid);

        nowaSesja.setToken(sid);
        nowaSesja.setRodzajKonta(konto.getTyp());
        nowaSesja.setKlucz(konto.getId());
        nowaSesja.setAktywna(false);

        return nowaSesja;
    }
}
