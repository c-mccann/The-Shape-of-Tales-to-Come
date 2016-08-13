package advanced.Smoothing;


import flanagan.analysis.CurveSmooth;
import org.jtransforms.fft.DoubleFFT_1D;
import org.jtransforms.fft.DoubleFFT_2D;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;


/**
 * Created by carlmccann2 on 05/02/2016.
 * Houses the smoothing algorithms used to produce smooth graphs
 */
public class Smoothing {

    public static void main(String[] args) throws FileNotFoundException {

        // temp initializers
        double[] tdata = new double[1167];
        double[] ydata = new double[1167];

        LinkedHashMap<Integer, Double> rawData = new LinkedHashMap<>();

//        try (BufferedReader br = new BufferedReader(new FileReader("src/advanced/out/Movie/Donnie_Darko/Donnie-Raw-Sentiment.txt"))) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/advanced/out/Movie/Donnie_Darko/Donnie-Darko-all-Sentiment.txt"))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {

                String lineSplit[] = line.split(",");


//                double sentiment = Double.parseDouble(lineSplit[0].trim());
//                int position = Integer.parseInt(lineSplit[1].trim());
//                rawData.put(Integer.parseInt(lineSplit[1].trim()), Double.parseDouble(lineSplit[0].trim()));

                double sentiment = Double.parseDouble(lineSplit[1].trim());         // all sentiment is pos, sent
                int position = Integer.parseInt(lineSplit[0].trim());               // rather than sent, pos, seen in character specific sentiment files
                tdata[i] = Double.parseDouble(lineSplit[0].trim());
                ydata[i] = sentiment;

                rawData.put(Integer.parseInt(lineSplit[0].trim()), Double.parseDouble(lineSplit[1].trim()));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        Smoothing smoothing = new Smoothing();
//        LinkedHashMap lhm = smoothing.simpleMovingAverage(10, rawData);

//        for (Object key : lhm.keySet()){
//
//        }

//
////        The physical layout of the input data has to be as follows:
////        a[k1][2*k2] = Re[k1][k2],
////        a[k1][2*k2+1] = Im[k1][k2], 0<=k1<rows, 0<=k2<columns,
//
//        System.out.println("2D Transform:");
//        System.out.println("complexForward: ");
//        System.out.println("The physical layout of the input data has to be as follows:");
//        System.out.println("a[k1][2*k2] = Re[k1][k2],");
//        System.out.println("a[k1][2*k2+1] = Im[k1][k2], 0<=k1<rows, 0<=k2<columns,");
//        System.out.println("\nrealForwardFull:");
//        System.out.println("Computes 2D forward DFT of real data leaving the result in a . This method computes full " +
//                "real forward transform, i.e. you will get the same result as from complexForward called with all " +
//                "\nimaginary part equal 0. Because the result is stored in a, the input array must be of size rows" +
//                " by 2*columns, with only the first rows by columns elements filled with real data. \nTo get back the" +
//                " original data, use complexInverse on the output of this method.\n");
//
////        LinkedHashMap lhm2 = smoothing.discreteFourierTransform(rawData);   // returns a Linked HashMap
//
//        double[][] temp2D = smoothing.discreteFourierTransform2(rawData);  // returns an array
//
//        System.out.println("[0][0]: " + temp2D[0][0]);
//        System.out.println("[0][1]: " + temp2D[0][1]);
//        System.out.println("[0][2]: " + temp2D[0][2]);
//        System.out.println("[0][3]: " + temp2D[0][3]);
//        System.out.println();
//        System.out.println("Is it Re[0][0],[0,2] Im[0][1],[0][3] or,");
//        System.out.println("Is it Re[0][0],[0,1] Im[0][2],[0][3]");
//
//        System.out.println("____________________________________________________________________________________________________________________________________________________");
//        System.out.println("\n1D Transform, complexForward:\na[2*k] = Re[k], \n" +
//                " a[2*k+1] = Im[k], 0<=k<n\n");
//
//        double[][] temp1D = smoothing.discreteFourierTransform3(rawData);  // 1D
//        System.out.println("[0][0]: " + temp1D[0][0]);
//        System.out.println("[0][1]: " + temp1D[0][1]);
//        System.out.println("[1][0]: " + temp1D[1][0]);
//        System.out.println("[1][1]: " + temp1D[1][1]);


        // Savitsky Golay Testing

        double[][] sGF = smoothing.savitzkyGolayFilter(tdata, ydata, 4, 200);


    }

