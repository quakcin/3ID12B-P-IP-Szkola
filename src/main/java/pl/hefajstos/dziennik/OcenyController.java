package pl.hefajstos.dziennik;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.hefajstos.autoryzacja.KontoBaza;
import pl.hefajstos.autoryzacja.Sesja;
import pl.hefajstos.autoryzacja.SesjaController;
import pl.hefajstos.klasy.KlasyController;
import pl.hefajstos.nauczyciele.Nauczyciel;
import pl.hefajstos.przedmioty.Przedmiot;
import pl.hefajstos.uczen.Uczen;

import java.util.ArrayList;
import java.util.List;

public class OcenyController
{

    public static List<Ocena> getOcenyByUczenIdForPrzedmiotId (JdbcTemplate jdbcTemplate, String uczenId, String przedmiotId)
    {
        return jdbcTemplate.query
            (
                "SELECT * FROM OCENA WHERE PRZEDMIOTID = ? AND UCZENID = ? ORDER BY DATA",
                BeanPropertyRowMapper.newInstance(Ocena.class),
                przedmiotId, uczenId
            );
    }
    public static List<UczenWDzienniku> getOcenyByKlasaAndPrzedmiotId (JdbcTemplate jdbcTemplate, String klasa, String przedmiotId)
    {
        /* dla kazdego ucznia w klasie
           pobierz liste ocen
         */
        ArrayList<UczenWDzienniku> uczniowie = new ArrayList<>();

        List<Uczen> uczniowieWKlasie = KlasyController.getListaUczniowByKlasa(jdbcTemplate, klasa);
        
        for (Uczen u : uczniowieWKlasie)
        {
            /* Dodaj do kontenera i pobierz
                jego / jej liste ocen
             */
            UczenWDzienniku uczenWDzienniku = new UczenWDzienniku();
            uczenWDzienniku.setUczen(u);
            List<Ocena> ocenUcznia = getOcenyByUczenIdForPrzedmiotId(jdbcTemplate, u.getId(), przedmiotId);
            uczenWDzienniku.setOceny(ocenUcznia);
            uczniowie.add(uczenWDzienniku);
        }
        
        return uczniowie;
    }

    public static boolean addOcenaByUczenIdInPrzedmiotId (JdbcTemplate jdbcTemplate, String sid, String uczenId, String przedmiotId, String ocena, String waga, String komentarz, String kategoria)
    {
        Sesja s = SesjaController.getSesjaByToken(jdbcTemplate, sid);
        try
        {
            jdbcTemplate.update (
                "INSERT INTO OCENA VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, SYSDATE)",
                ocena, waga, kategoria, komentarz, uczenId, przedmiotId, s.getKlucz()
            );
        }
        catch (DataAccessException e)
        {
            System.out.println("[OcenyController::addOcenaByUczenIdInPrzedmiotId()]: " + e.toString());
            return false;
        }

        return true;
    }

    public static boolean aktualizujOceneWBazie (JdbcTemplate jdbcTemplate, Ocena nowaOcena, String sid)
    {
        String sql = "UPDATE Ocena SET STOPIEN = ?, WAGA = ?, KATEGORIA = ?, KOMENTARZ = ?, " +
                    "UCZENID = ?, PRZEDMIOTID = ?, NAUCZYCIELID = ?, DATA = ? WHERE Id = ?";
        Sesja s = SesjaController.getSesjaByToken(jdbcTemplate, sid);

        try
        {
            jdbcTemplate.update
            (
                sql,
                nowaOcena.getStopien(), nowaOcena.getWaga(), nowaOcena.getKategoria(),
                nowaOcena.getKomentarz(), nowaOcena.getUczenId(), nowaOcena.getPrzedmiotId(),
                s.getKlucz(), nowaOcena.getData(), nowaOcena.getId()
            );
        }
        catch (DataAccessException e)
        {
            System.out.println("[OcenyController::aktualizujOceneWBazie(1)]: " + e.toString());
            return false;
        }
        return true;
    }

    public static boolean usunOcene (JdbcTemplate jdbcTemplate, String id)
    {
        try
        {
            jdbcTemplate.update ("DELETE FROM Ocena WHERE Id = ?", new Object [] { id });
        }
        catch (DataAccessException e)
        {
            System.out.println("[OcenyController::usunOcene()]: " + e.toString());
            return false;
        }
        return true;
    }

    public static Ocena getOcenaById (JdbcTemplate jdbcTemplate, String ocenaId)
    {
        List<Ocena> oceny = jdbcTemplate.query
        (
            "SELECT * FROM OCENA WHERE ID = ?",
            BeanPropertyRowMapper.newInstance(Ocena.class),
            ocenaId
        );
        for (Ocena o : oceny)
            return o;
        return null;
    }

}
