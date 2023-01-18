package pl.hefajstos.konta;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.hefajstos.autoryzacja.Sesja;
import pl.hefajstos.autoryzacja.SesjaController;
import pl.hefajstos.uczen.Uczen;

public class UzytkownikController
{
  public static Uzytkownik getUzytkownikBySID (JdbcTemplate jdbcTemplate, String sid)
  { /* tworzę kompozycję (kontoBaza, rodzajBaza) */

    Sesja s = SesjaController.getSesjaByToken(jdbcTemplate, sid);

    /*
    String sql = "SELECT * FROM KONTO WHERE ID = ?"
    Uzytkownik u = jdbcTemplate.queryForObject
        (sql, BeanPropertyRowMapper.newInstance(Uzytkownik.class), s.get);
    */
    return null;
  }
}
