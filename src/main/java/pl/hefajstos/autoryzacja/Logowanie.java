package pl.hefajstos.autoryzacja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.hefajstos.QuickJSON;

import java.util.List;
import java.util.UUID;

@RestController
public class Logowanie
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/zaloguj/{usr}/{pwd}")
    public ResponseEntity<String> getZaloguj (@PathVariable("usr") String usr, @PathVariable("pwd") String pwd)
    {
        String sql = String.format("SELECT * FROM Konto WHERE Nickname = '%s' AND Haslo = '%s'", usr, pwd);
        List<KontoBaza> konta = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(KontoBaza.class));

        if (konta.size() != 1)
        { /* konto nie istnieje! */
            return new ResponseEntity<>("{\"ok\":false}", HttpStatus.valueOf(200));
        }

        String uid = UUID.randomUUID().toString();

        jdbcTemplate.execute(
                String.format("INSERT INTO Sesja VALUES ('%s', CURRENT_TIMESTAMP, '%s', %d)",
                        uid, konta.get(0).getId(), konta.get(0).getTyp()));

        return new ResponseEntity<>(
                String.format("{\"ok\": true, \"uid\":\"%s\"}", uid),
                HttpStatus.valueOf(200));
    }
}
