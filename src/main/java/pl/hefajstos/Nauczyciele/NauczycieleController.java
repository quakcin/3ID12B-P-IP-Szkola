package pl.hefajstos.Nauczyciele;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.hefajstos.QuickJSON;
import pl.hefajstos.hefajstos.QuickJSONArray;

import java.util.List;

@RestController
public class NauczycieleController
{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/nauczyciel/lista/{sid}")
    public String getUczen (@PathVariable("sid") String sid)
    {
        String sql = "SELECT * FROM Nauczyciele_view";

        List<Nauczyciele_view> nauczyciele = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Nauczyciele_view.class));

        QuickJSONArray q = new QuickJSONArray("nauczyciele");
        for (Nauczyciele_view n : nauczyciele)
            q.add(n.toString());

        return q.ret();
    }
}
