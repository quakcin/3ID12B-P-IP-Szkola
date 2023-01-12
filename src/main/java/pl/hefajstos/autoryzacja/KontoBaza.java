package pl.hefajstos.autoryzacja;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
public class KontoBaza
{
    String nickname;
    String haslo;
    Integer typ;
    String id;

    public RodzajKonta getTyp ()
    {
        return switch (typ)
        {
            case 0 -> RodzajKonta.Uczen;
            case 1 -> RodzajKonta.Nauczyciel;
            case 2 -> RodzajKonta.Sekretarka;
            case 3 -> RodzajKonta.Dyrektor;
            default -> RodzajKonta.Inne;
        };
    }
}
