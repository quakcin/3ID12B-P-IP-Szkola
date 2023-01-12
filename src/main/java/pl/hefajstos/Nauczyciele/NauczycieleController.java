package pl.hefajstos.Nauczyciele;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.hefajstos.QuickJSONArray;

import java.util.List;
import java.util.UUID;

@RestController
public class NauczycieleController
{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/nauczyciel/lista/{sid}")
    public String getNauczyciele (@PathVariable("sid") String sid)
    {
        String sql = "SELECT * FROM Nauczyciele_view";

        List<NauczycieleView> nauczyciele = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(NauczycieleView.class));

        QuickJSONArray q = new QuickJSONArray("nauczyciele");
        for (NauczycieleView n : nauczyciele)
            q.add(n.toString());

        return q.ret();
    }



    @GetMapping("/nauczyciel/info/{sid}/{id}")
    public String getNauczyciel (@PathVariable("sid") String sid, @PathVariable("id") String id)
    {
        String sql = String.format("SELECT * FROM Nauczyciele_view WHERE Nauczyciel_Id = '%s'", id);

        List<NauczycieleView> nauczyciele = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(NauczycieleView.class));

        for (NauczycieleView n : nauczyciele)
            return n.toString();

        return "{\"ok\": true}";
    }
    @GetMapping("/nauczyciel/usun/{sid}/{id}")
    public String removeNauczyciel (@PathVariable("sid") String sid, @PathVariable("id") String id)
    {
        String sql = String.format("DELETE FROM Nauczyciel WHERE Id = '%s'", id);

        try
        {
            jdbcTemplate.execute(sql);
        }
        catch(Exception e)
        {
            return "{\"ok\": false}";
        }

        return "{\"ok\": true}";
    }
    
    @GetMapping("/nauczyciel/dodaj/{sid}/{imie}/{nazw}/{zatr}/{zaw}/{wych}/{prz}")
    public String removeNauczyciel
    (
            @PathVariable("sid") String sid,
            @PathVariable("imie") String imie,
            @PathVariable("nazw") String nazw,
            @PathVariable("zatr") String zatr,
            @PathVariable("zaw") String zaw,
            @PathVariable("wych") String wych,
            @PathVariable("prz") String prz
    )
    {
        String id = UUID.randomUUID().toString();
        String sql = String.format("INSERT INTO Konto VALUES ('%s', '%s', %s, '%s')",
                imie + nazw, nazw + "1234", "1", id
            );

        /*
            Dodanie ewidencji do tabeli kont
         */

        try
        {
            jdbcTemplate.execute(sql);
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            return "{\"ok\": false}";
        }

        /*
            Dodanie ewidencji do tabeli nauczycieli
         */

        sql = String.format("INSERT INTO Nauczyciel VALUES ('%s', '%s', '%s', TO_DATE('%s', 'yyyy-mm-dd'), '%s', '%s')",
                    id, imie, nazw, zatr, zaw, wych
                );

        try
        {
            jdbcTemplate.execute(sql);
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            return "{\"ok\": false}"; // TODO: usunąć z konta jeżeli to sie nie powiodło!
        }

        /*
            Powiązanie z nauczanymi przedmiotami
         */

        String[] id_przedmiotow = prz.split("_");

        for (String przd : id_przedmiotow)
        {
            sql = String.format("INSERT INTO Nauczyciel_Przedmiot VALUES ('%s', %s)", id, przd);

            try
            {
                jdbcTemplate.execute(sql);
            }
            catch(Exception e)
            {
                System.out.println(e.toString());
                return "{\"ok\": false}"; // TODO: usunąć z konta jeżeli to sie nie powiodło!
            }
        }

        return "{\"ok\": true}";
    }


    @GetMapping("/nauczyciel/edytuj/{sid}/{imie}/{nazw}/{zatr}/{zaw}/{wych}/{prz}/{id}")
    public String setNauczyciel
    (
            @PathVariable("sid") String sid,
            @PathVariable("imie") String imie,
            @PathVariable("nazw") String nazw,
            @PathVariable("zatr") String zatr,
            @PathVariable("zaw") String zaw,
            @PathVariable("wych") String wych,
            @PathVariable("prz") String prz,
            @PathVariable("id") String id
    )
    {
        /*
            Zmiana w tabeli nauczyciel
         */

        String sql = String.format("UPDATE Nauczyciel SET Imie = '%s', Nazwisko = '%s', Data_zatrudnienia = TO_DATE('%s', 'yyyy-mm-dd'), Stopien_zawodowy = '%s', Klasa_Id = '%s' WHERE Id = '%s'",
                imie, nazw, zatr, zaw, wych, id
        );

        try
        {
            jdbcTemplate.execute(sql);
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            return "{\"ok\": false}";
        }

        /*
            Usuniecie starego powiazania Nauczyciel <--> Przedmiot
         */

        sql = String.format("DELETE FROM Nauczyciel_Przedmiot WHERE Nauczyciel_Id = '%s'", id);

        try
        {
            jdbcTemplate.execute(sql);
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            return "{\"ok\": false}";
        }

        /*
            Powiązanie z nauczanymi przedmiotami
         */

        String[] id_przedmiotow = prz.split("_");

        for (String przd : id_przedmiotow)
        {
            sql = String.format("INSERT INTO Nauczyciel_Przedmiot VALUES ('%s', %s)", id, przd);

            try
            {
                jdbcTemplate.execute(sql);
            }
            catch(Exception e)
            {
                System.out.println(e.toString());
                return "{\"ok\": false}";
            }
        }

        return "{\"ok\": true}";
    }
}
