package pl.hefajstos.przedmioty;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "Przedmiot")
public class Przedmiot
{
    Integer id;
    String Nazwa;
    Integer Poziom;
    Integer Ilosc;
    String Obowiazkowy;

    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    public Integer getId() {
        return id;
    }
}
