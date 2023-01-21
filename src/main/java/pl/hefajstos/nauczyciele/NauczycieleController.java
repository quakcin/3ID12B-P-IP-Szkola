package pl.hefajstos.nauczyciele;

import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.hefajstos.autoryzacja.Sesja;
import pl.hefajstos.autoryzacja.SesjaController;

import java.util.List;
import java.util.UUID;

public class NauczycieleController
{
    public static List<Nauczyciel> getListaNauczycieli (JdbcTemplate jdbcTemplate)
    {
        String sql = "SELECT * FROM NauczycieleView";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Nauczyciel.class));
    }

    public static Nauczyciel getNauczycielById (JdbcTemplate jdbcTemplate, String nid)
    {
        List<Nauczyciel> nauczyciele = NauczycieleController.getListaNauczycieli(jdbcTemplate);
        for (Nauczyciel n : nauczyciele)
            if (n.getNauczycielId().equals(nid))
                return n;
        return null;
    }

    public static UUID dodajNauczycielaDoBazy (JdbcTemplate jdbcTemplate, Nauczyciel nowyNauczyciel, String zakodowanaListaPrzedmiotow)
    {
        UUID noweId = UUID.randomUUID();
        assert(nowyNauczyciel.getNauczycielId() == null);

        String sql = "INSERT INTO Konto VALUES (?, ?, 1, ?)";

        try
        {
            jdbcTemplate.update
            (
                sql,
                    nowyNauczyciel.getImie() + nowyNauczyciel.getNazwisko(),
                    nowyNauczyciel.getNazwisko() + "1234",
                    noweId.toString()
            );
        }
        catch (DataAccessException e)
        {
            System.out.println("[NauczycielController::dodajNauczycielaDoBazy(1)]: " + e.toString());
            return null;
        }

        try
        {
            jdbcTemplate.update
            (
            "INSERT INTO Nauczyciel VALUES (?, ?, ?, ?, ?, ?)",
                    noweId.toString(),
                    nowyNauczyciel.getImie(),
                    nowyNauczyciel.getNazwisko(),
                    nowyNauczyciel.getDataZatrudnienia(),
                    nowyNauczyciel.getStopienZawodowy(),
                    nowyNauczyciel.getKlasaId());
        }
        catch (DataAccessException e)
        {
            System.out.println("[NauczycieleController::dodajNauczycielaDoBazy(2)]: " + e.toString());
            return null;
        }

        /*
            Powiązanie z nauczanymi przedmiotami
         */

        String[] idPrzedmiotow = zakodowanaListaPrzedmiotow.split("_");

        for (String idPrzedmiotu : idPrzedmiotow)
        {
            sql = "INSERT INTO NauczycielPrzedmiot VALUES (?, ?)";

            try
            {
                jdbcTemplate.update(sql, noweId.toString(), idPrzedmiotu);
            }
            catch (Exception e)
            {
                System.out.println("[NauczycieleController::dodajNauczycielaDoBazy(3)]: " + e.toString());
                return null;
            }
        }

        return noweId;
    }

    public static boolean usunNauczyciela (JdbcTemplate jdbcTemplate, String id)
    {
        try
        {
            jdbcTemplate.update ("DELETE FROM Nauczyciel WHERE Id = ?", new Object [] { id });
            jdbcTemplate.update ("DELETE FROM Konto WHERE Id = ?", new Object [] { id });
        }
        catch (DataAccessException e)
        {
            System.out.println("[NauczycielController::usunNauczyciela()]: " + e.toString());
            return false;
        }
        return true;
    }

    public static boolean aktualizujNauczycielaWBazie (JdbcTemplate jdbcTemplate, Nauczyciel nowyNauczyciel, String zakodowanaListaPrzedmiotow)
    {
        /*
            Zmiana w tabeli nauczyciel
         */

        String sql = "UPDATE Nauczyciel SET Imie = ?, Nazwisko = ?, DataZatrudnienia = ?, StopienZawodowy = ?, KlasaId = ? WHERE Id = ?";

        try
        {
            jdbcTemplate.update
            (
                sql,
                nowyNauczyciel.getImie(),
                nowyNauczyciel.getNazwisko(),
                nowyNauczyciel.getDataZatrudnienia(),
                nowyNauczyciel.getStopienZawodowy(),
                nowyNauczyciel.getKlasaId(),
                nowyNauczyciel.getNauczycielId()
            );
        }
        catch (DataAccessException e)
        {
            System.out.println("[NauczycieleController::aktualizujNauczycielaWBazie(1)]: " + e.toString());
            return false;
        }


        /*
            Usuniecie starego powiazania Nauczyciel <--> Przedmiot
         */

        sql = "DELETE FROM NauczycielPrzedmiot WHERE NauczycielId = ?";

        try
        {
            jdbcTemplate.update(sql, nowyNauczyciel.getNauczycielId());
        }
        catch (Exception e)
        {
            System.out.println("[NauczycieleController::aktualizujNauczycielaWBazie(2)]: " + e.toString());
            return false;
        }

        /*
            Powiązanie z nauczanymi przedmiotami
         */

        String[] idPrzedmiotow = zakodowanaListaPrzedmiotow.split("_");

        for (String idPrzedmiotu : idPrzedmiotow)
        {
            sql = "INSERT INTO NauczycielPrzedmiot VALUES (?, ?)";

            try
            {
                jdbcTemplate.update(sql, nowyNauczyciel.getNauczycielId(), idPrzedmiotu);
            }
            catch (Exception e)
            {
                System.out.println("[NauczycieleController::aktualizujNauczycielaWBazie(2)]: " + e.toString());
                return false;
            }
        }

        return true;
    }

    public static List<Nauczyciel> getNauczycieleByPrzedmiot (JdbcTemplate jdbcTemplate, Integer id)
    {
        String sql = "SELECT * FROM NAUCZYCIELUCZY WHERE PRZEDMIOTID = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Nauczyciel.class), id);
    }

    public static List<LekcjaNauczyciela> getLekcjeNauczycielaBySession (JdbcTemplate jdbcTemplate, String sid)
    {
        Sesja s = SesjaController.getSesjaByToken(jdbcTemplate, sid);
        String sql = "SELECT * FROM NauczycielLekcje WHERE nauczycielid = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(LekcjaNauczyciela.class), s.getKlucz());
    }

}
