package net.etfbl.pj2.utilities;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.*;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import net.etfbl.pj2.figures.*;
import net.etfbl.pj2.cards.*;
import net.etfbl.pj2.gui.MainForm;
import net.etfbl.pj2.main.Program;
import net.etfbl.pj2.player.Player;

public class Utility {
	private static Random rand = new Random();
	
	public static List<Figure> generateRandomFiguresOfColor(Colors color) throws Exception{
		ArrayList<Figure> figures = new ArrayList<>();
		
		for(int i=0;i<Constants.NUMBER_OF_FIGURES;i++) {
			int num = rand.nextInt(Constants.NUMBER_OF_FIGURE_TYPES)+1;
			switch(num) {
			case 1: // Regular Figure
				figures.add(new RegularFigure("Figure"+(i+1), MainForm.MATRIX_DIMENSION / 2, color));
				break;
			case 2: // Flying Figure
				figures.add(new FlyingFigure("Figure"+(i+1), MainForm.MATRIX_DIMENSION / 2, color));
				break;
			case 3: // Super fast Figure
				figures.add(new SuperfastFigure("Figure"+(i+1), MainForm.MATRIX_DIMENSION / 2, color));
				break;
			default:
				throw new Exception("Invalid figure number generated of value " + num);
			}
		}
		return figures;
	}
	
	public static Colors getColorByPosition(int position) {
		Colors[] colors = Colors.values();
		if(position >= 0 && position <= colors.length) {
			return colors[position];
		}
		return Colors.WHITE;
	}
	
	public static List<Card> createAndShuffleDeckOfCards() {
		List<Card> cards = new ArrayList<Card>();
		for(int i=0;i<Constants.NUMBER_OF_REGULAR_CARD_KINDS; i++) {
			ArrayList<Integer> tmpArr = new ArrayList<>(Collections.nCopies(Constants.NUMBER_OF_REGULAR_CARDS_OF_EACH_KIND, i+1));
			ImageIcon icon;
			for(Integer cardNum: tmpArr) {
				try {
					BufferedImage bufferedImage = ImageIO.read(new File(Constants.RESOURCE_FILE_NAME + File.separator + "playing-card-" + cardNum + ".png"));
					icon = new ImageIcon(bufferedImage.getScaledInstance(Constants.CARD_WIDTH, Constants.CARD_HEIGHT, Image.SCALE_SMOOTH));
					cards.add(new RegularCard(cardNum, icon));
				}
				catch(IOException ex) {
					 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
				}
			}
		}
		Collections.shuffle(cards);
		for(int i=0;i<Constants.NUMBER_OF_SPECIAL_CARD_KINDS; i++) {
			ImageIcon icon;
			for(int j=0; j<Constants.NUMBER_OF_SPECIAL_CARDS_OF_EACH_KIND; j++) {
				try {
					BufferedImage bufferedImage = ImageIO.read(new File(Constants.RESOURCE_FILE_NAME + File.separator + "joker.png"));
					icon = new ImageIcon(bufferedImage.getScaledInstance(Constants.CARD_WIDTH, Constants.CARD_HEIGHT, Image.SCALE_SMOOTH));
					cards.add(new SpecialCard(new Random().nextInt(MainForm.MATRIX_DIMENSION)+1, icon));
				} catch (IOException ex) {
					 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
				}
			}
		}
		Collections.shuffle(cards);
		return cards;
	}
	
