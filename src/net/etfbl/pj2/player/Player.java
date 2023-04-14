package net.etfbl.pj2.player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.etfbl.pj2.figures.Figure;
import net.etfbl.pj2.gui.MainForm;
import net.etfbl.pj2.main.Program;
import net.etfbl.pj2.utilities.Constants;
import net.etfbl.pj2.utilities.Utility;

public class Player extends Thread{
	private String name;
	private List<Figure> figures;
	private Figure currentFigure;
	private boolean stopped;
	private boolean finished;
	
	public Player() {
		super();
		this.stopped = false;
		this.figures = new ArrayList<>(Constants.NUMBER_OF_FIGURES);
		this.currentFigure = null;
	}
	
	public Player(String name, List<Figure> figures) {
		super();
		this.figures = figures;
		this.stopped = false;
		this.name = name;
		if(!figures.isEmpty())
			this.currentFigure = figures.get(0);
		else
			this.currentFigure = null;
		
	}
	
	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public Figure getCurrentFigure() {
		return currentFigure;
	}

	public void setCurrentFigure(Figure currentFigure) {
		this.currentFigure = currentFigure;
	}

	public boolean isStopped() {
		return stopped;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	public List<Figure> getFigures() {
		return figures;
	}

	public void setFigures(List<Figure> figures) {
		this.figures = figures;
	}

	public void setPlayerName(String name) {
		this.name = name;
	}

	public String getPlayerName() {
		return name;
	}
	
	public synchronized Figure nextFigure() {
		int pos = figures.indexOf(currentFigure);
		if(figures.size() > (pos+1)) {
			currentFigure = figures.get(pos+1);
		}	
		else
			currentFigure = null;
		if(currentFigure != null)
			currentFigure.setStopped(true);
		return currentFigure;
	}
	@Override
	public void run() {
		currentFigure.setStopped(true);
		currentFigure.start();
		while(!finished) {
		try {
			if(stopped) {
				synchronized(this) {
					wait();
				}
			}
			synchronized(this) {
				if(currentFigure.getFinished() == false) {
					MainForm.getInstance().descriptionLabel.setForeground(currentFigure.getColor().getColor());
					MainForm.getInstance().descriptionLabel.setText(Utility.getTextWrapped(MainForm.getInstance().descriptionLabel.getText() + "It is " + name + "'s turn. "));
				}
				synchronized(currentFigure) {
					currentFigure.setStopped(false);
					currentFigure.notify();
				}
				wait();
				if(currentFigure.getFinished() == true) {
					nextFigure();
					if(currentFigure != null) {
						currentFigure.setStopped(true);
						currentFigure.start();
					}
					else
						finished = true;
					
				}
				stopped = true;
			}
			synchronized(MainForm.getInstance().PLAYER_SCHEDULER) {
				MainForm.getInstance().PLAYER_SCHEDULER.notify();
			}
		} catch (Exception ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
		}
		}
	}
	@Override
	public String toString() {
		return name;
	}
}
