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

    public static String fromList (String fieldName, List<? extends Jsonable> jsonables)
    {
        QuickJSONArray q = new QuickJSONArray(fieldName);
        for (Jsonable j : jsonables)
            q.add(j.toJson());
        return q.ret();
    }
    public QuickJSONArray add (String value)
    {
        str += ((str.equals("")) ? "" : ",") + value;
        return this;
    }

    public String ret () {
        return "{\"ok\": true, \"" + fieldName + "\": [" + str + "]}";
    }
}
