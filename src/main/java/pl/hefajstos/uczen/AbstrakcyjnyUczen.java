package pl.hefajstos.uczen;

import lombok.Data;
import pl.hefajstos.hefajstos.Jsonable;
import pl.hefajstos.hefajstos.QuickJSON;

import java.sql.Date;

@Data
public abstract class AbstrakcyjnyUczen implements Jsonable
{
    String id;
    String imie;
    String nazwisko;
    String pesel;
    Date dataUrodzenia;
    String miejsceUrodzenia;
    String klasa;
    Integer numer;

    public String toJson ()
    {
        return (new QuickJSON())
            .add("imie", getImie())
            .add("nazwisko", getNazwisko())
            .add("urodz", getDataUrodzenia().toString())
            .add("miejsc", getMiejsceUrodzenia())
            .add("pesel", getPesel())
            .add("klasa", getKlasa())
            .add("numer", getNumer() == null ? "" : "" + getNumer())
            .add("uid", getId())
            .ret();
    }
}
