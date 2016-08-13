package advanced;

import advanced.Processors.MovieProcessor;
import advanced.Processors.PlayProcessor;
import advanced.Processors.ShakespeareanProcessor;
import advanced.SentimentData.MovieSD;
import advanced.SentimentData.PlaySD;
import advanced.SentimentData.ShakespeareSD;
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
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;


/**
 * Created by carlmccann2 on 07/04/2016.
 * Encases all the processors, smoothing, graphing etc to be displayed in a gui
 * Badly Structured code needs work. Event Dispatch Thread is blocked at times
 */
public class GUI implements Runnable {

    public GUI() {
//        create window
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        create processors and, the handler for the output of the processors and smoothing components
        MovieProcessor movieProcessor = new MovieProcessor();
        PlayProcessor playProcessor = new PlayProcessor();
        ShakespeareanProcessor shakespeareanProcessor = new ShakespeareanProcessor();
        OutputProcessor outputProcessor = new OutputProcessor();
        Smoothing smoothing = new Smoothing();

        final int WIDTH = 1280;
        final int HEIGHT = 960;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        Bottom Bar for selecting media type, script, paramaters, clicking buttons etc
        String[] mediaStrings = {"Movie", "Play", "Shakespearean"};

        List scriptStrings = new ArrayList<String>();

        JPanel panelMedia = new JPanel();
        JComboBox mediaList = new JComboBox(mediaStrings);
        mediaList.setMaximumRowCount(4);
        panelMedia.add(mediaList);
        frame.add(panelMedia, BorderLayout.SOUTH);


        scriptStrings = fillScriptsComboBox((String) mediaList.getSelectedItem());

        JPanel panelScripts = new JPanel();
        JComboBox scriptsList = new JComboBox(scriptStrings.toArray(new String[0]));
        scriptsList.setMaximumRowCount(8);
        //panelFilms.add(filmList);
        panelMedia.add(scriptsList);
        frame.add(panelScripts, BorderLayout.WEST);

        JLabel charWhitespaceLabel = new JLabel("Character:");
        panelMedia.add(charWhitespaceLabel);
        JTextField characterWhiteSpaceField = new JTextField("37");
        panelMedia.add(characterWhiteSpaceField);

        JLabel dialWhitespaceLabel = new JLabel("Dialogue:");
        panelMedia.add(dialWhitespaceLabel);
        JTextField dialogueWhiteSpaceField = new JTextField("25");
        panelMedia.add(dialogueWhiteSpaceField);

        JLabel sceneWhitespaceLabel = new JLabel("Scene:");
        panelMedia.add(sceneWhitespaceLabel);
        JTextField sceneWhiteSpaceField = new JTextField("15");
        panelMedia.add(sceneWhiteSpaceField);

        JButton btnGen = new JButton("Generate Data");
        panelMedia.add(btnGen);

        JButton btnPlot = new JButton("Plot Graph");
        panelMedia.add(btnPlot);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        setting up the graph

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries allSentimentSavGol;
        List<XYSeries> characterSentimentListsSavGol = new ArrayList<XYSeries>();


        JFreeChart chart = ChartFactory.createXYLineChart("Sentiment Chart", "Time", "Sentiment", dataset, PlotOrientation.VERTICAL, true, true, true);
        chart.getXYPlot().setRenderer(new XYSplineRenderer());
        //chart.getXYPlot().setRenderer(new XYLine3DRenderer());


        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        //  turn off data point shapes
        renderer.setBaseShapesVisible(false);

        plot.setDomainCrosshairVisible(true);
        plot.setDomainCrosshairLockedOnData(false);
        plot.setRangeCrosshairVisible(true);
        plot.setRangeCrosshairLockedOnData(true);
        plot.setDomainCrosshairValue(0);
        plot.setRangeCrosshairValue(0);

        ChartPanel chartpanel = new ChartPanel(chart);

        chartpanel.setDomainZoomable(true);

        //add(chartpanel);
        JPanel sentimentGraphPanel = new JPanel();
        sentimentGraphPanel.setLayout(new BorderLayout());
        sentimentGraphPanel.add(chartpanel, BorderLayout.NORTH);

        //JFrame frame = new JFrame();
        frame.add(sentimentGraphPanel);
//        frame.pack();
//        frame.setVisible(true);
//
        frame.add(sentimentGraphPanel, BorderLayout.NORTH);


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//      Character selection for graphs


        JPanel characterListPanel = new JPanel();
        JScrollPane characterSelectionPane = new JScrollPane();
        JList<JCheckBox> characterList = new JList<>();


        GridLayout gL = new GridLayout(0, 1);
        characterListPanel.setLayout(gL);


        characterList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        characterList.setLayoutOrientation(JList.VERTICAL);
        characterList.setVisibleRowCount(-1);

        // choices will be limited to 12
        JLabel label = new JLabel("Characters");
        String[] chars = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

        characterListPanel.add(label);
        for (String character : chars) {
            characterSelectionPane.add(new JCheckBox(character));
            characterListPanel.add(new JCheckBox(character));
        }


        characterListPanel.add(characterList);

        characterList.setPreferredSize(new Dimension(250, 80));
        characterSelectionPane.setSize(new Dimension(250, 80));
        characterSelectionPane.add(characterList);
        characterSelectionPane.setVisible(true);
//        frame.add(characterSelectionPane,BorderLayout.CENTER);
//        frame.add(characterList);

        characterSelectionPane.add(characterListPanel);
        frame.add(characterListPanel, BorderLayout.LINE_START);
//        frame.add(characterSelectionPane);


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//      Display for selected dat point info

        GridLayout gridLayout = new GridLayout(6, 2);
        JPanel graphClickDataLabels = new JPanel(gridLayout);

        JLabel characterLabel = new JLabel("Character: ");
        characterLabel.setPreferredSize(new Dimension(1, 20));
        JLabel characterLabel2 = new JLabel("");

        JLabel sentimentLabel = new JLabel("Sentiment: ");
        JLabel sentimentLabel2 = new JLabel("");

        JLabel sentenceLabel = new JLabel("Sentence: ");
        JLabel sentenceLabel2 = new JLabel("");


        JLabel actSceneLabel = new JLabel("Act/Scene: ");
        JLabel actSceneLabel2 = new JLabel("");


        JLabel normSentLabel = new JLabel("Normalised Sentiment: ");
        JLabel normSentLabel2 = new JLabel("");

        JLabel sentencePositionLabel = new JLabel("Sentence Position: ");
        JLabel sentencePositionLabel2 = new JLabel("");


        graphClickDataLabels.add(characterLabel);
        graphClickDataLabels.add(characterLabel2);          // change all the Label2's with MovieSD data etc

        graphClickDataLabels.add(actSceneLabel);
        graphClickDataLabels.add(actSceneLabel2);

        graphClickDataLabels.add(sentencePositionLabel);
        graphClickDataLabels.add(sentencePositionLabel2);

        graphClickDataLabels.add(sentimentLabel);
        graphClickDataLabels.add(sentimentLabel2);

        graphClickDataLabels.add(normSentLabel);
        graphClickDataLabels.add(normSentLabel2);

        graphClickDataLabels.add(sentenceLabel);
        graphClickDataLabels.add(sentenceLabel2);

        frame.add(graphClickDataLabels, BorderLayout.CENTER);


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        Set up components within window, name it, size it and make it visible
        frame.pack();

        frame.setTitle("The Shape of Tales to Come: Sentiment Analyser");
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
        JOptionPane.showMessageDialog(frame, "The input fields for character, dialogue and scene refer to\n" +
                "the amount of space in the script before text of these types occur, will only need to be changed\n" +
                "when a movie has for example (25-10-10) attached to its title in the menu below");

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//       updates selectable scripts based on script type

        mediaList.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                System.out.println("NEW MEDIA");
                scriptsList.removeAllItems();

                List updateScripts = fillScriptsComboBox((String) mediaList.getSelectedItem());

                for (Object title : updateScripts) {
                    scriptsList.addItem(title);
                }
                ;

//                filmList.updateUI();
            }
        });

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        when the user click the button to generate data for the graph

        btnGen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(mediaList.getSelectedItem());
                System.out.println(scriptsList.getSelectedItem());
                System.out.println("Generate Data");

                JOptionPane.showMessageDialog(frame, "Generating data, please wait...");

                String mediaType = (String) mediaList.getSelectedItem();
                String scriptToProcess = (String) scriptsList.getSelectedItem();
                if (mediaType.equals("Movie")) {
                    System.out.println(mediaType);
                    System.out.println(scriptToProcess);

                    try {
                        movieProcessor.outputSentimentFile(mediaType + "/" + scriptToProcess,
                                Integer.parseInt(characterWhiteSpaceField.getText()),
                                Integer.parseInt(dialogueWhiteSpaceField.getText()),
                                Integer.parseInt(sceneWhiteSpaceField.getText()));
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                } else if (mediaType.equals("Play")) {
                    try {
                        ArrayList lines = playProcessor.fileOpen(mediaType + "/" + scriptToProcess);
                        ArrayList blocks = playProcessor.fileOpen2(mediaType + "/" + scriptToProcess);
                        ArrayList characters = playProcessor.characterPuller(lines);
                        playProcessor.sentimentAnalyser(blocks, characters);
                        playProcessor.fileWriter(scriptToProcess, playProcessor.allSentiment);
                        playProcessor.allSentiment.clear();
                        blocks.clear();
                        characters.clear();
                        lines.clear();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }


                } else if (mediaType.equals("Shakespearean")) {
                    try {
                        ArrayList blocks = shakespeareanProcessor.fileOpen2(scriptToProcess);
                        HashMap hM = shakespeareanProcessor.sentimentAnalyser(blocks);

                        shakespeareanProcessor.fileWriter2(scriptToProcess, shakespeareanProcessor.allSentiment);
                        shakespeareanProcessor.allSentiment.clear();
                        blocks.clear();
                        hM.clear();

                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
//              reset variables back to default
                characterWhiteSpaceField.setText("37");
                dialogueWhiteSpaceField.setText("25");
                sceneWhiteSpaceField.setText("15");
                JOptionPane.showMessageDialog(frame, "Data Generated");
            }
        });
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        When the user clicks the button to plot the data

        btnPlot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Plot Data");

                String mediaType = (String) mediaList.getSelectedItem();
                String scriptToProcess = (String) scriptsList.getSelectedItem();
                JOptionPane.showMessageDialog(frame, "Plotting graph, please wait...");

                dataset.removeAllSeries();

