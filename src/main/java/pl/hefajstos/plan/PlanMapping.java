package pl.hefajstos.plan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.hefajstos.QuickJSON;
import pl.hefajstos.hefajstos.QuickJSONArray;

@RestController
public class PlanMapping
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/plan/generuj/{sid}")
    public String mappingPlanGeneruj (@PathVariable("sid") String sid)
    {
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
}
