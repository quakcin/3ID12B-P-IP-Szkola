package pl.hefajstos.index;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
