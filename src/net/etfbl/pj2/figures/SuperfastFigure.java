package net.etfbl.pj2.figures;

import net.etfbl.pj2.interfaces.CanDropInHole;
import net.etfbl.pj2.utilities.Colors;

public class SuperfastFigure extends Figure implements CanDropInHole {

	public SuperfastFigure() {
		super();
	}

	public SuperfastFigure(String name, Integer startingPosition, Colors color) {
		super(name, startingPosition, color);
		super.pathTravelMultiplayer = 2; // Super fast figure travels double the amount of fields
	}
	
	public String getFigureType() {
		return "Superfast";
	}
	
	
}
