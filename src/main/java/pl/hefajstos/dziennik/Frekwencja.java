package pl.hefajstos.dziennik;

import lombok.Data;
import pl.hefajstos.hefajstos.Jsonable;
import pl.hefajstos.hefajstos.QuickJSON;

@Data
public class Frekwencja implements Jsonable
{
  Integer rodzaj;
  String uczenId;
  String nauczycielId;
  Integer tydzien;
  Integer dzienTygodnia;
  Integer godzina;
  String klasa;

  @Override
  public String toJson ()
  {
    return (new QuickJSON())
        .add("uczenId", getUczenId())
        .add("nauczycielId", getNauczycielId())
        .add("klasa", getKlasa())
        .addRaw("rodzaj", getRodzaj().toString())
        .addRaw("tydzien", getTydzien().toString())
        .addRaw("dzien", getDzienTygodnia().toString())
        .addRaw("godzina", getGodzina().toString())
        .ret();
  }

}
