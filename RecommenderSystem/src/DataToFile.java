import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;


public class DataToFile {

    public static double[][] Xdata = new double[1682][10];
    public static double[][] Thetadata = new double[943][10];
    public static double[][] Ydata = new double[1682][943];
    public static double[][] R = new double[1682][943];

    //load X parameters from text file into an array
    public static double[][] ConvertX() throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("data/X.txt"));

        try {
            for (int i = 0; i < Xdata.length; i++)
            {
                String[] elements = br.readLine().split(","); // 1 D array for each line = read and split by commas
                for (int j = 0; j < Xdata[i].length; j++ )
                {
                    Xdata[i][j] = Double.parseDouble(elements[j]); //converts text to double and stores it
                }
            }
            br.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return Xdata;
    }

    //same but for theta param
    public static double[][] ConvertTheta() throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("data/Theta.txt"));

        try {
            for (int i = 0; i < Thetadata.length; i++)
            {
                String[] elements = br.readLine().split(","); // 1 D array for each line = read and split by commas
                for (int j = 0; j < Thetadata[i].length; j++ )
                {
                    Thetadata[i][j] = Double.parseDouble(elements[j]); //converts text to double and stores it
                }
            }
            br.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return Thetadata;
    }

    // Y data (prediction ratings)
    public static double[][] ConvertY() throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("data/Y.txt"));

        try {
            for (int i = 0; i < Ydata.length; i++)
            {
                String[] elements = br.readLine().split(","); // 1 D array for each line = read and split by commas
                for (int j = 0; j < Ydata[i].length; j++ )
                {
                    Ydata[i][j] = Double.parseDouble(elements[j]); //converts text to double and stores it
                }
            }
            br.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return Ydata;
    }

    // R data (boolean Yes or No for user given rating (yes/no))

    public static double[][] ConvertR() throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("data/R.txt"));

        try {
            for (int i = 0; i < R.length; i++)
            {
                String[] elements = br.readLine().split(","); // 1 D array for each line = read and split by commas
                for (int j = 0; j < R[i].length; j++ )
                {
                    R[i][j] = Double.parseDouble(elements[j]); //converts text to double and stores it
                }
            }
            br.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return R;
    }
}
