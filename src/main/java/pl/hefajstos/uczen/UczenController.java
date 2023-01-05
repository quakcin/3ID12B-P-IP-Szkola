package pl.hefajstos.uczen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.hefajstos.QuickJSON;
import pl.hefajstos.hefajstos.QuickJSONArray;

import java.util.List;
import java.util.UUID;

@RestController
public class UczenController
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/uczen/info/{sid}/{uid}")
    public String getUczen (@PathVariable("sid") String sid, @PathVariable("uid") String uid)
    {
        String sql = "SELECT Id, Imie, Nazwisko, PESEL, Data_urodzenia, Miejsce_urodzenia FROM Uczen WHERE Id = '" + uid + "'";

        List<Uczen> uczniowie = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Uczen.class));

        for (Uczen u : uczniowie)
            return u.toString();

        return "";
    }

    @GetMapping("/uczen/dodaj/{sid}/{imie}/{nazw}/{pesel}/{uro}/{miej}")
    public String setUczen
    (
            @PathVariable("sid") String sid,
            @PathVariable("imie") String imie,
            @PathVariable("nazw") String nazw,
            @PathVariable("pesel") String pesel,
            @PathVariable("uro") String uro,
            @PathVariable("miej") String miej
    )
    {
        String uid = UUID.randomUUID().toString();
        String sql = String.format(
                "INSERT INTO Uczen VALUES ('%s', '%s', '%s', '%s', TO_DATE('%s', 'yyyy-mm-dd'), '%s')",
            uid, imie, nazw, pesel, uro, miej
        );

        jdbcTemplate.execute(sql);

        String sql2 = String.format(
            "INSERT INTO Konto VALUES ('%s', '%s', 0, '%s')",
            imie + nazw, pesel, uid
        );

        jdbcTemplate.execute(sql2);
        return (new QuickJSON()).add("uid", uid).ret();
    }

    @GetMapping("/uczen/edytuj/{sid}/{imie}/{nazw}/{pesel}/{uro}/{miej}/{uid}")
    public String updateUczen
            (
                    @PathVariable("sid") String sid,
                    @PathVariable("imie") String imie,
                    @PathVariable("nazw") String nazw,
                    @PathVariable("pesel") String pesel,
                    @PathVariable("uro") String uro,
                    @PathVariable("miej") String miej,
                    @PathVariable("uid") String uid
            )
    {
        String sql = String.format(
                "UPDATE Uczen SET Imie = '%s', Nazwisko = '%s', Pesel = '%s', Data_urodzenia = TO_DATE('%s', 'yyyy-mm-dd'), Miejsce_urodzenia = '%s' WHERE Id = '%s'",
                imie, nazw, pesel, uro, miej, uid
        );
        jdbcTemplate.execute(sql);
        System.out.println(sql);

        return (new QuickJSON()).add("ok", "true").ret();
    }

    /*
        @GetMapping("/uczen/dodaj/{sid}")
        public String getListaPrzedmiotow (@PathVariable("sid") String sid)
        {
            return "";
        }
     */

    @GetMapping("/uczen/usun/{sid}/{uid}")
    public String removeUczen (@PathVariable("sid") String sid, @PathVariable("uid") String uid)
    {
        String sql = String.format("DELETE FROM Uczen WHERE Id = '%s'", uid);
        jdbcTemplate.execute(sql);
        String sql2 = String.format("DELETE FROM Konto WHERE Id = '%s'", uid);
        jdbcTemplate.execute(sql2);
        return "{\"ok\": true}";
    }

    @GetMapping("/uczen/lista/{sid}")
    public String getListaPrzedmiotow (@PathVariable("sid") String sid)
    {
        String sql = "SELECT Id, Imie, Nazwisko, PESEL, Data_urodzenia, Miejsce_urodzenia FROM Uczen";

        List<Uczen> uczniowie = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Uczen.class));

        QuickJSONArray rets = new QuickJSONArray("listaUczniow");

        for (Uczen u : uczniowie)
            rets.add(u.toString());

        return rets.ret();
    }

}
