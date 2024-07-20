import java.util.ArrayList;

public class Vector
{
    public String name;
    public ArrayList<Double> values = new ArrayList<>();

    public Vector(String info)
    {
        String[] splittedInfo = info.split(",");
        for (int i = 0; i < splittedInfo.length-1; i++)
        {
            values.add(Double.valueOf(splittedInfo[i]));
        }
        name = splittedInfo[splittedInfo.length - 1];
    }

    public int getLength(){
        return values.size();
    }
}
