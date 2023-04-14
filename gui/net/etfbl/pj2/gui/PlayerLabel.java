package net.etfbl.pj2.gui;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import net.etfbl.pj2.utilities.Colors;

public class PlayerLabel extends JLabel{
	private static final long serialVersionUID = 1L;

	public PlayerLabel(boolean flag, Colors color, String playerName) {
		super();
		// If flag is true makes a JLabel object with 
		// specified player name provided as  argument
		
		this.setText(playerName);
		this.setRequestFocusEnabled(false);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setForeground(color.getColor());
		if(flag) {
			this.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		}
		else {
			
			this.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		}
	}
}
