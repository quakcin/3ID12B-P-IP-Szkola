package pl.hefajstos.hefajstos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.hefajstos.autoryzacja.Sesja;

@SpringBootTest
public class SesjaTest
{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /*
        TODO: Użyć MVC do testowania json'ów zwracanych przez REST'a
     */
}