    public LinkedHashMap simpleMovingAverage(int windowSize, LinkedHashMap<Integer, Double> rawData) {

        LinkedHashMap<Integer, Double> smoothedData = new LinkedHashMap<>();

        ArrayList<Integer> keyList = new ArrayList<>();
        ArrayList<Double> sentList = new ArrayList<>();
        // change for different windows
        for (Integer key : rawData.keySet()) {
            keyList.add(key);
            sentList.add(rawData.get(key));
        }

        for (int i = 0; i < keyList.size(); i++) {
            double average = 0;
            for (int j = 0; j < windowSize; j++) {
                average += sentList.get(i + j);
//                System.out.println("Sentlist["+(i+j)+"]: " + sentList.get(i + j));
//                System.out.println("Average["+(i+j)+"]: " + average);
            }
            average = average / (double) windowSize;
//            System.out.println("finished average: "+ average);
            smoothedData.put(keyList.get(i), average);
            if (i + windowSize == keyList.size()) {
                break;
            }
        }
        return smoothedData;
    }

    public LinkedHashMap exponentialMovingAverage(int period, LinkedHashMap<Integer, Double> rawData) {
        LinkedHashMap<Integer, Double> smoothedData = new LinkedHashMap<>();

        ArrayList<Integer> keyList = new ArrayList<>();
        ArrayList<Double> sentList = new ArrayList<>();
        for (Integer key : rawData.keySet()) {
            keyList.add(key);
            sentList.add(rawData.get(key));
        }


        double multiplier = 2 / ((double) period + 1);
        double previousEMA = 0;

        for (int i = 0; i < keyList.size(); i++) {
            double average = 0;

            if (i == 0) {
                for (int j = 0; j < period; j++) {
                    average += sentList.get(i + j);
                }
                average = average / period;
            } else {
                //  EMA = Price(t) * k + EMA(y) * (1 – k)
                average = (sentList.get(i) * multiplier) + (previousEMA * (1 - multiplier));
            }

            previousEMA = average;
            smoothedData.put(keyList.get(i), average);
            if (i + period == keyList.size()) {
                break;
            }

        }


        return smoothedData;
    }

    //    doesnt work properly
    public LinkedHashMap discreteFourierTransform(LinkedHashMap<Integer, Double> rawData) {
        System.out.println("DFT into lhm");
        LinkedHashMap<Integer, Double> smoothedData = new LinkedHashMap<>();

        int rows = rawData.size();
        int cols = 2;
        double[][] input = new double[rows][cols * 2];
        int i = 0;
        for (int key : rawData.keySet()) {
            input[i][0] = (double) key;
            input[i][1] = rawData.get(key);
            i++;
        }
        double[][] a = new double[i][4];
        double[] a1 = new double[i];

        for (i = 0; i < a.length; i++) {
            for (int j = 0; j < 2; j++) {
                a[i][j] = input[i][j];
            }

        }
//        System.out.println(Arrays.deepToString(a));
        System.out.println("input:  " + Arrays.deepToString(input));
//        System.out.println("A1:     " + Arrays.deepToString(a));


        DoubleFFT_2D dfft = new DoubleFFT_2D(rows, cols);


        dfft.realForwardFull(input);

//        for (int j = 0; j < input.length ; j++) {
//            System.out.println("A["+j+"][0]:    " + a[j][0] + "A["+j+"][1]:    " + a[j][1]);
//            System.out.println("I["+j+"][0]:    " + input[j][0] + "I["+j+"][1]:    " + input[j][1]);
//        }

        System.out.println("input:  " + Arrays.deepToString(input) + "\n");

        return null;
    }

