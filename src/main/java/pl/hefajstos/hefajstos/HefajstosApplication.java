package pl.hefajstos.hefajstos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.UUID;

@SpringBootApplication(scanBasePackages={"pl.*"})
public class HefajstosApplication implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public static void main(String[] args) {
        SpringApplication.run(HefajstosApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        /*
        String sql = "SELECT * FROM Uczen";

        List<Student> students = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Student.class));

        students.forEach(System.out :: println);
         */

        System.out.println("" + UUID.randomUUID() + "length is: " + (UUID.randomUUID().toString().length()));
    }

}
