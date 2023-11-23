package net.etfbl.pj2.gui;

import javax.swing.JFrame;
import java.awt.Color;
import java.io.File;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import net.etfbl.pj2.results.SimulationResult;
import net.etfbl.pj2.utilities.Colors;
import net.etfbl.pj2.utilities.Constants;

public class ShowResultForm {

	private JFrame frame;
	private String fileName;
	
	public ShowResultForm(String fileName) {
		this.fileName = fileName;
		initComponents();
	}
	public void show() {
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initComponents() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 1000, 350);
		frame.getContentPane().setBackground(Colors.BACKGROUND_COLOR.getColor());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		ImageIcon menuIcon = new ImageIcon(Constants.RESOURCE_FILE_NAME + File.separator + "icon.png");
		frame.setIconImage(menuIcon.getImage());
		frame.setTitle("Diamond Circle - Result");
		frame.setLocationRelativeTo(null);
		
		JLabel lblListOfSimulation = new JLabel("Simulation result:");
		lblListOfSimulation.setRequestFocusEnabled(false);
		lblListOfSimulation.setHorizontalTextPosition(SwingConstants.CENTER);
		lblListOfSimulation.setHorizontalAlignment(SwingConstants.CENTER);
		lblListOfSimulation.setForeground(new Color(37, 101, 174));
		lblListOfSimulation.setFont(new Font("Georgia", Font.ITALIC, 28));
		lblListOfSimulation.setBounds(275, 11, 434, 33);
		frame.getContentPane().add(lblListOfSimulation);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setForeground(Colors.FORTH_COLOR.getColor());
		tabbedPane.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		tabbedPane.setBounds(30, 43, 923, 212);
		frame.getContentPane().add(tabbedPane);
		
		SimulationResult res = SimulationResult.parseResult(fileName);
		for(int i=0;i<res.getPlayersNames().size();i++) {
			JPanel player = new JPanel();
			player.setFont(new Font("Century Gothic", Font.PLAIN, 18));
			player.setBackground(Colors.WHITE.getColor());
			tabbedPane.addTab(res.getPlayersNames().get(i), null, player, null);
			JTextPane textPane1 = new JTextPane();
			textPane1.setFont(new Font("Century Gothic", Font.PLAIN, 15));
			textPane1.setForeground(Colors.FORTH_COLOR.getColor());
			textPane1.setBackground(Colors.WHITE.getColor());
			textPane1.setText(res.getFiguresDescription().get(i));
			player.add(textPane1);
		}
		
		JLabel lblTotalSimulationTime = new JLabel(res.getSimulationTime());
		lblTotalSimulationTime.setRequestFocusEnabled(false);
		lblTotalSimulationTime.setHorizontalTextPosition(SwingConstants.LEFT);
		lblTotalSimulationTime.setHorizontalAlignment(SwingConstants.LEFT);
		lblTotalSimulationTime.setForeground(new Color(37, 101, 174));
		lblTotalSimulationTime.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		lblTotalSimulationTime.setBounds(30, 266, 414, 24);
		frame.getContentPane().add(lblTotalSimulationTime);
		
	}
}
