package pl.hefajstos.hefajstos;

public class QuickJSON
{
    public static final String RESP_OK = "{\"ok\": true}";
    public static final String RESP_BAD = "{\"ok\": false}";

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

    public QuickJSON addRaw (String name, String value)
    {
        str += ((str.equals("")) ? "" : ",") + "\"" + name + "\":" + value;
        return this;
    }

    public String ret ()
    {
        return "{" + str + "}";
    }
}
