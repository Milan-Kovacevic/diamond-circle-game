package net.etfbl.pj2.main;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.etfbl.pj2.gui.*;
import net.etfbl.pj2.utilities.Constants;

public final class Program{	
	private static Handler handler;
	public static Logger logger;
	
	static{
		File path = new File(Constants.LOGGER_FILE_NAME + File.separator);
		if(!path.exists()) {
			path.mkdir();
		}
		try{
			logger = Logger.getLogger("logger");
			handler = new FileHandler(path.getName() + File.separator + "LOG_" + System.currentTimeMillis() + ".txt", 8096,1,true);
			handler.setLevel(Level.ALL);
			logger.addHandler(handler);
		}
		catch(IOException ex){
			ex.printStackTrace(System.out);
		}
	}
	
	private static void closeHandler() {
		handler.close();
	}
	
	// Main program entrance
	public static void main(String[] args) {
		try {
			new Program().start();
			closeHandler();
			
		} catch (Exception ex) {
			logger.log(Level.WARNING, ex.fillInStackTrace().toString());
		}
	}
	
	public void start() {
		MenuForm menu = new MenuForm();
		while(!menu.isFieldsInitialized());
		if(menu.isFieldsInitialized()) {
			MainForm main = MainForm.createInstance(menu.getMatrixDimension(), menu.getPlayerNames());
			main.runGame();
		}			
	}
	
}
