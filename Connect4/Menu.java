import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.*;

/*The Menu class implements a menu bar for a game window. The game menu will allow
 * the user to start a new game, set game settings, etc, and thus interacts with a board.
 */
@SuppressWarnings("serial")
public class Menu extends JMenuBar implements ActionListener, ListSelectionListener
{
    private JMenuItem neuGame;
    private JMenu numPlayers;
    private JCheckBoxMenuItem onePlayer, twoPlayers;
    private JMenuItem statistics, settings, exit, about, viewHelp;
    private JDialog info, options;
    
    private JPanel themeChooser;
    private JList themeList;
    private JLabel preview;
    private JRadioButton size1, size2, size3, size4;
    private JButton okay, cancel;
    
    private Controller controller;
    private Connect4 frame;
    
    /* Creates a new Menu for a game */
    public Menu(Controller c, Connect4 f) {
        controller = c;
        frame = f;
        
        setupMainMenu();
        setupHelpMenu();
        setupAbout();
        setupOptions();
    }
    
    public void setupMainMenu() {   	
        neuGame = new JMenuItem("New game");
        neuGame.addActionListener(this);
        
        onePlayer = new JCheckBoxMenuItem("One player ", true);
        onePlayer.addActionListener(this);
        
        twoPlayers = new JCheckBoxMenuItem("Two players ", false);
        twoPlayers.addActionListener(this);
        
        numPlayers = new JMenu("Number of players ");
        numPlayers.add(onePlayer);
        numPlayers.add(twoPlayers);
        
        statistics = new JMenuItem("Statistics");
        statistics.addActionListener(this);
        
        settings = new JMenuItem("Settings");
    	settings.addActionListener(this);
        
        exit = new JMenuItem("Exit");
        exit.addActionListener(this);
    	
        JMenu gameOptions = new JMenu("Game");
        gameOptions.add(neuGame);
        gameOptions.addSeparator();
        gameOptions.add(numPlayers);
        gameOptions.addSeparator();
        gameOptions.add(statistics);
        gameOptions.add(settings);
        gameOptions.addSeparator();
        gameOptions.add(exit);
        add(gameOptions);
    }
    
    public void setupHelpMenu() {
        viewHelp = new JMenuItem("View online help");
        viewHelp.addActionListener(this);
        
        about = new JMenuItem("About Connect 4");
        about.addActionListener(this);
        
        JMenu help = new JMenu("Help");
	    help.add(viewHelp);
	    help.addSeparator();
	    help.add(about);
	    add(help);
    }
    
    /*Displays "About TicTacToe" popup*/
    public void setupAbout() {
        info = new JDialog(frame, "About Connect 4", true);
        
        JLabel version = new JLabel("Connect 4");
        version.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel author = new JLabel("Author: Ben-han Sung");
        author.setHorizontalAlignment(JLabel.CENTER);
        
        Container content = info.getContentPane();
        content.setLayout(new GridLayout(4, 1));
        content.add(new JLabel(" "));
        content.add(version);
        content.add(author);
        
        info.setSize(new Dimension(200, 90));
        info.setLocationRelativeTo(frame);
        info.setResizable(false);
    }
    
