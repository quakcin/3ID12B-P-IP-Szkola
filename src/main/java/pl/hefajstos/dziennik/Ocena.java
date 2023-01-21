package pl.hefajstos.dziennik;

import lombok.Data;
import pl.hefajstos.hefajstos.Jsonable;
import pl.hefajstos.hefajstos.QuickJSON;

import java.sql.Date;

@Data
public class Ocena implements Jsonable
{
    public Integer ocenaid;
    public String stopien;
    public Integer waga;
    public String uczenId;
    public Integer przedmiotId;
    public String nauczycielId;
    public Date data;
    public String imie;
    public String nazwisko;
    public Integer numer;
    public String klasa;


    public String toJson ()
    {
        return (new QuickJSON())
                .addRaw("id", getOcenaid().toString())
                .add("stopien", getStopien())
                .addRaw("waga", getWaga().toString())
                .add("uczen", getUczenId())
                .addRaw("przedmiot", getPrzedmiotId().toString())
                .add("nauczyciel", getNauczycielId())
                .add("data", getData().toString())
                .add("imie", getImie())
                .add("nazwisko", getNazwisko())
                .add("klasa", getKlasa())
                .addRaw("numer", getNumer().toString())
                .ret();
    }
}
