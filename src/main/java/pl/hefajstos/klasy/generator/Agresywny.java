package pl.hefajstos.klasy.generator;

import org.springframework.jdbc.core.JdbcTemplate;
import pl.hefajstos.nauczyciele.Nauczyciel;
import pl.hefajstos.nauczyciele.NauczycieleController;
import pl.hefajstos.uczen.AbstrakcyjnyUczen;
import pl.hefajstos.uczen.UczenController;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * Generuje klasy od podstaw, jeżeli jakoweś już istnieją
 * to usuwa je. Może przebić się przez ograniczenia
 * jeżeli system tego wymaga.
 */
public class Agresywny extends Generator
{
    public static RaportAgresywny generuj (JdbcTemplate jdbcTemplate, Parametry parametry)
    {
        List<Nauczyciel> nauczyciele = NauczycieleController.getListaNauczycieli(jdbcTemplate);
        List<AbstrakcyjnyUczen> uczniowie = UczenController.getListaUczniow(jdbcTemplate);

        Integer cMin = parametry.getMinUczniowWKlasie();
        Integer cMax = parametry.getMaxUczniowWKlasie();
        Boolean spelnioneOgraniczenia = true;

        /*
            0. Usunięcie wszystkich klas oraz ich poączeń
         */

        jdbcTemplate.execute("UPDATE Uczen SET Klasa = '0'");
        jdbcTemplate.execute("UPDATE Nauczyciel SET KlasaId = 'Bez'");
        jdbcTemplate.execute("DELETE FROM Klasa WHERE Nazwa != '0'");

        /*
            1. Podział na roczniki
         */

        ArrayList<AbstrakcyjnyUczen>[] rocznik = new ArrayList[8];

        for (int i = 0; i < 8; i++)
            rocznik[i] = new ArrayList<AbstrakcyjnyUczen>();

        Integer obecnyRok = Year.now().getValue();
        for (AbstrakcyjnyUczen u : uczniowie)
        {
            Integer poziom = obecnyRok - u.getDataUrodzenia().toLocalDate().getYear() - 7;
            if (poziom < 0)
                poziom = 0;
            if (poziom > 7)
                poziom = 7;
            rocznik[poziom].add(u);
        }

        /*
            Podział na klasy w rocznikach
            |max - min| - najbardziej optymalny podział wg. dyrektora
         */


        Integer[] ilosciKlas = new Integer[8];
        for (int i = 0; i < 8; i++)
            ilosciKlas[i] = 0;

        Integer optymalnyPodzial = cMax - 1;

        boolean warunkiSpelnione = false;
        Integer bezpiecznik = 100;

        do
        {
            if (bezpiecznik-- <= 0)
            {
                break;
            }
            Integer przydzieleniWychowawcy = 0;
            if (optymalnyPodzial <= 0)
                optymalnyPodzial = cMin;

            for (int i = 0; i < 8; i++)
            {
                Integer iloscUczniow = rocznik[i].size();
                Integer iloscKlas = (int) Math.ceil((float) iloscUczniow / (float) optymalnyPodzial);
                Integer uczniowieNaKlase = (int) Math.ceil((float) iloscUczniow / (float) iloscKlas);
                ilosciKlas[i] = iloscKlas;

                przydzieleniWychowawcy += iloscKlas;
            }

            if (przydzieleniWychowawcy <= 0)
            {
                optymalnyPodzial--;
                spelnioneOgraniczenia = false;
            }
            else if (przydzieleniWychowawcy <= nauczyciele.size())
                warunkiSpelnione = true;
            else
            {
                optymalnyPodzial++;
                spelnioneOgraniczenia = false;
            }
        }
        while(warunkiSpelnione == false);

        /*
            Utwórz Nowe klasy wg. optymalnego podziału
            ORAZ przypisanie wychowawców
         */

        int nauczyciel_idx = 0;
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < ilosciKlas[i]; j++)
            {
                String klasa = (i + 1) + (new Character((char)((char) 'A' + j))).toString();

                jdbcTemplate.update("INSERT INTO Klasa VALUES (?, ?)", klasa, (i + 1));

                jdbcTemplate.update(
                "UPDATE Nauczyciel SET KlasaId = ? WHERE Id = ?",
                    klasa,
                    nauczyciele.get(nauczyciel_idx++).getNauczycielId()
                );

            }
        }

        /*
            Używając optymalnegoPodziału, Przypisz kolejnych uczniów
            z roczników do ich nowych klas!
         */

        for (int i = 0; i < 8; i++)
        {
            int uczenOffset = 0;
            Integer iloscUczniow = rocznik[i].size();
            Integer iloscKlas = (int) Math.ceil((float) iloscUczniow / (float) optymalnyPodzial);
            Integer uczniowieNaKlase = (int) Math.ceil((float) iloscUczniow / (float) iloscKlas);
            for (int j = 0; j < ilosciKlas[i]; j++)
            {
                String klasa = (i + 1) + (new Character((char)((char) 'A' + j))).toString();
                // Przypisac kolejne [uczenOffset, czenOffset + uczniowieNaKlase] do klasy (i+1)'CH'
                for (int k = 0; k < uczniowieNaKlase; k++)
                {
                    int uczen_idx = uczenOffset + k;
                    if (uczen_idx >= rocznik[i].size())
                        break;

                    AbstrakcyjnyUczen uczen = rocznik[i].get(uczen_idx);
                    jdbcTemplate.update(
                    "UPDATE Uczen SET Klasa = ?, Numer = ? WHERE Id = ?",
                    klasa, "" + (k + 1), uczen.getId()
                    );
                }
                uczenOffset += uczniowieNaKlase;
            }
        }

        RaportAgresywny r = new RaportAgresywny(jdbcTemplate);
        r.setSpelnioneOgraniczenia(spelnioneOgraniczenia);
        return r;
    }
}
