package pl.hefajstos.konta;

import pl.hefajstos.autoryzacja.RodzajKonta;

public class UprawnieniaUcznia extends Uprawnienia
{
  @Override
  public RodzajKonta getRodzajKonta ()
  {
    return RodzajKonta.Uczen;
  }
}
