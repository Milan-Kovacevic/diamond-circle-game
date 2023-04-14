package net.etfbl.pj2.cards;

import javax.swing.ImageIcon;

public abstract class Card {
	protected Integer number;
	protected ImageIcon icon;
	
	public Card(Integer number) {
		super();
		this.number = number;
	}
	public Card(Integer number, ImageIcon icon) {
		super();
		this.number = number;
		this.icon = icon;
	}
	
	public ImageIcon getIcon() {
		return icon;
	}
	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}
	public synchronized Integer getNumber() {
		return number;
	}
	public synchronized void setNumber(Integer number) {
		this.number = number;
	}


	public Card() {
		super();
		this.number = 1;
	}
}
