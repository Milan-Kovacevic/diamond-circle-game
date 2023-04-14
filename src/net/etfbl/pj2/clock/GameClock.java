package net.etfbl.pj2.clock;

import java.util.logging.Level;
import net.etfbl.pj2.gui.MainForm;
import net.etfbl.pj2.main.Program;

public class GameClock extends Thread{
	private long currentTimeSeconds;
	private boolean stopped;
	
	public GameClock() {
		super();
		this.setDaemon(true);
	}
	
	public long getCurrentTimeSeconds() {
		return currentTimeSeconds;
	}
	public boolean isStopped() {
		return stopped;
	}
	public synchronized void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	@Override
	public void run() {
		long accumulator = 0L;
		while(true) {
			if(stopped) {
				synchronized(this) {
					try {
						wait();
					}
					catch(InterruptedException ex) {
						 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
					}
				}
			}
			try {
				if(!stopped) {
					accumulator+=1000;
					currentTimeSeconds = accumulator/1000;
					sleep(MainForm.SIMULATION_SPEED);
					MainForm.getInstance().timeOfSimulationLabel.setText(currentTimeSeconds + "s");
				}
			} catch (Exception ex) {
				 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
			}
		}
	}
	
	
}
