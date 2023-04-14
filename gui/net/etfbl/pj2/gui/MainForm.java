package net.etfbl.pj2.gui;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import net.etfbl.pj2.cards.*;
import net.etfbl.pj2.clock.GameClock;
import net.etfbl.pj2.figures.Figure;
import net.etfbl.pj2.figures.GhostFigure;
import net.etfbl.pj2.main.Program;
import net.etfbl.pj2.player.Player;
import net.etfbl.pj2.player.PlayerScheduler;
import net.etfbl.pj2.utilities.Colors;
import net.etfbl.pj2.utilities.Constants;
import net.etfbl.pj2.utilities.Utility;

public class MainForm{
	
	public static int MATRIX_DIMENSION;
	public static List<Player> PLAYERS;
	public static LinkedList<Card> CARDS;
	public static MatrixBlock[][] MATRIX;
	public final Dealer DEALER;
	public final GameClock GAME_CLOCK;
	public final PlayerScheduler PLAYER_SCHEDULER;
	public final Figure GHOST_FIGURE;
	public static int SIMULATION_SPEED;
	
	public boolean isFinished;
	private static MainForm main;
	private static int numberOfGames;
	private boolean isStopped;
	private long simulationTime;
	private FigureLabel[] figureLabels;
	
	public static MainForm createInstance(int matrixDimension, List<String> playerNames) {
		if(main == null)
			main = new MainForm(matrixDimension, playerNames);
		return main;
	}
	public static MainForm getInstance() throws Exception {
		if(main == null)
			throw new Exception("Main Form must be created and initialized first");
		return main;
	}
	private MainForm(int matrixDimension, List<String> playerNames) {
		super();
		this.isStopped = true;
		this.isFinished = false;
		PLAYERS = new ArrayList<>();
		MATRIX = new MatrixBlock[matrixDimension][matrixDimension];
		MATRIX_DIMENSION = matrixDimension;
		CARDS = new LinkedList<Card>(Utility.createAndShuffleDeckOfCards());
		this.figureLabels = new FigureLabel[Constants.NUMBER_OF_FIGURES*playerNames.size()];
		
		Collections.shuffle(playerNames);
		for(int i = 0; i < playerNames.size(); i++) {
			try {
				List<Figure> figures = Utility.generateRandomFiguresOfColor(Utility.getColorByPosition(i));
				PLAYERS.add(new Player(playerNames.get(i), figures));
			}
			catch(Exception ex) {
				 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
			}
		}
		initComponents();
		frame.setVisible(true);
		
		DEALER = new Dealer(CARDS);
		GAME_CLOCK = new GameClock();
		PLAYER_SCHEDULER = new PlayerScheduler(PLAYERS);
		GHOST_FIGURE = new GhostFigure("ghost", MATRIX_DIMENSION / 2);
	}

	public void runGame() {

		DEALER.setStopped(true);
		GAME_CLOCK.setStopped(true);
		PLAYER_SCHEDULER.setStopped(true);
		GHOST_FIGURE.setStopped(true);
		
		DEALER.start();
		GAME_CLOCK.start();
		PLAYER_SCHEDULER.start();
		GHOST_FIGURE.start();
		
		while(!isFinished) {
			synchronized(this) {
				try {
					wait();
				}catch(InterruptedException ex) {
					 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
				}
			}
		}
		numberOfGames++;
		
		DEALER.setStopped(true);
		GAME_CLOCK.setStopped(true);
		GHOST_FIGURE.setStopped(true);
		
		if(isFinished) {
			this.simulationTime = GAME_CLOCK.getCurrentTimeSeconds();
			Utility.writeSimulationResults(PLAYERS, simulationTime);
			updateTotalNumberOfSimlations();
		}
		return;
	}
	
