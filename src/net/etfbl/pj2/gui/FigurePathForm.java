package net.etfbl.pj2.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import net.etfbl.pj2.figures.Figure;
import net.etfbl.pj2.main.Program;
import net.etfbl.pj2.utilities.Colors;
import net.etfbl.pj2.utilities.Constants;
import net.etfbl.pj2.utilities.Utility;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

public class FigurePathForm{
	private JFrame frame;
	private MatrixBlock[][] matrix;
	private Figure figure;
	
	public FigurePathForm(Figure figure) {
		this.figure = figure;
		this.matrix = new MatrixBlock[MainForm.MATRIX_DIMENSION][MainForm.MATRIX_DIMENSION];
		initComponenets();
	}
	
	public void show() {
		frame.setVisible(true);
	}
	private void initComponenets() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 500, 500);
		frame.getContentPane().setBackground(Colors.BACKGROUND_COLOR.getColor());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		ImageIcon menuIcon = new ImageIcon(Constants.RESOURCE_FILE_NAME + File.separator + "icon.png");
		frame.setIconImage(menuIcon.getImage());
		frame.setTitle("Diamond Circle - Figure Travel Path");
		frame.setLocationRelativeTo(null);
		
		JPanel matrixPanel = new JPanel();
		matrixPanel.setBorder(new LineBorder(Colors.FORTH_COLOR.getColor()));
		matrixPanel.setBounds(43, 38, 400, 400);
		matrixPanel.setBackground(Color.WHITE);
		frame.getContentPane().add(matrixPanel);
		GridLayout layout = new GridLayout((MainForm.MATRIX_DIMENSION==0?1:MainForm.MATRIX_DIMENSION), MainForm.MATRIX_DIMENSION, 1, 1);
		matrixPanel.setLayout(layout);
		
		JLabel dummyLbl = new JLabel("Traveled Time:");
		dummyLbl.setRequestFocusEnabled(false);
		dummyLbl.setHorizontalTextPosition(SwingConstants.CENTER);
		dummyLbl.setHorizontalAlignment(SwingConstants.CENTER);
		dummyLbl.setForeground(new Color(37, 101, 174));
		dummyLbl.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		dummyLbl.setBounds(142, 11, 171, 24);
		frame.getContentPane().add(dummyLbl);
		
		JLabel traveledTimeLabel = new JLabel(figure.getTraveledTime()+"s");
		traveledTimeLabel.setRequestFocusEnabled(false);
		traveledTimeLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		traveledTimeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		traveledTimeLabel.setForeground(new Color(37, 101, 174));
		traveledTimeLabel.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		traveledTimeLabel.setBounds(298, 11, 66, 24);
		frame.getContentPane().add(traveledTimeLabel);
		
		for(int i=0;i<MainForm.MATRIX_DIMENSION;i++) {
			for(int j=0;j<MainForm.MATRIX_DIMENSION;j++) {
				MatrixBlock block = new MatrixBlock((i*MainForm.MATRIX_DIMENSION)+(j+1));
				matrix[i][j] = block;
				matrixPanel.add(block);
			}
		}
		List<Integer> traveledPath = new ArrayList<>(figure.getTraveledPath());
		if(traveledPath.size() <= 1)
			return;
		int lastElement = traveledPath.get(traveledPath.size()-1);
		int counter = traveledPath.get(0);
		int droppedAt = figure.getDroppedAtPosition();
		if(droppedAt != -1) {
			lastElement = droppedAt;
		}
		matrix[counter/MainForm.MATRIX_DIMENSION][counter % MainForm.MATRIX_DIMENSION].setBackground(figure.getColor().getColor());
		while(counter!=lastElement) {
			try {
				counter = Utility.calculateNextPositionInMatrix(counter);
			} catch (Exception ex) {
				Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
				break;
			}
			if(counter!=-1)
				matrix[counter/MainForm.MATRIX_DIMENSION][counter % MainForm.MATRIX_DIMENSION].setBackground(figure.getColor().getColor());
		}
		
		if(droppedAt != -1) {
			matrix[droppedAt/MainForm.MATRIX_DIMENSION][droppedAt % MainForm.MATRIX_DIMENSION].addHole();
		}
	}
}
