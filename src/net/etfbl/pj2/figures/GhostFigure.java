package net.etfbl.pj2.figures;

import java.util.Random;
import java.util.logging.Level;
import net.etfbl.pj2.gui.MainForm;
import net.etfbl.pj2.main.Program;
import net.etfbl.pj2.utilities.Colors;
import net.etfbl.pj2.utilities.Utility;

public class GhostFigure extends Figure {
	
	public GhostFigure() {
		super();
	}
	
	public GhostFigure(String name, Integer startingPosition) {
		super(name, startingPosition, Colors.WHITE);
	}
	
	public String getFigureType() {
		return "Ghost";
	}
	
	private Random rand = new Random();
	
	@Override
	public int move() {
		try {
			if(!stopped) {
				int nextPosition = -1;
				lastPosition = currentPosition;
				while(nextPosition==-1){
					nextPosition = Utility.calculateNextPositionInMatrixByDistance(currentPosition, rand.nextInt((MainForm.MATRIX_DIMENSION*MainForm.MATRIX_DIMENSION) / 2) + 1);
					currentPosition = (nextPosition == -1)? (MainForm.MATRIX_DIMENSION/2) : nextPosition;
				}
				int numberOfDiamonds = rand.nextInt(MainForm.MATRIX_DIMENSION - 1) + 2;
				MainForm.MATRIX[currentPosition/MainForm.MATRIX_DIMENSION][currentPosition%MainForm.MATRIX_DIMENSION].setNumberOfDiamonds(numberOfDiamonds);
				MainForm.MATRIX[currentPosition/MainForm.MATRIX_DIMENSION][currentPosition%MainForm.MATRIX_DIMENSION].setHasGhost(true);
			}
		}
		catch(InterruptedException ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
		} catch (Exception ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
		}
		return currentPosition;	
	}
	@Override
	public void run() {
		try {
			while(!MainForm.getInstance().isFinished) {
				if(stopped) {
					synchronized(this) {
						wait();
					}
				}
				if(!stopped) {
					move();
					if(MainForm.MATRIX[currentPosition/MainForm.MATRIX_DIMENSION][currentPosition%MainForm.MATRIX_DIMENSION].isHasGhost()) {
						MainForm.MATRIX[currentPosition/MainForm.MATRIX_DIMENSION][currentPosition%MainForm.MATRIX_DIMENSION].addGhost();
					}
					for(int i=0;i<5;i++) {
						sleep(MainForm.SIMULATION_SPEED);
						if(stopped)
							break;
					}

					MainForm.MATRIX[currentPosition/MainForm.MATRIX_DIMENSION][currentPosition%MainForm.MATRIX_DIMENSION].removeGhost();
					if(MainForm.MATRIX[currentPosition/MainForm.MATRIX_DIMENSION][currentPosition%MainForm.MATRIX_DIMENSION].getNumberOfDiamonds() > 0)
						MainForm.MATRIX[currentPosition/MainForm.MATRIX_DIMENSION][currentPosition%MainForm.MATRIX_DIMENSION].addDiamond();
				}
			}
		} catch (Exception ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
		}
		if(MainForm.MATRIX[currentPosition/MainForm.MATRIX_DIMENSION][currentPosition%MainForm.MATRIX_DIMENSION].isHasGhost())
			MainForm.MATRIX[currentPosition/MainForm.MATRIX_DIMENSION][currentPosition%MainForm.MATRIX_DIMENSION].removeGhost();
	}
	
}
