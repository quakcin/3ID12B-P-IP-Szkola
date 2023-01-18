package pl.hefajstos.konta;

import pl.hefajstos.autoryzacja.RodzajKonta;

public abstract class Uprawnienia
{
  RodzajKonta getRodzajKonta ()
  {
    return RodzajKonta.Inne;
  }
}
