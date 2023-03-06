import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.*;

public class MainProgram {

    //initialize data arrays
    static double[][] Xdata = new double[1682][10];
    static double[][] Thetadata = new double[943][10];
    static double[][] Ydata = new double[1682][943];
    static double[][] R = new double[1682][943];

    static String[] movies = new String[1682];

    public static void main(String[] args) throws IOException {
        //initialize classes
        //load into arrays
        DataToFile dtf = new DataToFile();
        cofiCostFunc ccf = new cofiCostFunc();
        Scanner sc = new Scanner(System.in);
        LoadMovies loadm = new LoadMovies();

        //convert data first into usable array format
        //FINAL IMPLEMENTATION NOTE: X and Theta are randomized and should not be imported when running a collab filtering from scratch
       // Xdata = dtf.ConvertX();

       // Thetadata = dtf.ConvertTheta();

        Ydata = dtf.ConvertY();

        R = dtf.ConvertR();

        //Now: get user to input their ratings (1682 movies in dataset)
        double[] my_ratings = new double[Ydata.length]; //stores ratings

        System.out.println("The list of movies can be accessed at the movies_ids.txt file");
        System.out.println("Enter how many ratings you would like to input");
        int n = sc.nextInt();
        for (int i=0; i< n ; i++) {
            System.out.println("Which movie ID would you like to enter a rating for");
            int l = sc.nextInt();
            System.out.println("What rating will you assign it (1-5)?");
            int p = sc.nextInt();
            //error check
            if (p > 5 || p < 1) {
                System.out.println("Please enter a valid rating");
            }
            else {
                my_ratings[l-1] = p; //index starts from 0, but movie list starts from 1
            }
        }

        //load movies file
        movies = loadm.getMovies();
        //output given ratings to user
        for (int i = 0; i < my_ratings.length; i++) {
            if (my_ratings[i] > 0) {
                System.out.println("Rating for movie "+movies[i]+" is "+my_ratings[i]); //imported text file is correct for indexing
            }
        }

        //add own ratings to Ydata matrix (append column to the right)
        double[][] YdataUser = new double[Ydata.length][Ydata[1].length+1];
        //first copy Ydata into this new matrix
        for (int i = 0; i < Ydata.length; i++) {
            System.arraycopy(Ydata[i], 0, YdataUser[i], 0, Ydata[i].length);
        }
        //add my_ratings vector onto final column
        for (int i = 0; i < my_ratings.length; i++) {
            YdataUser[i][Ydata[i].length] = my_ratings[i];
        }

        //repeat for R matrix
        double[][] RUser = new double[R.length][R[1].length+1];
        //copy R into new matrix
        for (int i = 0; i < R.length; i++) {
            System.arraycopy(R[i],0, RUser[i], 0, R[i].length);
        }
        //add binary / boolean 1&0 (actually in double format) vector onto the end
        double[] my_ratingsBool = new double[my_ratings.length]; //stores ratings booleans
        for (int i = 0; i < my_ratingsBool.length; i++) {
            if (my_ratings[i] != 0) {
                my_ratingsBool[i] = 1;
            }
            else{
                my_ratingsBool[i] = 0;
            }
        }
        //append
        for (int i = 0; i < my_ratingsBool.length; i++) {
            RUser[i][R[1].length] = my_ratingsBool[i];
        }


        //normalize data
        NormalizeRatings nr = new NormalizeRatings();
        double[] Ymean = nr.getMean(YdataUser);
        YdataUser = nr.Normalize(YdataUser); //update this
        //get the mean for later (add onto rating to even it out again)


        //Randomize X and Theta
        double[][] X = new double[Xdata.length][Xdata[1].length];
        double[][] Theta = new double[Thetadata.length+1][Thetadata[1].length]; //+1 because you are the new user added (Theta is a matrix of parameters for users)
        Random r = new Random();
        for(int i = 0; i < X.length; i++) {
            for (int j = 0; j < X[i].length; j++) {
                X[i][j] = 0.1 * r.nextGaussian();
            }
        }
        for(int i = 0; i < Theta.length; i++) {
            for (int j = 0; j < Theta[i].length; j++) {
                Theta[i][j] = 0.1 * r.nextGaussian();
            }
        }

        //GRADIENT DESCENT OPTIMIZE VARIABLES X AND THETA

        //compute gradients and cost with initial X and Theta
        double J = 0;
        J = ccf.JCalc(X, Theta, YdataUser, RUser, 10);
        System.out.println("Cost function is " +J);

        //initialize processed/ calculation arrays
        double[][] X_grad = new double[X.length][X[1].length];
        double[][] Theta_grad = new double[Theta.length][Theta[1].length];
        X_grad = ccf.XgradCalc(X, Theta, YdataUser, RUser, 10, X_grad);
        Theta_grad = ccf.ThetagradCalc(X, Theta, YdataUser, RUser, 10, Theta_grad);

        GradientDescent gd = new GradientDescent();
        //calculate the optimal parameters
        //iterations = 50 is a good amount with learning rate 0.001 (not too slow, will minimize function effectively)
        //parameters stored in these Xm and Thetam realmatrices
        System.out.println("Calculating X Params...");
        RealMatrix Xm = gd.OptimizeX(J, 10, X, Theta, X_grad, Theta_grad, YdataUser, RUser, 50, 0.001);
        System.out.println("Calculating Theta Params...");
        RealMatrix Thetam = gd.OptimizeTheta(J, 10, X, Theta, X_grad, Theta_grad, YdataUser, RUser, 50, 0.001);

        //Finally: predict ratings for users
        RealMatrix p = Xm.multiply(Thetam.transpose()); // 1682 x 10 * 10 x 944 matrix = 1682 x 944 matrix
        //get my predictions (in vector format)
        RealVector my_predictions = p.getColumnVector(YdataUser[1].length-1); //our predictions will be in the last column

        //Ymean convert to realVector format
        RealVector Ymeanm = MatrixUtils.createRealVector(Ymean);

        my_predictions = my_predictions.add(Ymeanm);

        //convert to vector format
        double[] pred = my_predictions.toArray();
        //for int 1-10
        //copy
        RealVector maxarray = my_predictions.copy();
        //store all max ratings
        int[] k = new int[10];
        for (int i = 0; i < 10; i++) {
            k[i] = maxarray.getMaxIndex();
            maxarray.setEntry(k[i], 0);
            //loops to find 10 highest ones and appends to k
        }

        //sort by ascending order
        Arrays.sort(pred);

        //output but print descending order so top recommendations are on top
        System.out.println("Top recommendations for you:");
        for (int i = 0; i < 10; i++) {
            System.out.println(movies[k[i]]);
        }

        //print ratings
        for (int i = my_predictions.getDimension()-1; i > my_predictions.getDimension()-11; i--) {
                System.out.println(pred[i]);
        }

    }
}

