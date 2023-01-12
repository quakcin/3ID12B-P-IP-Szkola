package pl.hefajstos.uczen;

import lombok.Data;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.hefajstos.hefajstos.Jsonable;
import pl.hefajstos.hefajstos.QuickJSON;

import javax.persistence.*;
import java.sql.Date;

@Data
public class Uczen implements Jsonable
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
