package net.etfbl.pj2.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

import net.etfbl.pj2.figures.*;
import net.etfbl.pj2.main.Program;
import net.etfbl.pj2.utilities.Colors;
import net.etfbl.pj2.utilities.Constants;

public class MatrixBlock extends JLabel {
	private static final long serialVersionUID = 1L;
	private int number;
	Figure figure;
	private int numberOfDiamonds;
	private boolean hasHole;
	private boolean hasGhost;
	
	public synchronized int getNumberOfDiamonds() {
		return numberOfDiamonds;
	}
	public synchronized void setNumberOfDiamonds(int numberOfDiamonds) {
		this.numberOfDiamonds = numberOfDiamonds;
	}
	public synchronized boolean isHasHole() {
		return hasHole;
	}
	public synchronized void setHasHole(boolean hasHole) {
		this.hasHole = hasHole;
	}
	public synchronized Figure getFigure() {
		return figure;
	}
	public synchronized void setFigure(Figure figure) {
		this.figure = figure;
	}
	public synchronized boolean isHasGhost() {
		return hasGhost;
	}
	public synchronized void setHasGhost(boolean hasGhost) {
		this.hasGhost = hasGhost;
	}
	public synchronized void paint(Color backcolor, Color forecolor) {
		this.setBackground(backcolor);
		this.setForeground(forecolor);
	}
	
	public synchronized void paintDefault() {
		this.setBackground(Colors.BACKGROUND_COLOR.getColor());
		this.setForeground(Colors.GRAY.getColor());
	}
	
	public synchronized void paintFigure() {
		ImageIcon ico = null;
		try {
			if(figure != null) {
				BufferedImage img = null;
				this.setBackground(figure.getColor().getColor());
				if(figure instanceof SuperfastFigure) {
					img = ImageIO.read(new File(Constants.RESOURCE_FILE_NAME + File.separator + "superfast-figure.png"));
				}	
				if(figure instanceof FlyingFigure) {
					img = ImageIO.read(new File(Constants.RESOURCE_FILE_NAME + File.separator + "flying-figure.png"));
				}
				if(figure instanceof RegularFigure) {
					img = ImageIO.read(new File(Constants.RESOURCE_FILE_NAME + File.separator + "regular-figure.png"));
				}
				
				if(img != null)
					ico = new ImageIcon(img.getScaledInstance(Constants.MATRIX_WIDTH/MainForm.MATRIX_DIMENSION - (MainForm.MATRIX_DIMENSION), Constants.MATRIX_WIDTH/MainForm.MATRIX_DIMENSION - (MainForm.MATRIX_DIMENSION), Image.SCALE_SMOOTH));
				this.setText("");
				this.setIconTextGap(0);
				this.setVerticalAlignment(SwingConstants.CENTER);
				this.setHorizontalAlignment(SwingConstants.CENTER);
				this.setIcon(ico);	
			}
			else {
				this.setIcon(null);
				this.setVerticalAlignment(SwingConstants.TOP);
				this.setHorizontalAlignment(SwingConstants.LEFT);
				this.setText("" + number);
				paintDefault();
			}
			
		} catch (IOException ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
		}
	}
	public synchronized void addDiamond() {
		if(hasHole)
			return;
		ImageIcon ico = null;
		try {
			BufferedImage img = ImageIO.read(new File(Constants.RESOURCE_FILE_NAME + File.separator + "diamond-dropped.png"));
			ico = new ImageIcon(img.getScaledInstance(Constants.MATRIX_WIDTH/MainForm.MATRIX_DIMENSION - (MainForm.MATRIX_DIMENSION*3), Constants.MATRIX_WIDTH/MainForm.MATRIX_DIMENSION - (MainForm.MATRIX_DIMENSION*3), Image.SCALE_SMOOTH));
		} catch (IOException ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
		}
		this.setText("");
		this.setIconTextGap(0);
		this.setVerticalAlignment(SwingConstants.CENTER);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setIcon(ico);	
	}
	public synchronized void removeDiamond() {
		this.setIcon(null);
		this.setVerticalAlignment(SwingConstants.TOP);
		this.setHorizontalAlignment(SwingConstants.LEFT);
		this.setText("" + number);
		paintFigure();
	}
	
	public synchronized void addGhost() {
		ImageIcon ico = null;
		try {
			BufferedImage img = ImageIO.read(new File(Constants.RESOURCE_FILE_NAME + File.separator + "ghost.png"));
			ico = new ImageIcon(img.getScaledInstance(Constants.MATRIX_WIDTH/MainForm.MATRIX_DIMENSION - (MainForm.MATRIX_DIMENSION*1), Constants.MATRIX_WIDTH/MainForm.MATRIX_DIMENSION - (MainForm.MATRIX_DIMENSION*1), Image.SCALE_SMOOTH));
		} catch (IOException ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
		}
		this.setText("");
		this.setIconTextGap(0);
		this.setVerticalAlignment(SwingConstants.CENTER);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		paintDefault();
		this.setIcon(ico);	
	}
	public synchronized void removeGhost() {
		hasGhost = false;
		this.setIcon(null);
		this.setVerticalAlignment(SwingConstants.TOP);
		this.setHorizontalAlignment(SwingConstants.LEFT);
		this.setText("" + number);
		paintFigure();
	}
	
	public synchronized void addHole() {
		ImageIcon ico = null;
		try {
			BufferedImage img = ImageIO.read(new File(Constants.RESOURCE_FILE_NAME + File.separator + "hole.png"));
			ico = new ImageIcon(img.getScaledInstance(Constants.MATRIX_WIDTH/MainForm.MATRIX_DIMENSION - (MainForm.MATRIX_DIMENSION*1), Constants.MATRIX_WIDTH/MainForm.MATRIX_DIMENSION - (MainForm.MATRIX_DIMENSION*1), Image.SCALE_SMOOTH));
		} catch (IOException ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
		}
		this.setText("");
		this.setIconTextGap(0);
		this.setVerticalAlignment(SwingConstants.CENTER);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setBackground(Colors.BLACK.getColor());
		this.setForeground(Colors.BLACK.getColor());
		this.setIcon(ico);	
	}
	public synchronized void removeHole() {
		this.setIcon(null);
		hasHole = false;
		this.setVerticalAlignment(SwingConstants.TOP);
		this.setHorizontalAlignment(SwingConstants.LEFT);
		this.setText("" + number);
		paintFigure();
		if(hasGhost) 
			addGhost();
		if(!hasGhost && numberOfDiamonds != 0)
			addDiamond();
		
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getNumber() {
		return number;
	}
	
	public MatrixBlock(int number) {
		super(Integer.valueOf(number).toString());
		this.number = number;
		this.figure = null;
		initComponent();	
	}
	private void initComponent() {
		this.setVerticalTextPosition(SwingConstants.TOP);
		this.setVerticalAlignment(SwingConstants.TOP);
		this.setHorizontalAlignment(SwingConstants.LEFT);
		this.setHorizontalTextPosition(SwingConstants.LEFT);
		this.setFont(new Font("Microsoft Ya Hei", Font.PLAIN, 10));
		this.setBorder(new LineBorder(new Color(0x2565AE)));
		this.setForeground(Colors.GRAY.getColor());
		this.setBackground(Colors.BACKGROUND_COLOR.getColor());
		this.setOpaque(true);
		this.setVerticalTextPosition(TOP);
		this.setHorizontalTextPosition(LEFT);
	}
}
