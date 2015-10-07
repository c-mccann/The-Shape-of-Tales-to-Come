/**
 * Created by carlmccann2 on 30/09/15.
 */

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame{
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;

    private JButton btnGraph = new JButton("Graph");
    JPanel pnlButton = new JPanel();

    public GUI()
    {

        pnlButton.setLayout(null);
        btnGraph.setBounds(10,400,200,50);
        pnlButton.setBounds(10,400,100,100);



        pnlButton.add(btnGraph);
        add(pnlButton);

        setTitle("The Shape of Tales to Come: Sentiment Analyser");
        setSize(WIDTH, HEIGHT);
        setVisible(true);






        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String args[]){
        GUI ui = new GUI();
    }

}

