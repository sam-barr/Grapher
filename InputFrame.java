import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
/**
 * Frame for user to add another Equation to the graphing calculator
 * @author Sam Barr
 */
public class InputFrame extends JFrame
{
    private JLabel promptLabel, colorLabel, errorLabel;
    private InputField inputField;
    private JComboBox<String> colorOptions;
    private JButton enterButton;
    private GridBagConstraints gbag;

    /**
     * Constructs an InputFrame object
     * @param GraphFrame graphFrame
     */
    public InputFrame(GraphFrame graphFrame)
    {
        super("Add a Equation");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(450, 140);
        setResizable(false);
        promptLabel = new JLabel("Enter a new Equation:");
        colorLabel = new JLabel("Select a color");
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        inputField = new InputField();
        colorOptions = new JComboBox<String>(GraphFrame.COLOR_OPTIONS);
        enterButton = new JButton("Add Equation");
        gbag = new GridBagConstraints();
        setLayout(new GridBagLayout());
        gbag.gridx = 0;
        gbag.gridy = 0;
        gbag.fill = GridBagConstraints.HORIZONTAL;
        gbag.insets = new Insets(0, 0, 3, 6);
        add(promptLabel, gbag);
        gbag.gridx++;
        add(inputField, gbag);
        gbag.gridx--;
        gbag.gridy++;
        add(colorLabel, gbag);
        gbag.gridx++;
        add(colorOptions, gbag);
        gbag.gridy++;
        gbag.gridx--;
        gbag.gridwidth = 2;
        add(enterButton, gbag);
        gbag.gridy++;
        gbag.gridx--;
        add(errorLabel, gbag);
        final GraphFrame gf = graphFrame;
        enterButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    try
                    {
                        String str = inputField.getText();
                        Equation eq = FunctionUtil.getFunction(str);
                        String color = (String)colorOptions.getSelectedItem();
                        Color c;
                        switch(color)
                        {
                            case "Red": c = Color.RED; break;
                            case "Blue": c = Color.BLUE; break;
                            case "Orange": c = Color.ORANGE; break;
                            case "Green": c = new Color(10, 210, 70); break;
                            case "Black": c = Color.BLACK; break;
                            case "Maroon": c = new Color(150, 5, 20); break;
                            default: c = Color.RED;
                        }
                        InputFrame.this.setVisible(false);
                        errorLabel.setText("");
                        inputField.setText("");
                        gf.addGraph(eq, c);
                    }
                    catch(IllegalArgumentException i){errorLabel.setText("The Equation you entered was not valid");}
                }
            });
        inputField.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    enterButton.doClick();
                }
            });
    }
    /**
     * JTextField used in InputFrame for user to input a new Equation
     * @author Sam Barr
     */
    class InputField extends JTextField //https://github.com/Obicere/ObicereCC/blob/master/src/org/obicere/cc/gui/swing/projects/CodePane.java#L471-L515
    {
        /**
         * Constructs an InputField object
         */
        public InputField()
        {
            super(20);
            InputMap parenMap = getInputMap();
            parenMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_9, KeyEvent.SHIFT_DOWN_MASK, true), "Open Typed");
            parenMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_0, KeyEvent.SHIFT_DOWN_MASK, true), "Close Typed");
            parenMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0, true), "Backspace");
            ActionMap actionMap = getActionMap();
            actionMap.put("Open Typed", new AbstractAction()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        int index = getCaretPosition();
                        String text = getText();
                        setText(text.substring(0, index) + ")" + text.substring(index));
                        setCaretPosition(index);
                    }
                });
            actionMap.put("Close Typed", new AbstractAction()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        int index = getCaretPosition();
                        String text = getText();
                        if(text.length() == index) return;
                        else if(text.charAt(index) == ')') setText(text.substring(0, index));
                    }
                });
            actionMap.put("Backspace", new AbstractAction()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        int index = getCaretPosition();
                        String text = getText();
                        int numOpen = 0;
                        int numClosed = 0;
                        for(char c : text.toCharArray()) 
                        {
                            if(c == '(') numOpen++;
                            else if(c == ')') numClosed++;
                        }
                        if(numClosed > numOpen)
                        {
                            int stop = 0;
                            for(int i = index; i < text.length(); i++)
                            {
                                if(text.charAt(i) == '(')stop++;
                                else if(stop == 0 && text.charAt(i) == ')') setText(text.substring(0, i) + text.substring(i + 1));
                                else if(text.charAt(i) == ')') stop--;
                            }
                            setCaretPosition(index);
                        }
                        else if(numClosed < numOpen)
                        {
                            int stop = 0;
                            for(int i = index - 1; i >= 0; i--)
                            {
                                if(text.charAt(i) == ')')stop++;
                                else if(stop == 0 && text.charAt(i) == '(') setText(text.substring(0, i) + text.substring(i + 1));
                                else if(text.charAt(i) == '(') stop--;
                            }
                            setCaretPosition(index - 1);
                        }
                    }
                });
        }
    }
}