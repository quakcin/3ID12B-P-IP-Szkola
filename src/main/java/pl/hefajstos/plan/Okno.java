package pl.hefajstos.plan;

import com.sun.xml.bind.v2.model.annotation.Quick;
import lombok.Data;
import pl.hefajstos.hefajstos.Jsonable;
import pl.hefajstos.hefajstos.QuickJSON;

@Data
public class Okno implements Jsonable
{
    public String przedmiot;
    public String nauczyciel;
    public Integer godzina;
    public Integer dzien;
    public String klasa;
    public String nauczycielId;

    @Override
    public String toJson() {
        return (new QuickJSON())
                .add("przedmiot", getPrzedmiot())
                .add("nauczyciel", getNauczyciel())
                .addRaw("godzina", getGodzina().toString())
                .addRaw("dzien", getDzien().toString())
                .add("klasa", getKlasa())
                .add("nauczycielId", getNauczycielId())
                .ret();
    }
}
