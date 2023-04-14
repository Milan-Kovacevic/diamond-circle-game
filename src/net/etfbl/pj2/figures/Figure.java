package net.etfbl.pj2.figures;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.etfbl.pj2.cards.*;
import net.etfbl.pj2.gui.MainForm;
import net.etfbl.pj2.interfaces.*;
import net.etfbl.pj2.main.Program;
import net.etfbl.pj2.utilities.Colors;
import net.etfbl.pj2.utilities.Utility;

public abstract class Figure extends Thread implements Moveable {
	protected Colors color;
	protected Long traveledTime;
	protected List<Integer> traveledPath;
	protected Integer currentPosition;
	protected Integer lastPosition;
	protected Boolean stopped;
	protected Boolean finished;
	protected String name;
	protected Integer droppedAtPosition;
	protected Integer pathTravelMultiplayer;
	protected Integer diamonds;
	
	public Figure() {
		super();
		this.color = Colors.WHITE;
		this.traveledTime = 0L;
		this.traveledPath = new ArrayList<Integer>();
		traveledPath.add(currentPosition);
		this.currentPosition = 0;
		this.droppedAtPosition = -1;
		this.lastPosition = 0;
		this.diamonds = 0;
		this.stopped = true;
		this.finished = false;
		this.name = "";
		this.pathTravelMultiplayer = 1;
	}
	public Figure(String name, Integer startingPosition, Colors color) {
		super();
		this.color = color;
		this.traveledTime = 0L;
		this.traveledPath = new ArrayList<Integer>();
		traveledPath.add(startingPosition);
		this.currentPosition = startingPosition;
		this.droppedAtPosition = -1;
		this.lastPosition = startingPosition;
		this.diamonds = 0;
		this.stopped = true;
		this.finished = false;
		this.name = name;
		this.pathTravelMultiplayer = 1;
	}

	public Colors getColor() {
		return color;
	}
	public void setColor(Colors color) {
		this.color = color;
	}
	public synchronized Boolean getFinished() {
		return finished;
	}
	public synchronized void setFinished(Boolean finished) {
		this.finished = finished;
	}
	public Long getTraveledTime() {
		return traveledTime;
	}
	public Integer getLastPosition() {
		return lastPosition;
	}
	public void setLastPosition(Integer lastPosition) {
		this.lastPosition = lastPosition;
	}
	public Integer getPathTravelMultiplayer() {
		return pathTravelMultiplayer;
	}
	public void setPathTravelMultiplayer(Integer pathTravelMultiplayer) {
		this.pathTravelMultiplayer = pathTravelMultiplayer;
	}
	public Integer getDroppedAtPosition() {
		return droppedAtPosition;
	}
	public void setDroppedAtPosition(Integer droppedAtPosition) {
		this.droppedAtPosition = droppedAtPosition;
	}
	public void setTraveledTime(Long traveledTime) {
		this.traveledTime = traveledTime;
	}
	public List<Integer> getTraveledPath() {
		return traveledPath;
	}
	public void setTraveledPath(List<Integer> traveledPath) {
		this.traveledPath = traveledPath;
	}
	public Integer getCurrentPosition() {
		return currentPosition;
	}
	public void setCurrentPosition(Integer currentPosition) {
		this.currentPosition = currentPosition;
	}
	public Boolean getStopped() {
		return stopped;
	}
	public void setStopped(Boolean stopped) {
		this.stopped = stopped;
	}
	public synchronized String getFigureName() {
		return name;
	}
	public synchronized void setFigureName(String name) {
		this.name = name;
	}
	
	public abstract String getFigureType();
	
