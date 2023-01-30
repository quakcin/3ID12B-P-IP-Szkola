package pl.hefajstos.autoryzacja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.hefajstos.QuickJSON;
import pl.hefajstos.hefajstos.QuickJSONArray;

@RestController
public class KontaMapping
{
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @GetMapping("/konto/lista/{sid}")
  public String mappingKontaLista(@PathVariable("sid") String sid)
  {
    if (SesjaController.getRodzajKontaBySesjaId(jdbcTemplate, sid).equals(RodzajKonta.Dyrektor) == false)
      return QuickJSON.RESP_BAD;

    return QuickJSONArray.fromList("konta", KontaController.getListaKont(jdbcTemplate));
  }

  @GetMapping("/konto/zmien_haslo/{sid}/{uid}/{has}")
  public String mappingZmienHaslo(@PathVariable("sid") String sid, @PathVariable("uid") String uid, @PathVariable("has") String noweHaslo)
  {
    if (SesjaController.getRodzajKontaBySesjaId(jdbcTemplate, sid).equals(RodzajKonta.Dyrektor) == false)
      return QuickJSON.RESP_BAD;

    return KontaController.zmienHaslo(jdbcTemplate, uid, noweHaslo)
        ? QuickJSON.RESP_OK
        : QuickJSON.RESP_BAD;
  }

  @GetMapping("/konto/id/{sid}")
  public String mappingKontoId (@PathVariable("sid") String sid)
  {
    return (new QuickJSON()).add("id", KontaController.getKontoID(jdbcTemplate, sid)).ret();
  }

  @GetMapping("/konto/reset/haslo/{sid}/{stare}/{nowe}")
  public String mappingKontoResetHaslo
  (
    @PathVariable("sid") String sid,
    @PathVariable("stare") String stare,
    @PathVariable("nowe") String nowe
  )
  {
    return KontaController.setHasloBySesjionId(jdbcTemplate, sid, stare, nowe)
        ? QuickJSON.RESP_OK
        : QuickJSON.RESP_BAD;
  }
}
