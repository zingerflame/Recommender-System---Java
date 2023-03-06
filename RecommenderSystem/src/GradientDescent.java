import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optim.nonlinear.scalar.GradientMultivariateOptimizer;
import org.apache.commons.math3.optim.BaseOptimizer;

public class GradientDescent {
    //we are trying to find optimal X and Theta values to minimize cost Function J (squared errors)
    public RealMatrix OptimizeX(double Jinitial, double lambda, double[][] X, double[][] Theta, double[][] X_grad, double[][] Theta_grad, double[][] Ynorm, double[][] R, int iterations, double alpha) {

        cofiCostFunc ccf = new cofiCostFunc();

        // convert 2D double array to RealMatrix format (we dont need the boolean matrix R as we can just run a for loop through it)
        RealMatrix Xm = MatrixUtils.createRealMatrix(X);
        RealMatrix Thetam = MatrixUtils.createRealMatrix(Theta);
        RealMatrix Ym = MatrixUtils.createRealMatrix(Ynorm);
        RealMatrix X_gradm = MatrixUtils.createRealMatrix(X_grad);
        RealMatrix Theta_gradm = MatrixUtils.createRealMatrix(Theta_grad);

        //initialize a few more vars
        int Xl1 = X.length;
        int Xl2 = X[1].length;
        int Thetal1 = Theta.length;
        int Thetal2 = Theta[1].length;

        //first step of descent:
        Xm = Xm.subtract(X_gradm.scalarMultiply(alpha)); //gradient descent on X
        Thetam = Thetam.subtract(Theta_gradm.scalarMultiply(alpha)); //gradient descent on Theta

        while (iterations > 0 ) {
            iterations--;
            //calculate cost function with descent
            double Jcurrent = ccf.JCalcMatrix(Xm, Thetam, Ym, R, lambda, Xl1, Xl2, Thetal1, Thetal2);

            //calculate NEW GRADIENTS : i.e. update X_gradm and Theta_gradm (hence the name gradient descent, heh)
            X_gradm = ccf.XgradCalcMatrix(Xm, Thetam, Ym, R, lambda, X_gradm);
            Theta_gradm = ccf.ThetagradCalcMatrix(Xm, Thetam, Ym, R, lambda, Theta_gradm);

            Xm = Xm.subtract(X_gradm.scalarMultiply(alpha)); //gradient descent on X
            Thetam = Thetam.subtract(Theta_gradm.scalarMultiply(alpha)); //gradient descent on Theta
        }
        // This code will repeat until convergence (local minimum of cost function J)

        return Xm;
    }

    public RealMatrix OptimizeTheta(double Jinitial, double lambda, double[][] X, double[][] Theta, double[][] X_grad, double[][] Theta_grad, double[][] Ynorm, double[][] R, int iterations, double alpha) {

        cofiCostFunc ccf = new cofiCostFunc();

        // convert 2D double array to RealMatrix format (we dont need the boolean matrix R as we can just run a for loop through it)
        RealMatrix Xm = MatrixUtils.createRealMatrix(X);
        RealMatrix Thetam = MatrixUtils.createRealMatrix(Theta);
        RealMatrix Ym = MatrixUtils.createRealMatrix(Ynorm);
        RealMatrix X_gradm = MatrixUtils.createRealMatrix(X_grad);
        RealMatrix Theta_gradm = MatrixUtils.createRealMatrix(Theta_grad);

        //initialize a few more vars
        int Xl1 = X.length;
        int Xl2 = X[1].length;
        int Thetal1 = Theta.length;
        int Thetal2 = Theta[1].length;

        //first step of descent:
        Xm = Xm.subtract(X_gradm.scalarMultiply(alpha)); //gradient descent on X
        Thetam = Thetam.subtract(Theta_gradm.scalarMultiply(alpha)); //gradient descent on Theta

        while (iterations > 0 ) {
            iterations--;
            //calculate cost function with descent
            double Jcurrent = ccf.JCalcMatrix(Xm, Thetam, Ym, R, lambda, Xl1, Xl2, Thetal1, Thetal2);

            //calculate NEW GRADIENTS : i.e. update X_gradm and Theta_gradm (hence the name gradient descent, heh)
            X_gradm = ccf.XgradCalcMatrix(Xm, Thetam, Ym, R, lambda, X_gradm);
            Theta_gradm = ccf.ThetagradCalcMatrix(Xm, Thetam, Ym, R, lambda, Theta_gradm);

            Xm = Xm.subtract(X_gradm.scalarMultiply(alpha)); //gradient descent on X
            Thetam = Thetam.subtract(Theta_gradm.scalarMultiply(alpha)); //gradient descent on Theta
        }
        // This code will repeat until convergence (local minimum of cost function J)

        return Thetam;
    }

}
