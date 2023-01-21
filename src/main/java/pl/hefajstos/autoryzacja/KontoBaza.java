package pl.hefajstos.autoryzacja;

import lombok.Data;
import pl.hefajstos.hefajstos.Jsonable;
import pl.hefajstos.hefajstos.QuickJSON;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
public class KontoBaza implements Jsonable
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


    @Override
    public String toJson()
    {
        return (new QuickJSON())
            .add("nickname", getNickname())
            .add("haslo", getHaslo())
            .addRaw("typ", typ.toString())
            .add("id", getId())
            .ret();
    }
}
