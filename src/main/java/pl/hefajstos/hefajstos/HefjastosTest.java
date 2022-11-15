package pl.hefajstos.hefajstos;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
@Data
public class HefjastosTest
{
  @GetMapping(value = "/test/{str}", produces = "application/json")
  public ResponseEntity<String> getTestJSONString (@PathVariable("str") String str)
  {
    return new ResponseEntity<>("{\"rets\": \"" + str + "\"}", HttpStatus.valueOf(200));
  }
}
