package pl.hefajstos.dziennik;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class OcenyController
{
    public static List<Ocena> getOcenyByKlasaAndPrzedmiotId (JdbcTemplate jdbcTemplate, String klasa, String przedmiotId)
    {
        String sql = "SELECT * FROM OCENYKLASY WHERE PRZEDMIOTID=? AND KLASA=?";
        return jdbcTemplate.query
        (
            sql,
            BeanPropertyRowMapper.newInstance(Ocena.class), przedmiotId, klasa
        );
    }
}
