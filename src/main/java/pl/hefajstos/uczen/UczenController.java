package pl.hefajstos.uczen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.hefajstos.QuickJSON;
import pl.hefajstos.hefajstos.QuickJSONArray;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class UczenController
{
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public static Uczen getUczenById (JdbcTemplate jdbcTemplate, String uid) /* FIXME: Nie działa i już */
    {
//        String sql = "SELECT * FROM UCZEN WHERE ID = ?";
//        try
//        {
//            return jdbcTemplate.queryForObject(
//                    sql, BeanPropertyRowMapper.newInstance(Uczen.class), uid);
//        }
//        catch (DataAccessException e)
//        {
//            System.out.println(e.toString());
//            return null;
//        }
        /* HOTFIX: Trzeba ulepszyc, narazie to wystarczy */
        List<Uczen> uczniowie = UczenController.getListaUczniow(jdbcTemplate);
        for (Uczen u : uczniowie)
            if (u.getId().equals(uid)) /* TODO: Remove trim() */
                return u;
        return null;
    }

    public static List<Uczen> getListaUczniow (JdbcTemplate jdbcTemplate)
    {
        String sql = "SELECT * FROM Uczen ORDER BY Klasa, Numer, Nazwisko, Imie";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Uczen.class));
    }

    public static UUID dodajUczniaDoBazy (JdbcTemplate jdbcTemplate, Uczen nowyUczen)
    {
        UUID noweId = UUID.randomUUID();
        assert(nowyUczen.getId() == null);

        String sql = "INSERT INTO Uczen VALUES (?, DEFAULT, ?, ?, ?, ?, ?, DEFAULT)";

        try
        {
            jdbcTemplate.update
            (
                sql, new Object []
                {
                    noweId.toString(),
                    nowyUczen.getImie(),
                    nowyUczen.getNazwisko(),
                    nowyUczen.getPesel(),
                    nowyUczen.getDataUrodzenia(),
                    nowyUczen.getMiejsceUrodzenia(),
                }
            );
        }
        catch (DataAccessException e)
        {
            System.out.println("[UczenController::dodajUczniaDoBazy(1)]: " + e.toString());
            return null;
        }

        try
        {
            jdbcTemplate.update
            (
                "INSERT INTO Konto VALUES (?, ?, 0, ?)",
                new Object []
                {
                    nowyUczen.getImie() + nowyUczen.getNazwisko(),
                    nowyUczen.getPesel(),
                    noweId.toString()
                }
            );
        }
        catch (DataAccessException e)
        {
            System.out.println("[UczenController::dodajUczniaDoBazy(2)]: " + e.toString());
        }

        return noweId;
    }

    public static void zapiszUczniaWBazie (JdbcTemplate jdbcTemplate, Uczen nowyUczen)
    {
        String sql = "UPDATE Uczen SET Imie = ?, Nazwisko = ?, Pesel = ?, DataUrodzenia = ?, MiejsceUrodzenia = ?, Klasa = ?, Numer = ? WHERE Id = ?";

        try
        {
            jdbcTemplate.update
            (
                sql, new Object []
                {
                    nowyUczen.getImie(),
                    nowyUczen.getNazwisko(),
                    nowyUczen.getPesel(),
                    nowyUczen.getDataUrodzenia(),
                    nowyUczen.getMiejsceUrodzenia(),
                    nowyUczen.getKlasa(),
                    nowyUczen.getNumer(),
                    nowyUczen.getId(),
                }
            );
        }
        catch (DataAccessException e)
        {
            System.out.println("[UczenController::zapiszUczniaWBazie()]: " + e.toString());
        }
    }

    public static boolean usunUcznia (JdbcTemplate jdbcTemplate, String id)
    {
        try
        {
            jdbcTemplate.update ("DELETE FROM Uczen WHERE Id = ?", new Object [] { id });
            jdbcTemplate.update ("DELETE FROM Konto WHERE Id = ?", new Object [] { id });
        }
        catch (DataAccessException e)
        {
            System.out.println("[UczenController::usunUcznia()]: " + e.toString());
            return false;
        }
        return true;
    }

}
