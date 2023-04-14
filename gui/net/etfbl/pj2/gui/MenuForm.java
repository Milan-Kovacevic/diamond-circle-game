package net.etfbl.pj2.gui;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import javax.swing.border.LineBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import java.awt.Cursor;
import java.awt.Insets;
import javax.swing.JRadioButton;
import java.awt.image.BufferedImage;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import net.etfbl.pj2.main.Program;
import net.etfbl.pj2.utilities.Colors;



public class MenuForm {

	private int matrixDimension;
	private List<String> playerNames;
	private Boolean fieldsInitialized;

	public MenuForm() {
		super();
		this.playerNames = new ArrayList<String>();
		this.matrixDimension = 0;
		this.fieldsInitialized = false;	
		
		initComponents();
		this.frame.setVisible(true);
	}
		
	public synchronized Boolean isFieldsInitialized() {
		return fieldsInitialized;
	}
	public int getMatrixDimension() {
		return matrixDimension;
	}
	public List<String> getPlayerNames(){
		return playerNames;
	}
	public void close() {
		frame.dispose();
		frame.setVisible(false); 
	}
	
	private static final int FIRST_COLOR = 0x3C99DC;
	private static final int SECOND_COLOR = 0x2565AE;
	private static final int THIRD_COLOR = 0x0F5298;

