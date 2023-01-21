package pl.hefajstos.nauczyciele;

import lombok.Data;
import pl.hefajstos.hefajstos.Jsonable;
import pl.hefajstos.hefajstos.QuickJSON;

@Data
public class LekcjaNauczyciela implements Jsonable
{
    public String nauczycielId;
    public String przedmiotId;
    public String nazwa;
    public String klasaId;

    public String toJson ()
    {
        return (new QuickJSON())
                .add("id", nauczycielId)
                .add("przid", przedmiotId)
                .add("prz", getNazwa())
                .add("klasa", getKlasaId())
                .ret();
    }
}
