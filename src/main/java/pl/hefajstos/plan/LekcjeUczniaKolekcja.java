package pl.hefajstos.plan;

import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.hefajstos.hefajstos.Iterowalne;
import pl.hefajstos.uczen.Uczen;
import pl.hefajstos.uczen.UczenController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

@Data
public class LekcjeUczniaKolekcja
{
  private ArrayList<List<Okno>> dni = new ArrayList<>();

  public LekcjeUczniaKolekcja (JdbcTemplate jdbcTemplate, Uczen u)
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
