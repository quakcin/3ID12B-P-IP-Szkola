package pl.hefajstos.hefajstos;

import org.junit.jupiter.api.Test;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class Uczniowie
{
    @Autowired
    private MockMvc mockMvc;

    /**
     * Sprawdzi poprawność działania modułu uczniów:
     * Zrobi to po przez dodanie nowego ucznia
     * Sprawdzi zwracane przez /uczen/info informacje
     * Usunie ucznia z bazy za pomocą /uczen/usun
     * Ponownie sprawdzi info nie istniejącego ucznia
     * @throws Exception
     */
    @Test
    public void powinnoPoprawnieUtworzycNowegoUczniaISprawdzicJegoInformacjeIGoUsunac () throws Exception {
        /* utworzenie ucznia */
        mockMvc.perform(get("/uczen/dodaj/1/Jan/Kowalski/12345678998/1999-11-11/Kielce")).andDo((e) -> {
            String resp = e.getResponse().getContentAsString();
            JSONObject jsresp = new JSONObject(resp);
            Boolean status = jsresp.getBoolean("ok");
            String uid = jsresp.getString("uid");
            assert(status == true);
            assert(uid.length() == 36);

            /* sprawdzenie jego informacji */
            mockMvc.perform(get("/uczen/info/1/" + uid)).andDo((f) -> {
                String resp2 = f.getResponse().getContentAsString();
                JSONObject jsresp2 = new JSONObject(resp2);
                String uid2 = jsresp2.getString("uid");
                String imie = jsresp2.getString("imie");
                String nazw = jsresp2.getString("nazwisko");
                String urodz = jsresp2.getString("urodz");
                String miejsc = jsresp2.getString("miejsc");
                String pesel = jsresp2.getString("pesel");
                String klasa = jsresp2.getString("klasa");
                String numer = jsresp2.getString("numer");

                assert(uid2.equals(uid));
                assert(imie.equals("Jan"));
                assert(nazw.equals("Kowalski"));
                assert(urodz.equals("1999-11-11"));
                assert(miejsc.equals("Kielce"));
                assert(pesel.length() == 11);
                assert(pesel.equals("12345678998"));
                assert(klasa.equals("0"));
                assert(numer.equals("0"));
            });
            
            /* Michał znowu się usunie */
            mockMvc.perform(get("/uczen/usun/1/" + uid)).andDo((f) -> {
                String resp2 = f.getResponse().getContentAsString();
                JSONObject jsresp2 = new JSONObject(resp2);
                Boolean status2 = jsresp2.getBoolean("ok");
                assert(status2 == true);
            });

            /* Był i nie ma... */
            mockMvc.perform(get("/uczen/info/1/" + uid)).andDo((f) -> {
                String resp2 = f.getResponse().getContentAsString();
                JSONObject jsresp2 = new JSONObject(resp2);
                Boolean status2 = jsresp2.getBoolean("ok");
                assert(status2 == false);
            });

        });
    }
}
