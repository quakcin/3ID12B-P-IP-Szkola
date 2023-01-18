package pl.hefajstos.konta;

import pl.hefajstos.autoryzacja.RodzajKonta;

public class UprawnieniaKierownicze extends Uprawnienia
{
  @Override
  public RodzajKonta getRodzajKonta ()
  {
    return RodzajKonta.Dyrektor;
  }
}
