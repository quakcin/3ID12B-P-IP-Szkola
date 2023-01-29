package pl.hefajstos.klasy.generator;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Wzorzec projektowy nr.1 (Strategia)
 */
public abstract class Generator
{

    public static void logging ()
    {
        System.out.println("[Generator]: Uruchomiono generator!");
    }

    /**
     * @return NULL - implementacja w finalnej klasie
     */
    public static Raport Generuj (JdbcTemplate jdbcTemplate, Parametry parametry)
    {
        Generator.logging(); /* wymaga log'owania */
        return null;
    }
}
