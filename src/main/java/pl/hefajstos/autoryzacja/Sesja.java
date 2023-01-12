package pl.hefajstos.autoryzacja;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.hefajstos.Jsonable;
import pl.hefajstos.hefajstos.QuickJSON;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
public class Sesja implements Jsonable
{
    private String token;
    private RodzajKonta rodzajKonta;
    private String klucz;
    private Boolean aktywna;
    private Timestamp expr;

    public Sesja () {};
    public Sesja (JdbcTemplate jdbcTemplate, String nazwaUzytkownika, String haslo)
    {
        /*
            Sprawdzenie czy dane logowania są poprawne
         */
        setExpr(new Timestamp(System.currentTimeMillis()));
        String sql = "SELECT * FROM KONTO WHERE NICKNAME = ? AND HASLO = ?";
        KontoBaza konto = jdbcTemplate.queryForObject(
                sql, BeanPropertyRowMapper.newInstance(KontoBaza.class), nazwaUzytkownika, haslo);

        if (konto == null)
        { /* nie udało się zalogować */
            this.setAktywna(false);
        }
        else
        { /* udało się zalogować */
            /* kopiowanie informacji do obiektu sesji */
            this.setAktywna(true);
            setKlucz(konto.getId());
            setRodzajKonta(konto.getTyp());
            /* utworzenie ewidencji sesji w bazie */
            UUID uuid = UUID.randomUUID();
            setToken(uuid.toString());
            jdbcTemplate.update
            (
                "INSERT INTO SESJA VALUES (?, ?, ?, ?)",
                new Object [] {getToken(), getExpr(), getKlucz(), getRodzajKonta().ordinal()}
            ); /* FIXME: Co jeżeli update() nie przejdzie! */
        }
    }

    public Sesja (JdbcTemplate jdbcTemplate, String sid)
    {
    }

    public String toJson()
    {
        if (getAktywna())
        { /* TODO: Poprawić konsystencję Syntaxu */
            return (new QuickJSON())
                    .addRaw("ok", "true")
                    .add("uid", getToken())
                    .add("typ", getRodzajKonta().toString())
                    .ret();
        }

        return (new QuickJSON())
            .addRaw("ok", "false")
            .ret();
    }

    public void wyloguj (JdbcTemplate jdbcTemplate)
    {
        jdbcTemplate.update
        (
                "DELETE FROM SESJA WHERE TOKEN = ?",
                new Object [] {getToken()}
        );
        setAktywna(false);
    }
}
