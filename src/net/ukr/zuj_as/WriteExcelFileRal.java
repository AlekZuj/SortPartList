package net.ukr.zuj_as;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteExcelFileRal {
	protected static final int NUMBER_COLUMN = 0;
	protected static final int DESIGNATION_COLUMN = 1;
	protected static final int NAME_COLUMN = 2;
	protected static final int MATERIAL_COLUMN = 3;
	protected static final int THICKNESS_COLUMN = 4;
	protected static final int RAL_AND_TYPE_COLUMN = 5;
	protected static final int COATING_SQUARE_COLUMN = 6;
	protected static final int WIDTH_HIGHT_LENGTH_COLUMN = 7;
	protected static final int QUANTITY_COLUMN = 8;

	private String projectName;

	public WriteExcelFileRal(String projectName) {
		super();
		this.projectName = projectName;
	}

	public WriteExcelFileRal() {
		super();
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void saveXLSX(String filePath, List<Part> partList) throws IOException {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet(projectName);
		makeTitle(wb);
		for (int i = 0; i < partList.size(); i++) {
			Row row = sheet.createRow(i + 2);
			Cell cellNum = row.createCell(NUMBER_COLUMN);
			Cell cellDes = row.createCell(DESIGNATION_COLUMN);
			Cell cellName = row.createCell(NAME_COLUMN);
			Cell cellMat = row.createCell(MATERIAL_COLUMN);
			Cell cellThick = row.createCell(THICKNESS_COLUMN);
			Cell cellRal = row.createCell(RAL_AND_TYPE_COLUMN);
			Cell cellCoatingSquare = row.createCell(COATING_SQUARE_COLUMN);
			Cell cellSize = row.createCell(WIDTH_HIGHT_LENGTH_COLUMN);
			Cell cellQua = row.createCell(QUANTITY_COLUMN);
			WriteExcelFile.makeRowStyle(wb, row, Color.WHITE, false, 14, true);
			cellNum.setCellValue(i + 1);
			cellDes.setCellValue(partList.get(i).getDesignation());
			if (partList.get(i).getDesignation() == null || partList.get(i).getDesignation().isEmpty()) {
				WriteExcelFile.makeRowStyle(wb, row, Color.RED, false, 14, true);
			}
			cellName.setCellValue(partList.get(i).getName());
			if (partList.get(i).getName().isEmpty()) {
				WriteExcelFile.makeRowStyle(wb, row, Color.RED, false, 14, true);
			}
			cellMat.setCellValue(partList.get(i).getMaterial());
			if (partList.get(i).getMaterial().isEmpty()) {
				WriteExcelFile.makeRowStyle(wb, row, Color.RED, false, 14, true);
			}
			if (partList.get(i).getThickness() < 0) {
				cellThick.setCellValue("");
			} else {
				cellThick.setCellValue(partList.get(i).getThickness());
			}
			if (partList.get(i).getThickness() < 0 && (partList.get(i).getMaterial().toLowerCase().contains("����")
					|| (partList.get(i).getMaterial().toLowerCase().contains("�����")
							&& !partList.get(i).getMaterial().toLowerCase().contains("az")))) {
				WriteExcelFile.makeRowStyle(wb, row, Color.RED, false, 14, true);
			}
			if (!partList.get(i).getRal().isEmpty()) {
				cellRal.setCellValue(partList.get(i).getRal() + " / " + partList.get(i).getCoatingType());
				cellCoatingSquare.setCellValue(partList.get(i).getCoatingSquare());
				cellSize.setCellValue(partList.get(i).getLength() + " x " + partList.get(i).getWidth() + " x "
						+ partList.get(i).getHeight());
			} else {
				cellRal.setCellValue("");
				cellCoatingSquare.setCellValue("");
				cellSize.setCellValue("");
			}
			cellQua.setCellValue(partList.get(i).getQuantity());
			if (partList.get(i).getQuantity() <= 0) {
				WriteExcelFile.makeRowStyle(wb, row, Color.RED, false, 14, true);
			}
		}
		WriteExcelFile.autoSizeColumns(wb);
		WriteExcelFile.makeAssembleList(wb, QUANTITY_COLUMN);
		WriteExcelFile.makeLegend(wb);
		File file = new File(filePath);
		FileOutputStream fos = new FileOutputStream(file);
		wb.write(fos);
		fos.close();
	}

	private Workbook makeTitle(Workbook wb) {
		Sheet sheet = wb.getSheetAt(0);
		Row rowTitle = sheet.createRow(0);
		Cell cellTitle = rowTitle.createCell(0);
		cellTitle.setCellValue("�������� ������� " + projectName);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
		Row rowTitleTwo = sheet.createRow(1);
		Cell cellTitleNum = rowTitleTwo.createCell(NUMBER_COLUMN);
		cellTitleNum.setCellValue("�������");
		Cell cellTitleDes = rowTitleTwo.createCell(DESIGNATION_COLUMN);
		cellTitleDes.setCellValue("�����������");
		Cell cellTitleName = rowTitleTwo.createCell(NAME_COLUMN);
		cellTitleName.setCellValue("������������");
		Cell cellTitleMat = rowTitleTwo.createCell(MATERIAL_COLUMN);
		cellTitleMat.setCellValue("��������");
		Cell cellTitleThick = rowTitleTwo.createCell(THICKNESS_COLUMN);
		cellTitleThick.setCellValue("������� ��������� �������");
		Cell cellTitleRal = rowTitleTwo.createCell(RAL_AND_TYPE_COLUMN);
		cellTitleRal.setCellValue("RAL / ��� ��������");
		Cell cellTitleCoatingSquare = rowTitleTwo.createCell(COATING_SQUARE_COLUMN);
		cellTitleCoatingSquare.setCellValue("������� ��������");
		Cell cellTitleSize = rowTitleTwo.createCell(WIDTH_HIGHT_LENGTH_COLUMN);
		cellTitleSize.setCellValue("������� �����");
		Cell cellTitleQau = rowTitleTwo.createCell(QUANTITY_COLUMN);
		cellTitleQau.setCellValue("���-��");
		WriteExcelFile.makeRowStyle(wb, rowTitle, Color.WHITE, true, 20, false);
		WriteExcelFile.makeRowStyle(wb, rowTitleTwo, Color.LIGHT_GRAY, true, 14, false);
		return wb;
	}
}
