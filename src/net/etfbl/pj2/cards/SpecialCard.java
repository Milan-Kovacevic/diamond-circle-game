package net.etfbl.pj2.cards;

import javax.swing.ImageIcon;

public class SpecialCard extends Card {

	public SpecialCard() {
		super();
	}

	public SpecialCard(Integer number, ImageIcon icon) {
		super(number, icon);
	}

	public SpecialCard(Integer number) {
		super(number);
	}

	@Override
	public String toString() {
		return "[" + this.getClass().getSimpleName() + ":" + number + "]";
	}
}
