package pl.hefajstos.dziennik;

import lombok.Data;
import pl.hefajstos.hefajstos.Jsonable;
import pl.hefajstos.hefajstos.QuickJSON;
import pl.hefajstos.hefajstos.QuickJSONArray;
import pl.hefajstos.uczen.Uczen;

import java.util.List;

@Data
public class UczenWDzienniku implements Jsonable
{
  public Uczen uczen;
  public List<Ocena> oceny;

  @Override
  public String toJson()
  {
    return (new QuickJSON())
        .addRaw("uczn", uczen.toJson())
        .addRaw("oceny",
            QuickJSONArray.fromList("oceny", oceny))
        .ret();
  }

}
