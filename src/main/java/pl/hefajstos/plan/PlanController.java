package pl.hefajstos.plan;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.hefajstos.klasy.KlasyController;
import pl.hefajstos.klasy.KlasyView;
import pl.hefajstos.nauczyciele.Nauczyciel;
import pl.hefajstos.nauczyciele.NauczycieleController;
import pl.hefajstos.przedmioty.Przedmiot;
import pl.hefajstos.przedmioty.PrzedmiotyController;
import pl.hefajstos.uczen.Uczen;
import pl.hefajstos.uczen.UczenController;

import java.sql.Date;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PlanController
{

    public static List<Okno> getPlanByKlasaId (JdbcTemplate jdbcTemplate, String klasaId)
    {
        String sql = "SELECT * FROM PLANLEKCJI WHERE KLASA = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Okno.class), klasaId);
    }

    public static List<Okno> getPlanByNauczycielId (JdbcTemplate jdbcTemplate, String nauczycielId)
    {
        String sql = "SELECT * FROM PLANLEKCJI WHERE NAUCZYCIELID = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Okno.class), nauczycielId);
    }

    public static List<Okno> getPlanByUczenId (JdbcTemplate jdbcTemplate, String uczenId)
    {
        Uczen u = UczenController.getUczenById(jdbcTemplate, uczenId);
        return getPlanByKlasaId(jdbcTemplate, u.getKlasa());
    }

    public static void deletePlan (JdbcTemplate jdbcTemplate)
    {
        try
        {
            jdbcTemplate.update ("DELETE FROM Lekcja");
        }
        catch (DataAccessException e)
        {
            System.out.println("[PlanController::deletePlan()]: " + e.toString());
        }
    }

    public static void generujPlanLekcji (JdbcTemplate jdbcTemplate)
    { /* TODO: Ulepszyć / Zoptymalizować */
        /*
        Algorytm:
        -1. Sprawdzić stan bazy i zabezpieczenia !!!
        0. Wyczyścić Stan Bazy
        1. Dla każdej klasy -> Pobierz listę przedmiotów na jej poziom
            1.a. Dla każdego przedmiotu dobierz nauczyciela
                1.a.1. Dopisz losowo do planu ewidencję
                1.a.2. Przesuń następną losową godzinę
         */

        /* usuniecie starego planu */
        deletePlan(jdbcTemplate);

        /* generacja */
        List<KlasyView> klasy = KlasyController.getListaKlas(jdbcTemplate);

        for (KlasyView k : klasy)
        {
            int dzienTygodnia = 0;
            int minutyOdPulnocy = 60 * 9; // 1h ahead

            List<Przedmiot> przedmioty = PrzedmiotyController.getListaPrzedmiotowByPoziom(jdbcTemplate, k.getPoziom());
            /* shuffle */
            Collections.shuffle(przedmioty);

            for (Przedmiot p : przedmioty)
            {
               /* znajdź nauczyciela który może uczyć przedmiotu p */
                List<Nauczyciel> nauczyciele = NauczycieleController.getNauczycieleByPrzedmiot(jdbcTemplate, p.getId());
                assert(nauczyciele.size() > 0);

                /* wybierz losowego, TODO: Ulepszyć / Zoptymalizować */
                Nauczyciel wybranyNauczyciel = nauczyciele.get(ThreadLocalRandom.current().nextInt(0, nauczyciele.size() - 1));

                /* dodaj kolejno przedmioty */
                for (int i = 0; i < p.getIlosc(); i++)
                {
                    new Lekcja(jdbcTemplate, dzienTygodnia, minutyOdPulnocy, k.getNazwa(), p.getId(), wybranyNauczyciel.getNauczycielId());
                    dzienTygodnia++;
                    if (dzienTygodnia >= 5)
                    {
                        minutyOdPulnocy += 50;
                        dzienTygodnia = 0;
                    }
                }

            }
        }

    }
}
