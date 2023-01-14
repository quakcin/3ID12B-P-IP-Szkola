package pl.hefajstos.przedmioty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.hefajstos.QuickJSON;
import pl.hefajstos.hefajstos.QuickJSONArray;


import java.util.List;

@RestController
public class PrzedmiotyMapping
{
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @GetMapping("/przedmioty/info/{sid}/{id}")
    public String infoPrzedmioty ( @PathVariable("sid") String sid, @PathVariable("id") Integer id )
    {
        Przedmiot p = PrzedmiotyController.getPrzedmiotById(jdbcTemplate, id);
        return p == null
            ? (new QuickJSON()).addRaw("ok", "false").ret()
            : p.toJson();
    }

    @GetMapping("/przedmioty/lista/{sid}")
    public String mappingPrzedmiotLista (@PathVariable("sid") String sid)
    {
        List<Przedmiot> przedmioty = PrzedmiotyController.getListaPrzedmiotow(jdbcTemplate);
        QuickJSONArray qja = new QuickJSONArray("przedmioty");
        for (Przedmiot p : przedmioty)
            qja.add(p.toJson());
        return qja.ret();
    }

    @GetMapping("/przedmioty/dodaj/{sid}/{nazwa}/{ilosc}/{poziom}/{obw}")
    public String mappingPrzedmiotDodaj
    (
            @PathVariable("sid") String sid,
            @PathVariable("nazwa") String nazwa,
            @PathVariable("ilosc") Integer ilosc,
            @PathVariable("poziom") Integer poziom,
            @PathVariable("obw") String obw
    )
    {
        Przedmiot nowyPrzedmiot = new Przedmiot();
        nowyPrzedmiot.setId(null);
        nowyPrzedmiot.setNazwa(nazwa);
        nowyPrzedmiot.setIlosc(ilosc);
        nowyPrzedmiot.setPoziom(poziom);
        nowyPrzedmiot.setObowiazkowy(obw);

        return PrzedmiotyController.dodajPrzedmiotDoBazy(jdbcTemplate, nowyPrzedmiot)
                ? QuickJSON.RESP_OK
                : QuickJSON.RESP_BAD;
    }

    @GetMapping("/przedmioty/usun/{sid}/{id}")
    public String mappingPrzedmiotUsun (@PathVariable("sid") String sid, @PathVariable("id") Integer id)
    {
        return PrzedmiotyController.usunPrzedmiotById(jdbcTemplate, id) ? QuickJSON.RESP_OK : QuickJSON.RESP_BAD;
    }

    @GetMapping("/przedmioty/edytuj/{sid}/{id}/{nazwa}/{ilosc}/{poziom}/{obw}")
    public String mappingPrzedmiotEdytuj
    (
            @PathVariable("sid") String sid,
            @PathVariable("id") Integer id,
            @PathVariable("nazwa") String nazwa,
            @PathVariable("ilosc") Integer ilosc,
            @PathVariable("poziom") Integer poziom,
            @PathVariable("obw") String obw
    )
    {
        Przedmiot nowyPrzedmiot = new Przedmiot();
        nowyPrzedmiot.setId(id);
        nowyPrzedmiot.setNazwa(nazwa);
        nowyPrzedmiot.setIlosc(ilosc);
        nowyPrzedmiot.setPoziom(poziom);
        nowyPrzedmiot.setObowiazkowy(obw);

        return (new QuickJSON())
                .add("ok", PrzedmiotyController
                        .zapiszPrzedmiotWBazie(jdbcTemplate, nowyPrzedmiot) ? "true" : "false")
                .ret();
    }

}
