package pl.hefajstos.hefajstos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@SpringBootApplication(scanBasePackages={"pl.*"})
public class HefajstosApplication implements CommandLineRunner {

    public static final String __haslo_administracyjne = "97b095f4-943b-11ed-a1eb-0242ac120002";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args)
    {
        SpringApplication.run(HefajstosApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        System.out.println("" + UUID.randomUUID() + "length is: " + (UUID.randomUUID().toString().length()));
    }

}
