package net.etfbl.pj2.cards;

import java.util.*;
import java.util.logging.Level;
import net.etfbl.pj2.gui.MainForm;
import net.etfbl.pj2.main.Program;

public class Dealer extends Thread {
	private LinkedList<Card> cards;
	private boolean stopped;
	private Card currentCard;
	
	// Constructors
	public Dealer(LinkedList<Card> cards) {
		super();
		this.setDaemon(true);
		this.cards = cards;
		this.stopped = false;
	}
	public Dealer() {
		super();
		this.setDaemon(true);
	}
	
	// Getters and Setters
	public boolean isStopped() {
		return stopped;
	}
	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}
	public LinkedList<Card> getCards() {
		return cards;
	}
	public void setCards(LinkedList<Card> cards) {
		this.cards = cards;
	}
	public synchronized Card getCurrentCard() {
		return currentCard;
	}
	public void setCurrentCard(Card currentCard) {
		this.currentCard = currentCard;
	}
	
	@Override
	public void run() {
		currentCard = cards.get(0);
		try {
			while(!MainForm.getInstance().isFinished) {
				if(stopped) {
					synchronized(this) {
						try {
							MainForm.getInstance().cardIconLabel.setIcon(currentCard.getIcon());
							wait();
						}catch(Exception ex) {
							 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
						}
					}
				}
				else {
					synchronized(this) {
						Card card = cards.remove();
						currentCard = card;
						try {
							MainForm.getInstance().cardIconLabel.setIcon(card.getIcon());
							MainForm.getInstance().cardIconLabel.setIcon(null);
							if(!stopped) {
								sleep(40);
							}

							cards.add(card);
						
						} catch(InterruptedException ex) {
							 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
						}catch (Exception ex) {
							 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
						}
						stopped = true;
					}
				}
			}
		} catch (Exception ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
		}
	}
}