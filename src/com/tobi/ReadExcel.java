package com.tobi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class ReadExcel  {

    private static int rCount;
    private static ArrayList<ArrayList<Double>> arrayOfArray = new ArrayList<>();

    public static void readExcelSheet(File file) throws Exception {
       // File file = new File("C:/users/emmanuel/desktop/VARGRADE.xlsx");
        FileInputStream inputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        XSSFRow row;
        rCount = -1;
        while (rowIterator.hasNext()) {
            row = (XSSFRow) rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            ArrayList<Double> array = new ArrayList<>();
            ++rCount;
            while (cellIterator.hasNext()) {
                Cell cell = (Cell) cellIterator.next();
                switch (cell.getCellType()) {
                    case NUMERIC:
                        array.add(cell.getNumericCellValue());
                        break;
                    case STRING:
                    case BOOLEAN:
                    case BLANK:
                        throw new NonNumericException();
                }
            }
            arrayOfArray.add(rCount, array);
        }
        inputStream.close();
    }

    private static double[][] arrayListToArray(ArrayList<ArrayList<Double>> arrayList){
        Object [] array = (Object[]) arrayList.toArray();
        double [][] ddd = null;
        for(int i = 0; i < array.length; i++){
            @SuppressWarnings("Unchecked")
            Object d [] = ((ArrayList<Double>)array[i]).toArray();
            if(i == 0)
                ddd = new double[array.length][d.length];
            for(int k = 0; k < d.length; k++){
                ddd[i][k] = (Double)d[k];
            }
        }
        return ddd;
    }
    public static double[][] getGrades(File file) throws Exception {
        readExcelSheet(file);
        return arrayListToArray(arrayOfArray);
    }

    static double[][] readGradesFromFileFormat(String  args){
        /*
        if(args.length == 0){
         System.out.println("Invalid Format :  Try java -classpath Filename");
         System.exit(1);
        }
       */
        boolean state = true;
        File file = new File(args);
        double myArray [][] = new double[3][3];
        try(FileReader fr = new FileReader(file);
            BufferedReader bf = new BufferedReader(fr);
            Scanner sc = new Scanner(bf);){
            while(sc.hasNextLine()){
                if(state){
                    String [] line = sc.nextLine().trim().split(" ");
                    myArray = new double[Integer.parseInt(line[0])][Integer.parseInt(line[1])];
                    state = false;
                }
                for (double[] myArray1 : myArray) {
                    String [] line =  sc.nextLine().trim().split(",");
                    for (int j = 0; j < line.length; j++) {
                        myArray1[j] = Double.parseDouble(line[j]);
                    }
                }
            }
            System.out.println(Arrays.deepToString(myArray));
        }catch(IOException e){}
        return myArray;
    }
}