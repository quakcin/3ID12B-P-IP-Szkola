package pl.hefajstos.Nauczyciele;

import lombok.Data;
import pl.hefajstos.hefajstos.QuickJSON;

import java.sql.Date;

@Data
public class NauczycieleView
{
    String nauczycielId;
    String imie;
    String nazwisko;
    Date dataZatrudnienia;
    String stopienZawodowy;
    String przedmioty;
    String klasaId;
    private final String bezPrzedmiotow = " - Klasa , ";

    @Override
    public String toString()
    {
        return (new QuickJSON())
                .add("imie", getImie())
                .add("nazw", getNazwisko())
                .add("id", getNauczycielId())
                .add("zatr", getDataZatrudnienia().toString())
                .add("stop", getStopienZawodowy())
                .add("prz", getPrzedmioty().equals(bezPrzedmiotow) ? "" : getPrzedmioty().substring(0, getPrzedmioty().length() - 2))
                .add("wych", getKlasaId())
                .ret();
    }
}
