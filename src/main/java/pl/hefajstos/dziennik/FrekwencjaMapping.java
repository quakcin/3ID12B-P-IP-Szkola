package pl.hefajstos.dziennik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.hefajstos.autoryzacja.KontaController;
import pl.hefajstos.autoryzacja.Sesja;
import pl.hefajstos.autoryzacja.SesjaController;
import pl.hefajstos.hefajstos.QuickJSON;
import pl.hefajstos.hefajstos.QuickJSONArray;
import pl.hefajstos.autoryzacja.RodzajKonta;

@RestController
public class FrekwencjaMapping
{
  @Autowired
  JdbcTemplate jdbcTemplate;


  @GetMapping("/frekwencja/lista/dzien/klasa/{sid}/{tydzien}/{dzien}/{klasa}")
  public String mappingFrekwencjaListaDzienKlasa
  (
      @PathVariable("sid") String sid,
      @PathVariable("tydzien") String tydzien,
      @PathVariable("dzien") String dzien,
      @PathVariable("klasa") String klasa
  )
  {
    RodzajKonta konto = SesjaController.getRodzajKontaBySesjaId(jdbcTemplate, sid);
    if (konto.equals(RodzajKonta.Dyrektor) == false && konto.equals(RodzajKonta.Nauczyciel) == false)
      return QuickJSON.RESP_BAD;

    return QuickJSONArray.fromList(
        "frekwencja",
        FrekwencjaController.getFrekwencjaByKlasaForDayAndWeek(jdbcTemplate, klasa, Integer.parseInt(dzien), Integer.parseInt(tydzien)));
  }


  @GetMapping("/frekwencja/edytuj/uczen/{sid}/{godzina}/{tydzien}/{dzien}/{rodzaj}/{uid}")
  public String mappingFrekwencjaEdytujUcznia
  (
      @PathVariable("sid") String sid,
      @PathVariable("uid") String uid,
      @PathVariable("rodzaj") String rodzaj,
      @PathVariable("dzien") String dzien,
      @PathVariable("tydzien") String tydzien,
      @PathVariable("godzina") String godzina
  )
  {
    if (SesjaController.getRodzajKontaBySesjaId(jdbcTemplate, sid).equals(RodzajKonta.Nauczyciel) == false)
      return QuickJSON.RESP_BAD;

    return FrekwencjaController.setFrekwencjaByUczenIdAsSesjaId(jdbcTemplate, sid, uid, Integer.valueOf(rodzaj), Integer.valueOf(dzien), Integer.valueOf(tydzien), Integer.valueOf(godzina))
            ? QuickJSON.RESP_OK : QuickJSON.RESP_BAD;
  }

  @GetMapping("/frekwencja/lista/uczen/{sid}/{tydzien}")
  public String mappingFrekwencjaListaUczen
      (
          @PathVariable("sid") String sid,
          @PathVariable("tydzien") String tydzien
      )
  {
    if (SesjaController.getRodzajKontaBySesjaId(jdbcTemplate, sid).equals(RodzajKonta.Uczen) == false)
      return QuickJSON.RESP_BAD;

    Sesja s = SesjaController.getSesjaByToken(jdbcTemplate, sid);
    return QuickJSONArray.fromList(
        "frekwencja",
        FrekwencjaController.getFrekwencjaByUczenIdForWeek(jdbcTemplate, tydzien, s.getKlucz()));
  }
}
