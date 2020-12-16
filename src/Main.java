import framework.MapPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main extends JFrame {

    Container c;

    JPanel options;
    MapPanel map;

    int width;
    int height;

    JMenuBar jMenuBar;

    JComboBox pathalgo;
    JComboBox mazealgo;

    public Main(){

        width = 31;
        height = 31;

        c = getContentPane();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));

        jMenuBar = new JMenuBar();

        JMenu settings = new JMenu("Options");

        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAbout();
            }
        });

        settings.add(about);
        jMenuBar.add(settings);

        setJMenuBar(jMenuBar);

        map = new MapPanel(width, height);

        options = new JPanel();
        options.setLayout(new GridLayout(2, 3, 20, 10));
        options.setPreferredSize(new Dimension(width * 32, 100));
        options.setBorder(new EmptyBorder(20, 20, 20, 20));

        pathalgo = new JComboBox(new String[] {"Dijkstra", "BFS", "AStar"});
        options.add(pathalgo);
        JButton button = new JButton("Find Path");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                map.runPathFinding(pathalgo.getSelectedItem());
            }
        });
        options.add(button);
        button = new JButton("Reset");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                map.reset();
            }
        });
        options.add(button);
        mazealgo = new JComboBox(new String[] {"DFS", "Prim"});
        options.add(mazealgo);
        button = new JButton("Gen Maze");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                map.runMazeGeneration(mazealgo.getSelectedItem());
            }
        });
        options.add(button);
        button = new JButton("Reset Path");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                map.resetPath();
            }
        });
        options.add(button);

        c.add(options);
        c.add(map);
    }

    /**
     * Creates the about dialog window
     */
    private void createAbout(){
        JDialog aboutDialog = new JDialog(this, "About", true);
        aboutDialog.getContentPane();
        aboutDialog.setSize(400, 250);
        aboutDialog.setResizable(false);
        aboutDialog.setLocationRelativeTo(this);
        aboutDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JTextArea aboutText = new JTextArea();

        try {
            String readme = Files.readString(Path.of("README.txt"));
            aboutText.setText(readme);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Set Font
        aboutText.setFont(new Font("SansSerif", Font.BOLD+Font.ITALIC, 16));
        aboutText.setLineWrap(true);
        aboutText.setWrapStyleWord(true);

        aboutDialog.add(aboutText);

        aboutDialog.setVisible(true);
    }

    public static void main(String[] args) {
        Main window = new Main();
        window.setTitle("");
        window.pack();
        window.setResizable(false);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
