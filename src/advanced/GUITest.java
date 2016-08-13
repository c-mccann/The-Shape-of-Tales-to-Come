package advanced; /**
 * Created by carlmccann2 on 30/09/15.
 * used for testing
 */

import advanced.Smoothing.Smoothing;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GUITest extends JFrame {


    public GUITest(ArrayList<XYSeries> xySeries) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final int WIDTH = 1280;
        final int HEIGHT = 960;


        String[] mediaStrings = {"Movie", "Play", "Shakespearean"};


        JPanel panelMedia = new JPanel();
        JComboBox mediaList = new JComboBox(mediaStrings);
        mediaList.setMaximumRowCount(4);
        panelMedia.add(mediaList);
        frame.add(panelMedia, BorderLayout.SOUTH);

        String[] filmStrings = {"Star Wars", "American Sniper", "The Godfather"};


        JPanel panelFilms = new JPanel();
        JComboBox filmList = new JComboBox(filmStrings);
        filmList.setMaximumRowCount(4);
        //panelFilms.add(filmList);
        panelMedia.add(filmList);
        frame.add(panelFilms, BorderLayout.WEST);

        String[] smoothingStrings = {"Raw Data", "Simple Moving Average", "Exponential Moving Average", "Savitzky-Golay Filter"};
        JComboBox smoothingList = new JComboBox(smoothingStrings);
        panelMedia.add(smoothingList);

        JButton btnGo = new JButton("Go!");
        panelMedia.add(btnGo);
        //JButton btnGraph = new JButton("Graph");


        //JPanel pnlButton = new JPanel();
        //JPanel pnlMedia = new JPanel();

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        };
        mediaList.setSelectedIndex(0);
        mediaList.addActionListener(actionListener);
        mediaList.setVisible(true);

//        pnlButton.setLayout(null);
//        btnGraph.setBounds(10,400,200,50);
//        pnlButton.setBounds(10,400,100,100);

//        pnlMedia.setLayout(null);
//        pnlMedia.setAlignmentY(1);


        XYSeries series = new XYSeries("Character 1 Dummy Data");


        series.add(1, 0.8);
        series.add(2, 0.4);
        series.add(3, 0.3);
        series.add(4, 0);
        series.add(5, 0.2);
        series.add(6, -0.5);
        series.add(7, -0.25);
        series.add(8, 0.8);

        XYSeries series2 = new XYSeries("Character 2 Dummy Data");
        series2.add(1, -0.8);
        series2.add(2, -0.4);
        series2.add(3, -0.3);
        series2.add(4, 0);
        series2.add(5, -0.2);
        series2.add(6, 0.5);
        series2.add(7, 0.25);
        series2.add(8, -0.8);

        XYSeries series3 = new XYSeries("Sample");


        XYSeriesCollection dataset = new XYSeriesCollection();

        // dummy data
        //dataset.addSeries(series);
        //dataset.addSeries(series2);

        //dataset.addSeries(xySeries);
        //dataset.addSeries(xySeries2);
        //dataset.addSeries(xySeries3);

        for (int i = 0; i < xySeries.size(); i++) {
            dataset.addSeries(xySeries.get(i));
        }


        JFreeChart chart = ChartFactory.createXYLineChart("Sentiment Chart", "Time", "Sentiment", dataset, PlotOrientation.VERTICAL, true, true, true);
        chart.getXYPlot().setRenderer(new XYSplineRenderer());
        //chart.getXYPlot().setRenderer(new XYLine3DRenderer());


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        //  turn off data points
        renderer.setBaseShapesVisible(false);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        plot.setDomainCrosshairVisible(true);
        plot.setDomainCrosshairLockedOnData(false);
        plot.setRangeCrosshairVisible(true);
        plot.setRangeCrosshairLockedOnData(true);
        plot.setDomainCrosshairValue(300);
        plot.setRangeCrosshairValue(0.4);

        System.out.println("X:  " + plot.getDomainCrosshairValue());
        System.out.println("Y:  " + plot.getRangeCrosshairValue());


        ChartPanel chartpanel = new ChartPanel(chart);

        chartpanel.setDomainZoomable(true);

        //add(chartpanel);
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new BorderLayout());
        jPanel4.add(chartpanel, BorderLayout.NORTH);

        //JFrame frame = new JFrame();
        frame.add(jPanel4);
