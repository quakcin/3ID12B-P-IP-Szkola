package pl.hefajstos.klasy.generator;

import lombok.Data;
import pl.hefajstos.hefajstos.QuickJSON;
import pl.hefajstos.hefajstos.QuickJSONArray;
import pl.hefajstos.uczen.AbstrakcyjnyUczen;

import java.util.ArrayList;
import java.util.List;


@Data
public class RaportPasywny extends Raport
{
    public List<AbstrakcyjnyUczen> poprawnieDodaniUczniowie;
    public List<AbstrakcyjnyUczen> blednieDodaniUczniowie;

    public RaportPasywny ()
    {
        poprawnieDodaniUczniowie = new ArrayList<>();
        blednieDodaniUczniowie = new ArrayList<>();
    }

    public String toJson ()
    {
        return (new QuickJSON())
            .addRaw("ok", "true")
            .addRaw("poprawnieDodani", QuickJSONArray.fromList("uczniowie", poprawnieDodaniUczniowie))
            .addRaw("blednieDodani", QuickJSONArray.fromList("uczniowie", blednieDodaniUczniowie))
            .ret();
    }
}
