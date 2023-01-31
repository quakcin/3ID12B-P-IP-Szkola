package pl.hefajstos.hefajstos;


import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;


import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HefajstosController
{
   public static boolean wykonajPlikSQL (JdbcTemplate jdbcTemplate, ClassLoader cl, String nazwa)
   {
      /*
         Załadowanie skryptu czyszczącego i wykonanie go token po tokenie
       */

      String cmd_set;
      try {
         cmd_set = String.join(" ", Files.readAllLines(
                 Paths.get(cl.getResource(
                         "baza/" + nazwa).toURI())));

      } catch (IOException | URISyntaxException e) {
         return false;
      }
      String[] cmds = cmd_set.split(";");

      for (String cmd : cmds)
      {
         try
         {
            System.out.println("[HefjastosController]: (Warn: Executing inline sql): \"" + cmd + "\"");
            jdbcTemplate.execute(cmd);
         }
         catch (DataAccessException e)
         {
            System.out.println("[HefjastosController]: (Err: " + e.toString() + "): \"" + cmd + "\"");
            // ignore
         }
      }
      return true;
   }

   public static boolean zaladujPrzykladowaBaze (JdbcTemplate jdbcTemplate, ClassLoader cl)
   {
      return resetujProgramDoStanuFabrycznego(jdbcTemplate, cl)
          && wykonajPlikSQL(jdbcTemplate, cl, "baza_przyk.sql");
   }

   public static boolean zaladujPrzykladowaBaze2 (JdbcTemplate jdbcTemplate, ClassLoader cl)
   {
      return resetujProgramDoStanuFabrycznego(jdbcTemplate, cl)
          && wykonajPlikSQL(jdbcTemplate, cl, "baza_przyk2.sql");
   }

   public static boolean resetujProgramDoStanuFabrycznego (JdbcTemplate jdbcTemplate, ClassLoader cl)
   {
      return wykonajPlikSQL(jdbcTemplate, cl, "baza_start.sql");
   }

}
