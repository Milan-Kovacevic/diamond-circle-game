package net.etfbl.pj2.results;

import java.util.ArrayList;
import java.util.List;

import net.etfbl.pj2.utilities.Constants;
import net.etfbl.pj2.utilities.Utility;

public class SimulationResult {
	private List<String> playersNames;
	private List<String> figuresDescription;
	private String simulationTime;
	

	public SimulationResult(List<String> playersNames, List<String> figuresDescription, String simulationTime) {
		super();
		this.playersNames = playersNames;
		this.figuresDescription = figuresDescription;
		this.simulationTime = simulationTime;
	}

	public List<String> getPlayersNames() {
		return playersNames;
	}

	public void setPlayersNames(List<String> playersNames) {
		this.playersNames = playersNames;
	}

	public List<String> getFiguresDescription() {
		return figuresDescription;
	}

	public void setFiguresDescription(List<String> figuresDescription) {
		this.figuresDescription = figuresDescription;
	}

	public String getSimulationTime() {
		return simulationTime;
	}

	public void setSimulationTime(String simulationTime) {
		this.simulationTime = simulationTime;
	}

	public static SimulationResult parseResult(String fileName) {
		List<String> playerNames = new ArrayList<String>();
		List<String> figuresDescription = new ArrayList<String>();
		
		final String SEPARATOR = "-";
		String content = Utility.readSimulationResults(fileName);
		String[] lines = content.split(System.lineSeparator());
		for(int i=0;i<lines.length-1;i+=(Constants.NUMBER_OF_FIGURES+1)) {
			playerNames.add(lines[i].split(SEPARATOR)[1].trim());
			String description = "";
			for(int j=1;j<=Constants.NUMBER_OF_FIGURES;j++) {
				description += lines[i+j] + System.lineSeparator();
			}
			figuresDescription.add(description);
		}
		String simulationTime = lines[lines.length-1];
		return new SimulationResult(playerNames, figuresDescription, simulationTime);
	}
}
