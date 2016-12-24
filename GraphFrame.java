import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JRadioButtonMenuItem;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.BasicStroke;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * Graphics class for the graphing calculator
 * @author Sam Barr
 */
public class GraphFrame extends JFrame
{
    public static final String[] COLOR_OPTIONS = {"Red", "Blue", "Orange", "Green", "Black", "Maroon"};
    private static final int UNDEFINED = Integer.MAX_VALUE;
    private static final int DEFAULT_POINT_SIZE = 4;
    private static final int HOVERED_POINT_SIZE = 6;
    private double xMin, xMax, xScale, yMin, yMax, yScale;
    private ArrayList<Equation> graphs, selected;
    private Hashtable<Equation, int[][]> points;
    private Hashtable<Equation, double[]> coordinates;
    private Hashtable<Equation, Color> colors;
    private Hashtable<Equation, Integer> pointSizes;
    private GraphComponent gc;
    private WindowFrame windowFrame;
    private InputFrame inputFrame;
    private MenuBar menuBar;
    private JRadioButtonMenuItem xAxisLabel, yAxisLabel;

    /**
     * Does the thing
     * @param String[] args
     */
    public static void main(String[] args)
    {
        new GraphFrame();
    }

    /**
     * Constructs a default GraphFrame
     */
    public GraphFrame()
    {
        this(-10., 10., 1., -10., 10., 1.);
        setVisible(true);
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Constructs a GraphFrame
     * @param double xMin
     * @param double xMax
     * @param double xScale
     * @param double yMin
     * @param double yMax
     * @param double yScale
     * @throws IllegalArgumentException if you're stupid
     */
    public GraphFrame(double xMin, double xMax, double xScale, double yMin, double yMax, double yScale)
    {
        super("Graph");
        if(xMin >= xMax) throw new IllegalArgumentException("Minimum x value cannot be greater than or equal to maximum x value");
        else if(yMin >= yMax) throw new IllegalArgumentException("Minimum y value cannot be greater than or equal to maximum y value");
        else if(xScale <= 0) throw new IllegalArgumentException("xScale must be greater than 0");
        else if(yScale <= 0) throw new IllegalArgumentException("yScale must be greater than 0");
        this.xMin = xMin;
        this.xMax = xMax;
        this.xScale = xScale;
        this.yMin = yMin;
        this.yMax = yMax;
        this.yScale = yScale;
        graphs = new ArrayList<Equation>();
        selected = new ArrayList<Equation>();
        points = new Hashtable<Equation, int[][]>();
        colors = new Hashtable<Equation, Color>();
        pointSizes = new Hashtable<Equation, Integer>();
        coordinates = new Hashtable<Equation, double[]>();
        windowFrame = new WindowFrame(this);
        gc = new GraphComponent(false, false);
        inputFrame = new InputFrame(this);
        getContentPane().add(gc);
        menuBar = new MenuBar();
        menuBar.add("Equations", new String[]{"Add a Equation", "Clear All"}, 
            new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    inputFrame.setVisible(true);
                }
            }, new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    graphs.clear();
                    pointSizes.clear();
                    points.clear();
                    colors.clear();
                    coordinates.clear();
                    selected.clear();
                    repaint();
                    for(MouseMotionListener l: gc.getMouseMotionListeners()) gc.removeMouseMotionListener(l);
                    for(MouseListener l: gc.getMouseListeners()) gc.removeMouseListener(l);
                }
            });
        menuBar.getMenu(0).add(MenuBar.makeMenu("Conic", new String[]{"Circle", "Ellipse", "Vertical Hyperbola", "Horizontal Hyperbola", "Vertical Parabola", "Horizontal Parabola"},
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        new ConicFrame(ConicFrame.CIRCLE, GraphFrame.this);
                    }
                }, new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        new ConicFrame(ConicFrame.ELLIPSE, GraphFrame.this);
                    }
                }, new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        new ConicFrame(ConicFrame.HYPERBOLA_VERTICAL, GraphFrame.this);
                    }
                }, new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        new ConicFrame(ConicFrame.HYPERBOLA_HORIZONTAL, GraphFrame.this);
                    }
                }, new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        new ConicFrame(ConicFrame.PARABOLA_VERTICAL, GraphFrame.this);
                    }
                }, new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        new ConicFrame(ConicFrame.PARABOLA_HORIZONTAL, GraphFrame.this);
                    }
                }), 1);
        menuBar.add("Window", new String[]{"Settings", "Standard"}, 
            new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    windowFrame.setVisible(true);
                }
            }, new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    setXMin(-10);
                    setXMax(10);
                    setXScale(1);
                    setYMin(-10);
                    setYMax(10);
                    setYScale(1);
                    windowFrame.update();
                    repaint();
                }
            });
        xAxisLabel = new JRadioButtonMenuItem("Draw X Axis Labels");
        yAxisLabel = new JRadioButtonMenuItem("Draw Y Axis Labels");
        menuBar.getMenu(1).add(xAxisLabel);
        menuBar.getMenu(1).add(yAxisLabel);
        xAxisLabel.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    gc.setDrawXAxisLabel(xAxisLabel.isSelected());
                    repaint();
                }
            });
        yAxisLabel.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    gc.setDrawYAxisLabel(yAxisLabel.isSelected());
                    repaint();
                }
            });
        setJMenuBar(menuBar);
    }

    /**
     * Sets xMin
     * @param double xMin
     */
    public synchronized void setXMin(double xMin)
    {
        this.xMin = xMin;
    }

    /**
     * Sets xMax
     * @param double xMax
     */
    public synchronized void setXMax(double xMax)
    {
        this.xMax = xMax;
    }

    /**
     * Sets xScale
     * @param double xScale
     */
    public synchronized void setXScale(double xScale)
    {
        this.xScale = xScale;
    }

    /**
     * Sets yMin
     * @param double yMin
     */
    public synchronized void setYMin(double yMin)
    {
        this.yMin = yMin;
    }

    /**
     * Sets yMax
     * @param double yMax
     */
    public synchronized void setYMax(double yMax)
    {
        this.yMax = yMax;
    }

    /**
     * Sets yScale
     * @param double yScale
     */
    public synchronized void setYScale(double yScale)
    {
        this.yScale = yScale;
    }

    /**
     * @return double
     */
    public double getXMin()
    {
        return xMin;
    }

    /**
     * @return double
     */
    public double getXMax()
    {
        return xMax;
    }

    /**
     * @return double
     */
    public double getXScale()
    {
        return xScale;
    }

    /**
     * @return double
     */
    public double getYMin()
    {
        return yMin;
    }

    /**
     * @return double
     */
    public double getYMax()
    {
        return yMax;
    }

    /**
     * @return double
     */
    public double getYScale()
    {
        return yScale;
    }

    /**
     * Scales an x value from the grid to an x value in the frame
     * @param double x
     * @return int
     */
    public int translateToWindowX(double x)
    {
        return (int)((x - xMin) * ((double)gc.getWidth() / (xMax - xMin)));
    }

    /**
     * Scales an x value from the frame to an x value in the grid
     * @param int x
     * @return double
     */
    public double translateToGridX(int x)
    {
        return  x * (xMax - xMin) / (double)gc.getWidth() + xMin;
    }

    /**
     * Scales a y value from the grid to a y value in the frame
     * @param double y
     * @return int
     */
    public int translateToWindowY(double y)
    {
        return (int)((yMax - y) * ((double)gc.getHeight() / (yMax - yMin)));
    }

    /**
     * Scales a y value from the frame to a y value in the grid
     * @param int y
     * @return double
     */
    public double translateToGridY(int y)
    {
        return y * (yMax - yMin) / (double)gc.getHeight() + yMin;
    }

    /**
     * Adds an Equation to the frame
     * @param Equation eq
     * @param Color c
     */
    public void addGraph(Equation eq, Color c)
    {
        gc.addGraph(eq, c);
    }

    /**
     * Rounds a number
     * @param double num
     * @param int place
     * @return double
     */
    public double round(double num, int place)
    {
        return Math.round(num * Math.pow(10, place)) / (double)Math.pow(10, place);
    }
    /**
     * Class for drawing the Equations
     * @author Sam Barr
     */
    class GraphComponent extends JComponent
    {
        private final Font COORDINATE_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 15);
        private final Font AXIS_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
        private boolean drawXAxisLabel, drawYAxisLabel;

        /**
         * Constructs a GraphComponent object
         * @param boolean drawXAxisLabel
         * @param boolean drawYAxisLabel
         */
        public GraphComponent(boolean drawXAxisLabel, boolean drawYAxisLabel)
        {
            super();
            this.drawXAxisLabel = drawXAxisLabel;
            this.drawYAxisLabel = drawYAxisLabel;
        }

        /**
         * Draws the graphs
         * @param Graphics g
         */
        public void drawGraphs(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(1));
            for(Equation eq: graphs)
            {
                g2.setColor(colors.get(eq));
                int[][] pointsArr = new int[getWidth()][eq.getNumValues()];
                for(int j = 0; j < getWidth(); j++)
                {
                    double[] yValues = eq.getValues(translateToGridX(j));
                    for(int i = 0; i < yValues.length; i++)
                    {
                        if(Double.isNaN(yValues[i])) {pointsArr[j][i] = UNDEFINED; continue;}
                        pointsArr[j][i] = translateToWindowY(yValues[i]);
                        g2.fillOval(j - pointSizes.get(eq) / 2, pointsArr[j][i] - pointSizes.get(eq) / 2, pointSizes.get(eq), pointSizes.get(eq));
                        if(j > 0) drawLine(g, j, pointsArr[j][i], j - 1, pointsArr[j - 1][i], pointSizes.get(eq));
                        points.put(eq, pointsArr);
                    }
                }
            }
        }

        public void drawLine(Graphics g, int x1, int y1, int x2, int y2, int pointSize)
        {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(pointSize - 1.5f));
            if((y1 > this.getHeight() || y1 < 0) && (y2 > this.getHeight() || y2 < 0)) return;
            g2.drawLine(x1, y1, x2, y2);
        }

        /**
         * Draws the x axis
         * @param Graphics g
         */
        public void drawYAxis(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.BLACK);
            g2.drawLine(translateToWindowX(0), 0, translateToWindowX(0), getHeight());
            g2.setFont(AXIS_FONT);
            for(double i = 0; i < yMax; i+=yScale)
            {
                if(drawYAxisLabel && i != 0)
                {
                    if(xMin > 0) g2.drawString(Double.toString(round(i, 3)), 15, translateToWindowY(i) + 3);
                    else if(xMax < 0) g2.drawString(Double.toString(round(i, 3)), getWidth() - 28, translateToWindowY(i) + 3);
                    else g2.drawString(Double.toString(round(i, 3)), translateToWindowX(0) + 10, translateToWindowY(i) + 3);
                }
                if(xMin > 0) g2.drawLine(0, translateToWindowY(i), 10, translateToWindowY(i));
                else if(xMax < 0) g2.drawLine(getWidth(), translateToWindowY(i), getWidth() - 10, translateToWindowY(i));
                else g2.drawLine(translateToWindowX(0) + 5, translateToWindowY(i), translateToWindowX(0) - 5, translateToWindowY(i));
            }
            for(double i = 0; i > yMin; i-=yScale)
            {
                if(drawYAxisLabel && i != 0)
                {
                    if(xMin > 0)g2.drawString(Double.toString(round(i, 3)), 15, translateToWindowY(i) + 3);
                    else if(xMax < 0) g2.drawString(Double.toString(round(i, 3)), getWidth() - 28, translateToWindowY(i) + 3);
                    else g2.drawString(Double.toString(round(i, 3)), translateToWindowX(0) + 10, translateToWindowY(i) + 3);
                }
                if(xMin > 0)g2.drawLine(0, translateToWindowY(i), 10, translateToWindowY(i));
                else if(xMax < 0) g2.drawLine(getWidth(), translateToWindowY(i), getWidth() - 10, translateToWindowY(i));
                else g2.drawLine(translateToWindowX(0) + 5, translateToWindowY(i), translateToWindowX(0) - 5, translateToWindowY(i));
            }
        }

        /**
         * Draws the y axis
         * @param Graphics g
         */
        public void drawXAxis(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.BLACK);
            g2.drawLine(0, translateToWindowY(0), getWidth(), translateToWindowY(0));
            g2.setFont(AXIS_FONT);
            for(double i = 0; i < xMax; i+=xScale)
            {
                if(drawXAxisLabel && i != 0)
                {
                    if(yMin > 0)g2.drawString(Double.toString(round(i, 3)), translateToWindowX(i) - 6, getHeight() - 15);
                    else if(yMax < 0) g2.drawString(Double.toString(round(i, 3)), translateToWindowX(i) - 6, 20);
                    else g2.drawString(Double.toString(round(i, 3)), translateToWindowX(i) - 6, translateToWindowY(0) - 10);
                }
                if(yMin > 0) g2.drawLine(translateToWindowX(i), getHeight(), translateToWindowX(i), getHeight() - 10);
                else if(yMax < 0) g2.drawLine(translateToWindowX(i), 0, translateToWindowX(i), 10);
                else g2.drawLine(translateToWindowX(i), translateToWindowY(0) + 5, translateToWindowX(i), translateToWindowY(0) - 5);
            }
            for(double i = 0; i > xMin; i-=xScale)
            {
                if(drawXAxisLabel && i != 0)
                {
                    if(yMin > 0)g2.drawString(Double.toString(round(i, 3)), translateToWindowX(i) - 6, getHeight() - 15);
                    else if(yMax < 0) g2.drawString(Double.toString(round(i, 3)), translateToWindowX(i) - 6, 20);
                    else g2.drawString(Double.toString(round(i, 3)), translateToWindowX(i) - 6, translateToWindowY(0) - 10);
                }
                if(yMin > 0) g2.drawLine(translateToWindowX(i), getHeight(), translateToWindowX(i), getHeight() - 10);
                else if(yMax < 0) g2.drawLine(translateToWindowX(i), 0, translateToWindowX(i), 10);
                else g2.drawLine(translateToWindowX(i), translateToWindowY(0) + 5, translateToWindowX(i), translateToWindowY(0) - 5);
            }
        }

        /**
         * Draws the coordinates of the graph your mouse is hovering over
         * @param Graphics g
         */
        public void drawCoordinates(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.BLACK);
            g2.setFont(COORDINATE_FONT);
            for(double[] co: coordinates.values())
            {
                g2.drawString("(" + round(co[0], 3) + ", " + round(co[1], 3) + ")", translateToWindowX(co[0]), translateToWindowY(co[1]));
            }
        }

        /**
         * Draws coordinates where two selected Equations are equal
         * @param Graphics g
         */
        public void drawEqual(Graphics g)
        {
            for(int i = 0; i < selected.size(); i++)
            {
                Equation eq1 = selected.get(i);
                for(int j = i + 1; j < selected.size(); j++)
                {
                    Equation eq2 = selected.get(j);
                    if(eq1.equals(eq2)) continue;
                    findEqual(g, eq1, eq2);
                }
            }
        }

        /**
         * Called by drawEqual() to draw where two given Equations are equal
         * @param Gaphics g
         * @param Equation eq1
         * @param Equation eq2
         */
        public void findEqual(Graphics g, Equation eq1, Equation eq2)
        {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.BLACK);
            g2.setFont(COORDINATE_FONT);
            for(int i = 0; i < getWidth(); i++)
            {
                for(int j = 0; j < eq1.getNumValues(); j++)
                {
                    double y1 = eq1.getValues(translateToGridX(i))[j];
                    for(int k = 0; k < eq2.getNumValues(); k++)
                    {
                        double y2 = eq2.getValues(translateToGridX(i))[k];
                        if(y1 == UNDEFINED || y2 == UNDEFINED) continue;
                        else if(round(y1, 1) == round(y2, 1))
                        {
                            double loc = translateToGridX(i);
                            double dif = Math.abs(y1 - y2);
                            for(double m = loc; m < xMax; m += .001)
                            {
                                double difTemp = Math.abs(eq1.getValues(m)[j] - eq2.getValues(m)[k]);
                                if(difTemp < dif)
                                {
                                    dif = difTemp;
                                }
                                else if (difTemp > dif && dif < .001)
                                {
                                    i = translateToWindowX(m);
                                    g2.drawString("(" + round(m - .001, 3) + ", " + round(eq1.getValues(m - .001)[j], 3) + ")", i, points.get(eq1)[i][j]);
                                    break;
                                }
                                else if (difTemp > dif && dif >= .001)
                                {
                                    i = translateToWindowX(m);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        /**
         * Called to paint the GraphComponent
         * @param Graphics g
         */
        public void paintComponent(Graphics g)
        {
            drawGraphs(g);
            drawXAxis(g);
            drawYAxis(g);
            drawCoordinates(g);
            drawEqual(g);
        }

        /**
         * Sets whether the GraphComponent draws X axis labels
         * @param boolean drawXAxisLabel
         */
        public void setDrawXAxisLabel(boolean drawXAxisLabel)
        {
            this.drawXAxisLabel = drawXAxisLabel;
        }

        /**
         * Sets whether the GraphComponent draws Y axis labels
         * @param boolean drawYAxisLabel
         */
        public void setDrawYAxisLabel(boolean drawYAxisLabel)
        {
            this.drawYAxisLabel = drawYAxisLabel;
        }

        /**
         * Adds a graph to the GraphComponent
         * @param Equation eq     Equation to graph
         * @param Color c       Color to display graph as
         */
        public void addGraph(Equation eq, Color c)
        {
            pointSizes.put(eq, DEFAULT_POINT_SIZE);
            colors.put(eq, c);
            graphs.add(eq);
            GraphFrame.this.repaint();
            final HoverListener hoverListener = new HoverListener(eq);
            gc.addMouseMotionListener(hoverListener);
            gc.addMouseListener(new ClickListener(eq, hoverListener));
        }
    }
    /**
     * MouseMotionListener that looks to see if mouse is hovered over a Equation
     * @author Sam Barr
     */
    class HoverListener extends MouseMotionAdapter
    {
        private Equation eq;

        /**
         * Constructs a HoverListener with a given Equation
         * @param Equation eq     Equation the HoverListener is listening to
         */
        public HoverListener(Equation eq)
        {
            this.eq = eq;
        }

        /**
         * Looks to see if mouse is on top of the Equation
         * @param MouseEvent e
         */
        public void mouseMoved(MouseEvent e)
        {
            boolean isHovered = false;
            int x = e.getX();
            int y = e.getY();
            int[][] pointsArr = points.get(eq);
            for(int i = 0; i < eq.getNumValues(); i++)
            {
                if(pointsArr == null) return;
                else if(y - pointSizes.get(eq) <= pointsArr[x][i] && y + pointSizes.get(eq) >= pointsArr[x][i])
                {
                    if(!selected.contains(eq)) pointSizes.put(eq, HOVERED_POINT_SIZE);
                    coordinates.put(eq, new double[]{translateToGridX(x), eq.getValues(translateToGridX(x))[i]});    
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    GraphFrame.this.repaint();
                    isHovered = true;
                }
                else if(!isHovered && pointSizes.get(eq).equals(HOVERED_POINT_SIZE))
                {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    if(!selected.contains(eq))
                    {
                        pointSizes.put(eq, DEFAULT_POINT_SIZE);
                        coordinates.remove(eq);
                        GraphFrame.this.repaint();
                    }
                }
            }
        }
    }
    /**
     * MouseListener that detects if you click on a Equation
     * @author Sam Barr
     */
    class ClickListener extends MouseAdapter
    {
        private Equation eq;
        private HoverListener hoverListener;

        /**
         * Constructs a ClickListener with a given Equation and HoverListener
         * @param Expressoin eq     Equation the ClickListener is listening to
         * @param HoverListener hoverListener       HoverListener to remove from the GraphComponent when the Equation is removed
         */
        public ClickListener(Equation eq, HoverListener hoverListener)
        {
            this.eq = eq;
            this.hoverListener = hoverListener;
        }

        /**
         * Looks to see if you clicked on the Equation
         * @param MouseEvent e
         */
        public void mouseClicked(MouseEvent e)
        {
            if(e.getButton() == MouseEvent.BUTTON1)
            {
                int x = e.getX();
                int y = e.getY();
                leftClick(x, y);
            }
            else if(e.getButton() == MouseEvent.BUTTON3)
            {
                int x = e.getX();
                int y = e.getY();
                rightClick(x, y);
            }
        }

        /**
         * Called if click is a left click
         * @param int x     horizontal location of mouse on screen
         * @param int y     vertical location of mouse on screen
         */
        public void leftClick(int x, int y)
        {
            int[][] pointsArr = points.get(eq);
            for(int i = 0; i < eq.getNumValues(); i++)
            {
                if(pointsArr == null) return;
                else if(y - pointSizes.get(eq) <= pointsArr[x][i] && y + pointSizes.get(eq) >= pointsArr[x][i])
                {
                    if(selected.contains(eq))
                    {
                        selected.remove(eq);
                        GraphFrame.this.repaint();
                    }
                    else
                    {
                        selected.add(eq);
                        pointSizes.put(eq, HOVERED_POINT_SIZE);
                        GraphFrame.this.repaint();
                    }
                }
            }
        }

        /**
         * Called if click is a right click
         * @param int x     horizontal location of mouse on screen
         * @param int y     vertical location of mouse on screen
         */
        public void rightClick(int x, int y)
        {
            int[][] pointsArr = points.get(eq);
            for(int i = 0; i < eq.getNumValues(); i++)
            {
                if(pointsArr == null) return;
                else if(y - pointSizes.get(eq) <= pointsArr[x][i] && y + pointSizes.get(eq) >= pointsArr[x][i])
                {
                    if(JOptionPane.showConfirmDialog(null, "Do you want to delete this Equation?", "Select an Option", JOptionPane.YES_NO_OPTION) == 0)
                    {
                        graphs.remove(eq);
                        points.remove(eq);
                        colors.remove(eq);
                        pointSizes.remove(eq);
                        selected.remove(eq);
                        coordinates.remove(eq);
                        removeMouseListener(this);
                        gc.removeMouseMotionListener(hoverListener);
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        GraphFrame.this.repaint();
                        break;
                    }
                }
            }
        }
    }
}