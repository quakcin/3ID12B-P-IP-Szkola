package pl.hefajstos.dziennik;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.hefajstos.autoryzacja.KontoBaza;
import pl.hefajstos.autoryzacja.Sesja;
import pl.hefajstos.autoryzacja.SesjaController;
import pl.hefajstos.uczen.Uczen;
import pl.hefajstos.uczen.UczenController;

import java.util.List;
import java.util.stream.Collectors;

public class FrekwencjaController
{
  public static List<Frekwencja> getFrekwencja (JdbcTemplate jdbcTemplate)
  {
    return jdbcTemplate.query
    (
        "SELECT * FROM Frekwencja",
        BeanPropertyRowMapper.newInstance(Frekwencja.class)
    );
  }

  public static List<Frekwencja> getFrekwencjaByKlasa (JdbcTemplate jdbcTemplate, String klasa)
  {
    return getFrekwencja(jdbcTemplate).stream().filter(f -> f.getKlasa().equals(klasa)).collect(Collectors.toList());
  }

  public static List<Frekwencja> getFrekwencjaByKlasaForDayAndWeek (JdbcTemplate jdbcTemplate, String klasa, Integer day, Integer week)
  {
    return getFrekwencjaByKlasa(jdbcTemplate, klasa)
        .stream()
        .filter(f -> f.getDzienTygodnia().equals(day) && f.getTydzien().equals(week))
        .collect(Collectors.toList());
  }

  public static boolean addFrekwencjaByUczenIdAsSesjaId (JdbcTemplate jdbcTemplate, String sid, String uid, Integer rodzaj, Integer dzien, Integer tydzien, Integer godzina)
  {
    Sesja s = SesjaController.getSesjaByToken(jdbcTemplate, sid);
    Uczen u = UczenController.getUczenById(jdbcTemplate, uid);
    try
    {
      jdbcTemplate.update (
          "INSERT INTO FREKWENCJA VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?)",
          rodzaj, uid, u.getKlasa(), s.getKlucz(), tydzien, dzien, godzina
      );
    }
    catch (DataAccessException e)
    {
      System.out.println("[FrekwencjaController::addFrekwencja...()]: " + e.toString());
      return false;
    }

    return true;
  }


}
