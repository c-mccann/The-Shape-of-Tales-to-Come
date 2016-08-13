package simple; /**
 * Created by carlmccann2 on 30/09/15.
 */

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {


    public GUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final int WIDTH = 640;
        final int HEIGHT = 480;


        String[] mediaStrings = {"Book", "Play", "Movie", "Shakespearean"};


        JPanel panelMedia = new JPanel();
        JComboBox mediaList = new JComboBox(mediaStrings);
        mediaList.setMaximumRowCount(4);
        panelMedia.add(mediaList);
        frame.add(panelMedia, BorderLayout.SOUTH);

        String[] filmStrings = {"Star Wars", "American Sniper", "The Godfather"};


        JPanel panelFilms = new JPanel();
        JComboBox filmList = new JComboBox(filmStrings);
        filmList.setMaximumRowCount(4);
        panelFilms.add(filmList);
        frame.add(panelFilms, BorderLayout.WEST);


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


        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        dataset.addSeries(series2);


        JFreeChart chart = ChartFactory.createXYLineChart("Sentiment Chart", "Time", "Sentiment", dataset, PlotOrientation.VERTICAL, true, true, true);
        chart.getXYPlot().setRenderer(new XYSplineRenderer());
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

    public static void main(String args[]) {
        GUI ui = new GUI();
    }

}

