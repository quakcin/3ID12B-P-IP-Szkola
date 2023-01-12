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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
public class Logowanie
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

//    public Sesja zaloguj (String nazwaUzytkownika, String haslo)
//    {
//        Sesja nowaSesja = new Sesja(nazwaUzytkownika, haslo);
//        return null;
//    }
//
//    @GetMapping("/zaloguj/{sid}/{usr}/{pwd}")
//    public String getZaloguj (@PathVariable("sid") String sid, @PathVariable("usr") String usr, @PathVariable("pwd") String pwd)
//    {
//        String sql = "SELECT * FROM KONTO WHERE NICKNAME = ? AND HASLO = ?";
//
//        List<KontoBaza> konta = jdbcTemplate.query(sql,
//                BeanPropertyRowMapper.newInstance(KontoBaza.class), usr, pwd);
//
//        for (KontoBaza baz : konta)
//            System.out.println(baz.getNickname() + " " + baz.getTyp().toString());
//
//        if (konta.size() != 1)
//        { /* konto nie istnieje! */
//            return "{\"ok\":false}";
//        }
//
//        String uid = UUID.randomUUID().toString();
//
//        jdbcTemplate.execute(
//                String.format("INSERT INTO Sesja VALUES ('%s', CURRENT_TIMESTAMP, '%s', %d)",
//                        uid, konta.get(0).getId(), konta.get(0).getTyp()));
//
//        return String.format(
//                "{\"ok\": true, \"uid\":\"%s\", \"typ\":\"%s\"}",
//                uid,
//                RodzajKonta.values()[konta.get(0).getTyp()].toString());
//
//    }
}
