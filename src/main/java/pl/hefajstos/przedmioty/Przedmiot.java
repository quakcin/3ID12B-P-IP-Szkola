package pl.hefajstos.przedmioty;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
public class Przedmiot
{
    Integer id;
    String nazwa;
    Integer poziom;
    Integer ilosc;
    String obowiazkowy;
}
