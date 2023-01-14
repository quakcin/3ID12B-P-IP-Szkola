package pl.hefajstos.hefajstos;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class Przedmioty
{
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void powinnoDodacNowyPrzedmiotPobracJegoInfoAPotemGoUsunac () throws Exception {
        mockMvc.perform(get("/przedmioty/dodaj/1/NowyPrzedmiot/4/8/T")).andDo((a) -> {
            String resp = a.getResponse().getContentAsString();
            JSONObject jsresp = new JSONObject(resp);
            Boolean status = jsresp.getBoolean("ok");
            assert(status == true);
        });
        mockMvc.perform(get("/przedmioty/lista/1")).andDo((a) -> {
            String resp = a.getResponse().getContentAsString();
            JSONObject jsresp = new JSONObject(resp);
            JSONArray jslista = jsresp.getJSONArray("przedmioty");
            JSONObject jsprzedmiot = null;

            for (int i = jslista.length() - 1; i >= 0; i--)
            {
                JSONObject o = jslista.getJSONObject(i);
                if (o.getString("nazwa").equals("NowyPrzedmiot"))
                {
                    jsprzedmiot = o;
                    break;
                }
            }

            assert(jsprzedmiot != null);

            Integer id = jsprzedmiot.getInt("id");
            Integer poz = jsprzedmiot.getInt("poziom");
            Integer ilosc = jsprzedmiot.getInt("ilosc");
            String obw = jsprzedmiot.getString("obw");

            assert(poz == 8);
            assert(ilosc == 4);
            assert(obw.equals("T"));

            mockMvc.perform(get("/przedmioty/info/1/" + id)).andDo((b) -> {
                JSONObject jsresp2 = new JSONObject(b.getResponse().getContentAsString());
                String nazwa = jsresp2.getString("nazwa");
                Integer poz2 = jsresp2.getInt("poziom");
                Integer ilosc2 = jsresp2.getInt("ilosc");
                String obw2 = jsresp2.getString("obw");

                assert(nazwa.equals("NowyPrzedmiot"));
                assert(poz2 == 8);
                assert(ilosc2 == 4);
                assert(obw2.equals("T"));

            });

            mockMvc.perform(get("/przedmioty/usun/1/" + id)).andDo((b) -> {
                JSONObject jsresp2 = new JSONObject(b.getResponse().getContentAsString());
                Boolean status = jsresp2.getBoolean("ok");
                assert(status == true);
            });

            mockMvc.perform(get("/przedmioty/info/1/" + id)).andDo((b) -> {
                JSONObject jsresp2 = new JSONObject(b.getResponse().getContentAsString());
                Boolean status = jsresp2.getBoolean("ok");
                assert(status == false);
            });

        });

    }
}
