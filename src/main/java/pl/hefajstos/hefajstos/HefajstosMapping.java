package pl.hefajstos.hefajstos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.autoryzacja.RodzajKonta;
import pl.hefajstos.autoryzacja.SesjaController;
import pl.hefajstos.autoryzacja.RodzajKonta;

@RestController
public class HefajstosMapping
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping(path = "/hefajstos/factory/reset/{sid}")
    public String hefajstosFactoryResetMapping (@PathVariable("sid") String sid)
    {
//        RodzajKonta konto = SesjaController.getRodzajKontaBySesjaId(jdbcTemplate, sid);
//        if (konto.equals(RodzajKonta.Dyrektor) == false)
//            return QuickJSON.RESP_BAD;

        if (sid.equals("supertajnehaslo") == false)
            return QuickJSON.RESP_BAD;

        return HefajstosController.resetujProgramDoStanuFabrycznego(jdbcTemplate, getClass().getClassLoader())
                ? QuickJSON.RESP_OK
                : QuickJSON.RESP_BAD;
    }

    @GetMapping(path = "/hefajstos/load/example/{sid}/{idx}")
    public String hefajstosZaladujPrzykladMapping (@PathVariable("sid") String sid, @PathVariable("idx") Integer idx)
    {
//        if (SesjaController.getRodzajKontaBySesjaId(jdbcTemplate, sid).equals(RodzajKonta.Dyrektor) == false)
//            return QuickJSON.RESP_BAD;

        if (sid.equals("supertajnehaslo") == false)
            return QuickJSON.RESP_BAD;

        boolean czySieUdaloZaladowacPrzyklad = idx == 0
            ? HefajstosController.zaladujPrzykladowaBaze(jdbcTemplate, getClass().getClassLoader())
            : HefajstosController.zaladujPrzykladowaBaze2(jdbcTemplate, getClass().getClassLoader());

        return czySieUdaloZaladowacPrzyklad
                ? QuickJSON.RESP_OK
                : QuickJSON.RESP_BAD;
    }
}
