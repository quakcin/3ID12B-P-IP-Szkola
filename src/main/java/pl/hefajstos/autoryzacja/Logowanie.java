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

    @GetMapping("/zaloguj/{sid}/{usr}/{pwd}")
    public ResponseEntity<String> getZaloguj (@PathVariable("sid") String sid, @PathVariable("usr") String usr, @PathVariable("pwd") String pwd)
    {
        String sql = String.format("SELECT * FROM Konto WHERE Nickname = '%s' AND Haslo = '%s'", usr, pwd);
        List<KontoBaza> konta = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(KontoBaza.class));

        System.out.println(sql);
        System.out.println("Kocham jave: " + konta.size());

        System.out.println(konta);

        for (KontoBaza baz : konta)
            System.out.println(baz.getNickname() + " " + baz.getTyp().toString());

        if (konta.size() != 1)
        { /* konto nie istnieje! */
            return new ResponseEntity<>("{\"ok\":false}", HttpStatus.valueOf(200));
        }

        String uid = UUID.randomUUID().toString();

        jdbcTemplate.execute(
                String.format("INSERT INTO Sesja VALUES ('%s', CURRENT_TIMESTAMP, '%s', %d)",
                        uid, konta.get(0).getId(), konta.get(0).getTyp()));

        return new ResponseEntity<>(
                String.format("{\"ok\": true, \"uid\":\"%s\", \"typ\":%d}", uid, konta.get(0).getTyp()),
                HttpStatus.valueOf(200));
    }
}
