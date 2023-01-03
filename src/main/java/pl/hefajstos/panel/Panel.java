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
        String obrys = "<h2>error 500</h2>";

        /*
            Ładowanie panelu nawigacji
         */
        try {
            obrys = String.join("", Files.readAllLines(
                    Paths.get((getClass().getClassLoader()).getResource(
                            "Panel/panel.html").toURI())));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        /*
            Ładowanie strony dla żądanego podkatalogu zasobów
         */

        try {
            String kontent = String.join("", Files.readAllLines(
                    Paths.get((getClass().getClassLoader()).getResource(
                            String.format("Panel/%s/%s.html", konto, okno)).toURI())));

            obrys = obrys.replaceAll("%kontent", kontent);

            /*
                Wyciągnięcie tytułu z pliku zasobów
             */

            String[] lines = kontent.split("\n\r");
            for (String line : lines)
            {
                System.out.println("Line: " + line + "\n");
                String[] tokens = line.split(" ");
                if (tokens[0].equals("H-API-TITLE:"))
                {
                    System.out.println(String.format("tok: %s\n", tokens[0]));
                    obrys = obrys.replaceAll("%tytul", tokens[1]);
                    break;
                }
            }

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return obrys.replaceAll("%navbar", getPanelNawigacji(rodzajKonta));
    }
}
