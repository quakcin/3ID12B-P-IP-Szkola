package pl.hefajstos.hefajstos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.DayOfWeek;
import java.util.List;


public class Studenci {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private List<Student> Studenci;

    public List<Student> getUczniowie(){
        String sql = "SELECT * FROM Uczen";

        this.Studenci = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Student.class));

        return this.Studenci;
    }
}
