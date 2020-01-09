package com.tobi.variogram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
/**
 *
 * @author Agboola Emmanuel
 */
public class VariogramCalculator {

    private static int count;

    public enum Direction {NOUTH_SOUTH, WEST_EAST};
/*
    public static double [][] grades ={{0.35,0.35,0.35,0.35,0.35,0.45,0.55,0.55,0.65},
                           {0.35,0.35,0.35,0.35,0.25,0.35,0.50,0.35,0.25},
                           {0.25,0.25,0.65,0.65,0.55,0.55,0.35,0.35,0.55},
                           {0.35,0.15,0.45,0.65,0.65,0.65,0.65,0.45,0.45},
                           {0.25,0.25,0.45,0.55,0.65,0.65,0.25,0.65,0.55},
                          };

    public static void  main (String [] args){
        for (int i = 1; i < 9; i++)
            VariogramCalculator.calVariogram(grades, i, Direction.WEST_EAST);
    }
*/
    static double[][] readGrades(String  args){
        /*
        if(args.length == 0){
         System.out.println("Invalid Format :  Try java -classpath Filename");
         System.exit(1);
        }
       */
        boolean state = true;
        File file = new File(args);
        double myArray [][] = new double[3][3];
        try( FileReader fr = new FileReader(file);
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

    public static String [] calVariogram(double [][] grades, int distance, VariogramCalculator.Direction d){
        String [] results = new String[3];
        if(d.equals(Direction.WEST_EAST)){
            results = VariogramCalculator.calVariogramNormal(grades, distance, d);}
        if(d.equals(Direction.NOUTH_SOUTH)){
            results = VariogramCalculator.calVariogramNormal(VariogramCalculator.toNorthSouth(grades), distance, d);
        }
        return results;
    }
    //Assuming array is rectangular,
    public static String[] calVariogramNormal(double [][] grades, int distance, VariogramCalculator.Direction d ){

        setCount(grades[0].length);
        StringBuffer sumOfGradesDiff = new StringBuffer();
        if(distance >= grades[0].length){
            System.out.println("Distances between grades must be less than row limit..");
            System.exit(1);
        }

        double sumOfSquares = 0;
        int distances = distance;
        int count = 0;

        List<Double> list = new ArrayList();

        for (double[] grade : grades) {
            int f = 0;
            Map<Integer , Boolean> map = new HashMap<>();
            Map<Integer , Integer> map1 = new HashMap<>();
            inner:
            for (int j = 0; j < grade.length; j++) {
                //when grade is the initial pair
                if (j == 0) {
                    sumOfGradesDiff.append("(").append(grade[j]).append(" - ");
                    System.out.print(grade[j] + "  ");
                    //map1.put(j , j + distances);
                    //when a corresponding pair occurs
                } else if (j  % distances == 0) {
                    if(f  ==  0){
                        if( j + distances >= (grade.length)){
                            sumOfGradesDiff.append(grade[j]).append(")2").append("\n");
                            System.out.print(grade[j] + "  ");
                            f = 1;
                            map1.put(j - distances , j);
                            ++count;
                            continue;
                        }
                        map1.put(j - distances , j);
                        sumOfGradesDiff.append(grade[j]).append(")2 + (").append(grade[j]);
                        System.out.print(grade[j] + "  ");
                        f = 1;
                        ++count;
                        continue;
                    }
                    if(f == 1){
                        if( (j + distances) >= (grade.length) ){
                            map1.put(j - distances , j );
                            ++count;
                            sumOfGradesDiff.append(" - ").append(grade[j]).append(")2\n");
                            System.out.print(grade[j] + "  ");
                            continue;
                        }
                        map1.put( j - distances , j);
                        System.out.print(grade[j] + "  ");
                        ++count;
                        sumOfGradesDiff.append(" - ").append(grade[j]).append(")2 + (").append(grade[j]);
                        //++f;
                    }
                }else{
                    //when grade distancs is offset i.e a round of pairing occurs
                    if( (j + distances) < grade.length  ){
                        if( (j + distances + distances) >= (grade.length) ){
                            Integer i = j + distances;
                            Boolean b = true;
                            map.put(i , b) ;
                        }
                        map1.put(j , j + distances );
                        ++count;
                        System.out.print(grade[j] + "  ");
                        continue;
                    }
                    //when grade has no pair+
                    if( map.get(j) != null ){
                        // map1.put(j - distances , j );
                        System.out.print(grade[j] + "  ");
                    }
                }
            }

            Set<Integer> lists = map1.keySet();
            double sum = 0;
            for (Integer n : lists) {
                sum += (Math.pow(grade[n] - grade[(int)map1.get(n)] , 2));
                System.out.print("...........("+(grade[n] +"-"+ grade[(int)map1.get(n)])+")2\t");
                System.out.println("........."+sum);
            }
            //sum = lists.stream().map((i) -> Math.pow((grade[i]  - grade[(int)map1.get(i)]), 2)).reduce(sum, (accumulator, _item) -> accumulator + _item); // System.out.print("( "+grade[i]+" - "+grade[(int)map1.get(i)]+" ) +");
            list.add(sum );
            System.out.println();
        }

        for(Double n : list){
            sumOfSquares += n;
        }
        //  sumOfSquares = list.stream().map((l) -> l).reduce(sumOfSquares, (accumulator, _item) -> accumulator + _item);

        String r [] = new String[3];
        double sumSqr = StrictMath.round(sumOfSquares * 1000d) / 1000d;
        double variogramValue = StrictMath.round((sumOfSquares / (2 * count))* 1000d ) / 1000d;
        r[0] = ""+distances;
        r[1] = ""+count;
        r[2] = ""+ variogramValue;

        System.out.println(" sum of squares of differences = "+ (sumSqr));
        System.out.println(" No of pairs = "+count);
        System.out.println(" Variogram Value = " + variogramValue);
        System.out.println(sumOfGradesDiff);
        return r;
    }
    //To Change Direction From West-East to North-South
    public static double [][] toNorthSouth( double [][] grades){
        double [][] copyGrades = new double[grades[0].length][grades.length];
        //System.out.println(copyGrades[0].length);
        //System.out.println(copyGrades.length);
        for(int m = 1; m <= grades[0].length; m++){
            for(int j = 1; j > 0 ; j-- ){
                int u = 0;
                for(int k = grades.length - 1 ; k >= 0; k--){
                    copyGrades[m - 1][u] = grades[k][m -1];
                    ++u;
                }
            }
        }
        return copyGrades;
    }
    public static int getCount() {
        return count;
    }
    public static void setCount(int count) {
        VariogramCalculator.count = count;
    }
}
