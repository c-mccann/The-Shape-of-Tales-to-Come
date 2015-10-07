import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.*;

public class GraphTest {

    public static final String Title = "The Shape of Tales to Come: Sentiment Analyser";

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                display();
            }
        });
    }

    private static void display() {
        XYSeries series = new XYSeries(Title);
        for (int i = 0; i <= 10; i++) {

            if(i > 5){
                series.add(i,-i);
            }
            else{
                series.add(i, i);
            }


        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        NumberAxis domain = new NumberAxis("Dialogue Timeline");
        NumberAxis range = new NumberAxis("Sentiment");
        XYSplineRenderer r = new XYSplineRenderer(3);
        XYPlot xyplot = new XYPlot(dataset, domain, range, r);
        JFreeChart chart = new JFreeChart(xyplot);
        ChartPanel chartPanel = new ChartPanel(chart){

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(640, 480);
            }
        };
        JFrame frame = new JFrame(Title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(chartPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}