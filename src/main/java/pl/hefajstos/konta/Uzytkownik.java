package pl.hefajstos.konta;

import lombok.Data;

/*
TODO: Przenieść do pl.hefajstos.autoryzacja
      wykorzystać istniejące klasy
 */
@Data
public class Uzytkownik extends Uprawnienia
{ /* design pattern */
  private Uprawnienia uprawnienia;
  public String nickname;
  public Integer typ;
  public String haslo;
  public String id;
}
