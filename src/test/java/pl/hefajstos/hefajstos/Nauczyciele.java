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
public class Nauczyciele
{
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void powinnoDodacNauczycielaPobracJegoInfoOrazGoUsunac () throws Exception
    {
        mockMvc.perform(get("/nauczyciel/dodaj/1/Janusz/Jankowski/1976-09-01/dr/1A/1_2_3")).andDo((a) -> {
            JSONObject jsresp = new JSONObject(a.getResponse().getContentAsString());
            Boolean status = jsresp.getBoolean("ok");
            assert(status == true);

            String id = jsresp.getString("uid");

            mockMvc.perform(get("/nauczyciel/info/1/" + id)).andDo((b) -> {
                JSONObject jsresp2 = new JSONObject(b.getResponse().getContentAsString());
                Boolean status2 = jsresp2.getBoolean("ok");
                assert(status2 == true);

                String imie = jsresp2.getString("imie");
                assert(imie.equals("Janusz"));
                String nazw = jsresp2.getString("nazw");
                assert(nazw.equals("Jankowski"));
                String id2 = jsresp2.getString("id");
                assert(id2.equals(id));
                String zatr = jsresp2.getString("zatr");
                assert(zatr.equals("1976-09-01"));
                String stop = jsresp2.getString("stop");
                assert(stop.equals("dr"));
                String wych = jsresp2.getString("wych");
                assert(wych.equals("1A"));
                String przedmioty = jsresp2.getString("prz");
                assert(przedmioty.equals("1_2_3"));
            });

            mockMvc.perform(get("/nauczyciel/usun/1/" + id)).andDo((b) -> {
                JSONObject jsresp2 = new JSONObject(b.getResponse().getContentAsString());
                Boolean status2 = jsresp2.getBoolean("ok");
                assert(status2 == true);
            });

            mockMvc.perform(get("/nauczyciel/info/1/" + id)).andDo((b) -> {
                JSONObject jsresp2 = new JSONObject(b.getResponse().getContentAsString());
                Boolean status2 = jsresp2.getBoolean("ok");
                assert(status2 == false);
            });
        });
    }
}
