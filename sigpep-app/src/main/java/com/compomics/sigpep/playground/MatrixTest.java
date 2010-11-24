package com.compomics.sigpep.playground;

import com.compomics.sigpep.util.Combinations;

/**
 * Created by IntelliJ IDEA.<br>
 * User: mmueller<br>
 * Date: 12-Nov-2007<br>
 * Time: 15:03:26<br>
 */
public class MatrixTest {

    public static void main(String[] args) {

        Combinations c = new Combinations(2, "y2","y3","y4");
        while(c.hasNext()){
            System.out.println(c.next());    
        }

//        Matrix x = new Matrix(4, 5);
//        Matrix u1 = new Matrix(1, x.getRowDimension(), 1);
//        Matrix u2 = new Matrix(x.getColumnDimension(), 1, 1);
//
//
//        //column 1
//        x.set(0, 0, 0);
//        x.set(1, 0, 1);
//        x.set(2, 0, 0);
//        x.set(3, 0, 0);
//
//        //column 2
//        x.set(0, 1, 0);
//        x.set(1, 1, 0);
//        x.set(2, 1, 0);
//        x.set(3, 1, 1);
//
//        //column 3
//        x.set(0, 2, 1);
//        x.set(1, 2, 0);
//        x.set(2, 2, 0);
//        x.set(3, 2, 0);
//
//        //column 4
//        x.set(0, 3, 0);
//        x.set(1, 3, 0);
//        x.set(2, 3, 1);
//        x.set(3, 3, 0);
//
//        //column 5
//        x.set(0, 4, 0);
//        x.set(1, 4, 0);
//        x.set(2, 4, 1);
//        x.set(3, 4, 0);
//
//
//        //calculate row sum
//        //Matrix rowSum = new DenseMatrix(x.numRows(), u2.numColumns());
//        Matrix rowSum = x.times(u2);
//
//        //calculate col sum
//        //Matrix colSum = new DenseMatrix(u1.numRows(), x.numColumns());
//        Matrix colSum = u1.times(x);
//
//        System.out.println("matrix");
//        x.print(1,0);
//        System.out.println("column sums");
//        colSum.print(1,0);
//
//        //find minimal column sum starting from the right column
//        double min = x.getRowDimension();
//        int m = 0;
//        int minColIndex = -1;
//        for(int n = colSum.getColumnDimension() - 1; n >= 0; n--){
//
//            double sum = colSum.get(m,n);
//            if(sum < min){
//                min = sum;
//                minColIndex = n;
//            }
//
//        }
//
//        System.out.println("index of column with min. sum");
//        System.out.println(minColIndex);
//
//        int[] rowIndexes = new int[x.getRowDimension()];
//        for(int r = 0; r < x.getRowDimension(); r++)
//            rowIndexes[r] = r;
//
//
//
//        int [] colIndex =  {minColIndex};
//
//        System.out.println(Arrays.toString(rowIndexes));
//        System.out.println(Arrays.toString(colIndex));
//
//        Matrix minColumn = x.getMatrix(rowIndexes, colIndex);
//        System.out.println("column with min. sum");
//        minColumn.print(1,0);
//
//        //find next column which when merged with the minimum column produces a lower column sum
//        double mergedMin = min;
//        int mergedColIndex = -1;
//        for(int n = colSum.getColumnDimension() - 1; n >= 0; n--){
//
//            //skip min column
//            if(n == minColIndex)
//                continue;
//
//            int [] nextColIndex =  {n};
//            Matrix nextCol = x.getMatrix(rowIndexes, nextColIndex);
//            System.out.println("min col");
//            minColumn.print(1,0);
//            System.out.println("next col");
//            nextCol.print(1,0);
//
//
//            Matrix merge = SigPepUtil.intersection(minColumn,nextCol);
//            System.out.println("min column AND next column");
//
//
//            merge.print(1, 0);
//
//
//            //convert to absolute values
//
//            Matrix sum = u1.times(merge);
//            double newSum = sum.get(0,0);
//            if(newSum < mergedMin){
//
//                mergedMin = newSum;
//                mergedColIndex = n;
//            }
//
//
//            System.out.println("sum = " + sum.get(0,0));
//
//
//
//        }
//
//        System.out.println("mergedColIndex = " + mergedColIndex);
//        System.out.println("mergedMin = " + mergedMin);

    }
}
