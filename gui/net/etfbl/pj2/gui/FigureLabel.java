package net.etfbl.pj2.gui;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import net.etfbl.pj2.utilities.Colors;

public class FigureLabel extends JLabel {
	
	private static final long serialVersionUID = 1L;

	public FigureLabel(String figureName) {
		super(figureName);
		super.setVerticalAlignment(SwingConstants.CENTER);
		super.setVerticalTextPosition(SwingConstants.CENTER);
		super.setHorizontalTextPosition(SwingConstants.CENTER);
		super.setHorizontalAlignment(SwingConstants.CENTER);
		super.setFont(new Font("Century Gothic", Font.ITALIC, 14));
		super.setForeground(Colors.FORTH_COLOR.getColor());
	}
	

}
