package pl.hefajstos.hefajstos;

import java.util.List;

public class QuickJSONArray
{
    private String str;
    private String fieldName;

    public QuickJSONArray (String fieldName)
    {
        this.fieldName = fieldName;
        str = "";
    }
    public QuickJSONArray add (String value)
    {
        str += ((str.equals("")) ? "" : ",") + value;
        return this;
    }

    public String ret () {
        return "{\"" + fieldName + "\": [" + str + "]}";
    }
}
