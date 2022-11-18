package pl.hefajstos.autoryzacja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.hefajstos.QuickJSON;

import java.util.List;

@RestController
public class SesjaKontroler
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/lista_sesji")
    public ResponseEntity<String> getTestJSONString ()
    {
        String sql = "SELECT * FROM Sesja";
        List<SesjaBaza> sesje = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(SesjaBaza.class));

        String lista = "{\"sesje\": [";
        for (SesjaBaza sesjaBaza : sesje) {
            lista += (new QuickJSON())
                    .add("token", sesjaBaza.getToken())
                    .add("expr", sesjaBaza.getExpr().toString())
                    .add("id", sesjaBaza.getId())
                    .ret() + ((sesje.indexOf(sesjaBaza) + 1 < sesje.toArray().length) ? ", " : "");
        }
        lista += "]}";
        sesje.forEach(System.out :: println);
        return new ResponseEntity<>(lista, HttpStatus.valueOf(200));
    }

    public Sesja getRodzajKonta(String token)
    {
        String sql = "SELECT * FROM Sesja WHERE token = '" + token + "'";
        List<SesjaBaza> sesje = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(SesjaBaza.class));

        return sesje.size() != 1
            ? null
            : new Sesja(sesje.get(0).getToken(), RodzajKonta.values()[sesje.get(0).getTyp()], sesje.get(0).getId());
    }
}
