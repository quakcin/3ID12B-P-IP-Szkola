package pl.hefajstos.uczen;

import lombok.Data;
import pl.hefajstos.hefajstos.QuickJSON;

import javax.persistence.*;
import java.sql.Date;

//@Table(name = "Uczen")
//@Entity
@Data
public class Uczen
{
    String Id;
    String Imie;
    String Nazwisko;
    String PESEL;
    Date Data_urodzenia;
    String Miejsce_urodzenia;
    String Klasa;
    Integer Numer;

    @Override
    public String toString ()
    {
        return (new QuickJSON())
                .add("imie", getImie())
                .add("nazwisko", getNazwisko())
                .add("urodz", getData_urodzenia().toString())
                .add("miejsc", getMiejsce_urodzenia())
                .add("pesel", getPESEL())
                .add("klasa", getKlasa())
                .add("numer", getNumer() == null ? "" : "" + getNumer())
                .add("uid", getId())
                .ret();
    }
}