//        frame.pack();
//        frame.setVisible(true);
//
        frame.add(jPanel4, BorderLayout.NORTH);

        //pnlButton.add(btnGraph);
        //pnlMedia.add(mediaList);

        //frame.add(pnlMedia);
        //frame.add(pnlButton, BorderLayout.SOUTH);
        frame.pack();

        frame.setTitle("The Shape of Tales to Come: Sentiment Analyser");
        frame.setSize(WIDTH, HEIGHT);
        //setSize(1024,768);
        frame.setVisible(true);


        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String args[]) throws FileNotFoundException {

        ArrayList<XYSeries> datasets = new ArrayList<>();
        Smoothing smoothing = new Smoothing();

//
////        // Movie testing
////        MovieProcessor tp = new MovieProcessor();
////        tp.main(args);
////        HashMap hm = tp.charSentReturner();
////        ArrayList al = (ArrayList)hm.get("DONNIE");
////        ArrayList al2 = (ArrayList)hm.get("FRANK");
////        ArrayList al3 = (ArrayList)hm.get("ROSE");
////
////        XYSeries xySeries = new XYSeries("Donnie");
////        XYSeries xySeries2 = new XYSeries("Frank");
////        XYSeries xySeries3 = new XYSeries("Rose");
////
////
////        for(Object key :  hm.keySet()){
////            ArrayList arrayList = (ArrayList)hm.get(key);
////        }
////        for(int i = 0; i < al.size();i++){
////
////            MovieSD movieSD = (MovieSD)al.get(i);
////            xySeries.add(movieSD.getMSP(),movieSD.getNormSent());
////
////        }
////
////        for(int i = 0; i < al2.size();i++){
////            MovieSD movieSD = (MovieSD)al2.get(i);
////            xySeries2.add(movieSD.getMSP(),movieSD.getNormSent());
////        }
////
////        for(int i = 0; i<al3.size();i++){
////            MovieSD movieSD = (MovieSD)al3.get(i);
////            xySeries3.add(movieSD.getMSP(),movieSD.getNormSent());
////        }
//
//
//
//        // play testing
//
////        PlayProcessor pp = new PlayProcessor();
////
////        ArrayList lines = pp.fileOpen("Play/The Importance of Being Earnest. Wilde, O.txt");
////        ArrayList lines2 = pp.fileOpen2("Play/The Importance of Being Earnest. Wilde, O.txt");
////        ArrayList characters = pp.characterPuller(lines);
////        HashMap characterSentiment = pp.sentimentAnalyser(lines2,characters);
////
////        ArrayList al4 = (ArrayList)characterSentiment.get("Jack");
////        ArrayList al5 = (ArrayList)characterSentiment.get("Cecily");
////        ArrayList al6 = (ArrayList)characterSentiment.get("Miss Prism");
////
////        //XYSeries xySeries4 = new XYSeries("Jack EMA");        // not implemented to receive arraylist<PlaySD>
////        XYSeries xySeries5 = new XYSeries("Jack SMA");
////        XYSeries xySeries6 = new XYSeries("Jack Raw");
////
////        for(Object key : characterSentiment.keySet()) {
////            System.out.println(key);
////        }
////        System.out.println();
//////        for (int i = 0; i < al4.size(); i++) {
//////            PlaySD playSD = (PlaySD) al4.get(i);
//////            playSD.dataPrinter();
//////        }
////
////
////        LinkedHashMap lhm3 = smoothing.simpleMovingAverage(5,al4);
////        for(Object key: lhm3.keySet()){
////            xySeries5.add((int)key,(double)lhm3.get(key));
////            System.out.println("CHECK:  " + (int)key + ", " + (double)lhm3.get(key));
////        }
////
////        for(int i = 0; i<al4.size();i++){
////            PlaySD playSD = (PlaySD) al4.get(i);
////            System.out.println("(" + playSD.pSPReturner() + ", " + playSD.getNormSent() + ")");
////            //xySeries4.add(playSD.pSPReturner(),playSD.getNormSent());
////        }
////        for(int i = 0; i<al4.size();i++){
////            PlaySD playSD = (PlaySD) al4.get(i);
////
////            //xySeries5.add(playSD.pSPReturner(),playSD.getNormSent());
////        }
////        for(int i = 0; i<al4.size();i++){
////            PlaySD playSD = (PlaySD) al4.get(i);
////            xySeries6.add(playSD.pSPReturner(),playSD.getNormSent());
////        }
//
//
//        //  Movie
//
////        datasets.add(xySeries);
////        datasets.add(xySeries2);
////        datasets.add(xySeries3);
//
//        //  Play
//
////        datasets.add(xySeries4);
////        datasets.add(xySeries5);
////        datasets.add(xySeries6);
//
//
//        //  Smoothing tests
//
//        LinkedHashMap<Integer,Double> rawData = new LinkedHashMap<>();
//
//
//                                         //the-file-name = donnie darko, the-file-name-2 = erin brockovich
////        try (BufferedReader br = new BufferedReader(new FileReader
////                ("src/advanced/out/Movie/Donnie_Darko/Donnie-Raw-Sentiment.txt"))) {
//
//        double[] xdata = new double[1167];
//        double[] ydata = new double[1167];
//        int j =0;
//        try (BufferedReader br = new BufferedReader(new FileReader
//                ("src/advanced/out/Movie/Donnie-Darko-all-sentiment.csv"))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//
//                String lineSplit[] = line.split(",");
//
////                double sentiment = Double.parseDouble(lineSplit[0].trim());
////                int position = Integer.parseInt(lineSplit[1].trim());
////                rawData.put(Integer.parseInt(lineSplit[1].trim()), Double.parseDouble(lineSplit[0].trim()));
//
//                double sentiment = Double.parseDouble(lineSplit[1].trim());         // all sentiment is pos, sent
//                int position = Integer.parseInt(lineSplit[0].trim());               // rather than sent, pos, seen in character specific sentiment files
//                rawData.put(Integer.parseInt(lineSplit[0].trim()), Double.parseDouble(lineSplit[1].trim()));
//                xdata[j] = Double.parseDouble(lineSplit[0].trim());
//                ydata[j] = sentiment;
//                j++;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        LinkedHashMap lhm = smoothing.simpleMovingAverage(10,rawData);
//
//        XYSeries xySeries7 = new XYSeries("Donnie Darko SMA");
//        XYSeries xySeries8 = new XYSeries("Donnie Darko Raw");
//        XYSeries xySeries9 = new XYSeries("Donnie Darko EMA");
//        for(Object key: lhm.keySet()){
//            xySeries7.add((int)key,(double)lhm.get(key));
//        }
//        for(Object key : rawData.keySet()){
//            xySeries8.add((int)key,(double)rawData.get(key));
//        }
//        lhm = smoothing.exponentialMovingAverage(20,rawData);
//        for(Object key : lhm.keySet()){
//            xySeries9.add((int)key,(double)lhm.get(key));
//        }
//
//
//
////        datasets.add(xySeries9);
////        datasets.add(xySeries7);
////        datasets.add(xySeries8);
//
//
//
//        XYSeries xySeries10 = new XYSeries("Donnie Darko Fourier");
////        double[][] a = smoothing.discreteFourierTransform2(rawData);    // 2D
//        double[][] a = smoothing.discreteFourierTransform3(rawData);    // 1D
//
//        for (int i = 0; i < a.length ; i++) {
////            xySeries10.add(a[i][0],a[i][2]);    // 2D
//            xySeries10.add(a[i][0],a[i][1]);    // 1D
//        }
//
//        System.out.println("DD Fourier data:    " + Arrays.deepToString(a));
//
//        //datasets.add(xySeries10);
//
//        double[][] a2 = smoothing.savitzkyGolayFilter(xdata, ydata, 10,1000);
//
//
//        for (int i = 0; i < a2.length; i++) {
//            for (int k = 0; k < 2; k++) {
//                xdata[i] = a2[i][0];
//                ydata[i] = a2[i][1];
//            }
//        }
//        a2 = smoothing.savitzkyGolayFilter(xdata, ydata, 10,1000);
//
//        XYSeries xySeries11 = new XYSeries("Donnie Darko Savitzky-Golay Filter");
//
//        for (int i = 0; i < a2.length ; i++) {
//            xySeries11.add(a2[i][0],a2[i][1]);
//            System.out.println(a2[i][0] + "     " + a2[i][1]);
//        }

//        datasets.add(xySeries11);
//
//
//        // Try Smoothing the fourier transform data???
//        // List t = new ArrayList<>(Arrays.asList(a))
//        //
//
//
//        //////////////////////////////////////////
//        double[] xdata2 = new double[377];
//        double[] ydata2 = new double[377];
//        j = 0;
//        try (BufferedReader br = new BufferedReader(new FileReader
//                ("src/advanced/out/Movie/Donnie-Darko-all-sentiment.csv"))) {
//            String line;
//
//            while ((line = br.readLine()) != null) {
//
//                String lineSplit[] = line.split(",");
//                System.out.println(Arrays.toString(lineSplit));
//
////                double sentiment = Double.parseDouble(lineSplit[0].trim());
////                int position = Integer.parseInt(lineSplit[1].trim());
////                rawData.put(Integer.parseInt(lineSplit[1].trim()), Double.parseDouble(lineSplit[0].trim()));
//
//                double sentiment = Double.parseDouble(lineSplit[1].trim());         // all sentiment is pos, sent
//                int position = Integer.parseInt(lineSplit[0].trim());               // rather than sent, pos, seen in character specific sentiment files
////                rawData.put(Integer.parseInt(lineSplit[0].trim()), Double.parseDouble(lineSplit[1].trim()));
//                if( lineSplit[2] == "DONNIE"){
//                    xdata2[j] = Double.parseDouble(lineSplit[0].trim());
//                    ydata2[j] = sentiment;
//                    j++;
//                }
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        double[][] a3 = smoothing.savitzkyGolayFilter(xdata2, ydata2, 10,(int)(xdata.length / 1.167));
//
//
//        for (int i = 0; i < a3.length; i++) {
//            for (int k = 0; k < 2; k++) {
//                xdata2[i] = a3[i][0];
//                ydata2[i] = a3[i][1];
//            }
//        }
//        a3 = smoothing.savitzkyGolayFilter(xdata2, ydata2, 10,(int)(xdata.length / 1.167));
//
//        XYSeries xySeries12 = new XYSeries("Donnie Character Savitzky-Golay Filter");
//
//        for (int i = 0; i < a3.length ; i++) {
//            xySeries12.add(a3[i][0],a3[i][1]);
//        }
//
//        datasets.add(xySeries12);


///////////////////////////////////////////////////////////////////////////////////
        // American Beauty for Presentation
        double[] xdata3 = new double[377];
        double[] ydata3 = new double[377];
        double[] xdata4 = new double[1000];
        double[] ydata4 = new double[1000];
        double[] rawDataX = new double[1224];
        double[] rawDataY = new double[1224];
        double[] lesDataX = new double[377];
        double[] lesDataY = new double[377];

//        double[] xdata3 = new double[377];
//        double[] ydata3 = new double[377];
//        double[] xdata4 = new double[1000];
//        double[] ydata4 = new double[1000];
//        double[] rawDataX = new double[1224];
//        double[] rawDataY = new double[1224];
//        double[] lesDataX = new double[377];
//        double[] lesDataY = new double[377];

        int co = 0;
        try (BufferedReader br = new BufferedReader(new FileReader
                ("src/advanced/out/Movie/American-Beauty-all-sentiment.csv"))) {
            String line;
            int les_count = 0;
            while ((line = br.readLine()) != null) {

                String lineSplit[] = line.split(",");
                // splits in the format
                // x pos, sent, character, sentence, scene, blah

//                double sentiment = Double.parseDouble(lineSplit[0].trim());
//                int position = Integer.parseInt(lineSplit[1].trim());
//                rawData.put(Integer.parseInt(lineSplit[1].trim()), Double.parseDouble(lineSplit[0].trim()));

                double sentiment = Double.parseDouble(lineSplit[1].trim());         // all sentiment is pos, sent
                int position = Integer.parseInt(lineSplit[0].trim());               // rather than sent, pos, seen in character specific sentiment files
//                rawData.put(Integer.parseInt(lineSplit[0].trim()), Double.parseDouble(lineSplit[1].trim()));
                rawDataX[co] = (double) position;
                rawDataY[co] = sentiment;
                System.out.println((double) position);
                System.out.println(sentiment);

                if (lineSplit[2].equals("LESTER")) {
                    lesDataX[les_count] = (double) position;
                    lesDataY[les_count] = sentiment;
                    les_count++;
                    System.out.println("Match");
                }
                co++;

            }
            System.out.println("Lester count:   " + les_count);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XYSeries americanBeautyRaw = new XYSeries("American Beauty Raw Data");
        for (int i = 0; i < rawDataX.length; i++) {
            americanBeautyRaw.add(rawDataX[i], rawDataY[i]);
        }
        XYSeries lesterRaw = new XYSeries("Lester Raw Data");
        for (int i = 0; i < lesDataX.length; i++) {
            lesterRaw.add(lesDataX[i], lesDataY[i]);
            System.out.println(lesDataX[i]);
            System.out.println(lesDataY[i]);
        }

//        datasets.add(americanBeautyRaw);
//        datasets.add(lesterRaw);

        LinkedHashMap<Integer, Double> rawData = new LinkedHashMap<>();
//        try (BufferedReader br = new BufferedReader(new FileReader
//                ("src/advanced/out/Movie/Donnie_Darko/Donnie-Raw-Sentiment.txt"))) {


        int j = 0;
        try (BufferedReader br = new BufferedReader(new FileReader
                ("src/advanced/out/Movie/American-Beauty-all-sentiment.csv"))) {
//        try (BufferedReader br = new BufferedReader(new FileReader
//                ("src/advanced/out/Movie/50-50-all-sentiment.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {

                String lineSplit[] = line.split(",");

//                double sentiment = Double.parseDouble(lineSplit[0].trim());
//                int position = Integer.parseInt(lineSplit[1].trim());
//                rawData.put(Integer.parseInt(lineSplit[1].trim()), Double.parseDouble(lineSplit[0].trim()));

                double sentiment = Double.parseDouble(lineSplit[1].trim());         // all sentiment is pos, sent
                int position = Integer.parseInt(lineSplit[0].trim());               // rather than sent, pos, seen in character specific sentiment files
                rawData.put(Integer.parseInt(lineSplit[0].trim()), Double.parseDouble(lineSplit[1].trim()));

                j++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        XYSeries amBeauSMA = new XYSeries("American Beauty Simple Moving Average w/size: 20");
        LinkedHashMap lhm = smoothing.simpleMovingAverage(20, rawData);
        for (Object key : lhm.keySet()) {
            System.out.println(key);
            amBeauSMA.add((int) key, (double) lhm.get(key));
        }

        XYSeries amBeauEMA = new XYSeries("American Beauty Exponential Moving Average w/size: 20");
        lhm = smoothing.exponentialMovingAverage(20, rawData);
        for (Object key : lhm.keySet()) {
            amBeauEMA.add((int) key, (double) lhm.get(key));
        }

//        datasets.add(amBeauSMA);
//        datasets.add(amBeauEMA);


        XYSeries amBeautySGFXY = new XYSeries("American Beauty Savitzky-Golay Filter (poly:6, passes:4, width:300)");
        double[][] amBeautySGF = smoothing.savitzkyGolayFilter(rawDataX, rawDataY, 6, 300);
        double[] tempX = new double[amBeautySGF.length];
        double[] tempY = new double[amBeautySGF.length];

        for (int i = 0; i < amBeautySGF.length; i++) {
            tempX[i] = amBeautySGF[i][0];
            tempY[i] = amBeautySGF[i][1];
        }
        amBeautySGF = smoothing.savitzkyGolayFilter(tempX, tempY, 6, 300);
        for (int i = 0; i < amBeautySGF.length; i++) {
            tempX[i] = amBeautySGF[i][0];
            tempY[i] = amBeautySGF[i][1];
        }
        amBeautySGF = smoothing.savitzkyGolayFilter(tempX, tempY, 6, 300);
        for (int i = 0; i < amBeautySGF.length; i++) {
            tempX[i] = amBeautySGF[i][0];
            tempY[i] = amBeautySGF[i][1];
        }
        amBeautySGF = smoothing.savitzkyGolayFilter(tempX, tempY, 6, 300);
        for (int i = 0; i < amBeautySGF.length; i++) {
            amBeautySGFXY.add(amBeautySGF[i][0], amBeautySGF[i][1]);
        }
        datasets.add(amBeautySGFXY);
        datasets.add(amBeauEMA);
//        datasets.add(americanBeautyRaw);

        double[] tempX2 = new double[377];
        double[] tempY2 = new double[377];
        XYSeries lesterSGFXY = new XYSeries("Lester Savitzky-Golay Filter (poly:6, passes:20, width:100)");
        double[][] lesterSGF = smoothing.savitzkyGolayFilter(lesDataX, lesDataY, 6, 100);
        int passes = 20;

        while (passes > 1) {
            for (int i = 0; i < lesterSGF.length; i++) {
                tempX2[i] = lesterSGF[i][0];
                tempY2[i] = lesterSGF[i][1];
            }
            lesterSGF = smoothing.savitzkyGolayFilter(tempX2, tempY2, 6, 100);
            passes--;
        }

        for (int i = 0; i < lesterSGF.length; i++) {
            lesterSGFXY.add(lesterSGF[i][0], lesterSGF[i][1]);
        }

//        datasets.add(lesterSGFXY);

        GUITest ui = new GUITest(datasets);
    }

    public void addSeries() {

    }

}

