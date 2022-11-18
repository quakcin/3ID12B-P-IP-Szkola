package pl.hefajstos.autoryzacja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Wylogowywanie
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/wyloguj/{sid}")
    public ResponseEntity<String> getWyloguj(@PathVariable("sid") String sid)
    {
        try
        {
            jdbcTemplate.update(String.format("DELETE FROM Sesja WHERE Token = '%s'", sid));
        }
        catch (DataAccessException dae)
        {
            return new ResponseEntity<>("{\"ok\": false}", HttpStatus.valueOf(200));
        }

        return new ResponseEntity<>("{\"ok\": true}", HttpStatus.valueOf(200));
    }
}
