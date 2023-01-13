package pl.hefajstos.klasy;

import lombok.Data;
import pl.hefajstos.hefajstos.Jsonable;
import pl.hefajstos.hefajstos.QuickJSON;

@Data
public class KlasyView implements Jsonable
{
    String nazwa;
    Integer liczbaUczniow;
    String wychowawca;
    Integer poziom;

    public String toJson ()
    {
        return (new QuickJSON())
                .add("nazwa", getNazwa())
                .add("liczb", getLiczbaUczniow().toString())
                .add("wych", getWychowawca())
                .addRaw("poziom", getPoziom().toString())
                .ret();
    }
}
