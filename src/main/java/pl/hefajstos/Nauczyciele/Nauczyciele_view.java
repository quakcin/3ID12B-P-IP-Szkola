package pl.hefajstos.Nauczyciele;

import com.sun.xml.bind.v2.model.annotation.Quick;
import lombok.Data;
import org.hibernate.annotations.Table;
import pl.hefajstos.hefajstos.QuickJSON;

import javax.persistence.Entity;
import java.sql.Date;

@Data
public class Nauczyciele_view
{
    String Nauczyciel_Id;
    String Imie;
    String Nazwisko;
    Date Data_zatrudnienia;
    String Stopien_zawodowy;
    String Przedmioty;

    private final String bezPrzedmiotow = " klasa , ";

    @Override
    public String toString()
    {
        return (new QuickJSON())
                .add("imie", getImie())
                .add("nazw", getNazwisko())
                .add("id", getNauczyciel_Id())
                .add("zatr", getData_zatrudnienia().toString())
                .add("stop", getStopien_zawodowy())
                .add("prz", getPrzedmioty().equals(bezPrzedmiotow) ? "" : getPrzedmioty())
                .ret();
    }
}
