package pl.hefajstos.przedmioty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Lista
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /*
        Reprezentacja przedmiotu:
        {"przedmioty": [
            {"id", "nazwa", "poziom", "ramka czasowa"},
            ...
        ]}
     */
    @GetMapping("/przedmioty/lista/{sid}")
    public String getListaPrzedmiotow (@PathVariable("sid") String sid)
    {
        return "{\"przedmioty\": [\n" +
                "\t{\"id\": 0, \"nazwa\": \"Język Polski\", \"poziom\": 1, \"godziny\": 6},\n" +
                "\t{\"id\": 1, \"nazwa\": \"Matematyka\", \"poziom\": 2, \"godziny\": 5},\n" +
                "\t{\"id\": 2, \"nazwa\": \"Informatyka\", \"poziom\": 3, \"godziny\": 2}\n" +
                "]}\n";
    }
}
