package pl.hefajstos.autoryzacja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SesjaKontroler
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
