package pl.hefajstos.plan;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.hefajstos.hefajstos.QuickJSON;
import pl.hefajstos.hefajstos.QuickJSONArray;
import pl.hefajstos.klasy.KlasyController;
import pl.hefajstos.klasy.Klasa;
import pl.hefajstos.nauczyciele.Nauczyciel;
import pl.hefajstos.nauczyciele.NauczycieleController;
import pl.hefajstos.przedmioty.Przedmiot;
import pl.hefajstos.przedmioty.PrzedmiotyController;
import pl.hefajstos.uczen.Uczen;
import pl.hefajstos.uczen.UczenController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static String generujPlanLekcji (JdbcTemplate jdbcTemplate)
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

        /* json report */
        QuickJSONArray rep = new QuickJSONArray("raport");

        /* generacja */
        List<Klasa> klasy = KlasyController.getListaKlas(jdbcTemplate);

        for (Klasa k : klasy)
        {
            QuickJSONArray repKlasa = new QuickJSONArray("przedmioty");
            int dzienTygodnia = 0;
            int minutyOdPulnocy = 0;

            List<Przedmiot> przedmioty = PrzedmiotyController.getListaPrzedmiotowByPoziom(jdbcTemplate, k.getPoziom());
            /* shuffle */
            Collections.shuffle(przedmioty);

            for (Przedmiot p : przedmioty)
            {
               /* znajdź nauczyciela który może uczyć przedmiotu p */
                List<Nauczyciel> nauczyciele = NauczycieleController.getNauczycieleByPrzedmiot(jdbcTemplate, p.getId());
                Collections.shuffle(nauczyciele);
                assert(nauczyciele.size() > 0);

                /* wybierz losowego, TODO: Ulepszyć / Zoptymalizować */
                Nauczyciel wybranyNauczyciel = nauczyciele.get(0);
                repKlasa.add(new QuickJSON()
                        .add("nazwa", p.getNazwa())
                        .add("nauczyciel", wybranyNauczyciel.getImie() + " " + wybranyNauczyciel.getNazwisko())
                        .ret()
                );

                /* dodaj kolejno przedmioty */
                for (int i = 0; i < p.getIlosc(); i++)
                {
                    new Lekcja(jdbcTemplate, dzienTygodnia, minutyOdPulnocy, k.getNazwa(), p.getId(), wybranyNauczyciel.getNauczycielId());
                    dzienTygodnia++;
                    if (dzienTygodnia >= 5)
                    {
                        minutyOdPulnocy += 1;
                        dzienTygodnia = 0;
                    }
                }

            }
            rep.add(new QuickJSON().add("klasa", k.getNazwa()).addRaw("przedmioty", repKlasa.ret()).ret());
        }
        return rep.ret();
    }

    public static List<GodzinaLekcyjna> getGodziny (JdbcTemplate jdbcTemplate)
    {
        String sql = "SELECT * FROM GODZINYLEKCYJNE";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(GodzinaLekcyjna.class));
    }

    public static boolean zapiszGodzinyWBazie (JdbcTemplate jdbcTemplate, String lista_godzin )
    {

        String[] okna = lista_godzin.split("-");

        int i = 1;
        for (String okno : okna)
        {
            String[] tokeny = okno.split("_");
            String sql = "UPDATE GodzinyLekcyjne SET GODZROZP = ?, GODZZAK = ? WHERE ID = ?";

            try
            {
                jdbcTemplate.update
                (
                        sql,
                        tokeny[0], tokeny[1], i++
                );
            }
            catch (DataAccessException e)
            {
                System.out.println("[PlanController::zapiszGodzinyWBazie()]: " + e.toString());
                return false;
            }
        }

        return true;
    }

    public static Integer[] getGodzinyByKlasa (JdbcTemplate jdbcTemplate, String klasa, String dzien)
    {
        String sql = "SELECT * FROM PLANlEKCJI WHERE DZIEN = ? AND KLASA=?";
        List<Okno> okna = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Okno.class), dzien, klasa);

        Integer[] minMax = new Integer[2];
        minMax[0] = okna.get(0).getGodzina();
        minMax[1] = okna.get(okna.size() - 1).getGodzina() + 1;
        return minMax;
    }

    public static List<Okno> getGodzinyKlasyByDzien (JdbcTemplate jdbcTemplate, String dzien, String klasa)
    {
        String sql = "SELECT * FROM PLANlEKCJI WHERE DZIEN = ? AND KLASA=?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Okno.class), dzien, klasa);
    }

}
