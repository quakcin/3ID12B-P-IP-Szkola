package pl.hefajstos.autoryzacja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Ping
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SesjaKontroler sesjaKontroler;

    @GetMapping("/ping/{sid}")
    public String getPing (@PathVariable("sid") String sid)
    {
        Sesja sesja = sesjaKontroler.getRodzajKonta(sid);
        if (sesja == null)
            return "{\"ok\":true, \"typ\": \"brak\"}";

        return String.format("{\"ok\": true, \"typ\":\"%s\"}",
                sesja.getRodzajKonta());
    }
}
