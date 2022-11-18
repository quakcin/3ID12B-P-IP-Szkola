package pl.hefajstos.autoryzacja;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Sesja
{
    private String token;
    private RodzajKonta rodzajKonta;
    private String klucz;
}