	private JFrame frame;
	private JTextField firstPlayerTxtField;
	private JTextField secondPlayerTxtField;
	private JTextField thirdPlayerTxtField;
	private JTextField forthPlayerTxtField;
	private JTextField txtEnterDimensionOf;
	private JTextField textField;
	
	
	private void initComponents() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 710);
		frame.getContentPane().setBackground(Colors.BACKGROUND_COLOR.getColor());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		ImageIcon menuIcon = new ImageIcon("res" + File.separator + "icon.png");
		frame.setIconImage(menuIcon.getImage());
		frame.setTitle("Diamond Circle - Options");
		frame.setLocationRelativeTo(null);
		
		BufferedImage img;
		ImageIcon ico = null;
		try {
			img = ImageIO.read(new File("res" + File.separator + "diamond.png"));
			ico = new ImageIcon(img.getScaledInstance(128, 100, Image.SCALE_FAST));
		} catch (IOException ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
		}
		JLabel iconLbl = new JLabel("");
		iconLbl.setIcon(ico);
		iconLbl.setBounds(153, 2, 128, 128);
		frame.getContentPane().add(iconLbl);
		
		
		JLabel welcomeLbl1 = new JLabel("Welcome to Diamond Circle");
		welcomeLbl1.setFont(new Font("Georgia", Font.PLAIN, 24));
		welcomeLbl1.setForeground(new Color(THIRD_COLOR));
		welcomeLbl1.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeLbl1.setBounds(10, 116, 414, 39);
		frame.getContentPane().add(welcomeLbl1);
		
		JLabel welcomeLbl2 = new JLabel("game simulation");
		welcomeLbl2.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeLbl2.setForeground(new Color(THIRD_COLOR));
		welcomeLbl2.setFont(new Font("Georgia", Font.PLAIN, 24));
		welcomeLbl2.setBounds(85, 148, 263, 28);
		frame.getContentPane().add(welcomeLbl2);
		
		JPanel panel = new JPanel();
		panel.setFocusable(false);
		panel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		panel.setLayout(null);
		panel.setBorder(new LineBorder(new Color(37, 101, 174), 1, true));
		panel.setBackground(Color.WHITE);
		panel.setBounds(21, 193, 391, 456);
		frame.getContentPane().add(panel);
		
		JLabel lblNewLabel = new JLabel("Select number of players and enter player names");
		lblNewLabel.setToolTipText("");
		lblNewLabel.setSize(346, 30);
		lblNewLabel.setLocation(22, 68);
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		lblNewLabel.setForeground(new Color(SECOND_COLOR));
		
		JButton acceptBtn = new JButton("Accept options and start simulation");
		acceptBtn.setBorder(new LineBorder(new Color(THIRD_COLOR)));
		acceptBtn.setBackground(Colors.BACKGROUND_COLOR.getColor());
		acceptBtn.setForeground(new Color(THIRD_COLOR));
		acceptBtn.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		acceptBtn.setBounds(22, 400, 346, 38);
		acceptBtn.setContentAreaFilled(true);
		panel.add(acceptBtn);
		
		JRadioButton twoPlayersBtn = new JRadioButton("Two players");
		twoPlayersBtn.setBorder(UIManager.getBorder("RadioButton.border"));
		twoPlayersBtn.setContentAreaFilled(false);
		twoPlayersBtn.setSelected(true);
		twoPlayersBtn.setFont(new Font("Century Gothic", Font.PLAIN, 12));
		twoPlayersBtn.setForeground(new Color(SECOND_COLOR));
		twoPlayersBtn.setBounds(20, 101, 110, 23);
		panel.add(twoPlayersBtn);
		
		JRadioButton threePlayersBtn = new JRadioButton("Three players");
		threePlayersBtn.setForeground(new Color(37, 101, 174));
		threePlayersBtn.setFont(new Font("Century Gothic", Font.PLAIN, 12));
		threePlayersBtn.setContentAreaFilled(false);
		threePlayersBtn.setBorder(UIManager.getBorder("RadioButton.border"));
		threePlayersBtn.setBounds(140, 101, 110, 23);
		panel.add(threePlayersBtn);
		
		JRadioButton fourPlayersBtn = new JRadioButton("Four players");
		fourPlayersBtn.setForeground(new Color(37, 101, 174));
		fourPlayersBtn.setFont(new Font("Century Gothic", Font.PLAIN, 12));
		fourPlayersBtn.setContentAreaFilled(false);
		fourPlayersBtn.setBorder(UIManager.getBorder("RadioButton.border"));
		fourPlayersBtn.setBounds(264, 101, 102, 23);
		panel.add(fourPlayersBtn);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(fourPlayersBtn);
		buttonGroup.add(threePlayersBtn);
		buttonGroup.add(twoPlayersBtn);
		
		JPanel playerPanel = new JPanel();
		playerPanel.setBorder(new LineBorder(new Color(FIRST_COLOR), 1, true));
		playerPanel.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		playerPanel.setBounds(22, 131, 346, 252);
		playerPanel.setLayout(null);
		playerPanel.setBackground(Color.WHITE);
		
		panel.add(playerPanel);
		
		JLabel firstPlayerNameLbl = new JLabel("Enter 1# player name:");
		firstPlayerNameLbl.setHorizontalAlignment(SwingConstants.LEFT);
		firstPlayerNameLbl.setForeground(new Color(FIRST_COLOR));
		firstPlayerNameLbl.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		firstPlayerNameLbl.setBounds(23, 11, 182, 25);
		playerPanel.add(firstPlayerNameLbl);
		
		firstPlayerTxtField = new JTextField();
		firstPlayerTxtField.requestFocusInWindow();
		firstPlayerTxtField.setMargin(new Insets(2, 6, 2, 2));
		firstPlayerTxtField.setHorizontalAlignment(SwingConstants.LEFT);
		firstPlayerTxtField.setForeground(new Color(15, 82, 152));
		firstPlayerTxtField.setFont(new Font("Century Gothic", Font.ITALIC, 14));
		firstPlayerTxtField.setDoubleBuffered(true);
		firstPlayerTxtField.setColumns(10);
		firstPlayerTxtField.setBounds(23, 34, 300, 25);
		firstPlayerTxtField.grabFocus();	 
		playerPanel.add(firstPlayerTxtField);
		
		JLabel secondPlayerNameLbl = new JLabel("Enter 2# player name:");
		secondPlayerNameLbl.setHorizontalAlignment(SwingConstants.LEFT);
		secondPlayerNameLbl.setForeground(new Color(60, 153, 220));
		secondPlayerNameLbl.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		secondPlayerNameLbl.setBounds(23, 67, 182, 25);
		playerPanel.add(secondPlayerNameLbl);
		
		secondPlayerTxtField = new JTextField();
		secondPlayerTxtField.setMargin(new Insets(2, 6, 2, 2));
		secondPlayerTxtField.setHorizontalAlignment(SwingConstants.LEFT);
		secondPlayerTxtField.setForeground(new Color(15, 82, 152));
		secondPlayerTxtField.setFont(new Font("Century Gothic", Font.ITALIC, 14));
		secondPlayerTxtField.setDoubleBuffered(true);
		secondPlayerTxtField.setColumns(10);
		secondPlayerTxtField.setBounds(23, 93, 300, 25);
		playerPanel.add(secondPlayerTxtField);
		
		JLabel thirdPlayerNameLbl = new JLabel("Enter 3# player name:");
		thirdPlayerNameLbl.setVisible(false);
		thirdPlayerNameLbl.setHorizontalAlignment(SwingConstants.LEFT);
		thirdPlayerNameLbl.setForeground(new Color(60, 153, 220));
		thirdPlayerNameLbl.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		thirdPlayerNameLbl.setBounds(23, 123, 182, 25);
		playerPanel.add(thirdPlayerNameLbl);
		
		thirdPlayerTxtField = new JTextField();
		thirdPlayerTxtField.setVisible(false);
		thirdPlayerTxtField.setMargin(new Insets(2, 6, 2, 2));
		thirdPlayerTxtField.setHorizontalAlignment(SwingConstants.LEFT);
		thirdPlayerTxtField.setForeground(new Color(15, 82, 152));
		thirdPlayerTxtField.setFont(new Font("Century Gothic", Font.ITALIC, 14));
		thirdPlayerTxtField.setDoubleBuffered(true);
		thirdPlayerTxtField.setColumns(10);
		thirdPlayerTxtField.setBounds(23, 148, 300, 25);
		playerPanel.add(thirdPlayerTxtField);
		
		JLabel forthPlayerNameLbl = new JLabel("Enter 4# player name:");
		forthPlayerNameLbl.setVisible(false);
		forthPlayerNameLbl.setHorizontalAlignment(SwingConstants.LEFT);
		forthPlayerNameLbl.setForeground(new Color(60, 153, 220));
		forthPlayerNameLbl.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		forthPlayerNameLbl.setBounds(23, 181, 182, 25);
		playerPanel.add(forthPlayerNameLbl);
		
		forthPlayerTxtField = new JTextField();
		forthPlayerTxtField.setVisible(false);
		forthPlayerTxtField.setMargin(new Insets(2, 6, 2, 2));
		forthPlayerTxtField.setHorizontalAlignment(SwingConstants.LEFT);
		forthPlayerTxtField.setForeground(new Color(15, 82, 152));
		forthPlayerTxtField.setFont(new Font("Century Gothic", Font.ITALIC, 14));
		forthPlayerTxtField.setDoubleBuffered(true);
		forthPlayerTxtField.setColumns(10);
		forthPlayerTxtField.setBounds(23, 204, 300, 25);
		playerPanel.add(forthPlayerTxtField);
		
		txtEnterDimensionOf = new JTextField();
		txtEnterDimensionOf.setToolTipText("Dimension goes in range [7-10]");
		txtEnterDimensionOf.setText("Enter dimension of game field...");
		txtEnterDimensionOf.setMargin(new Insets(2, 6, 2, 2));
		txtEnterDimensionOf.setHorizontalAlignment(SwingConstants.LEFT);
		txtEnterDimensionOf.setDoubleBuffered(true);
		txtEnterDimensionOf.setFont(new Font("Century Gothic", Font.ITALIC, 15));
		txtEnterDimensionOf.setForeground(new Color(THIRD_COLOR));
		txtEnterDimensionOf.setBounds(22, 23, 346, 30);
		txtEnterDimensionOf.setColumns(10);
		panel.add(txtEnterDimensionOf);
		
		textField = new JTextField();
		textField.setBounds(20, 51, 0, 0);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		txtEnterDimensionOf.addFocusListener(new FocusListener() {
			@Override
		    public void focusGained(FocusEvent e) {
		        if (txtEnterDimensionOf.getText().equals("Enter dimension of game field...")) {
		        	txtEnterDimensionOf.setText("");
		        	txtEnterDimensionOf.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e) {
		        if (txtEnterDimensionOf.getText().isEmpty()) {
		        	txtEnterDimensionOf.setText("Enter dimension of game field...");
		        	txtEnterDimensionOf.setFont(new Font("Century Gothic", Font.ITALIC, 15));
		        }
		    }
		});
		twoPlayersBtn.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(twoPlayersBtn.isSelected()) {
					thirdPlayerTxtField.setVisible(false);
					thirdPlayerNameLbl.setVisible(false);
					forthPlayerTxtField.setVisible(false);
					forthPlayerNameLbl.setVisible(false);
				}
				
			}
		});
		threePlayersBtn.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(threePlayersBtn.isSelected()) {
					thirdPlayerTxtField.setVisible(true);
					thirdPlayerNameLbl.setVisible(true);
					forthPlayerTxtField.setVisible(false);
					forthPlayerNameLbl.setVisible(false);
				}
			}
		});
		fourPlayersBtn.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(fourPlayersBtn.isSelected()) {
					thirdPlayerTxtField.setVisible(true);
					thirdPlayerNameLbl.setVisible(true);
					forthPlayerTxtField.setVisible(true);
					forthPlayerNameLbl.setVisible(true);
				}
			}
		});
		acceptBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = txtEnterDimensionOf.getText();
				try {
					if("Enter dimension of game field...".equals(input)) {
						JOptionPane.showMessageDialog(null, "Please click on the text box 'Enter dimension of game field...' and enter a game dimension field", 
								"Missing game field dimension", JOptionPane.ERROR_MESSAGE);
						return;
					}
					else if(Integer.parseInt(input) < 7 || Integer.parseInt(input)  > 10 ) {
						JOptionPane.showMessageDialog(null, "Enter a valid game field dimension!" + System.lineSeparator() + "Game field length is a positive integer number in range [7-10]", 
								"Invalid game dimension", JOptionPane.ERROR_MESSAGE);
						return;
					}
					else {
						matrixDimension = Integer.parseInt(input);
					}
					playerNames.clear();
					if(twoPlayersBtn.isSelected()) {
						if(firstPlayerTxtField.getText().isEmpty() || secondPlayerTxtField.getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "Please enter first and second player name before starting simulation", "Player names missing", JOptionPane.ERROR_MESSAGE);
							return;
						}
						playerNames.addAll(Arrays.asList(firstPlayerTxtField.getText(), secondPlayerTxtField.getText()));
					}
					else if(threePlayersBtn.isSelected()) {
						if(firstPlayerTxtField.getText().isEmpty() || secondPlayerTxtField.getText().isEmpty() || thirdPlayerTxtField.getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "Please enter first, second and third player name before starting simulation", "Player names missing", JOptionPane.ERROR_MESSAGE);
							return;
						}
						playerNames.addAll(Arrays.asList(firstPlayerTxtField.getText(), secondPlayerTxtField.getText(), thirdPlayerTxtField.getText()));
					}
					else if(fourPlayersBtn.isSelected()) {
						if(firstPlayerTxtField.getText().isEmpty() || secondPlayerTxtField.getText().isEmpty() || thirdPlayerTxtField.getText().isEmpty() || forthPlayerTxtField.getText().isEmpty() ) {
							JOptionPane.showMessageDialog(null, "Please enter name for all players before starting simulation", "Player names missing", JOptionPane.ERROR_MESSAGE);
							return;
						}
						playerNames.addAll(Arrays.asList(firstPlayerTxtField.getText(), secondPlayerTxtField.getText(), thirdPlayerTxtField.getText(), forthPlayerTxtField.getText()));
					}
					if(!(playerNames.stream().distinct().count() == playerNames.size())) {
						JOptionPane.showMessageDialog(null, "Player names must be different", "Player names matching", JOptionPane.ERROR_MESSAGE);
						return;
					}
					else {
						fieldsInitialized = true;
						close();
					}
				}
				catch(Exception ex) {
					 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
				}
				
				
			}
		});
		
	}
}
