package pl.hefajstos.hefajstos;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureMockMvc
@SpringBootTest
public class SesjaTest
{
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void powinnoZalogowacIZwrocicTokenPoprawnieUtworzonejSesji () throws Exception {
        mockMvc.perform(get("/zaloguj/1/Dyrektor/1234")).andDo((e) -> {
            String resp = e.getResponse().getContentAsString();
            JSONObject jsresp = new JSONObject(resp);
            Boolean status = jsresp.getBoolean("ok");
            String uid = jsresp.getString("uid");
            String typKonta = jsresp.getString("typ");

            assert(status == true);
            assert(uid.length() == 36);
            assert(typKonta.equals("Dyrektor"));
        });
    }
}
