package pl.hefajstos.uczen;

import lombok.Data;
import pl.hefajstos.przedmioty.Przedmiot;

import java.util.List;

@Data
public class UczenDyslektyk extends UczenSpecjalny
{
    private List<Przedmiot> dodatkoweZajęcia;
}
