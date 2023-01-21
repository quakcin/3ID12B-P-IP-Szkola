package pl.hefajstos.autoryzacja;


import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class KontaController
{
  public static List<KontoBaza> getListaKont (JdbcTemplate jdbcTemplate)
  {
    return jdbcTemplate.query
        (
            "SELECT * FROM Konto",
            BeanPropertyRowMapper.newInstance(KontoBaza.class)
        );
  }

  public static boolean zmienHaslo (JdbcTemplate jdbcTemplate, String kontoId, String noweHaslo)
  {
    /* TODO: Sprawdzić czy dyrektor, lub ten sam uzytkownik */
    try
    {
      jdbcTemplate.update("UPDATE Konto SET haslo = ? WHERE id = ?",
          noweHaslo, kontoId);
    }
    catch (DataAccessException e)
    {
      System.out.println("[KontaController]: (zmienHaslo) " + e.toString());
      return false;
    }
    return true;
  }

  public static String getKontoID (JdbcTemplate jdbcTemplate, String sid)
  {
    Sesja s = SesjaController.getSesjaByToken(jdbcTemplate, sid);
    return s.getKlucz();
  }
}