	private JFrame frame;
	public JLabel cardIconLabel;
	public JLabel timeOfSimulationLabel;
	public JLabel descriptionLabel;
	public JLabel gamesPlayedLabel;
	
	
	static{
		File path = new File(Constants.SIMULATION_FILE_NAME + File.separator);
		if(!path.exists()) {
			path.mkdir();
		}
	}
	private static int getTotalNumberOfSimlations(){
		File path = new File(Constants.SIMULATION_FILE_NAME + File.separator);
		File[] files = path.listFiles();
		int count = 0;
		for(File f: files) {
			if(f.isFile())
				count ++;
		}
		return count;
	}

	public void updateTotalNumberOfSimlations() {
		numberOfGames = getTotalNumberOfSimlations();
		gamesPlayedLabel.setText(numberOfGames + "");
	}
	
	private void initComponents() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 1000, 790);
		frame.getContentPane().setBackground(Colors.BACKGROUND_COLOR.getColor());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		ImageIcon menuIcon = new ImageIcon(Constants.RESOURCE_FILE_NAME + File.separator + "icon.png");
		frame.setIconImage(menuIcon.getImage());
		frame.setTitle("Diamond Circle - Simulation");
		frame.setLocationRelativeTo(null);
		
		ImageIcon background=new ImageIcon(Constants.RESOURCE_FILE_NAME + File.separator + "background1.png");
	    Image image=background.getImage();
	    Image temp=image.getScaledInstance(1000,790,Image.SCALE_SMOOTH);
	    background=new ImageIcon(temp);	    
	    JLabel back=new JLabel(background);
	    back.setLayout(null);
	    back.setBounds(0,0,Constants.GAME_WIDTH,Constants.GAME_HEIGHT);
		frame.getContentPane().add(back);
		
		JPanel upPanel = new JPanel();
		upPanel.setLayout(null);
		upPanel.setBounds(0, 0, 984, 106);
		upPanel.setBackground(Colors.FORTH_COLOR.getColor());
		back.add(upPanel);
		
		JPanel welcomeBackPanel = new JPanel();
		welcomeBackPanel.setLayout(null);
		welcomeBackPanel.setBackground(Colors.FORTH_COLOR.getColor());
		welcomeBackPanel.setBorder(new LineBorder(Color.WHITE));
		welcomeBackPanel.setBounds(293, 23, 403, 52);
		upPanel.add(welcomeBackPanel);
		
		BufferedImage img;
		ImageIcon ico = null;
		try {
			img = ImageIO.read(new File(Constants.RESOURCE_FILE_NAME + File.separator + "white-diamond.png"));
			ico = new ImageIcon(img.getScaledInstance(38, 38, Image.SCALE_SMOOTH));
		} catch (IOException ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
		}
		JLabel iconLbl = new JLabel("");
		iconLbl.setBounds(37, 5, 40, 40);
		welcomeBackPanel.add(iconLbl);
		iconLbl.setBackground(Color.PINK);
		iconLbl.setIcon(ico);
		
		JLabel lblNewLabel = new JLabel("Diamond Circle");
		lblNewLabel.setBounds(79, 0, 252, 53);
		welcomeBackPanel.add(lblNewLabel);
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Georgia", Font.ITALIC, 34));
		
		JLabel iconLbl_1 = new JLabel("");
		iconLbl_1.setBounds(324, 5, 40, 40);
		welcomeBackPanel.add(iconLbl_1);
		iconLbl_1.setBackground(Color.PINK);
		iconLbl_1.setIcon(ico);
		
		JPanel sidePanel = new JPanel();
		sidePanel.setLayout(new GridLayout(0, 4, 0, 0));
		sidePanel.setBorder(new LineBorder(Colors.THIRD_COLOR.getColor()));
		sidePanel.setBounds(10, 117, 964, 44);
		sidePanel.setBackground(Colors.FIRST_COLOR.getColor());
		back.add(sidePanel);
		
		for(int i=0;i<Constants.MAX_NUMBER_OF_PLAYERS;i++){
			if(i<PLAYERS.size())
				sidePanel.add(new PlayerLabel(true, Utility.getColorByPosition(i), PLAYERS.get(i).getPlayerName()));
			else
				sidePanel.add(new PlayerLabel(false, Utility.getColorByPosition(i), "Player"+(i+1)));
		}
		
		JPanel leftPanel = new JPanel();
		leftPanel.setBorder(new LineBorder(Colors.FORTH_COLOR.getColor(), 1, true));
		leftPanel.setBounds(10, 181, 176, 554);
		leftPanel.setBackground(Colors.SECOND_COLOR.getColor());
		back.add(leftPanel);
		leftPanel.setLayout(new GridLayout(PLAYERS.size()*Constants.NUMBER_OF_FIGURES, 1, 5, 5));
		
		for(int i=0;i<PLAYERS.size();i++) {
			for(int j=0;j<Constants.NUMBER_OF_FIGURES;j++) {
				figureLabels[(i*Constants.NUMBER_OF_FIGURES)+j] = new FigureLabel(PLAYERS.get(i).getPlayerName() + "'s " + PLAYERS.get(i).getFigures().get(j).getFigureName());
				leftPanel.add(figureLabels[(i*Constants.NUMBER_OF_FIGURES)+j]);
			}
		}
		for(int i=0;i<figureLabels.length;i++) {		
			figureLabels[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					FigureLabel src = (FigureLabel)e.getSource();
					for(int j=0;j<figureLabels.length;j++) {
						if(src == figureLabels[j]) {
							int playerPos = j / Constants.NUMBER_OF_FIGURES;
							int figurePos = j % Constants.NUMBER_OF_FIGURES;
							FigurePathForm window = new FigurePathForm(PLAYERS.get(playerPos).getFigures().get(figurePos));
							window.show();
							return;
						}
					}
					
				}
			});
			
		}
		JPanel downPanel = new JPanel();
		downPanel.setBorder(new LineBorder(Colors.FORTH_COLOR.getColor(), 1, true));
		downPanel.setLayout(null);
		downPanel.setBounds(196, 606, 571, 129);
		downPanel.setBackground(Colors.FIRST_COLOR.getColor());
		back.add(downPanel);
		
		JLabel lblCardDescription = new JLabel("Card description:");
		lblCardDescription.setRequestFocusEnabled(false);
		lblCardDescription.setHorizontalTextPosition(SwingConstants.CENTER);
		lblCardDescription.setHorizontalAlignment(SwingConstants.CENTER);
		lblCardDescription.setForeground(new Color(37, 101, 174));
		lblCardDescription.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		lblCardDescription.setBounds(200, 11, 171, 24);
		downPanel.add(lblCardDescription);
		
		descriptionLabel = new JLabel("Description");
		descriptionLabel.setToolTipText("Card description");
		descriptionLabel.setVerticalAlignment(SwingConstants.TOP);
		descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		descriptionLabel.setFont(new Font("Century Gothic", Font.ITALIC, 14));
		descriptionLabel.setForeground(Colors.FORTH_COLOR.getColor());
		descriptionLabel.setBounds(27, 35, 517, 83);
		downPanel.add(descriptionLabel);
		
		JPanel edgePanel = new JPanel();
		edgePanel.setBorder(new LineBorder(Colors.FORTH_COLOR.getColor(), 1, true));
		edgePanel.setLayout(null);
		edgePanel.setBounds(777, 606, 197, 129);
		edgePanel.setBackground(Colors.SECOND_COLOR.getColor());
		back.add(edgePanel);
		
		try {
			img = ImageIO.read(new File(Constants.RESOURCE_FILE_NAME + File.separator + "playing-cards.png"));
			ico = new ImageIcon(img.getScaledInstance(50, 45, Image.SCALE_SMOOTH));
		} catch (IOException ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
		}
		
		JButton btnShow = new JButton("Show list of files");
		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameResultsForm results = new GameResultsForm();
				results.show();
			}
		});
		btnShow.setToolTipText("Show list of files with results");
		btnShow.setHorizontalTextPosition(SwingConstants.CENTER);
		btnShow.setDoubleBuffered(true);
		btnShow.setBorderPainted(false);
		btnShow.setForeground(Colors.FIRST_COLOR.getColor());
		btnShow.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		btnShow.setFocusable(false);
		btnShow.setBorder(new LineBorder(Colors.FIRST_COLOR.getColor()));
		btnShow.setBackground(Colors.FORTH_COLOR.getColor());
		btnShow.setBounds(23, 73, 150, 43);
		edgePanel.add(btnShow);
		
		JLabel iconLbl_2 = new JLabel("");
		iconLbl_2.setIcon(ico);
		iconLbl_2.setBackground(Color.PINK);
		iconLbl_2.setBounds(70, 14, 57, 51);
		edgePanel.add(iconLbl_2);
		
		JButton btnStartstop = new JButton("Start / Stop");
		btnStartstop.setToolTipText("Start and stop game simulation");
		btnStartstop.setForeground(Color.WHITE);
		btnStartstop.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		btnStartstop.setFocusable(false);
		btnStartstop.setContentAreaFilled(true);
		btnStartstop.setBorder(new LineBorder(Color.WHITE));
		btnStartstop.setBackground(Colors.FORTH_COLOR.getColor());
		btnStartstop.setBounds(831, 42, 128, 33);
		upPanel.add(btnStartstop);
		
		btnStartstop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!isFinished) {
					if(isStopped) {
						synchronized(PLAYER_SCHEDULER) {
							PLAYER_SCHEDULER.setStopped(false);
							PLAYER_SCHEDULER.notify();
						}
//						synchronized(GAME_CLOCK) {
//							GAME_CLOCK.setStopped(false);
//							GAME_CLOCK.notify();
//						}
						synchronized(GHOST_FIGURE) {
							GHOST_FIGURE.setStopped(false);
							GHOST_FIGURE.notify();
						}
						isStopped = false;
					}
					else {
						PLAYER_SCHEDULER.setStopped(true);
//						GAME_CLOCK.setStopped(true);
						GHOST_FIGURE.setStopped(true);
						isStopped = true;
					}
					
				}
			}
		});
		
		
		JPanel playedGamesPanel = new JPanel();
		playedGamesPanel.setBounds(10, 23, 173, 72);
		playedGamesPanel.setLayout(null);
		playedGamesPanel.setBackground(Colors.FORTH_COLOR.getColor());
		playedGamesPanel.setBorder(new LineBorder(Color.WHITE));
		upPanel.add(playedGamesPanel);
		
		JLabel lblNewLabel_1 = new JLabel("Current number of");
		lblNewLabel_1.setBounds(23, 0, 140, 24);
		playedGamesPanel.add(lblNewLabel_1);
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		
		JLabel lblNewLabel_1_1 = new JLabel("played games:");
		lblNewLabel_1_1.setBounds(33, 18, 116, 24);
		playedGamesPanel.add(lblNewLabel_1_1);
		lblNewLabel_1_1.setForeground(Color.WHITE);
		lblNewLabel_1_1.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		
		gamesPlayedLabel = new JLabel("0");
		updateTotalNumberOfSimlations();
		gamesPlayedLabel.setBounds(80, 37, 48, 24);
		playedGamesPanel.add(gamesPlayedLabel);
		gamesPlayedLabel.setForeground(Color.WHITE);
		gamesPlayedLabel.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		
		JPanel matrixPanel = new JPanel();
		matrixPanel.setBorder(new LineBorder(Colors.FORTH_COLOR.getColor()));
		matrixPanel.setBounds(292, 181, 400, 400);
		matrixPanel.setBackground(Color.WHITE);
		back.add(matrixPanel);
		GridLayout layout = new GridLayout((MATRIX_DIMENSION==0?1:MATRIX_DIMENSION), MATRIX_DIMENSION, 1, 1);
		matrixPanel.setLayout(layout);
		
		for(int i=0;i<MATRIX_DIMENSION;i++) {
			for(int j=0;j<MATRIX_DIMENSION;j++) {
				MatrixBlock block = new MatrixBlock((i*MATRIX_DIMENSION)+(j+1));
				MATRIX[i][j] = block;
				matrixPanel.add(block);
			}
		}
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 104, 984, 6);
		frame.getContentPane().add(panel);
		panel.setBackground(Color.WHITE);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Colors.THIRD_COLOR.getColor());
		panel_1.setBounds(0, 168, 984, 2);
		back.add(panel_1);
		
		JSlider slider = new JSlider(250, 2000, 1000);
		slider.setMinorTickSpacing(50);
		slider.setSnapToTicks(true);
		slider.setValueIsAdjusting(true);
		slider.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		slider.setToolTipText("Control the speed of simulation");
		back.add(slider);
		SIMULATION_SPEED = slider.getValue();
		slider.setMajorTickSpacing(50);
		slider.setMaximum(2000);
		slider.setMinimum(250);
		slider.setDoubleBuffered(true);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				 JSlider source = (JSlider) e.getSource();
				 if(source.getValueIsAdjusting())
					 return;
				 SIMULATION_SPEED = source.getValue();
				 
			}
		});
		slider.setBounds(774, 182, 200, 26);
		slider.setBackground(Colors.BACKGROUND_COLOR.getColor());
		
		JPanel cardPanel = new JPanel();
		back.add(cardPanel);
		cardPanel.setBorder(new LineBorder(Colors.FORTH_COLOR.getColor()));
		cardPanel.setLayout(null);
		cardPanel.setBounds(778, 220, 196, 333);
		cardPanel.setBackground(Colors.FIRST_COLOR.getColor());
		
		JLabel currentCard = new JLabel("Current Card");
		currentCard.setRequestFocusEnabled(false);
		currentCard.setHorizontalTextPosition(SwingConstants.CENTER);
		currentCard.setHorizontalAlignment(SwingConstants.CENTER);
		currentCard.setForeground(Colors.FORTH_COLOR.getColor());
		currentCard.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		currentCard.setBounds(32, 11, 132, 24);
		cardPanel.add(currentCard);
		
		JPanel cardInnerPanel = new JPanel();
		cardInnerPanel.setBackground(Colors.THIRD_COLOR.getColor());
		cardInnerPanel.setBounds(10, 11, 176, 24);
		cardInnerPanel.setLayout(null);
		cardPanel.add(cardInnerPanel);
		
		cardIconLabel = new JLabel("");
		cardIconLabel.setBackground(Color.PINK);
		cardIconLabel.setBounds(-22, 44, 206, 276);
		cardPanel.add(cardIconLabel);
		
		timeOfSimulationLabel = new JLabel("0s");
		back.add(timeOfSimulationLabel);
		timeOfSimulationLabel.setRequestFocusEnabled(false);
		timeOfSimulationLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		timeOfSimulationLabel.setHorizontalAlignment(SwingConstants.LEFT);
		timeOfSimulationLabel.setForeground(new Color(37, 101, 174));
		timeOfSimulationLabel.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		timeOfSimulationLabel.setBounds(930, 554, 44, 24);
		
		JLabel DummyLbl = new JLabel("Time of simulation:");
		back.add(DummyLbl);
		DummyLbl.setRequestFocusEnabled(false);
		DummyLbl.setHorizontalTextPosition(SwingConstants.CENTER);
		DummyLbl.setHorizontalAlignment(SwingConstants.LEFT);
		DummyLbl.setForeground(new Color(37, 101, 174));
		DummyLbl.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		DummyLbl.setBounds(801, 554, 133, 24);
	}
}
