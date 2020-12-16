package framework;

import algorithms.Algorithm;
import algorithms.mazegeneration.DepthFirstSearch;
import algorithms.mazegeneration.Prim;
import algorithms.searchalgorithms.AStar;
import algorithms.searchalgorithms.BreathFirstSearch;
import algorithms.searchalgorithms.Dijkstra;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;

public class MapPanel extends JPanel {

    private int columns;
    private int rows;

    private int[][] map;
    private int[][] oldmap;

    private boolean pathexists;
    private boolean drawing_phase;

    /**
     * 0 = empty
     * 1 = wall
     * 2 = red
     * 3 = blue
     * 4 = yellow
     * 5 = green
     */
    private Image[] tiles = new Image[6];
    private Image[] backgroundBorder = new Image[8];

    private LinkedList<Point> startandfinish;

    private Algorithm algorithm;

    public MapPanel(int columns, int rows){
        this.columns = columns;
        this.rows = rows;

        startandfinish = new LinkedList<>();

        this.map = new int[columns][MapPanel.this.rows];

        loadSprites();

        setPreferredSize(new Dimension(columns * 32 + 32, rows * 32 + 32));

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = (e.getX() - 16) / 32;
                int y = (e.getY() - 16) / 32;

                if (drawing_phase && e.getX() > 16 && e.getY() > 16 && x < columns && y < rows) {
                    if (e.getModifiersEx() == 1024) {
                        if (map[x][y] == 0) map[x][y] = 1;
                    }
                    else if(e.getModifiersEx() == 4096) {
                        if(map[x][y] == 1) map[x][y] = 0;
                    }

                    repaint();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = (e.getX() - 16) / 32;
                int y = (e.getY() - 16) / 32;

                if(drawing_phase && e.getX() > 16 && e.getY() > 16 && x < columns && y < rows){
                    if(e.getButton() == 1){
                        if(map[x][y] == 0) map[x][y] = 1;
                        else if(map[x][y] == 1) map[x][y] = 0;
                        repaint();
                    }

                    else if(e.getButton() == 3){
                        if(map[x][y] == 1) map[x][y] = 0;
                        else if(map[x][y] == 2){
                          map[x][y] = 0;
                          startandfinish.remove(new Point(x, y));
                        }
                        else if(startandfinish.size() < 2) {
                            map[x][y] = 2;
                            startandfinish.add(new Point(x, y));
                        }
                        repaint();
                    }
                }
            }
        });

        drawing_phase = true;

        repaint();
    }

    private void loadSprites(){
        String path = System.getProperty("user.dir") + "\\sprites";
        try {
            BufferedImage spritesheet = ImageIO.read(new File(path + "\\spritesheet.png"));
            tiles[0] = spritesheet.getSubimage(0,0, 32, 32);
            tiles[1] = spritesheet.getSubimage(32, 64, 32, 32);
            tiles[2] = spritesheet.getSubimage(32, 32, 32, 32);
            tiles[3] = spritesheet.getSubimage(0, 64, 32, 32);
            tiles[4] = spritesheet.getSubimage(32, 0, 32, 32);
            tiles[5] = spritesheet.getSubimage(0, 32, 32, 32);

            spritesheet = ImageIO.read(new File(path + "\\backgroundsprites.png"));
            int c = 0;
            for(int i = 0; i <= 32; i += 16){
                for(int j = 0; j <= 32 && c < backgroundBorder.length; j += 16){
                    backgroundBorder[c] = spritesheet.getSubimage(j, i, 16, 16);
                    c++;
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void reset(){
        if(algorithm != null && !algorithm.isInterrupted()) algorithm.interrupt();
        map = new int[columns][rows];
        oldmap = null;
        startandfinish = new LinkedList<>();
        drawing_phase = true;
        pathexists = false;
        repaint();
    }

    public void resetPath(){
        if(oldmap != null){
            map = oldmap;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D graphics2D = (Graphics2D) g;

        graphics2D.drawImage(backgroundBorder[5], 0, 0, null);
        graphics2D.drawImage(backgroundBorder[6], getWidth() - 16, 0, null);
        graphics2D.drawImage(backgroundBorder[7], 0, getHeight() - 16, null);
        graphics2D.drawImage(backgroundBorder[2], getWidth() - 16, getHeight() - 16, null);

        for(int i = 16; i < getWidth() - 16; i += 16){
            graphics2D.drawImage(backgroundBorder[3], i, 0, null);
            graphics2D.drawImage(backgroundBorder[0], i, getHeight() - 16, null);
        }

        for(int i = 16; i < getHeight() - 16; i += 16){
            graphics2D.drawImage(backgroundBorder[4], 0, i, null);
            graphics2D.drawImage(backgroundBorder[1], getWidth() - 16, i, null);
        }
        for(int i = 0; i < columns; i++){
            for(int j = 0; j < columns; j++){
                graphics2D.drawImage(tiles[map[i][j]], i * 32 + 16, j * 32 + 16, null);
            }
        }
    }

    public void runPathFinding(Object algo){
        if(algorithm != null) algorithm.interrupt();
        if ("Dijkstra".equals(algo)) {
            algorithm = new Dijkstra(this, startandfinish);
        } else if ("BFS".equals(algo)) {
            algorithm = new BreathFirstSearch(this, startandfinish);
        } else if ("AStar".equals(algo)) {
            algorithm = new AStar(this, startandfinish);
        }
        else return;
        Thread thread = new Thread(algorithm);
        thread.start();
    }

    public void runMazeGeneration(Object algo){
        if(algorithm != null) algorithm.interrupt();
        drawing_phase = false;
        if ("DFS".equals(algo)) {
            algorithm = new DepthFirstSearch(this);
        } else if ("Prim".equals(algo)) {
            algorithm = new Prim(this);
        }
        else return;
        Thread thread = new Thread(algorithm);
        thread.start();
        drawing_phase = true;
    }

    public void setMapPoint(int x, int y, int value){
        map[x][y] = value;
    }

    public int[][] getMap(){
        return map;
    }

    public void setOldMap(int[][] map){
        if(!pathexists){
            oldmap = new int[columns][rows];
            for(int i = 0; i < map.length; i++){
                for(int j = 0; j < map[i].length; j++){
                    oldmap[i][j] = map[i][j];
                }
            }
        }
    }

    public int getColumns(){
        return columns;
    }

    public int getRows(){
        return rows;
    }
}
