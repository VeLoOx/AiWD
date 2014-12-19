package pl.aiwd.parsers.datafiles;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CVSParser {

	private String separator = ";";
	private String cvsFilePath = "D:\\Studia\\przedmioty2_2\\AiWD\\Projekt2\\git\\FCM\\Data\\";
	private String cvsFileName = "data.csv";
	private String newCvsFileName = "normalizedata.csv";
	private String line = "";

	/*
	 * Key = nazwa kolumny; Value = double[] -> double[0] = MIN; double[1] = max
	 * 
	 * MIN - MAX - uzywane do normalizacji MIN - MAX z przedzialu [0,1]
	 */
	private Map<String, double[]> columnMinMaxMap = new HashMap<String, double[]>();
	private int columnNumber = 0;
	private String[] columnNames;
	private long linesNumber = 0;

	private boolean show = true; // czy wyswietlac komunikaty

	private void initializeMinMaxMap(String line) {
		columnNames = line.split(separator);
		columnNumber = columnNames.length;

		for (int i = 0; i < columnNumber; i++) {
			double[] tab = new double[2];
			tab[0] = Double.MAX_VALUE; // initial MIN VAL
			tab[1] = Double.MIN_VALUE; // initial MAX val

			columnMinMaxMap.put(columnNames[i], tab);
		}

		linesNumber++;
	}

	public FileInputStream loadFile(String path, String name) {
		if (path == null) {
			path = cvsFilePath;
			name = cvsFileName;
		}

		try {
			FileInputStream f = new FileInputStream(path + name);
			return f;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String takeNumbNormalizeAndRetString(String numb, double min,
			double max, double newMin, double newMax) {
		String n = "";

		double val = Double.parseDouble(numb);

		double newVal = ((val - min) / (max - min)) * (newMax - newMin)
				+ newMin;
		n = String.valueOf(newVal);

		return n;
	}

	// ############ samo miesko
	public void procesMinMaxFile(FileInputStream fis) {

		InputStreamReader isr = null;

		BufferedReader reader = null;
		try {

			fis.getChannel().position(0);
			isr = new InputStreamReader(fis);
			reader = new BufferedReader(isr);

			line = reader.readLine();
			initializeMinMaxMap(line);

			while ((line = reader.readLine()) != null) {
				String[] col = line.split(separator);
				for (int i = 0; i < col.length; i++) {
					// przetwarzanie kazdej lini cvs

					double[] tab = columnMinMaxMap.get(columnNames[i]);

					double val = Double.parseDouble(col[i]);
					if (val < tab[0])
						tab[0] = val;
					if (val > tab[1])
						tab[1] = val;

					columnMinMaxMap.put(columnNames[i], tab);

					if (show)
						System.out.print(col[i] + " ");
				}
				if (show)
					System.out.println(" DONE");

				linesNumber++;
			}
		} catch (IOException e) {

		}
	}

	public void procesNormalizeFile(String path, String name,
			FileInputStream fis, double newMin, double newMax) {

		if (path == null) {
			path = cvsFilePath;
			name = newCvsFileName;
		}

		InputStreamReader isr = null;
		BufferedReader reader = null;
		PrintWriter out = null;
		try {
			out = new PrintWriter(path + name);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			fis.getChannel().position(0);
			isr = new InputStreamReader(fis);
			reader = new BufferedReader(isr);

			line = reader.readLine(); // pominiecie lini z nazwami kolumn
			out.println(line);

			while ((line = reader.readLine()) != null) {

				String[] col = line.split(separator);
				String newLine = "";

				for (int i = 0; i < col.length; i++) {
					double[] mm = columnMinMaxMap.get(columnNames[i]);

					String newVal = takeNumbNormalizeAndRetString(col[i],
							mm[0], mm[1], newMin, newMax);

					newLine = newLine + newVal + ";"; // utworzenie nowej lini
				}

				out.println(newLine.substring(0, newLine.length() - 1));
			}

			out.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// ################ wyswietlanie

	public void showMap() {
		System.out.println("\nNAZWA :   MIN VAL    :    MAX VAL   ");
		for (int i = 0; i < columnNumber; i++) {
			double[] tab = columnMinMaxMap.get(columnNames[i]);
			System.out.println(columnNames[i] + ":   MIN=" + tab[0] + "   MAX="
					+ tab[1]);
		}
		System.out.println("\nPrzeczytano lacznie " + linesNumber + " lini");
	}

	public void setComunicates(boolean s) {
		show = s;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public Map<String, double[]> getColumnMinMaxMap() {
		return columnMinMaxMap;
	}

	public void setColumnMinMaxMap(Map<String, double[]> columnMinMaxMap) {
		this.columnMinMaxMap = columnMinMaxMap;
	}

	public int getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public static void main(String[] args) {
		CVSParser cvsp = new CVSParser();
		cvsp.setComunicates(false);

		FileInputStream readFile = cvsp.loadFile(null, null);

		cvsp.procesMinMaxFile(readFile);

		cvsp.showMap();

		cvsp.procesNormalizeFile(null, null, readFile, 0, 1);

		try {
			readFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
