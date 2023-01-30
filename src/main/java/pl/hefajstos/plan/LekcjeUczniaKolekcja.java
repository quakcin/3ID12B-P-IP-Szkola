package pl.hefajstos.plan;

import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.hefajstos.uczen.AbstrakcyjnyUczen;

import java.util.ArrayList;
import java.util.List;

@Data
public class LekcjeUczniaKolekcja
{
  private ArrayList<List<Okno>> dni = new ArrayList<>();

  public LekcjeUczniaKolekcja (JdbcTemplate jdbcTemplate, AbstrakcyjnyUczen u)
  {
    for (int i = 0; i < 5; i++)
    {
      dni.add(PlanController.getGodzinyKlasyByDzien(jdbcTemplate, "" + i, u.getKlasa()));
    }
  }

  public LekcjeUczniaKolekcjaIterator getIterator ()
  {
    return new LekcjeUczniaKolekcjaIterator(this);
  }
}
