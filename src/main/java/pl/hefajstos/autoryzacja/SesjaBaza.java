package pl.hefajstos.autoryzacja;

import lombok.Data;

import javax.persistence.Table;
import java.sql.Timestamp;

@Data
public class SesjaBaza
{
    String token;
    String id;
    Timestamp expr;
    Integer typ;
}