	private void updateMoveDescription() {
		try {
			int distance = MainForm.getInstance().DEALER.getCurrentCard().getNumber();
			int numberOfDiamonds = MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION ].getNumberOfDiamonds() + diamonds;
			int totalDistance = (distance * pathTravelMultiplayer) + numberOfDiamonds;
			int pos = currentPosition;
			int aditionalDiamonds = 0;
			int lastValidPosition = currentPosition;
			for(int i=0;i<totalDistance;i++) {
				pos = Utility.calculateNextPositionInMatrix(pos);
				if(pos>0 && MainForm.MATRIX[pos / MainForm.MATRIX_DIMENSION][pos % MainForm.MATRIX_DIMENSION ].getNumberOfDiamonds() > 0) {
					aditionalDiamonds+=MainForm.MATRIX[pos / MainForm.MATRIX_DIMENSION][pos % MainForm.MATRIX_DIMENSION ].getNumberOfDiamonds();
				}
				lastValidPosition = (pos>0)?pos:lastValidPosition;
			}
			while(pos>0 && MainForm.MATRIX[pos / MainForm.MATRIX_DIMENSION][pos % MainForm.MATRIX_DIMENSION].getFigure() != null) {
				pos = Utility.calculateNextPositionInMatrix(pos);
				if(pos>0 && MainForm.MATRIX[pos / MainForm.MATRIX_DIMENSION][pos % MainForm.MATRIX_DIMENSION ].getNumberOfDiamonds() > 0) {
					aditionalDiamonds+=MainForm.MATRIX[pos / MainForm.MATRIX_DIMENSION][pos % MainForm.MATRIX_DIMENSION ].getNumberOfDiamonds();
				}
				lastValidPosition = (pos>0)?pos:lastValidPosition;
				totalDistance++;
			}
			traveledPath.add(lastValidPosition);
			MainForm.getInstance().descriptionLabel.setText(Utility.getTextWrapped(MainForm.getInstance().descriptionLabel.getText() + this.getFigureName() +
					" travels " + totalDistance + " fields, moves from position " + (currentPosition + 1) + " to " + (lastValidPosition + 1) + ((numberOfDiamonds>0)?(" with " + numberOfDiamonds + " diamonds. "):". ")));
			if(aditionalDiamonds > 0) {
				MainForm.getInstance().descriptionLabel.setText(Utility.getTextWrapped(MainForm.getInstance().descriptionLabel.getText() 
						+ "Figure also picked " + aditionalDiamonds + " aditional diamonds while traveling. "));
			}
		} catch (Exception ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
		}	
		
	}

	private void drawFigure() {
		if(currentPosition != lastPosition) {
			MainForm.MATRIX[lastPosition / MainForm.MATRIX_DIMENSION][lastPosition % MainForm.MATRIX_DIMENSION].paintFigure();
			if(MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION].getFigure() == null) {
				MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION].setFigure(this);
				MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION].paintFigure();
				MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION].setFigure(null);
			}
			else {
				MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION].paint(this.color.getColor(), Colors.BACKGROUND_COLOR.getColor());
			}
		}
	}
	
	@Override
	public int move() {
		try {
			MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION].setFigure(null);
			updateMoveDescription();
			int distance = MainForm.getInstance().DEALER.getCurrentCard().getNumber();	
				
			int numberOfDiamonds = MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION].getNumberOfDiamonds();
			diamonds +=	numberOfDiamonds;
			if(numberOfDiamonds > 0) {
				MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION].removeDiamond();
				MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION].setNumberOfDiamonds(0);
			}
			int totalDistance = (distance * pathTravelMultiplayer) + diamonds;
			diamonds = 0;
			int value = currentPosition;
			
			for(int i=0;i<totalDistance;i++) {
				lastPosition = currentPosition;
				value = Utility.calculateNextPositionInMatrix(value);				
				this.currentPosition = value;	
				if(value == -1)
					break;
				int dmd =  MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION ].getNumberOfDiamonds();
				if(dmd > 0) {
					MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION].removeDiamond();
					MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION].setNumberOfDiamonds(0);
				}
				diamonds += dmd;
				drawFigure();
				sleep(MainForm.SIMULATION_SPEED);
			}
			while(value!=-1 && MainForm.MATRIX[value / MainForm.MATRIX_DIMENSION][value % MainForm.MATRIX_DIMENSION].getFigure() != null) {
				lastPosition = currentPosition;
				value = Utility.calculateNextPositionInMatrix(value);	
				this.currentPosition = value;
				if(value == -1) {
					MainForm.MATRIX[lastPosition / MainForm.MATRIX_DIMENSION][lastPosition % MainForm.MATRIX_DIMENSION].setFigure(null);
					MainForm.MATRIX[lastPosition / MainForm.MATRIX_DIMENSION][lastPosition % MainForm.MATRIX_DIMENSION].paintDefault();
					break;
				}
				int dmd =  MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION ].getNumberOfDiamonds();
				if(dmd > 0) {
					MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION].removeDiamond();
					MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION].setNumberOfDiamonds(0);
				}
				diamonds += dmd;
				totalDistance++;
				drawFigure();
				sleep(MainForm.SIMULATION_SPEED);
			}
			if(value == -1 || Utility.calculateNextPositionInMatrix(value) == -1) {
				if(currentPosition == -1)
					currentPosition = lastPosition;
				finished = true;
				MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION].setFigure(null);
				MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION].paintFigure();
				synchronized(MainForm.getInstance().PLAYER_SCHEDULER.getCurrentPlayer()) {
					MainForm.getInstance().PLAYER_SCHEDULER.getCurrentPlayer().notify();
				}
				//*****************************//
				MainForm.getInstance().descriptionLabel.setForeground(color.getColor());
				MainForm.getInstance().descriptionLabel.setText(Utility.getTextWrapped(MainForm.getInstance().descriptionLabel.getText() + this.getFigureName() + " completed a diamond circle path..."));
				return totalDistance;
			}
			MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION].setFigure(this);
			MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION].paintFigure();
			return totalDistance;
		}catch(Exception ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
			return 0;
		}
	}
	@Override
	public void run() {
		try {
			if( MainForm.MATRIX[this.getCurrentPosition()/ MainForm.MATRIX_DIMENSION][this.getCurrentPosition()%MainForm.MATRIX_DIMENSION].isHasHole() == true) {
				this.setFinished(true);
			}
			else{
				MainForm.MATRIX[this.getCurrentPosition()/MainForm.MATRIX_DIMENSION][this.getCurrentPosition()%MainForm.MATRIX_DIMENSION].setFigure(this);
				MainForm.MATRIX[this.getCurrentPosition()/MainForm.MATRIX_DIMENSION][this.getCurrentPosition()%MainForm.MATRIX_DIMENSION].paintFigure();
			}
		} catch (Exception ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
		}
		while(!finished) {
			try {
				if(stopped) {
					synchronized(this) {					
						wait();
					}
				}
				synchronized(this) {
					long currentTime = Long.valueOf(MainForm.getInstance().GAME_CLOCK.getCurrentTimeSeconds());
					if(!finished) {
						if( MainForm.getInstance().DEALER.getCurrentCard() instanceof RegularCard) {
							move();
						}
					}
					else if(MainForm.getInstance().DEALER.getCurrentCard() instanceof SpecialCard) {
						if(MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION ].isHasHole() == true && this instanceof CanDropInHole) {
							MainForm.getInstance().descriptionLabel.setText(Utility.getTextWrapped(MainForm.getInstance().descriptionLabel.getText() + name + " dropped in hole at position " + (currentPosition + 1)));
							MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION].setFigure(null);
							finished = true;
							droppedAtPosition = currentPosition;
						}
					}
					if(finished) {
						stopped = true;
						MainForm.MATRIX[currentPosition / MainForm.MATRIX_DIMENSION][currentPosition % MainForm.MATRIX_DIMENSION].setFigure(null);
						break;
					}
					this.traveledTime += Long.valueOf(MainForm.getInstance().GAME_CLOCK.getCurrentTimeSeconds() - currentTime);
					stopped = true;
					synchronized(MainForm.getInstance().PLAYER_SCHEDULER.getCurrentPlayer()) {
						MainForm.getInstance().PLAYER_SCHEDULER.getCurrentPlayer().notify();
					}
				}
			} catch (Exception ex) {
				 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
			}	
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Figure))
			return false;
		else {
			return this.color.getColor() == ((Figure)obj).color.getColor() && this.getFigureName().equals(((Figure)obj).getFigureName());	
		}
	}
	
}
