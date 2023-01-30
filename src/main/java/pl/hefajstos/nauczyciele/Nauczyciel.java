package pl.hefajstos.nauczyciele;

import lombok.Data;
import pl.hefajstos.hefajstos.Jsonable;
import pl.hefajstos.hefajstos.QuickJSON;

import java.sql.Date;

@Data
public class Nauczyciel implements Jsonable
{
    String nauczycielId;
    String imie;
    String nazwisko;
    Date dataZatrudnienia;
    String stopienZawodowy;
    String przedmioty;
    String klasaId;
    private final String bezPrzedmiotow = " - Klasa , ";

    public Nauczyciel() {

    }

    @Override
    public String toJson() {
        return (new QuickJSON())
                .add("imie", getImie())
                .add("nazw", getNazwisko())
                .add("id", getNauczycielId())
                .add("zatr", getDataZatrudnienia().toString())
                .add("stop", getStopienZawodowy())
                .add("prz", getPrzedmioty().equals(bezPrzedmiotow) ? "" : getPrzedmioty().substring(0, getPrzedmioty().length() - 2))
                .add("wych", getKlasaId())
                .addRaw("ok", "true")
                .ret();
    }
}

