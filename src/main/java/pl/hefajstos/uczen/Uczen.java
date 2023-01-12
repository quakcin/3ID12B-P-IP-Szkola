package pl.hefajstos.uczen;

import lombok.Data;
import pl.hefajstos.hefajstos.QuickJSON;

import javax.persistence.*;
import java.sql.Date;

@Data
public class Uczen
{
    String id;
    String imie;
    String nazwisko;
    String pesel;
    Date dataUrodzenia;
    String miejsceUrodzenia;
    String klasa;
    Integer numer;

    @Override
    public String toString ()
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