    //    doesnt work properly
    public double[][] discreteFourierTransform2(LinkedHashMap<Integer, Double> rawData) {
        System.out.println("DFT into array 2D transform");
        LinkedHashMap<Integer, Double> smoothedData = new LinkedHashMap<>();

        System.out.println(rawData);

        int rows = rawData.size();
        int cols = 2;
        double[][] input = new double[rows][cols * 2];
        int i = 0;
        for (int key : rawData.keySet()) {
            input[i][0] = (double) key;
            input[i][1] = rawData.get(key);
            i++;
        }
        double[][] a = new double[i][4];
        double[] a1 = new double[i];

        for (i = 0; i < a.length; i++) {
            for (int j = 0; j < 2; j++) {
                a[i][j] = input[i][j];
            }

        }
//        System.out.println(Arrays.deepToString(a));
        System.out.println("input:  " + Arrays.deepToString(input));
//        System.out.println("A1:     " + Arrays.deepToString(a));


        DoubleFFT_2D dfft = new DoubleFFT_2D(rows, cols);


        dfft.realForwardFull(input);

//        for (int j = 0; j < input.length ; j++) {
//            System.out.println("A["+j+"][0]:    " + a[j][0] + "A["+j+"][1]:    " + a[j][1]);
//            System.out.println("I["+j+"][0]:    " + input[j][0] + "I["+j+"][1]:    " + input[j][1]);
//        }

        System.out.println("input:  " + Arrays.deepToString(input) + "\n");

        return input;
    }

    //    doesnt work properly
    public double[][] discreteFourierTransform3(LinkedHashMap<Integer, Double> rawData) {
        System.out.println("DFT into array 1D transform");
        LinkedHashMap<Integer, Double> smoothedData = new LinkedHashMap<>();

        int rows = rawData.size();
        int cols = 2;
        double[] input = new double[2 * rows];
        int i = 0;
        for (int key : rawData.keySet()) {
            input[i] = rawData.get(key);
            i++;
        }
        double[][] a = new double[i][2];
        double[] a1 = new double[i];

//        for (i = 0; i < a.length; i++) {
//            for(int j = 0; j < 2; j++){
//                a[i][j] = input[i][j];
//            }
//
//        }
//        System.out.println(Arrays.deepToString(a));
        System.out.println("input:  " + Arrays.toString(input));
//        System.out.println("A1:     " + Arrays.deepToString(a));


        DoubleFFT_1D dfft = new DoubleFFT_1D(rows);


        dfft.realForwardFull(input);
        System.out.println("input:  " + Arrays.toString(input));


        int j = 0;
        for (int key : rawData.keySet()) {
            a[j][0] = key;
            j += 1;
        }
        for (j = 0; j < input.length; j += 2) {
            a[j / 2][1] = input[j];
        }


//        for (int j = 0; j < input.length ; j++) {
//            System.out.println("A["+j+"][0]:    " + a[j][0] + "A["+j+"][1]:    " + a[j][1]);
//            System.out.println("I["+j+"][0]:    " + input[j][0] + "I["+j+"][1]:    " + input[j][1]);
//        }
        return a;
    }

    public double[][] savitzkyGolayFilter(double[] xdata, double[] ydata, int sGPolyDegree, int sgFilterWidth) {

        double[][] smoothedValues = new double[xdata.length][2];

        CurveSmooth csm = new CurveSmooth(xdata, ydata);
        // default is 4
        if (sGPolyDegree != -1) {
            csm.setSGpolyDegree(sGPolyDegree);
        }

        double[] smoothedData1 = csm.savitzkyGolay(sgFilterWidth);


        for (int i = 0; i < xdata.length; i++) {

            smoothedValues[i][0] = xdata[i];
            smoothedValues[i][1] = smoothedData1[i];
        }

        return smoothedValues;
    }
}
