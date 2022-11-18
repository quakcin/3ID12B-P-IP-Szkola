package pl.hefajstos.autoryzacja;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Table(name = "Konto")
public class KontoBaza
{
    String nickname;
    String haslo;
    Integer typ;
    String id;
}
