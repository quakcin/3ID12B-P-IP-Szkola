package pl.hefajstos.panel;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.autoryzacja.RodzajKonta;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

@RestController
public class Panel
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Data
    @AllArgsConstructor
    private class Okno
    {
        String sciezka;
        String tytul;
    }

    private HashMap<String, Okno> getRodzajeOkien (RodzajKonta rodzajKonta)
    {
        HashMap<String, Okno> okna = new HashMap<>();
        if (rodzajKonta.equals(RodzajKonta.Uczen))
        {
            okna.put("konto", new Okno("Panel/Uczen/konto.html", "Moje Konto"));
        }
        if (rodzajKonta.equals(RodzajKonta.Nauczyciel))
        {
            okna.put("konto", new Okno("Panel/Nauczyciel/konto.html", "Moje Konto"));
        }
        if (rodzajKonta.equals(RodzajKonta.Dyrektor))
        {
            okna.put("konto", new Okno("Panel/Dyrektor/konto.html", "Moje Konto"));
        }
        if (rodzajKonta.equals(RodzajKonta.Sekretarka))
        {
            okna.put("konto", new Okno("Panel/Sekretarka/konto.html", "Moje Konto"));
        }
        return okna;
    }

    private String getPanelNawigacji (RodzajKonta rodzajKonta)
    {
        String panel = "";
        String sciezka = String.format("Panel/NavBar/%s.html", rodzajKonta.toString());
        try {
            panel = String.join("", Files.readAllLines(
                    Paths.get((getClass().getClassLoader()).getResource(
                            sciezka).toURI())));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return panel;
    }

    @GetMapping("/panel/{konto}/{okno}")
    /*
        /panel/konto
        /panel/plan_lekcji ...
     */
    @ResponseBody
    public String getIndeks (@PathVariable("konto") String konto, @PathVariable("okno") String okno)
    {
        RodzajKonta rodzajKonta = RodzajKonta.valueOf(konto);
        HashMap<String, Okno> okna = getRodzajeOkien(rodzajKonta);

        String obrys = "<h2>error 500</h2>";
        try {
            obrys = String.join("", Files.readAllLines(
                    Paths.get((getClass().getClassLoader()).getResource(
                            "Panel/panel.html").toURI())));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        try {
            Okno wybrane = okna.get(okno);
            String kontent = String.join("", Files.readAllLines(
                    Paths.get((getClass().getClassLoader()).getResource(
                            wybrane.sciezka).toURI())));
            obrys = obrys.replaceAll("%kontent", kontent);
            obrys = obrys.replaceAll("%tytul", wybrane.tytul);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return obrys.replaceAll("%navbar", getPanelNawigacji(rodzajKonta));
    }
}
