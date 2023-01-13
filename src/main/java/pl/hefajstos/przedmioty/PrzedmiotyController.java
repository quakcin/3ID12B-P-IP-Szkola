package pl.hefajstos.przedmioty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.hefajstos.QuickJSON;
import pl.hefajstos.uczen.Uczen;

import java.util.List;
import java.util.UUID;

@RestController
public class PrzedmiotyController
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static Przedmiot getPrzedmiotById (JdbcTemplate jdbcTemplate, Integer id)
    {
        List<Przedmiot> przedmioty = getListaPrzedmiotow(jdbcTemplate);
        for (Przedmiot p : przedmioty)
            if (p.getId().equals(id))
                return p;
        return null;
    }

    public static List<Przedmiot> getListaPrzedmiotow (JdbcTemplate jdbcTemplate)
    {
        String sql = "SELECT * FROM Przedmiot";
        return jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Przedmiot.class));
    }

    public static boolean usunPrzedmiotById (JdbcTemplate jdbcTemplate, Integer id)
    {
        try
        {
            String sql = "DELETE FROM Przedmiot WHERE Id = ?";
            jdbcTemplate.update(sql, id);
        }
        catch (DataAccessException e)
        {
            System.out.println("[PrzedmiotController::usunPrzedmiotById]: " + e.toString());
            return false;
        }
        return true;
    }

    // pass

    public static boolean zapiszPrzedmiotWBazie (JdbcTemplate jdbcTemplate, Przedmiot nowyPrzedmiot )
    {
        String sql = "UPDATE Przedmiot SET Nazwa = ?, Poziom = ?, Ilosc = ?, Obowiazkowy = ? WHERE Id = ?";

        try
        {
            jdbcTemplate.update
            (
                sql,
                nowyPrzedmiot.getNazwa(),
                nowyPrzedmiot.getPoziom(),
                nowyPrzedmiot.getIlosc(),
                nowyPrzedmiot.getObowiazkowy(),
                nowyPrzedmiot.getId()
            );
        }
        catch (DataAccessException e)
        {
            System.out.println("[PrzedmiotController::zapiszPrzedmiotWBazie()]: " + e.toString());
            return false;
        }
        return true;
    }

    public static boolean dodajPrzedmiotDoBazy (JdbcTemplate jdbcTemplate, Przedmiot nowyPrzedmiot)
    {
        String sql = "INSERT INTO Przedmiot VALUES (DEFAULT, ?, ?, ?, ?)";
        try
        {
            jdbcTemplate.update
            (
                sql,
                nowyPrzedmiot.getNazwa(),
                nowyPrzedmiot.getPoziom(),
                nowyPrzedmiot.getIlosc(),
                nowyPrzedmiot.getObowiazkowy());
        }
        catch (DataAccessException e)
        {
            System.out.println("[PrzedmiotController::dodajPrzedmiotDoBazy(1)]: " + e.toString());
            return false;
        }
        return true;
    }
}
