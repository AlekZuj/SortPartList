//v 0.1-0.4 - try to read and write file? sorting AllayList.
//v 0.5 - introduced design and reminder elements. Testing started.
//v 0.6 - changed methods of making cell styles. NULL check added in delete enter method.

package net.ukr.zuj_as;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) {
		String version = "0.6";
		System.out.println("SortPartList V " + version + " by AlekZ");
		Scanner sc = new Scanner(System.in);
		System.out.print("Inpun target file path (E:\\First\\List.xlsx): ");
		String inputFilePath = sc.nextLine();
		System.out.print("Input project name (AV03 003000159B): ");
		String projectName = sc.nextLine();
		long tstart = System.currentTimeMillis();
		ReadExcelFile ref = new ReadExcelFile();
		List<Part> list = new ArrayList<Part>();
		String strRead = "";
		String strSort = "";
		String strWrite = "";
		try {
			list = ref.readXLSX(inputFilePath);
		} catch (NumberFormatException | IOException e) {
			strRead = e.toString();
		}

		try {
			SortPartList.removeStdParts(list);
			SortPartList.removeVnsItem(list);
			SortPartList.removeStupidItem(list);
			SortPartList.fillMaterial(list);
			SortPartList.delThickness(list);
			SortPartList.makeDesignationBetter(list);
		} catch (IOException e1) {
			strSort = e1.toString();
		}

		WriteExcelFile wef = new WriteExcelFile(projectName);
		String outputFilePath = filePathMaker(inputFilePath, projectName);
		WriteExcelFileRal wefLong = new WriteExcelFileRal(projectName);

		try {
			wef.saveXLSX(outputFilePath, list);
			wefLong.saveXLSX(filePathMakerRal(outputFilePath), list);
		} catch (IOException e) {
			strWrite = e.toString();
		}

		System.out.println("Done!");
		long tend = System.currentTimeMillis();
		System.out.println("Working time: " + (tend - tstart) + " ms");
		try {
			logFileWriter(projectName, strRead, strSort, strWrite, (tend - tstart), version);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sc.close();

		for (int i = 0; i < 5; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void logFileWriter(String projectName, String strRead, String strSort, String strWrite, long time,
			String version) throws IOException, UnknownHostException {
		File logFile = new File("LogFile.txt");
		List<String> log = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				log.add(line);
			}
		} catch (IOException e) {
			throw e;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss / dd.MM.yyyy");
		try {
			log.add(sdf.format(new Date()) + " V" + version + " " + InetAddress.getLocalHost().getHostName() + " "
					+ projectName + " " + strRead + " " + strSort + " " + strWrite + " " + time + "ms");
		} catch (UnknownHostException e) {
			throw e;
		}
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFile))) {
			for (int i = 0; i < log.size(); i++) {
				bw.write(log.get(i) + System.lineSeparator());
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static String filePathMaker(String filePath, String projectName) {
		int index = filePath.lastIndexOf("\\");
		StringBuilder sb = new StringBuilder();
		sb.append(filePath.substring(0, index));
		sb.append("\\Перечень деталей " + projectName + ".xlsx");
		return sb.toString();
	}

	public static String filePathMakerRal(String filePath) {
		int n = filePath.indexOf(".xlsx");
		StringBuilder sb = new StringBuilder();
		sb.append(filePath.substring(0, n));
		sb.append(" с покраской.xlsx");
		return sb.toString();
	}

}
