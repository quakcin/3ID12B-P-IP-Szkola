package pl.hefajstos.dziennik;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.hefajstos.autoryzacja.KontoBaza;
import pl.hefajstos.autoryzacja.Sesja;
import pl.hefajstos.autoryzacja.SesjaController;
import pl.hefajstos.klasy.KlasyController;
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
                "SELECT * FROM OCENA WHERE PRZEDMIOTID = ? AND UCZENID = ?",
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


}
