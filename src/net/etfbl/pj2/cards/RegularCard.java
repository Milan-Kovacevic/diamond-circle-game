package net.etfbl.pj2.cards;

import javax.swing.ImageIcon;

public class RegularCard extends Card {

	public RegularCard() {
		super();
	}

	public RegularCard(Integer number, ImageIcon icon) {
		super(number, icon);
	}

	public RegularCard(Integer number) {
		super(number);
	}
	
	@Override
	public String toString() {
		return "[" + this.getClass().getSimpleName() + ":" + number + "]";
	}
}
