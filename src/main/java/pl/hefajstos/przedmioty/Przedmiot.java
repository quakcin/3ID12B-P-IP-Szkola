package pl.hefajstos.przedmioty;

import lombok.Data;
import pl.hefajstos.hefajstos.Jsonable;
import pl.hefajstos.hefajstos.QuickJSON;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
public class Przedmiot implements Jsonable
{
    Integer id;
    String nazwa;
    Integer poziom;
    Integer ilosc;
    String obowiazkowy;

    public String toJson () {
        return (new QuickJSON())
                .add("id", "" + getId())
                .add("nazwa", getNazwa())
                .add("poziom", "" + getPoziom())
                .add("ilosc", "" + getIlosc())
                .add("obw", getObowiazkowy())
                .ret();
    }
}
