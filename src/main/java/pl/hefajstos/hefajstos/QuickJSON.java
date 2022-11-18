package pl.hefajstos.hefajstos;

public class QuickJSON
{
    private String str;
    public QuickJSON ()
    {
        str = "";
    }
    public QuickJSON add (String name, String value)
    {
        str += ((str.equals("")) ? "" : ",") + "\"" + name + "\":\"" + value + "\"";
        return this;
    }

    public String ret ()
    {
        return "{" + str + "}";
    }
}
