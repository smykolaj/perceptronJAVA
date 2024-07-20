import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.nio.file.Path;
import java.util.*;

public class Perceptron
{
    HashMap<Integer, Double> accuracyMap = new HashMap<>();
    double bias;
    ArrayList<Double> weightsV = new ArrayList<>();
    ArrayList<Vector> vectors = new ArrayList<>();
    long epochs;
    Path trainSet;
    Path testSet;
    double learningRate = 0.01;
    HashMap<String, Double> nameToNumber = new HashMap<>();
    ArrayList<String> uniqueNames = new ArrayList<>(2);


    public Perceptron(String trainSet, String testSet, long epochs)
    {
        this.epochs = epochs;
        this.trainSet = Path.of(trainSet);
        this.testSet = Path.of(testSet);
        weightsV = new ArrayList<>(readFile());
        for (int i = 0; i < readFile(); i++)
        {
            weightsV.add(-1+ Math.random()*2);
        }
        nameToNumber.put(uniqueNames.get(0), 0.0);
        nameToNumber.put(uniqueNames.get(1), 1.0);
        System.out.println("First weights vector: " + weightsV);
        bias = Math.random();
        System.out.println("First bias: " + bias);
    }

    private int readFile()
    {
        Scanner sc = null;
        try
        {
            sc = new Scanner(trainSet);
            while (sc.hasNextLine())
            {

                String data = sc.nextLine();
                Vector v = new Vector(data);
                vectors.add(v);

            }
        } catch (Exception e)
        {
            System.out.println("File not found");
        }
        uniqueNames.add(vectors.get(0).name);
        for (Vector v : vectors)
        {
            if (!uniqueNames.get(0).equals(v.name))
                uniqueNames.add(v.name);
        }
        return vectors.get(0).getLength();

    }

    public void train()
    {
        double accuracy = 0.0;
        for (int i = 0; i < epochs; i++)
        {
            double correctResults = 0.0;
            for (Vector v : vectors)
            {
                double desired = nameToNumber.get(v.name);
                double res = activationFunc(net(v));
                if (res == desired)
                    correctResults++;

                double helperProduct = learningRate * (desired - res);
                bias -= helperProduct;
                for (int j = 0; j < weightsV.size(); j++)
                {
                    weightsV.set(j, weightsV.get(j) + (helperProduct * v.values.get(j)));
                }
            }

            accuracy = (correctResults / vectors.size())*100;
            accuracyMap.put(i, accuracy);

        }
        System.out.println("Final weights vector: " + weightsV);
        System.out.println("Final bias: " + bias);
        System.out.println("Accuracy for train set: "+ accuracy);

    }

    public double net(Vector v)
    {
        double sum = 0;
        for (int i = 0; i < weightsV.size()-1; i++)
        {
            sum += weightsV.get(i) * v.values.get(i);
        }
        sum -= bias;
        return sum;

    }

    public double activationFunc(double net)
    {
        if (net >= 0.0)
            return 1.0;
        return 0.0;
    }

    public void classifySingleVector(String data){
        Vector v = new Vector(data+",unknowm");
        classifyVector(v);
    }

    public boolean classifyVector(Vector v){
        System.out.print("Expected output: " + v.name);
        String output;
        double actf = activationFunc(net(v));
        output = getKeyByValue(nameToNumber, actf);
        System.out.println("    Classified output: " + output);
        return v.name.equals(output);
    }

    public static <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null; // Value not found
    }



    public ArrayList<Vector> returnTestSet(){
        Scanner sc = null;
        ArrayList<Vector> testData = new ArrayList<>();
        try
        {
            sc = new Scanner(testSet);
            while (sc.hasNextLine())
            {
                String data = sc.nextLine();
                testData.add(new Vector(data));
            }
        } catch (Exception e)
        {
            System.out.println("File not found");
        }
        return testData;
    }

    public void classifyTestSet(){
        double correct = 0.0;
        double total = 0.0;
        for(Vector v : returnTestSet())
        {
            if (classifyVector(v))
                correct++;
            total++;
        }
        System.out.println("Accuracy for test set: " + correct/total * 100);

    }

    public void plotAccuracy() {
        List<Integer> kValues = new ArrayList<>(accuracyMap.keySet());
        List<Double> accuracies = new ArrayList<>(accuracyMap.values());

        XYChart chart = new XYChartBuilder()
                .width(800).height(600)
                .title("Perceptron Accuracy")
                .xAxisTitle("epochs")
                .yAxisTitle("Accuracy")
                .build();

        chart.getStyler().setMarkerSize(4);
        XYSeries series = chart.addSeries("Accuracy", kValues, accuracies);
        series.setMarker(SeriesMarkers.CIRCLE);

        new SwingWrapper(chart).displayChart();
    }

}


