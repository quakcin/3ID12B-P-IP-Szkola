package pl.hefajstos.uczen;

import lombok.Data;
import pl.hefajstos.nauczyciele.Nauczyciel;
@Data
public class UczenZAspergerem extends UczenSpecjalny
{
    private Nauczyciel nauczycielWspomagajacy;
}
