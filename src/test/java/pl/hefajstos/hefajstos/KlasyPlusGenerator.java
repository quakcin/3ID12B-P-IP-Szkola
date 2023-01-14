package pl.hefajstos.hefajstos;


import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Ponieważ klasy nie mogą istnieć bez generatora, ich testy zostały
 * złączone w jednen konrektny test:
 * 1. Utworzenie 2 uczniów z tym samym rokiem urodzenia
 * 2. Utworzenie 1 nauczyciela
 * 3. Agresywne generowanie klas
 * 4. Sprawdzenie integralności jednej wygenerowanej klasy (Powinna zawierać dwóch uczniów)
 * 5. Dodanie nowego ucznia
 * 6. Pasywne generowanie klas (Powinni dopisać nowego ucznia do istniejącej już klasy)
 * 7. Ponowne sprawdzenie integralności (Finalnie klasa powinna składać się z 3 uczniów)
 *
 * Test zakłada że baza danych podczas testowania jest świerza - tzw. bez dodanych zbędnych
 * danych (widnieje tylko konto dyrektora)
 */
@SpringBootTest
@AutoConfigureMockMvc
public class KlasyPlusGenerator
{
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void powinnoUtworzycNowaKlaseZParomaStatycznymiUczniamiIJednymWychowawca() throws Exception
    {
        mockMvc.perform(get("/hefajstos/factory_reset/" + HefajstosApplication.__haslo_administracyjne)).andDo((a) -> {
            JSONObject jsresp = new JSONObject(a.getResponse().getContentAsString());
            Boolean status = jsresp.getBoolean("ok");
            assert(status == true);
        });

        mockMvc.perform(get("/uczen/dodaj/1/Adam/Niezgoda/11111111111/2015-01-01/Kielce")).andDo((a) -> {
            JSONObject jsresp = new JSONObject(a.getResponse().getContentAsString());
            Boolean status = jsresp.getBoolean("ok");
            assert(status == true);
        });
        mockMvc.perform(get("/uczen/dodaj/1/Alojzy/Szpak/11111111112/2015-01-02/Kielce")).andDo((a) -> {
            JSONObject jsresp = new JSONObject(a.getResponse().getContentAsString());
            Boolean status = jsresp.getBoolean("ok");
            assert(status == true);
        });
        mockMvc.perform(get("/przedmioty/dodaj/1/NowyPrzedmiot/1/2/T")).andDo((a) -> {
            JSONObject jsresp = new JSONObject(a.getResponse().getContentAsString());
            Boolean status = jsresp.getBoolean("ok");
            assert(status == true);
        });
        mockMvc.perform(get("/nauczyciel/dodaj/1/Jaroslaw/Aroslaw/1989-05-06/mgr/Bez/1")).andDo((a) -> {
            JSONObject jsresp = new JSONObject(a.getResponse().getContentAsString());
            Boolean status = jsresp.getBoolean("ok");
            assert(status == true);
        });

        mockMvc.perform(get("/klasy/generuj/agresywnie/1/1/2")).andDo((a) -> {
            JSONObject jsresp = new JSONObject(a.getResponse().getContentAsString());
            Boolean status = jsresp.getBoolean("ok");
            assert(status == true);
        });

        mockMvc.perform(get("/klasy/lista/1")).andDo((a) -> {
            JSONObject jsresp = new JSONObject(a.getResponse().getContentAsString());
            JSONArray jslistaKlas = jsresp.getJSONArray("klasy");
            assert(jslistaKlas.length() == 1);
        });

        mockMvc.perform(get("/klasy/uczniowie/1/2A")).andDo((a) -> {
            JSONObject jsresp = new JSONObject(a.getResponse().getContentAsString());
            JSONArray jslistaKlas = jsresp.getJSONArray("lista");
            assert(jslistaKlas.length() == 2);
        });
        mockMvc.perform(get("/uczen/dodaj/1/Genezyp/Kapen/11111111113/2015-01-03/Zakopane")).andDo((a) -> {
            JSONObject jsresp = new JSONObject(a.getResponse().getContentAsString());
            Boolean status = jsresp.getBoolean("ok");
            assert(status == true);
        });

        mockMvc.perform(get("/klasy/generuj/pasywnie/1")).andDo((a) -> {
            JSONObject jsresp = new JSONObject(a.getResponse().getContentAsString());
            Boolean status = jsresp.getBoolean("ok");
            assert(status == true);
        });

        mockMvc.perform(get("/klasy/lista/1")).andDo((a) -> {
            JSONObject jsresp = new JSONObject(a.getResponse().getContentAsString());
            JSONArray jslistaKlas = jsresp.getJSONArray("klasy");
            assert(jslistaKlas.length() == 1);
        });

        mockMvc.perform(get("/klasy/uczniowie/1/2A")).andDo((a) -> {
            JSONObject jsresp = new JSONObject(a.getResponse().getContentAsString());
            JSONArray jslistaKlas = jsresp.getJSONArray("lista");
            assert(jslistaKlas.length() == 3);
        });

        mockMvc.perform(get("/hefajstos/factory_reset/" + HefajstosApplication.__haslo_administracyjne)).andDo((a) -> {
            JSONObject jsresp = new JSONObject(a.getResponse().getContentAsString());
            Boolean status = jsresp.getBoolean("ok");
            assert(status == true);
        });
    }
}
