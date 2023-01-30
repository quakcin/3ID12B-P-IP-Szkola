package pl.hefajstos.uczen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.hefajstos.QuickJSON;
import pl.hefajstos.hefajstos.QuickJSONArray;
import pl.hefajstos.autoryzacja.SesjaController;
import pl.hefajstos.autoryzacja.RodzajKonta;


import java.sql.Date;
import java.util.UUID;

@RestController
public class UczenMapping
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/uczen/info/{sid}/{uid}")
    public String mappingUczenInfo (@PathVariable("sid") String sid, @PathVariable("uid") String uid)
    {
        AbstrakcyjnyUczen u = UczenController.getUczenById(jdbcTemplate, uid);
        return u != null
                ? u.toJson()
                : QuickJSON.RESP_BAD;
    }

    @GetMapping("/uczen/lista/{sid}")
    public String mappingUczenLista (@PathVariable("sid") String sid)
    {
        // listaUczniow
        KolekcjaUczniow kolekcjaUczniow = new KolekcjaUczniow(jdbcTemplate);
        UczniowieIterator uczniowieIterator = kolekcjaUczniow.getIterator();
        QuickJSONArray qja = new QuickJSONArray("listaUczniow");
        while (uczniowieIterator.czyNastepny())
            qja.add(uczniowieIterator.nastepny().toJson());
        return qja.ret();
    }


    @GetMapping("/uczen/dodaj/{sid}/{imie}/{nazw}/{pesel}/{uro}/{miej}")
    public String mappingUczenDodaj
    (
        @PathVariable("sid") String sid,
        @PathVariable("imie") String imie,
        @PathVariable("nazw") String nazw,
        @PathVariable("pesel") String pesel,
        @PathVariable("uro") Date uro,
        @PathVariable("miej") String miej
    )
    {
        Uczen nowyUczen = new Uczen();
        nowyUczen.setId(null);
        nowyUczen.setImie(imie);
        nowyUczen.setNazwisko(nazw);
        nowyUczen.setDataUrodzenia(uro);
        nowyUczen.setPesel(pesel);
        nowyUczen.setMiejsceUrodzenia(miej);
        UUID id = UczenController.dodajUczniaDoBazy(jdbcTemplate, nowyUczen);
        return id == null
            ? QuickJSON.RESP_BAD
            : (new QuickJSON()).addRaw("ok", "true")
                .add("uid", id.toString())
                .ret();
    }

    @GetMapping("/uczen/edytuj/{sid}/{imie}/{nazw}/{pesel}/{uro}/{miej}/{klasa}/{numer}/{uid}")
    public String mappingUczenEdytuj
    (
        @PathVariable("sid") String sid,
        @PathVariable("imie") String imie,
        @PathVariable("nazw") String nazw,
        @PathVariable("pesel") String pesel,
        @PathVariable("uro") Date uro,
        @PathVariable("miej") String miej,
        @PathVariable("klasa") String klasa,
        @PathVariable("numer") Integer numer,
        @PathVariable("uid") String uid
    )
    {
        Uczen nowyUczen = new Uczen();
        nowyUczen.setId(uid);
        nowyUczen.setImie(imie);
        nowyUczen.setNazwisko(nazw);
        nowyUczen.setDataUrodzenia(uro);
        nowyUczen.setPesel(pesel);
        nowyUczen.setKlasa(klasa);
        nowyUczen.setNumer(numer);
        nowyUczen.setMiejsceUrodzenia(miej);

        UczenController.zapiszUczniaWBazie(jdbcTemplate, nowyUczen);
        return (new QuickJSON()).addRaw("ok", "true").ret();
    }

    @GetMapping("/uczen/usun/{sid}/{uid}")
    public String mappingUczenUsun (@PathVariable("sid") String sid, @PathVariable("uid") String uid)
    {
        return UczenController.usunUcznia(jdbcTemplate, uid)
                ? QuickJSON.RESP_OK : QuickJSON.RESP_BAD;
    }

}
