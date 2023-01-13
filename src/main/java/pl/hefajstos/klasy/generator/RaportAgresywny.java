package pl.hefajstos.klasy.generator;

import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.hefajstos.hefajstos.Jsonable;
import pl.hefajstos.hefajstos.QuickJSON;
import pl.hefajstos.hefajstos.QuickJSONArray;
import pl.hefajstos.klasy.KlasyController;
import pl.hefajstos.klasy.KlasyView;

import java.util.List;
@Data
public class RaportAgresywny extends Raport
{
    private Integer iloscUtworzonychKlas;
    private List<KlasyView> listaUtworzonychKlas;
    private Boolean spelnioneOgraniczenia;

    public RaportAgresywny(JdbcTemplate jdbcTemplate)
    { /* Raport generowany z bierzącego stanu bazy */
        List<KlasyView> listaKlas = KlasyController.getListaKlas(jdbcTemplate);
        setIloscUtworzonychKlas(listaKlas.size());
        setListaUtworzonychKlas(listaKlas);
    }
    @Override
    public String toJson()
    {
        return (new QuickJSON())
                .addRaw("ok", "true")
                .addRaw("utworzono", QuickJSONArray.fromList("klasy", getListaUtworzonychKlas()))
                .addRaw("iloscKlas", getIloscUtworzonychKlas().toString())
                .addRaw("spelnioneOgraniczenia", getSpelnioneOgraniczenia().toString())
                .ret();
    }
}
