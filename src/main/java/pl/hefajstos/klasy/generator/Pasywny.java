package pl.hefajstos.klasy.generator;

import org.springframework.jdbc.core.JdbcTemplate;
import pl.hefajstos.klasy.KlasyController;
import pl.hefajstos.klasy.KlasyView;
import pl.hefajstos.uczen.UczenController;
import pl.hefajstos.uczen.Uczen;

import java.time.Year;
import java.util.List;

/**
 * Pasywnie generuje klasy - tzn. Przypisuje uczniów do klas
 * jeżeli nie są oni przypisani do żadnej.
 */
public class Pasywny extends Generator
{
    public static RaportPasywny generuj (JdbcTemplate jdbcTemplate, Parametry parametry)
    {
        List<Uczen> uczniowie = UczenController.getListaUczniow(jdbcTemplate);

        /* Alogrytm: Znajdź uczniów bez klas -> Przypisz do najbaridziej optymalnej */

        RaportPasywny raport = new RaportPasywny();

        for (Uczen u : uczniowie)
            if (u.getKlasa().equals("0"))
            { /* przypisz go gdzieś */
                if (przypiszDoKlasy(jdbcTemplate, u))
                    raport.getPoprawnieDodaniUczniowie().add(u);
                else
                    raport.getBlednieDodaniUczniowie().add(u);
            }

        return raport;
    }

    public static boolean przypiszDoKlasy (JdbcTemplate jdbcTemplate, Uczen u)
    {
        List<KlasyView> klasy = KlasyController.getListaKlas(jdbcTemplate);
        Integer obecnyRok = Year.now().getValue();
        int rocznikowoKlasa = obecnyRok - u.getDataUrodzenia().getYear() - 7 - 1899;
//        System.out.println(u.toString() + " powinien byc w klasie " + rocznikowoKlasa);

        KlasyView najlepszyWybor = null;
        for (KlasyView k : klasy)
            if (k.getPoziom().equals(rocznikowoKlasa))
            { /* klasa pasuje do rocznika */
//                System.out.println("DOPASOWANIE: " + k.getNazwa() + " ucz: " + k.getLiczbaUczniow());
                if (najlepszyWybor != null)
                    System.out.println(najlepszyWybor.toString());

                if (najlepszyWybor == null)
                {
                    najlepszyWybor = k; /* jedyny możliwy wybor na teraz */
                }
                else if (k.getLiczbaUczniow() < najlepszyWybor.getLiczbaUczniow())
                {
                    najlepszyWybor = k; /* lepsza opcja dla ucznia! */
                }
            }

        if (najlepszyWybor == null)
            return false;

//        System.out.println("WYBRANO: " + najlepszyWybor.toString());

        /* dodanie ucznia do klasy */
        KlasyController.przypiszUczniaDoKlasy(jdbcTemplate, najlepszyWybor, u);
        u.setKlasa(najlepszyWybor.getNazwa());
        return true;
    }
}
