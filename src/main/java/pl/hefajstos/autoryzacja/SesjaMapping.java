package pl.hefajstos.autoryzacja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SesjaMapping
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/zaloguj/{sid}/{usr}/{pwd}")
    public String mappingZaloguj
    (
        @PathVariable("sid") String sid,
        @PathVariable("usr") String usr,
        @PathVariable("pwd") String pwd)
    {
        return SesjaKontroler.zaloguj(jdbcTemplate, usr, pwd).toJson();
    }

}
