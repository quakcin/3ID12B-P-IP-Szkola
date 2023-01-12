package pl.hefajstos.Klasy;

import lombok.Data;
import pl.hefajstos.hefajstos.QuickJSON;

@Data
public class KlasyView
{
    String nazwa;
    Integer liczbaUczniow;
    String wychowawca;

    @Override
    public String toString ()
    {
        return (new QuickJSON())
                .add("nazwa", getNazwa())
                .add("liczb", getLiczbaUczniow().toString())
                .add("wych", getWychowawca())
                .ret();
    }
}
