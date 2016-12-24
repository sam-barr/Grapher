import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * Frame for user to change bounds of window
 * @author Sam Barr
 */
public class WindowFrame extends JFrame
{
    private JLabel xMinLabel, xMaxLabel, xScaleLabel, yMinLabel, yMaxLabel, yScaleLabel, errLabel;
    private JTextField xMinText, xMaxText, xScaleText, yMinText, yMaxText, yScaleText;
    private JButton getButton;
    private GridBagConstraints gbag;
    private GraphFrame graphFrame;

    /**
     * Constructs a WindowFrame
     * @param GraphFrame graphFrame
     */
    public WindowFrame(GraphFrame graphFrame)
    {
        super("Settings");
        setSize(190, 220);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setResizable(false);
        this.graphFrame = graphFrame;
        xMinLabel = new JLabel("xMin: ");
        xMaxLabel = new JLabel("xMax: ");
        xScaleLabel = new JLabel("xScale: ");
        yMinLabel = new JLabel("yMin: ");
        yMaxLabel = new JLabel("yMax: ");
        yScaleLabel = new JLabel("yScale: ");
        errLabel = new JLabel();
        errLabel.setForeground(Color.RED);
        ActionListener enterListener = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                getButton.doClick();
            }
        };
        xMinText = new JTextField(10);
        xMinText.setText(Double.toString(graphFrame.getXMin()));
        xMinText.addActionListener(enterListener);
        xMaxText = new JTextField(10);
        xMaxText.setText(Double.toString(graphFrame.getXMax()));
        xMaxText.addActionListener(enterListener);
        xScaleText = new JTextField(10);
        xScaleText.setText(Double.toString(graphFrame.getXScale()));
        xScaleText.addActionListener(enterListener);
        yMinText = new JTextField(10);
        yMinText.setText(Double.toString(graphFrame.getYMin()));
        yMinText.addActionListener(enterListener);
        yMaxText = new JTextField(10);
        yMaxText.setText(Double.toString(graphFrame.getYMax()));
        yMaxText.addActionListener(enterListener);
        yScaleText = new JTextField(10);
        yScaleText.setText(Double.toString(graphFrame.getYScale()));
        yScaleText.addActionListener(enterListener);
        getButton = new JButton("Set Window");
        setLayout(new GridBagLayout());
        gbag = new GridBagConstraints();
        gbag.gridx = 0;
        gbag.gridy = 0;
        gbag.fill = GridBagConstraints.HORIZONTAL;
        gbag.insets = new Insets(0, 0, 3, 0);
        add(xMinLabel, xMinText);
        add(xMaxLabel, xMaxText);
        add(xScaleLabel, xScaleText);
        add(yMinLabel, yMinText);
        add(yMaxLabel, yMaxText);
        add(yScaleLabel, yScaleText);
        gbag.gridwidth = 2;
        gbag.fill = GridBagConstraints.HORIZONTAL;
        add(getButton, gbag);
        gbag.gridy++;
        add(errLabel, gbag);
        final GraphFrame gf = graphFrame;
        getButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    double xMinTemp, xMaxTemp, xScaleTemp, yMinTemp, yMaxTemp, yScaleTemp;
                    try
                    {
                        xMinTemp = Double.parseDouble(xMinText.getText());
                        xMaxTemp = Double.parseDouble(xMaxText.getText());
                        xScaleTemp = Double.parseDouble(xScaleText.getText());
                        yMinTemp = Double.parseDouble(yMinText.getText());
                        yMaxTemp = Double.parseDouble(yMaxText.getText());
                        yScaleTemp = Double.parseDouble(yScaleText.getText());
                    }
                    catch(NumberFormatException i) {errLabel.setText("Enter parameters as numbers"); return;}
                    if(xMinTemp >= xMaxTemp) {errLabel.setText("xMin must be less than xMax"); return;}
                    else if(yMinTemp >= yMaxTemp) {errLabel.setText("yMin must be less than yMax"); return;}
                    else if(yScaleTemp <= 0 || xScaleTemp <= 0) {errLabel.setText("Scale must be greater than zero"); return;}
                    gf.setXMin(xMinTemp);
                    gf.setXMax(xMaxTemp);
                    gf.setXScale(xScaleTemp);
                    gf.setYMin(yMinTemp);
                    gf.setYMax(yMaxTemp);
                    gf.setYScale(yScaleTemp);
                    update();
                    errLabel.setText("");
                    setVisible(false);
                    gf.repaint();
                }
            });
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
     * Updates the text in the WindowPanel to its current values
     */
    public void update()
    {
        xMinText.setText(Double.toString(graphFrame.getXMin()));
        xMaxText.setText(Double.toString(graphFrame.getXMax()));
        xScaleText.setText(Double.toString(graphFrame.getXScale()));
        yMinText.setText(Double.toString(graphFrame.getYMin()));
        yMaxText.setText(Double.toString(graphFrame.getYMax()));
        yScaleText.setText(Double.toString(graphFrame.getYScale()));
    }
}