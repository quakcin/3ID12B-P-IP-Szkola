package pl.hefajstos.dziennik;

import com.sun.xml.bind.v2.model.annotation.Quick;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.autoryzacja.Sesja;
import pl.hefajstos.autoryzacja.SesjaController;
import pl.hefajstos.hefajstos.QuickJSON;
import pl.hefajstos.hefajstos.QuickJSONArray;
import pl.hefajstos.klasy.KlasyController;
import pl.hefajstos.nauczyciele.NauczycieleController;

import java.sql.Date;

@RestController
public class OcenyMapping {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/oceny/dodaj/{sid}/{prz}/{ucz}/{ocn}/{wga}/{msg}/{kat}")
    public String mappingOcenyDodajOcene
    (
        @PathVariable("sid") String sid,
        @PathVariable("prz") String przedmiot,
        @PathVariable("ucz") String uczen,
        @PathVariable("ocn") String ocena,
        @PathVariable("wga") String waga,
        @PathVariable("msg") String komentarz,
        @PathVariable("kat") String kategoria
    )
    {
        return OcenyController.addOcenaByUczenIdInPrzedmiotId(jdbcTemplate, sid, uczen, przedmiot, ocena, waga, komentarz, kategoria)
            ? QuickJSON.RESP_OK : QuickJSON.RESP_BAD;
    }
    @GetMapping("/oceny/lista/{sid}/{klasa}/{prz}")
    public String mappingKlasyUczniowyieByKlasa(@PathVariable("sid") String sid, @PathVariable("klasa") String klasa, @PathVariable("prz") String pid) {
        return QuickJSONArray.fromList
        (
    "lista",
                OcenyController.getOcenyByKlasaAndPrzedmiotId(jdbcTemplate, klasa, pid)
        );
    }

    @GetMapping("/oceny/usun/{sid}/{oid}")
    public String mappingOcenyUsunOcene
    (
        @PathVariable("sid") String sid,
        @PathVariable("oid") String oid
    )
    {
        return OcenyController.usunOcene(jdbcTemplate, oid)
            ? QuickJSON.RESP_OK
            : QuickJSON.RESP_BAD;
    }

    @GetMapping("/oceny/info/{sid}/{id}")
    public String mappingOcenyInfo
    (
        @PathVariable("sid") String sid,
        @PathVariable("id") String id
    )
    {
        Ocena o = OcenyController.getOcenaById(jdbcTemplate, id);
        return o == null ? QuickJSON.RESP_BAD : o.toJson();
    }

    @GetMapping("/oceny/edytuj/{sid}/{prz}/{ucz}/{ocn}/{wga}/{msg}/{kat}/{oid}/{dat}")
    public String mappingOcenyEdytuj
    (
        @PathVariable("sid") String sid,
        @PathVariable("prz") String przedmiot,
        @PathVariable("ucz") String uczen,
        @PathVariable("ocn") String ocena,
        @PathVariable("wga") String waga,
        @PathVariable("msg") String komentarz,
        @PathVariable("kat") String kategoria,
        @PathVariable("oid") String oid,
        @PathVariable("dat") String data
    )
    {
        Ocena nowaOcena = new Ocena();
        nowaOcena.setId(Integer.parseInt(oid));
        nowaOcena.setPrzedmiotId(Integer.parseInt(przedmiot));
        nowaOcena.setUczenId(uczen);
        nowaOcena.setStopien(ocena);
        nowaOcena.setWaga(Integer.parseInt(waga));
        nowaOcena.setKomentarz(komentarz);
        nowaOcena.setKategoria(kategoria);
        nowaOcena.setData(Date.valueOf(data));
        return OcenyController.aktualizujOceneWBazie(jdbcTemplate, nowaOcena, sid)
                ? QuickJSON.RESP_OK : QuickJSON.RESP_BAD;
    }

    @GetMapping("/oceny/ucznia/{sid}")
    public String mappingOcenyUcznia(@PathVariable("sid") String sid)
    {
        Sesja s = SesjaController.getSesjaByToken(jdbcTemplate, sid);
        return QuickJSONArray.fromList("oceny", OcenyController.getOcenyByUczenId(jdbcTemplate, s.getKlucz()));
    }

}
