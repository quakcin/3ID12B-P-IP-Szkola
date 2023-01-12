package pl.hefajstos.przedmioty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.hefajstos.QuickJSON;

import java.util.List;

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


    @GetMapping("/przedmioty/edytuj/{sid}/{id}/{nazwa}/{ilosc}/{poziom}/{obw}")
    public String editPrzedmiot
    (
            @PathVariable("sid") String sid,
            @PathVariable("id") String id,
            @PathVariable("nazwa") String nazwa,
            @PathVariable("ilosc") String ilosc,
            @PathVariable("poziom") String poziom,
            @PathVariable("obw") String obw
    )
    {
        String sql = String.format("UPDATE Przedmiot SET Nazwa = '%s', Poziom = %s, Ilosc = %s, Obowiazkowy = '%s' WHERE Id = %s",
                nazwa, poziom, ilosc, obw, id);
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
        String sql = String.format("INSERT INTO Przedmiot VALUES (DEFAULT, '%s', %s, %s, '%s')",
            nazwa, poziom, ilosc, obw);
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
