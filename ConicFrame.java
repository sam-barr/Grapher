import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * @author Sam Barr
 */
public class ConicFrame extends JFrame
{
    public static final ImageIcon CIRCLE = new ImageIcon("CIRCLE.png");
    public static final ImageIcon ELLIPSE = new ImageIcon("ELLIPSE.png");
    public static final ImageIcon HYPERBOLA_HORIZONTAL = new ImageIcon("HYPERBOLA_HORIZONTAL.png");
    public static final ImageIcon HYPERBOLA_VERTICAL = new ImageIcon("HYPERBOLA_VERTICAL.png");
    public static final ImageIcon PARABOLA_HORIZONTAL = new ImageIcon("PARABOLA_HORIZONTAL.png");
    public static final ImageIcon PARABOLA_VERTICAL = new ImageIcon("PARABOLA_VERTICAL.png");
    private GridBagConstraints gbag;
    private JComboBox<String> colorOptions;

    /**
     * Creates a ConicFrame object
     * @param ImageIcon img
     * @param GraphFrame gf
     */
    public ConicFrame(ImageIcon image, GraphFrame graphFrame)
    {
        super();
        setVisible(true);
        setResizable(false);
        setLayout(new GridBagLayout());
        gbag = new GridBagConstraints();
        gbag.gridwidth = 2;
        add(new JLabel(image), gbag);
        gbag.gridwidth = 1;
        gbag.gridx = 0;
        gbag.gridy = 2;
        gbag.insets = new Insets(0, 0, 3, 0);
        final JLabel rLabel = new JLabel("r = ");
        final JTextField rField = new JTextField(10);
        add(rLabel, rField);
        final JLabel aLabel = new JLabel("a = ");
        final JTextField aField = new JTextField(10);
        add(aLabel, aField);
        final JLabel bLabel = new JLabel("b = ");
        final JTextField bField = new JTextField(10);
        add(bLabel, bField);
        final JLabel hLabel = new JLabel("h = ");
        final JTextField hField = new JTextField(10);
        add(hLabel, hField);
        final JLabel kLabel = new JLabel("k = ");
        final JTextField kField = new JTextField(10);
        add(kLabel, kField);
        add(new JLabel("Color: "), gbag);
        gbag.gridx++;
        colorOptions = new JComboBox<String>(GraphFrame.COLOR_OPTIONS);
        colorOptions.setPreferredSize(kField.getPreferredSize());
        add(colorOptions, gbag);
        gbag.gridx--;
        gbag.gridy++;
        gbag.gridwidth = 2;
        gbag.fill = GridBagConstraints.HORIZONTAL;
        JButton enterButton = new JButton();
        add(enterButton, gbag);
        final GraphFrame gf = graphFrame;
        final ImageIcon img = image;
        if(img.equals(CIRCLE))
        {
            remove(aLabel);
            remove(aField);
            remove(bLabel);
            remove(bField);
            setTitle("Add a Circle");
            setSize(250, 250);
            enterButton.setText("Add Circle");
            enterButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        try
                        {
                            double r = Double.parseDouble(rField.getText());
                            double h = Double.parseDouble(hField.getText());
                            double k = Double.parseDouble(kField.getText());
                            gf.addGraph(new Circle(r, h, k), getColor());
                            dispose();
                        }
                        catch(NumberFormatException i){}
                    }
                });
        }
        else if(img.equals(ELLIPSE) || img.equals(HYPERBOLA_HORIZONTAL) || img.equals(HYPERBOLA_VERTICAL))
        {
            remove(rLabel);
            remove(rField);
            if(img.equals(ELLIPSE)) setTitle("Add an Ellipse");
            else  setTitle("Add a Hyperbola");
            setSize(250, 280);
            if(img.equals(ELLIPSE)) enterButton.setText("Add Ellipse");
            else enterButton.setText("Add a Hyperbola");
            enterButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        try
                        {
                            double a = Double.parseDouble(aField.getText());
                            double b = Double.parseDouble(bField.getText());
                            double h = Double.parseDouble(hField.getText());
                            double k = Double.parseDouble(kField.getText());
                            Conic c;
                            if(img.equals(ELLIPSE)) c = new Ellipse(a, b, h, k);
                            else if(img.equals(HYPERBOLA_HORIZONTAL)) c = new Hyperbola(a, b, h, k, Hyperbola.HORIZONTAL);
                            else c = new Hyperbola(a, b, h, k, Hyperbola.VERTICAL);
                            gf.addGraph(c, getColor());
                            dispose();
                        }
                        catch(NumberFormatException i){}
                    }
                });
        }
        else if(img.equals(PARABOLA_HORIZONTAL) || img.equals(PARABOLA_VERTICAL))
        {
            remove(rLabel);
            remove(rField);
            remove(bLabel);
            remove(bField);
            setTitle("Add a Parabola");
            setSize(250, 260);
            enterButton.setText("Add Parabola");
            enterButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        try
                        {
                            double a = Double.parseDouble(aField.getText());
                            double h = Double.parseDouble(hField.getText());
                            double k = Double.parseDouble(kField.getText());
                            Conic c;
                            if(img.equals(PARABOLA_HORIZONTAL)) c = new Parabola(a, h, k, Parabola.HORIZONTAL);
                            else c = new Parabola(a, h, k, Parabola.VERTICAL);
                            gf.addGraph(c, getColor());
                            dispose();
                        }
                        catch(NumberFormatException i){}
                    }
                });
        }
        else throw new IllegalArgumentException();
    }

    /**
     * Convience method for adding a JLabel followed by a JTextArea
     * @param JLabel label
     * @param JTextArea text
     */
    public void add(JLabel label, JTextField text)
    {
        add(label, gbag);
        gbag.gridx++;
        add(text, gbag);
        gbag.gridx--;
        gbag.gridy++;
    }

    /**
     * Returns the color currently displayed by the JComboBox
     * @return Color
     */
    public Color getColor()
    {
        String color = (String) colorOptions.getSelectedItem();
        switch(color)
        {
            case "Red": return Color.RED;
            case "Blue": return Color.BLUE;
            case "Orange": return Color.ORANGE;
            case "Green": return new Color(10, 210, 70);
            case "Black": return Color.BLACK;
            case "Maroon": return new Color(150, 5, 20);
            default: return Color.RED;
        }
    }
}