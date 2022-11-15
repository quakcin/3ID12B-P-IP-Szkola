package pl.hefajstos.hefajstos;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HefajstosIndex
{
  @GetMapping("/")
  public String index ()
  {
    return "<h2>Hello World!</h2>";
  }
}
