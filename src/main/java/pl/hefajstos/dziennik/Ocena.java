package pl.hefajstos.dziennik;

import lombok.Data;
import pl.hefajstos.hefajstos.Jsonable;
import pl.hefajstos.hefajstos.QuickJSON;

import java.sql.Date;

@Data
public class Ocena implements Jsonable
{
    public Integer id;
    public String stopien;
    public Integer waga;
    public String kategoria;
    public String komentarz;
    public String uczenId;
    public Integer przedmiotId;
    public String nauczycielId;
    public Date data;

    public String toJson ()
    {
        return (new QuickJSON())
                .addRaw("id", getId().toString())
                .add("stopien", getStopien())
                .addRaw("waga", getWaga().toString())
                .add("uczen", getUczenId())
                .add("kategoria", getKategoria())
                .addRaw("przedmiot", getPrzedmiotId().toString())
                .add("nauczyciel", getNauczycielId())
                .add("data", getData().toString())
                .add("komentarz", getKomentarz())
                .ret();
    }
}
