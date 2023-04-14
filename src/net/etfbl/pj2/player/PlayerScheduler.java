package net.etfbl.pj2.player;

import java.util.*;
import java.util.logging.Level;

import net.etfbl.pj2.cards.SpecialCard;
import net.etfbl.pj2.gui.MainForm;
import net.etfbl.pj2.interfaces.CanDropInHole;
import net.etfbl.pj2.main.Program;
import net.etfbl.pj2.utilities.Utility;

public class PlayerScheduler extends Thread {
	private LinkedList<Player> players;
	private Player currentPlayer;
	private boolean stopped;
	
	
	public PlayerScheduler() {
		super();
		this.players = new LinkedList<Player>();
	}
	
	public PlayerScheduler(List<Player> players) {
		super();
		this.stopped = false;
		this.players = new LinkedList<Player>(players);
		if(players!=null)
			this.currentPlayer = players.get(0);
		else
			this.currentPlayer = null;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	public void setPlayers(List<Player> players) {
		this.players = new LinkedList<Player>(players);
	}
	public synchronized Player getCurrentPlayer() {
		return currentPlayer;
	}
	public synchronized void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	public synchronized boolean isStopped() {
		return stopped;
	}
	public synchronized void setStopped(boolean stopped) {
		this.stopped = stopped;
	}
	
	@Override
	public void run() {
		for(Player p : players) {
			p.setStopped(true);
			p.start();
		}
		MainForm main;
		try {
			main = MainForm.getInstance();
		}
		catch(Exception ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
			return;
		}
		currentPlayer = players.get(0);
		List<Integer> currentTurnHolePositions = new ArrayList<>();
		List<Integer> lastTurnHolePositions = new ArrayList<>();
		while(!main.isFinished && currentPlayer!=null) {
			try {
				if(stopped) {
					synchronized(this) {
						try {
							wait();	
							synchronized(MainForm.getInstance().GAME_CLOCK) {
								MainForm.getInstance().GAME_CLOCK.setStopped(false);
								MainForm.getInstance().GAME_CLOCK.notify();
							}
						}
						catch(InterruptedException ex) {
							 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
						}
					}
				}
				if(!stopped) {
					MainForm.getInstance().descriptionLabel.setText("");
					synchronized(MainForm.getInstance().DEALER) {
						MainForm.getInstance().DEALER.setStopped(false);
						MainForm.getInstance().DEALER.notify();
					}
					for(Integer i : lastTurnHolePositions) {
						MainForm.MATRIX[i/MainForm.MATRIX_DIMENSION][i%MainForm.MATRIX_DIMENSION].setHasHole(false);
						MainForm.MATRIX[i/MainForm.MATRIX_DIMENSION][i%MainForm.MATRIX_DIMENSION].removeHole();
					}
					try {	
						if(players.isEmpty()) {
							main.isFinished = true;
							break;
						}
						currentPlayer = players.remove(0);
						if(currentPlayer == null) {
							break;
						}
						else {
							synchronized(currentPlayer) {
								currentPlayer.notify();
								currentPlayer.setStopped(false);						
							}	
							synchronized(this) {
								wait();
							}
						}
						if(MainForm.getInstance().DEALER.getCurrentCard() instanceof SpecialCard) {
							
							currentTurnHolePositions = Utility.generateHolePositionsOnMatrix();
							for(Integer i : currentTurnHolePositions) {
								MainForm.MATRIX[i/MainForm.MATRIX_DIMENSION][i%MainForm.MATRIX_DIMENSION].setHasHole(true);
								MainForm.MATRIX[i/MainForm.MATRIX_DIMENSION][i%MainForm.MATRIX_DIMENSION].addHole();
							}
							for(Player p : MainForm.PLAYERS) {
								if(p.getCurrentFigure() instanceof CanDropInHole && currentTurnHolePositions.contains(p.getCurrentFigure().getCurrentPosition())) {
									p.getCurrentFigure().setFinished(true);
									synchronized(p) {
										p.notify();
									}
								}
							}
							if(currentPlayer.getCurrentFigure()!=null)
								MainForm.getInstance().descriptionLabel.setForeground(currentPlayer.getCurrentFigure().getColor().getColor());
							MainForm.getInstance().descriptionLabel.setText(Utility.getTextWrapped(MainForm.getInstance().descriptionLabel.getText() + "Holes appeared on the ground... "));
							lastTurnHolePositions = new ArrayList<>(currentTurnHolePositions);

							sleep(MainForm.SIMULATION_SPEED);
						}
						if(currentPlayer.isFinished()) {
							players.remove(currentPlayer);
						}
						else {
							players.add(currentPlayer);
						}
						if(stopped) {
							MainForm.getInstance().GAME_CLOCK.setStopped(true);
						}
					}
					catch(Exception ex) {
						 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
					}
				}
			} catch (Exception ex) {
				 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
			}
		}
		synchronized(main) {
			main.notify();
		}
	}
}
