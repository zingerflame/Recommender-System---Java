import org.apache.commons.lang3.ArrayUtils;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class NormalizeRatings {

    public double[][] Normalize(double[][] Y) {
        //Preprocess data by subtracting mean rating for every movie (every row)
        //Matrix size is 1682x943
        //normalized Y so that each movie has a rating of 0 on average, and returns the mean rating in Ymean

        double[] Ymean = new double[Y.length];
        double[][] Ynorm = new double[Y.length][Y[1].length]; //same size as Y

        //to normalize we need to exclude all zero elements (Finding Ymean)

        //take the average
        for (int i = 0; i < Y.length; i++) {
            double row_avg = 0;
            int nonzeros = 0; //number of things to divide by when calculating mean
            for (int j = 0; j < Y[i].length; j++) {
                row_avg += Y[i][j];
                if (Y[i][j] != 0) {
                    nonzeros += 1; // add onto counter
                }
            }
            if (nonzeros == 0) {
                Ymean[i] = 0;
            }
            else {
                Ymean[i] = row_avg / nonzeros; //returns a vector of means for each moviee
                //System.out.println(Ymean[i]); - debugging
            }
        }
        //normalization - subtract mean from every row
        for (int i = 0; i < Y.length; i++ ){
            for (int j = 0; j < Y[i].length; j++) {
                if (Y[i][j] != 0) { //if it has a rating, normalize it by subtracting the mean, otherwise don't
                    Ynorm[i][j] = Y[i][j] - Ymean[i]; //update into Ynorm
                }
            }
        }

        return Ynorm;
    }

    public double[] getMean(double[][] Y) {

        double[] Ymean = new double[Y.length];

        //take the average
        for (int i = 0; i < Y.length; i++) {
            double row_avg = 0;
            int nonzeros = 0; //number of things to divide by when calculating mean
            for (int j = 0; j < Y[i].length; j++) {
                if (Y[i][j] != 0) {
                    row_avg += Y[i][j];
                    nonzeros += 1; // add onto counter
                }
            }
            if (nonzeros == 0) {
                Ymean[i] = 0;
            }
            else {
                Ymean[i] = row_avg / nonzeros; //returns a vector of means for each moviee
                //System.out.println(Ymean[i]); - debugging
            }
        }

        return Ymean;
    }
}
