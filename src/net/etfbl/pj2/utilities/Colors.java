package net.etfbl.pj2.utilities;

import java.awt.Color;

public enum Colors {
	RED(Color.red), YELLOW(Color.orange), GREEN(Color.green), BLUE(Color.blue), GRAY(Color.gray), BLACK(Color.black), WHITE(Color.white),
	FIRST_COLOR(new Color(0xF5FBFF)), SECOND_COLOR(new Color(0xEDF7FC)), THIRD_COLOR(new Color(0xB9E2F5)), FORTH_COLOR(new Color(0x2565AE)), BACKGROUND_COLOR(new Color(0xDCF0FA)),
	REGULAR_FIGURE_COLOR(new Color(0x5ebfc4)), FLYING_FIGURE_COLOR(Color.magenta), SUPERFAST_FIGURE_COLOR(Color.pink);
	
	private Color color;
	
	private Colors(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
}
