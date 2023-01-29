package pl.hefajstos.plan;

import pl.hefajstos.hefajstos.Iterowalne;

import java.util.List;

/**
 * Wzorzec Projektowy nr.2 (Iterator)
 */
public class LekcjeUczniaKolekcjaIterator implements Iterowalne<List<Okno>>
{
  private LekcjeUczniaKolekcja lekcjeUczniaKolekcja;
  private int ptr = 0;

  public LekcjeUczniaKolekcjaIterator (LekcjeUczniaKolekcja lekcjeUczniaKolekcja)
  {
    this.lekcjeUczniaKolekcja = lekcjeUczniaKolekcja;
  }

  @Override
  public List<Okno> nastepny()
  {
    List<Okno> okno = lekcjeUczniaKolekcja.getDni().get(ptr);
    ptr++;
    return okno;
  }

  @Override
  public boolean czyNastepny()
  {
    return ptr < lekcjeUczniaKolekcja.getDni().size();
  }
}
