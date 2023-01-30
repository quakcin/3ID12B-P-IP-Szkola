package pl.hefajstos.uczen;

import lombok.Data;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Data
public class KolekcjaUczniow
{
  private List<AbstrakcyjnyUczen> uczniowie;

  public KolekcjaUczniow (JdbcTemplate jdbcTemplate)
  {
    String sql = "SELECT * FROM Uczen ORDER BY Klasa, Numer, Nazwisko, Imie";
    this.uczniowie = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(AbstrakcyjnyUczen.class));
  }

  public UczniowieIterator getIterator ()
  {
    return new UczniowieIterator(this);
  }
}
