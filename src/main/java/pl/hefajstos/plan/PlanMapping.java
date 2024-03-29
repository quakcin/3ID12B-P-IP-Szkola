package pl.hefajstos.plan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.hefajstos.QuickJSON;
import pl.hefajstos.hefajstos.QuickJSONArray;
import pl.hefajstos.autoryzacja.SesjaController;
import pl.hefajstos.autoryzacja.RodzajKonta;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class PlanMapping
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/plan/generuj/{sid}")
    public String mappingPlanGeneruj (@PathVariable("sid") String sid)
    {
        if (SesjaController.getRodzajKontaBySesjaId(jdbcTemplate, sid).equals(RodzajKonta.Dyrektor) == false)
            return QuickJSON.RESP_BAD;

        return PlanController.generujPlanLekcji(jdbcTemplate);
    }

    @GetMapping("/plan/klasa/{sid}/{id}")
    public String mappingPlanKlasa (@PathVariable("sid") String sid, @PathVariable("id") String klasaId)
    {
        return QuickJSONArray.fromList("plan", PlanController.getPlanByKlasaId(jdbcTemplate, klasaId));
    }

    @GetMapping("/plan/nauczyciel/{sid}/{id}")
    public String mappingPlanNauczyciel (@PathVariable("sid") String sid, @PathVariable("id") String nauczycielId)
    {
        RodzajKonta konto = SesjaController.getRodzajKontaBySesjaId(jdbcTemplate, sid);
        if (konto.equals(RodzajKonta.Dyrektor) == false && konto.equals(RodzajKonta.Nauczyciel) == false)
            return QuickJSON.RESP_BAD;

        return QuickJSONArray.fromList("plan", PlanController.getPlanByNauczycielId(jdbcTemplate, nauczycielId));
    }

    @GetMapping("/plan/uczen/{sid}/{id}")
    public String mappingPlanUczen (@PathVariable("sid") String sid, @PathVariable("id") String uczenId)
    {
        return QuickJSONArray.fromList("plan", PlanController.getPlanByUczenId(jdbcTemplate, uczenId));
    }

    @GetMapping("/plan/godziny/{sid}")
    public String mappingPlanUczen (@PathVariable("sid") String sid)
    {
        return QuickJSONArray.fromList("godziny", PlanController.getGodziny(jdbcTemplate));
    }

    @GetMapping("/plan/godziny/edytuj/{sid}/{lista}")
    public String mappingGodzinyEdycja (@PathVariable("sid") String sid, @PathVariable("lista") String lista)
    {
        if (SesjaController.getRodzajKontaBySesjaId(jdbcTemplate, sid).equals(RodzajKonta.Dyrektor) == false)
            return QuickJSON.RESP_BAD;

        return PlanController.zapiszGodzinyWBazie(jdbcTemplate, lista)
                ? QuickJSON.RESP_OK : QuickJSON.RESP_BAD;
    }

    @GetMapping("/plan/godziny/klasa/dzien/{sid}/{klas}/{dzien}")
    public String mappingGodzinyKlasy (@PathVariable("sid") String sid, @PathVariable("klas") String klasa, @PathVariable("dzien") String dzien)
    {
        Integer[] minMax = PlanController.getGodzinyByKlasa(jdbcTemplate, klasa, dzien);
        return (new QuickJSON())
                .addRaw("min", minMax[0].toString())
                .addRaw("max", minMax[1].toString())
                .ret();
    }

    @GetMapping("/plan/klasa/godziny/{sid}/{klas}/{dzien}")
    public String mappingPlanKlasa (@PathVariable("sid") String sid,  @PathVariable("klas") String klasa, @PathVariable("dzien") String dzien)
    {
        return QuickJSONArray.fromList("plan", PlanController.getGodzinyKlasyByDzien(jdbcTemplate, dzien, klasa));
    }

    @GetMapping("/plan/uczen/godziny/{sid}")
    public String mappingPlanGodzinyUczen (@PathVariable("sid") String sid)
    {
        if (SesjaController.getRodzajKontaBySesjaId(jdbcTemplate, sid).equals(RodzajKonta.Uczen) == false)
            return QuickJSON.RESP_BAD;

        LekcjeUczniaKolekcjaIterator okna = PlanController.getGodzinyUcznia(jdbcTemplate, sid);
        QuickJSONArray qja = new QuickJSONArray("dni");

        while (okna.czyNastepny())
        {
            qja.add(QuickJSONArray.fromList("lekcje", okna.nastepny()));
        }

        return qja.ret();
    }
}
