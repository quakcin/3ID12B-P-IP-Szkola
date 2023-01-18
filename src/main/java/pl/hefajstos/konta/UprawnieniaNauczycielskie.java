package pl.hefajstos.konta;

import pl.hefajstos.autoryzacja.RodzajKonta;

public class UprawnieniaNauczycielskie extends Uprawnienia
{
  @Override
  public RodzajKonta getRodzajKonta ()
  {
    return RodzajKonta.Nauczyciel;
  }
}
