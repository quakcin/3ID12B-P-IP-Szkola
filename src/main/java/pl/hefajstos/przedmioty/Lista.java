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

    @GetMapping("/przedmioty/lista/{sid}")
    public String getListaPrzedmiotow (@PathVariable("sid") String sid)
    {
        return "{\"ok\": true}";
    }
}