//                false in the constructor stops duplicate x values
                XYSeries allSentimentSavGol = new XYSeries(scriptToProcess.replace(".txt", ""), false);
                allSentimentSavGol.clear();
                characterSentimentListsSavGol.clear();
//                to allow lookup from graph to update properly
                outputProcessor.allSentimentLookup.clear();

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  prepares the data from output of processors into lists and hashmaps etc, sets up x and ydata for smoothing
//
                if (outputProcessor.processResults("src/advanced/out/" + mediaType + "/"
                        + scriptToProcess + "-all-sentiment.csv", mediaType) == -1) {


                    JOptionPane.showMessageDialog(frame, "No data available for script, please generate it");
                } else {
                    ArrayList<XYSeries> datasets = new ArrayList<>();
                    double[] xData = new double[outputProcessor.allSentiment.size()];
                    double[] yData = new double[outputProcessor.allSentiment.size()];


                    int i = 0;
                    if (mediaType.equals("Movie")) {
                        for (Object o : outputProcessor.allSentiment) {
                            xData[i] = ((MovieSD) o).getMSP();
                            yData[i] = ((MovieSD) o).getNormSent();
                            i++;
                        }

                    } else if (mediaType.equals("Play")) {
                        for (Object o : outputProcessor.allSentiment) {
                            xData[i] = ((PlaySD) o).getPSP();
                            yData[i] = ((PlaySD) o).getNormSent();
                            i++;
                        }
                    } else if (mediaType.equals("Shakespearean")) {
                        for (Object o : outputProcessor.allSentiment) {
                            xData[i] = ((ShakespeareSD) o).getPSP();
                            yData[i] = ((ShakespeareSD) o).getNormSent();
                            i++;
                        }
                    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                    generates smoothed graph data for the whole script
                    double[][] allSentSmooth = smoothing.savitzkyGolayFilter(xData, yData, 2, outputProcessor.allSentiment.size()/5);
                    for (int j = 0; j < allSentSmooth.length; j++) {
                        xData[j] = allSentSmooth[j][0];
                        yData[j] = allSentSmooth[j][1];
                    }

                    for (i = 0; i < 3; i++) {

                        for (int j = 0; j < allSentSmooth.length; j++) {
                            xData[j] = allSentSmooth[j][0];
                            yData[j] = allSentSmooth[j][1];
                        }
                        allSentSmooth = new double[xData.length][2];
                        allSentSmooth = smoothing.savitzkyGolayFilter(xData, yData, 2, outputProcessor.allSentiment.size()/5);
                    }
//                   stops the problem i was having with duplicate values
                    outputProcessor.allSentiment.clear();

                    List<String> selectedCharacters = new ArrayList<String>();

                    if (allSentimentSavGol.getItemCount() > 0) {
                        allSentimentSavGol.clear();
                    }


                    for (i = 0; i < allSentSmooth.length; i++) {
//                        stops duplicates
                        if (i != 0 && allSentSmooth[i][0] != allSentSmooth[i - 1][0]) {
                            allSentimentSavGol.add(allSentSmooth[i][0], allSentSmooth[i][1]);
                        }
                    }

                    i = 0;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                    fills the character selection checkbox list in descending order of sentences spoken

                    List<String> wordCount = new ArrayList<String>();
                    wordCount.clear();

                    for (String character : outputProcessor.characterSentiment.keySet()) {
//                        not available for selection if they've spoken less than 10 sentences
                        if (outputProcessor.characterSentiment.get(character).size() > 10) {
                            wordCount.add(
                                    Integer.toString(outputProcessor.characterSentiment.get(character).size()) + character);
                        }
                    }

                    Collections.sort(wordCount, new Comparator<String>() {
                        public int compare(String comp1, String comp2) {
                            return pullInt(comp1) - pullInt(comp2);
                        }

                        int pullInt(String wcCharacter) {
                            String number = wcCharacter.replaceAll("\\D", "");
                            return number.isEmpty() ? 0 : Integer.parseInt(number);
                        }
                    });

                    List<String> sortedCharacters = new ArrayList<String>();
                    Collections.reverse(wordCount);


                    for (String s : wordCount) {
                        boolean noIntFound = false;
                        while (noIntFound == false) {
                            String oldString = s;
                            s = s.replaceAll("^\\d", "");
                            if (oldString.equals(s)) {
                                noIntFound = true;
                            }
                        }

                        sortedCharacters.add(s);

                    }
//                    stops duplicates in graph data later
                    wordCount.clear();


                    for (i = 0; i < chars.length; i++) {
                        if (i < sortedCharacters.size()) {    // if there are less than 12 characters in the script
                            chars[i] = sortedCharacters.get(i);
                        }
                    }
//                    stops duplicates later in graph data
                    sortedCharacters.clear();


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                    generates graphs for selected characters

                    for (Component comp : characterListPanel.getComponents()) {
                        if (comp instanceof JCheckBox) {
                            if (((JCheckBox) comp).isSelected()) {
                                System.out.println(((JCheckBox) comp).getText());

                                if (outputProcessor.characterSentiment.get(((JCheckBox) comp).getText()) == null) {
                                    continue;
                                }
                                List tempList = outputProcessor.characterSentiment.get(((JCheckBox) comp).getText());
                                System.out.println(tempList.size());
                                double[] charXData = new double[tempList.size()];
                                double[] charYData = new double[tempList.size()];


                                if (mediaType.equals("Movie")) {
                                    i = 0;
                                    for (Object o : tempList) {
                                        charXData[i] = ((MovieSD) o).getMSP();
                                        charYData[i] = ((MovieSD) o).getNormSent();
                                        i++;
                                    }
                                } else if (mediaType.equals("Play")) {
                                    i = 0;
                                    for (Object o : tempList) {
                                        charXData[i] = ((PlaySD) o).getPSP();
                                        charYData[i] = ((PlaySD) o).getNormSent();
                                        i++;
                                    }
                                } else if (mediaType.equals("Shakespearean")) {
                                    i = 0;
                                    for (Object o : tempList) {
                                        charXData[i] = ((ShakespeareSD) o).getPSP();
                                        charYData[i] = ((ShakespeareSD) o).getNormSent();
                                        i++;
                                    }
                                }
                                i = 0;
                                double[][] tempCharacterSentSmooth = smoothing.savitzkyGolayFilter(charXData, charYData, 2, tempList.size()/5);

//                                3 seems to give the smoothest graph without losing the overall picture
                                for (i = 0; i < 1; i++) {
                                    for (int j = 0; j < tempCharacterSentSmooth.length; j++) {
                                        charXData[j] = tempCharacterSentSmooth[j][0];
                                        charYData[j] = tempCharacterSentSmooth[j][1];
                                    }
                                    tempCharacterSentSmooth = smoothing.savitzkyGolayFilter(charXData, charYData, 2, tempList.size()/5);
                                }


                                XYSeries tempCharXY = new XYSeries(((JCheckBox) comp).getText(), false);

                                for (i = 0; i < tempCharacterSentSmooth.length; i++) {
//                                    stop duplicate data points
                                    if (i != 0 && tempCharacterSentSmooth[i][0] != tempCharacterSentSmooth[i - 1][0]) {
                                        tempCharXY.add(tempCharacterSentSmooth[i][0], tempCharacterSentSmooth[i][1]);
                                    }
                                }
                                characterSentimentListsSavGol.add(tempCharXY);
                                selectedCharacters.add(((JCheckBox) comp).getText());
//                                stops duplicates from happening in character graphs

                                tempList.clear();


                            }

                        }
                    }


                    characterListPanel.removeAll();
                    characterListPanel.add(label);

                    for (String character : chars) {
                        characterSelectionPane.add(new JCheckBox(character));
                        characterListPanel.add(new JCheckBox(character));
                    }

                    //                    prevents duplicate data in graphs

                    sortedCharacters.clear();
                    wordCount.clear();
                    outputProcessor.characterSentiment.clear();

                    characterListPanel.add(characterList);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//                    add whole plot smoothed sentiment data to dataset
                    dataset.addSeries(allSentimentSavGol);
//                    add specific character smoothed data to dataset
                    for (XYSeries s : characterSentimentListsSavGol) {
                        dataset.addSeries(s);
                    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                    updates graph
                    sentimentGraphPanel.removeAll();
                    sentimentGraphPanel.revalidate(); // This removes the old chart

                    JFreeChart chart = ChartFactory.createXYLineChart("Sentiment Chart", "Sentences", "Sentiment", dataset, PlotOrientation.VERTICAL, true, true, true);
                    chart.getXYPlot().setRenderer(new XYLineAndShapeRenderer());
                    //chart.getXYPlot().setRenderer(new XYLine3DRenderer());

                    XYPlot plot = (XYPlot) chart.getPlot();
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
//                    //  turn off data points
                    renderer.setBaseShapesVisible(false);

                    plot.setDomainCrosshairVisible(true);
                    plot.setDomainCrosshairLockedOnData(false);
                    plot.setRangeCrosshairVisible(true);
                    plot.setRangeCrosshairLockedOnData(true);
                    plot.setDomainCrosshairValue(0);
                    plot.setRangeCrosshairValue(0);

                    ChartPanel refreshChartPanel = new ChartPanel(chart);
                    refreshChartPanel.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {

                        }

                        @Override
                        public void mousePressed(MouseEvent e) {

                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {

                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {

                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            System.out.println("x:\t" + plot.getDomainCrosshairValue());
                            System.out.println((int) Math.round(plot.getDomainCrosshairValue()));
                            System.out.println("y:\t" + plot.getRangeCrosshairValue());

                            int search = (int) Math.round(plot.getDomainCrosshairValue()) - 1;
                            if (search < 0) {
                                search = 0;
                            } else if (search > outputProcessor.allSentimentLookup.size()) {
                                search = outputProcessor.allSentimentLookup.size() - 1;

                            }

                            String temp = outputProcessor.allSentimentLookup.get(0).toString();
                            String[] lineSplit = temp.split("\\.");
                            String[] dataTypeSplit = lineSplit[2].split("@");

                            if (dataTypeSplit[0].equals("MovieSD")) {
                                MovieSD sd = (MovieSD) outputProcessor.allSentimentLookup.get(search);
                                sd.dataPrinter();
                                characterLabel2.setText(sd.getCharacter());
                                actSceneLabel2.setText(sd.getScene());
                                sentimentLabel2.setText(Double.toString(sd.getSentiment()));
                                normSentLabel2.setText(Double.toString(sd.getNormSent()));
                                sentenceLabel2.setText(sd.getSentence());
                                sentencePositionLabel2.setText(Integer.toString(sd.getMSP()) + " / " +
                                        Integer.toString(outputProcessor.allSentimentLookup.size()));

                            }
                            else if (dataTypeSplit[0].equals("PlaySD")) {
                                PlaySD sd = (PlaySD) outputProcessor.allSentimentLookup.get(search);
                                sd.dataPrinter();
                                characterLabel2.setText(sd.getCharacter());
                                actSceneLabel2.setText(sd.getAct());
                                sentimentLabel2.setText(Double.toString(sd.getSentiment()));
                                normSentLabel2.setText(Double.toString(sd.getNormSent()));
                                sentenceLabel2.setText(sd.getSentence());
                                sentencePositionLabel2.setText(Integer.toString(sd.getPSP()) + " / " +
                                        Integer.toString(outputProcessor.allSentimentLookup.size()));
                            }
                            else if (dataTypeSplit[0].equals("ShakespeareSD")) {
                                ShakespeareSD sd = (ShakespeareSD) outputProcessor.allSentimentLookup.get(search);
                                sd.dataPrinter();
                                characterLabel2.setText(sd.getCharacter());
                                actSceneLabel2.setText(sd.getAct() + " " + sd.getScene());
                                sentimentLabel2.setText(Double.toString(sd.getSentiment()));
                                normSentLabel2.setText(Double.toString(sd.getNormSent()));
                                sentenceLabel2.setText(sd.getSentence());
                                sentencePositionLabel2.setText(Integer.toString(sd.getPSP()) + " / " +
                                        Integer.toString((outputProcessor.allSentimentLookup.size())));




                            }
                        }
                    });
                    sentimentGraphPanel.setLayout(new BorderLayout());
                    sentimentGraphPanel.add(refreshChartPanel, BorderLayout.NORTH);
                    sentimentGraphPanel.repaint(); // This method makes the new

                    JOptionPane.showMessageDialog(frame, "Graph Plotted");


                }

            }
        });


    }

    public static List fillScriptsComboBox(String mediaType) {

        List scripts = new ArrayList<String>();

        File folder;
        File[] scriptFiles;
        if (mediaType.equals("Movie")) {
            folder = new File("src/res/Movie");
            scriptFiles = folder.listFiles();
        } else if (mediaType.equals("Play")) {
            folder = new File("src/res/Play");
            scriptFiles = folder.listFiles();
        } else if (mediaType.equals("Shakespearean")) {
            folder = new File("src/res/Shakespearean");
            scriptFiles = folder.listFiles();
        } else {
            folder = null;
            scriptFiles = null;
        }


        for (File file : scriptFiles) {
            if (!file.getName().equals(".DS_Store")) {
//                scripts.add(file.getName().replace(".txt","").replace("+"," "));
                scripts.add(file.getName());
            }
        }

        return scripts;
    }

    public static void main(String args[]) {


//        (new Thread(new FinalGUI())).start();
        GUI gui = new GUI();
    }

    public void run() {
        GUI gui = new GUI();
    }
}
