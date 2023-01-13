package pl.hefajstos.przedmioty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.hefajstos.QuickJSON;
import pl.hefajstos.uczen.Uczen;

import java.util.List;
import java.util.UUID;

@RestController
public class PrzedmiotyController
{
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @GetMapping("/przedmioty/usun/{sid}/{id}")
    public String removePrzedmiot
    (
            @PathVariable("sid") String sid,
            @PathVariable("id") String id
    )
    {
        String sql = String.format("DELETE FROM Przedmiot WHERE Id = %s", id);
        try
        {
            jdbcTemplate.execute(sql);
        }
        catch (Exception e)
        {
            return "{\"ok\":false}";
        }

        return "{\"ok\":true}";
    }

    @GetMapping("/przedmioty/info/{sid}/{id}")
    public String infoPrzedmioty
    (
            @PathVariable("sid") String sid,
            @PathVariable("id") String id
    )
    {
        String sql = String.format("SELECT * FROM Przedmiot WHERE Id = %s", id);

        List<Przedmiot> przedmioty = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Przedmiot.class));

        for (Przedmiot p : przedmioty)
            return (new QuickJSON())
                    .add("id", "" + p.getId())
                    .add("nazwa", p.getNazwa())
                    .add("poziom", "" + p.getPoziom())
                    .add("ilosc", "" + p.getIlosc())
                    .add("obw", p.getObowiazkowy())
                    .ret();

        return "{\"ok\":false}";
    }


    // pass

    public static boolean zapiszPrzedmiotWBazie (JdbcTemplate jdbcTemplate, Przedmiot nowyPrzedmiot )
    {
        String sql = "UPDATE Przedmiot SET Nazwa = ?, Poziom = ?, Ilosc = ?, Obowiazkowy = ? WHERE Id = ?";

        try
        {
            jdbcTemplate.update
            (
                sql,
                nowyPrzedmiot.getNazwa(),
                nowyPrzedmiot.getPoziom(),
                nowyPrzedmiot.getIlosc(),
                nowyPrzedmiot.getObowiazkowy(),
                nowyPrzedmiot.getId()
            );
        }
        catch (DataAccessException e)
        {
            System.out.println("[PrzedmiotController::zapiszPrzedmiotWBazie()]: " + e.toString());
            return false;
        }
        return true;
    }

    @GetMapping("/przedmioty/dodaj/{sid}/{nazwa}/{ilosc}/{poziom}/{obw}")
    public String addPrzedmiot
    (
            @PathVariable("sid") String sid,
            @PathVariable("nazwa") String nazwa,
            @PathVariable("ilosc") String ilosc,
            @PathVariable("poziom") String poziom,
            @PathVariable("obw") String obw
    )
    {
        String sql = "INSERT INTO Przedmiot VALUES (DEFAULT, ?, ?, ?, ?)";
        try
        {
            jdbcTemplate.update
            (
                sql,
                nowyPrzedmiot.getNazwa(),
                nowyPrzedmiot.getPoziom(),
                nowyPrzedmiot.getIlosc(),
                nowyPrzedmiot.getObowiazkowy());
        }
        catch (DataAccessException e)
        {
            System.out.println("[PrzedmiotController::dodajPrzedmiotDoBazy(1)]: " + e.toString());
            return false;
        }
        return true;
    }

    @GetMapping("/przedmioty/lista/{sid}")
    public String getTestJSONString (@PathVariable("sid") String sid)
    {
        String sql = "SELECT * FROM Przedmiot";

        List<Przedmiot> przedmioty = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Przedmiot.class));

        String lista = "{\"przedmioty\": [";
        for (Przedmiot p : przedmioty) {
            lista += (new QuickJSON())
                    .add("id", "" + p.getId())
                    .add("nazwa", p.getNazwa())
                    .add("poziom", "" + p.getPoziom())
                    .add("ilosc", "" + p.getIlosc())
                    .add("obw", p.getObowiazkowy())
                    .ret() + ((przedmioty.indexOf(p) + 1 < przedmioty.toArray().length) ? ", " : "");
        }
        lista += "]}";
        przedmioty.forEach(System.out :: println);
        return lista;
    }
}
