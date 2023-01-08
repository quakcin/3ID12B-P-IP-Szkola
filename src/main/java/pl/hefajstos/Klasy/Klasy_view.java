package pl.hefajstos.Klasy;

import lombok.Data;
import pl.hefajstos.hefajstos.QuickJSON;

@Data
public class Klasy_view
{
    String Nazwa;
    Integer Liczba_uczniow;
    String Wychowawca;

    @Override
    public String toString ()
    {
        return (new QuickJSON())
                .add("nazwa", getNazwa())
                .add("liczb", getLiczba_uczniow().toString())
                .add("wych", getWychowawca())
                .ret();
    }
}
