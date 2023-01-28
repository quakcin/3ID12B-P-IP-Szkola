package pl.hefajstos.plan;

import lombok.Data;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.hefajstos.hefajstos.Jsonable;
import pl.hefajstos.hefajstos.QuickJSON;

@Data
public class GodzinaLekcyjna implements Jsonable {
    private Integer Id;
    private String godzRozp;
    private String godzZak;

    @Override
    public String toJson() {
        return (new QuickJSON())
                .addRaw("id", getId().toString())
                .add("godzRozp", getGodzRozp())
                .add("godzZak", getGodzZak())
                .ret();
    }
}
