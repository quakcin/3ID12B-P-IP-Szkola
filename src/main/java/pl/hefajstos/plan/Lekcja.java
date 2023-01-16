package pl.hefajstos.plan;

import lombok.Data;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class Lekcja
{
    public Integer dzienTygodnia;
    public Integer godzina;
    public String klasaId;
    public Integer przedmiotId;
    public String nauczycielId;

    public Lekcja (JdbcTemplate jdbcTemplate, Integer dzienTygodnia, Integer godzina, String klasaId, Integer przedmiotId, String nauczycielId)
    {
        /* TODO: Dokończyć! */
        // System.out.println("Utworzono nową lekcję " + klasaId + " o " + (godzina / 60) + " w " + dzienTygodnia + " przedmiot " + przedmiotId + " z " + nauczycielId);
        String sql = "INSERT INTO Lekcja VALUES (DEFAULT, ?, ?, ?, ?, ?)";

        try
        {
            jdbcTemplate.update
            (
                    sql, dzienTygodnia, godzina,
                    klasaId, przedmiotId, nauczycielId
            );
        }
        catch (DataAccessException e)
        {
            System.out.println("[Lekcja::Lekcja()]: " + e.toString());
        }
    }
}
