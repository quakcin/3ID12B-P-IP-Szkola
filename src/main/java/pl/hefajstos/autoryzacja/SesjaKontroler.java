package pl.hefajstos.autoryzacja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SesjaKontroler
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static Sesja zaloguj (JdbcTemplate jdbcTemplate, String nazwaUzytkownika, String haslo)
    {
        return new Sesja(jdbcTemplate, nazwaUzytkownika, haslo);
    }

    public static Sesja getRodzajKonta (String sid)
    {
        return new Sesja();
    }

    /*
    @GetMapping("/lista_sesji")
    public ResponseEntity<String> getTestJSONString ()
    {
        String sql = "SELECT * FROM SESJA";
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
    */
}