    public void setupOptions() {
    	options = new JDialog(frame, "Settings", true);
    	options.setSize(new Dimension(300, 380));
    	options.setLocationRelativeTo(frame);
    	options.setResizable(false);
    	
    	//Setup the "theme" JPanel - holds GUI components for choosing board theme
        themeChooser = new JPanel();
        themeChooser.setPreferredSize(new Dimension(283, 200));
        themeChooser.setBorder(BorderFactory.createTitledBorder("Theme"));
        
        //Setup a scrollable list of board themes for the user to choose
        themeList = new JList(new String[]{" Default", " Fluorescence", " Connect 4"});
        themeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        themeList.setSelectedIndex(0);
        themeList.addListSelectionListener(this);
        JScrollPane scroller = new JScrollPane(themeList);
        scroller.setPreferredSize(new Dimension(80, 160));
        
        //Setup a preview of the theme currently selected in the above list
        preview = new JLabel(new ImageIcon(Controller.loader.getResource("themes/default/icon.png")));
        preview.setPreferredSize(new Dimension(160, 160));
        preview.setAlignmentX(CENTER_ALIGNMENT);
        preview.setAlignmentY(CENTER_ALIGNMENT);
        preview.setBorder(BorderFactory.createLineBorder(new Color(152, 152, 152)));
        
        //Add the list and the preview into "theme" JPanel
        themeChooser.add(scroller);
        themeChooser.add(preview);
    	
    	JPanel boardResizer = new JPanel();
        boardResizer.setLayout(new GridLayout(4, 1));
        boardResizer.setPreferredSize(new Dimension(283, 110));
        boardResizer.setBorder(BorderFactory.createTitledBorder("Board size"));
    	
    	size1 = new JRadioButton("7 x 6");
    	size2 = new JRadioButton("8 x 7");
    	size3 = new JRadioButton("9 x 7");
    	size4 = new JRadioButton("10 x 7");
    	
    	ButtonGroup group = new ButtonGroup();
    	size1.setSelected(true);
        group.add(size1);
        group.add(size2);
        group.add(size3);
        group.add(size4);
        
        boardResizer.add(size1);
        boardResizer.add(size2);
        boardResizer.add(size3);
        boardResizer.add(size4);
        
        okay = new JButton("    OK    ");
        okay.addActionListener(this);
        
        cancel = new JButton("  Cancel  ");
        cancel.addActionListener(this);
        
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new FlowLayout(FlowLayout.RIGHT));
        wrapper.add(themeChooser);
        wrapper.add(boardResizer);
        wrapper.add(okay);
        wrapper.add(cancel);
        
        Container content = options.getContentPane();
        content.add(wrapper);
    }
    
    public void updateSettings() {
    	 int width, height;
         if (size1.isSelected()) {
         	width = 7;
         	height = 6;
         }
         else if (size2.isSelected()) {
         	width = 8;
         	height = 7;
         }
         else if (size3.isSelected()) {
         	width = 9;
         	height = 7;
         }
         else {
         	width = 10;
         	height = 7;
         }
         if (controller.getBoard().getBoardWidth() != width || controller.getBoard().getBoardHeight() != height) {
         	controller.getBoard().setDimensions(width, height);
         	frame.pack();
         }
         
         if (themeList.getSelectedIndex() == 0) {
             controller.setTheme("default");
         }
         else if (themeList.getSelectedIndex() == 1) {
             controller.setTheme("fluorescence");
         }
         else if (themeList.getSelectedIndex() == 2) {
             controller.setTheme("connect4");
         }
         controller.getBoard().repaint();
    }
    
    /* Processes menu clicks appropriately */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == neuGame) {
            controller.getBoard().clear();
            controller.reset(false, false);
        }
        else if (e.getSource() == onePlayer) {
            onePlayer.setState(true);
            twoPlayers.setState(false);
            controller.setNumberOfPlayers(1);
        }
        else if (e.getSource() == twoPlayers) {
            twoPlayers.setState(true);
            onePlayer.setState(false);
            controller.setNumberOfPlayers(2);
        }
        else if (e.getSource() == exit) {
            frame.dispose();
        }
        else if (e.getSource() == about) {
            info.setVisible(true);
        }
        else if (e.getSource() == viewHelp) {
        	BrowserControl.openUrl("http://en.wikipedia.org/wiki/Connect_Four");
        }
        else if (e.getSource() == settings) {
            options.setVisible(true);
        }
        else if (e.getSource() == statistics) {
        	controller.displayStatistics();
        }
        else if (e.getSource() == okay) {
        	updateSettings();
            options.setVisible(false);
        }
        else if (e.getSource() == cancel) {
            options.setVisible(false);
        }
    }
    
    public void valueChanged(ListSelectionEvent e) {
        //If anything is selected in the list of board themes in the "Appearance" tab of the "Settings" JDialog...
        String icon_path;
        if (themeList.getSelectedIndex() == 0) 
        	icon_path = "themes/default/icon.png";
        else if (themeList.getSelectedIndex() == 1) 
        	icon_path = "themes/fluorescence/icon.png";
        else 
        	icon_path = "themes/connect4/icon.png";
        ImageIcon theme = new ImageIcon(Controller.loader.getResource(icon_path));
        
        //Update the theme preview in the "Appearance" tab
        preview.setIcon(theme);
        themeChooser.revalidate();
        cancel.setEnabled(true);
    }
}