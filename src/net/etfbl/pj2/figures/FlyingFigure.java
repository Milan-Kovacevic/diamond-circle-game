package net.etfbl.pj2.figures;

import net.etfbl.pj2.utilities.Colors;

public class FlyingFigure extends Figure {

	public FlyingFigure() {
		super();
	}
	
	public String getFigureType() {
		return "Flying";
	}
	
	public FlyingFigure(String name, Integer startingPosition, Colors color) {
		super(name, startingPosition, color);
	}
	
}
