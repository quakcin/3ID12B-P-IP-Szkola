package pl.hefajstos.plan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.hefajstos.QuickJSONArray;

@RestController
public class PlanMapping
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/plan/generuj/{sid}")
    public String mappingPlanGeneruj (@PathVariable("sid") String sid)
    {
        PlanController.generujPlanLekcji(jdbcTemplate);
        return "";
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
}
