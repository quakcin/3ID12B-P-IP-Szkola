package pl.hefajstos.Index;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.autoryzacja.KontoBaza;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
public class Indeks
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    @ResponseBody
    public String getIndeks ()
    {
        String page = "<h2>error 500</h2>";
        try {
            page = String.join("", Files.readAllLines(
                Paths.get((getClass().getClassLoader()).getResource(
                        "Indeks/index.html").toURI())));

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return page;
    }
}
