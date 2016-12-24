import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
/**
 * Class to make using a JMenuBar less annoying
 * @author Sam Barr
 */
public class MenuBar extends JMenuBar
{
    /**
     * Constructs a MenuBar
     */
    public MenuBar()
    {
        super();
    }

    /**
     * Adds a JMenu to the MenuBar
     * @param String menuName       Title of JMenu
     * @param String[] itemNames        Titles of JMenuItems
     * @param ActionListener l      ActionListeners for the JMenuItems
     */
    public void add(String menuName, String[] itemNames, ActionListener... l)
    {
        JMenu menu =  new JMenu(menuName);
        for(int i = 0; i < itemNames.length; i++)
        {
            JMenuItem temp = menu.add(new JMenuItem(itemNames[i]));
            if(i < l.length)temp.addActionListener(l[i]);
        }
        add(menu);
    }

    /**
     * Returns a JMenu with specified items
     * @param String menuName       Title of JMenu
     * @param String[] itemNames        Titles of JMenuItems
     * @param ActionListener l      ActionListeners for the JMenuItems
     */
    public static JMenu makeMenu(String menuName, String[] itemNames, ActionListener... l)
    {
        JMenu stuff = new JMenu(menuName);
        for(int i = 0; i < itemNames.length; i++)
        {
            JMenuItem temp = stuff.add(new JMenuItem(itemNames[i]));
            if(i < l.length) temp.addActionListener(l[i]);
        }
        return stuff;
    }
}