	public static int calculateNextPositionInMatrix(int currentPosition) throws Exception {
		final int DIM = MainForm.MATRIX_DIMENSION;
		int value = currentPosition;
			switch(checkQuadrant(value)) {
				case 1:
					value = value + DIM + 1;
					break;
				case 2:
					value = value - DIM + 1;
					break;
				case 3:
					value = value - DIM - 1;
					break;
				case 4:
					value = value + DIM - 1;
					break;
				case 0:
					value = value + 1;
					break;
				case -1:
					return -1;
				default:
					throw new Exception("Invalid position of figure");
			}
		return value;
	}
	public static int calculateNextPositionInMatrixByDistance(int currentPosition, int distance) throws Exception {
		final int DIM = MainForm.MATRIX_DIMENSION;
		int value = currentPosition;
		for(int i=0;i<distance;i++) {
			switch(checkQuadrant(value)) {
				case 1:
					value = value + DIM + 1;
					break;
				case 2:
					value = value - DIM + 1;
					break;
				case 3:
					value = value - DIM - 1;
					break;
				case 4:
					value = value + DIM - 1;
					break;
				case 0:
					value = value + 1;
					break;
				case -1:
					return -1;
				default:
					throw new Exception("Invalid position of figure");
			}
		}
		return value;
	}
	private static int checkQuadrant(int pos) {
		// Coordinate System //
		//  2   |   1
		//  3   |   4
		//*******************//
		
		final int DIM = MainForm.MATRIX_DIMENSION;
		
		if(pos == (Math.ceil(DIM / 2.0) - 1) + (DIM / 2 * DIM)) {
			return -1;
		}
		if(pos < DIM * (Math.ceil(DIM / 2.0) - 1) && (pos % DIM) >= (DIM / 2)) {
			return 1;
		}
		if(pos >= DIM * (Math.ceil(DIM / 2.0) - 1) && (pos % DIM) > (Math.ceil(DIM / 2.0) - 1)){
			return 4;
		}
		if(pos >= ((DIM/2 + 1) * DIM) && (pos % DIM) <= (Math.ceil(DIM / 2.0) - 1)) {
			return 3;
		}
		if(pos < ((DIM/2 + 1) * DIM) && (pos % DIM) < (DIM/2-1)) {
			return 2;
		}
		return 0;
	}
	
	// returns array of positions of holes
	public static List<Integer> generateHolePositionsOnMatrix() throws Exception {
		ArrayList<Integer> holePositions = new ArrayList<Integer>();
		final int MAX_NUM = (MainForm.MATRIX_DIMENSION * MainForm.MATRIX_DIMENSION + 1) / 2;
		final int START_POS = MainForm.MATRIX_DIMENSION / 2;
		int pos;
		int i=0;
		while(i<MainForm.MATRIX_DIMENSION) {
			pos = rand.nextInt(MAX_NUM - 1) + 1;
			int holePos = START_POS;
			for(int j=0;j<pos;j++)
				holePos = calculateNextPositionInMatrix(holePos);
			if(!holePositions.contains(holePos)) {
				holePositions.add(holePos);
				i++;
			}
		}
		return holePositions;
	}
	
	public static void writeSimulationResults(List<Player> players, long totalSimulationTime) {
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new File(Constants.SIMULATION_FILE_NAME + File.separator + "GAME_" + System.currentTimeMillis()))))){
			for(int i=0;i<players.size();i++) {
				writer.println("Player " + (i+1) + " - " + players.get(i).getPlayerName());
				for(Figure f: players.get(i).getFigures()) {				
					writer.print("\t" + f.getFigureName() + " (type:" + f.getFigureType() + ", color:" + f.getColor() + ") - traveled path ("); 
					for(int j=0;j<f.getTraveledPath().size();j++) {
						if(j<f.getTraveledPath().size()-1)
							writer.print("" + (f.getTraveledPath().get(j) + 1) + ", ");
						else
							writer.print("" + (f.getTraveledPath().get(j) + 1));
					}
					writer.print(") - finish line reached: " + (f.getDroppedAtPosition() > 0?"No":"Yes"));
					writer.println();
				}
			}
			writer.println("Total time of simulation: " + totalSimulationTime + "s");
					
		}
		catch(IOException ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
		}
		catch(Exception ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
		}
	}
	public static String readSimulationResults(String fileName) {
		StringBuilder sb = new StringBuilder("");
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(Constants.SIMULATION_FILE_NAME + File.separator + fileName)))){
			String line;
			while((line = reader.readLine())!=null) {
				sb.append(line);
				sb.append(System.lineSeparator());
			}				
		}
		catch(IOException ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
		}
		catch(Exception ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
		}
		return sb.toString();
	}
	
	public static String getTextWrapped(String input) {
		final String html = "<html><body style='width: %1spx'><center>%1s<center>";
		return String.format(html, 400, input);
	}
}
