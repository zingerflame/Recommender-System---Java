import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;

public class LoadMovies {

    public static String[] movies = new String[1682];

    public String[] getMovies() throws IOException{
        BufferedReader br = new BufferedReader(new FileReader("data/movie_ids.txt"));

        for (int i = 0; i < movies.length; i++) {
            String line = br.readLine();
            movies[i] = line;
        }
        return movies;
    }
}
