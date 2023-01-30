package pl.hefajstos.uczen;

import pl.hefajstos.hefajstos.Iterowalne;

/**
 * Kolejna implementacja Iteratora
 */
public class UczniowieIterator implements Iterowalne<Uczen>
{
  private KolekcjaUczniow kolekcjaUczniow;
  int ptr = 0;
  public UczniowieIterator (KolekcjaUczniow kolekcjaUczniow)
  {
    this.kolekcjaUczniow = kolekcjaUczniow;
  }

  @Override
  public Uczen nastepny()
  {
    Uczen u = kolekcjaUczniow.getUczniowie().get(ptr);
    ptr = ptr + 1;
    return u;
  }

  @Override
  public boolean czyNastepny()
  {
    return ptr < kolekcjaUczniow.getUczniowie().size();
  }
}
