package net.etfbl.pj2.gui;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import net.etfbl.pj2.main.Program;
import net.etfbl.pj2.utilities.Colors;
import net.etfbl.pj2.utilities.Constants;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameResultsForm {

	public GameResultsForm() {
		initComponents();
		loadAndPopulateSimulationResultFileNames();
	}
	public void show() {
		frame.setVisible(true);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private JFrame frame;
	private JTable table;
	
	private void initComponents() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 521, 690);
		frame.getContentPane().setBackground(Colors.BACKGROUND_COLOR.getColor());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		ImageIcon menuIcon = new ImageIcon(Constants.RESOURCE_FILE_NAME + File.separator + "icon.png");
		frame.setIconImage(menuIcon.getImage());
		frame.setTitle("Diamond Circle - Simulation Results");
		frame.setLocationRelativeTo(null);
	
		JPanel welcomeBackPanel = new JPanel();
		welcomeBackPanel.setLayout(null);
		welcomeBackPanel.setBorder(new LineBorder(Colors.FORTH_COLOR.getColor()));
		welcomeBackPanel.setBackground(Colors.SECOND_COLOR.getColor());
		welcomeBackPanel.setBounds(43, 26, 418, 52);
		frame.getContentPane().add(welcomeBackPanel);
		
		JLabel lblNewLabel = new JLabel("Diamond Circle");
		lblNewLabel.setForeground(Colors.FORTH_COLOR.getColor());
		lblNewLabel.setFont(new Font("Georgia", Font.ITALIC, 34));
		lblNewLabel.setBounds(79, 0, 252, 53);
		welcomeBackPanel.add(lblNewLabel);
		
		JLabel lblListOfSimulation = new JLabel("Duble-click on file name to show simulation result");
		lblListOfSimulation.setRequestFocusEnabled(false);
		lblListOfSimulation.setHorizontalTextPosition(SwingConstants.CENTER);
		lblListOfSimulation.setHorizontalAlignment(SwingConstants.CENTER);
		lblListOfSimulation.setForeground(new Color(37, 101, 174));
		lblListOfSimulation.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		lblListOfSimulation.setBounds(35, 91, 434, 24);
		frame.getContentPane().add(lblListOfSimulation);
		
		//************************************/
		String[] columnNames = {"File Name", "Last Modified", "Size (bytes)"};
		TableModel model = new DefaultTableModel(null, columnNames);

		table = new JTable(model) {
			private static final long serialVersionUID = 1L;

			@Override
	         public boolean editCellAt(int row, int column, java.util.EventObject e) {
	             return false;
	          }
	      };
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() > 1) {
					JTable source = (JTable)e.getSource();
					int row = source.rowAtPoint(e.getPoint());
					String fileName = source.getModel().getValueAt(row, 0)+"";
					ShowResultForm res = new ShowResultForm(fileName);
					res.show();
				}
			}
		});

		table.setForeground(Colors.FORTH_COLOR.getColor());
		table.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setBackground(Colors.SECOND_COLOR.getColor());
		table.setRowHeight(25);

		JTableHeader header = table.getTableHeader();
		header.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		header.setBackground(Colors.FORTH_COLOR.getColor());
		header.setForeground(Colors.WHITE.getColor());
		header.setSize(header.getWidth(), 40);
		TableColumn col0=table.getColumnModel().getColumn(0);
        TableColumn col1=table.getColumnModel().getColumn(1);
        col0.setPreferredWidth(150);
        col1.setPreferredWidth(120);
        
		JScrollPane pane = new JScrollPane(table);
		pane.setBackground(Colors.FIRST_COLOR.getColor());
	    pane.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		
	    JPanel panel = new JPanel();
	    panel.setLayout(new BorderLayout(0, 0));
		panel.setBorder(new LineBorder(Colors.FORTH_COLOR.getColor()));
		panel.setBackground(Colors.FIRST_COLOR.getColor());
		panel.setBounds(25, 124, 454, 502);
		panel.add(pane);
		frame.getContentPane().add(panel);
	}
	
	private void loadAndPopulateSimulationResultFileNames() {
		File path = new File(Constants.SIMULATION_FILE_NAME + File.separator);
		try {
			if(!path.exists())
				throw new IOException("Root directory " + Constants.SIMULATION_FILE_NAME + File.separator + " doesnt exist on file system");
			DateFormat formater = new SimpleDateFormat("dd/MM/yyyy hh:mm a");         
			File[] files = path.listFiles();
			for(File f: files) {
				if(f.isFile()) {
					DefaultTableModel model = (DefaultTableModel)table.getModel();
					Date date = new Date(f.lastModified());
					model.addRow(new String[]{f.getName() , formater.format(date), f.length()+""});
				}
			}
		}
		catch(IOException ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
		}
		catch(Exception ex) {
			 Program.logger.log(Level.WARNING, ex.fillInStackTrace().toString());
		}
	}
	
}
