package pl.hefajstos.konta;

import pl.hefajstos.autoryzacja.RodzajKonta;

public class UprawnieniaDyrektora extends Uprawnienia
{
  @Override
  RodzajKonta getRodzajKonta ()
  {
    return RodzajKonta.Dyrektor;
  }
}
