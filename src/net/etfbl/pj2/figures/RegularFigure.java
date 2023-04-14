package net.etfbl.pj2.figures;

import net.etfbl.pj2.interfaces.CanDropInHole;
import net.etfbl.pj2.utilities.Colors;

public class RegularFigure extends Figure implements CanDropInHole {

	public RegularFigure() {
		super();
	}
	
	public RegularFigure(String name, Integer startingPosition, Colors color) {
		super(name, startingPosition, color);
	}
	
	public String getFigureType() {
		return "Regular";
	}

}
