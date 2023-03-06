import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class cofiCostFunc {

    //calculate the cost function J
    public double JCalc(double[][] Xdata, double[][] Thetadata, double[][] Ydata, double[][] R, double lambda) {
        double J = 0; //cost compute

        // convert 2D double array to RealMatrix format (we dont need the boolean matrix R as we can just run a for loop through it)
        RealMatrix X = MatrixUtils.createRealMatrix(Xdata);
        RealMatrix Theta = MatrixUtils.createRealMatrix(Thetadata);
        RealMatrix Y = MatrixUtils.createRealMatrix(Ydata);

        //cost function
        RealMatrix m1 = X.multiply(Theta.transpose());
        RealMatrix m2 = m1.subtract(Y);

        //element by element Multiplication of R times Matrix X*Theta'-Y (m2)
        for (int i = 0; i < R.length; i++) {
            for (int j = 0; j < R[i].length; j++) {
                m2.multiplyEntry(i, j, R[i][j]);
            }
        }

        //element by element Square
        for (int i = 0; i < R.length; i++) {
            for (int j = 0; j < R[i].length; j++) {
                m2.multiplyEntry(i, j, m2.getEntry(i, j));
            }
        }

        //sum into J
        for (int i = 0; i < R.length; i++) {
            for (int j = 0; j < R[i].length; j++) {
                J+=  m2.getEntry(i,j);
            }
        }
        J = 0.5*J;
        //add regularization params (J + (lambda/2)(sum(sum(X^2)) + sum(sum(Theta^2)))
        double sumX = 0;
        double sumTheta = 0;
        //square all X
        for (int i = 0; i < Xdata.length; i++) {
            for (int j = 0; j < Xdata[i].length; j++) {
                X.multiplyEntry(i, j, X.getEntry(i, j));
                sumX += X.getEntry(i,j);
            }
        }
        //square all Theta
        for (int i = 0; i < Thetadata.length; i++) {
            for (int j = 0; j < Thetadata[i].length; j++) {
                Theta.multiplyEntry(i, j, Theta.getEntry(i, j));
                sumTheta += Theta.getEntry(i,j);
            }
        }

        J += (lambda/2)*(sumX) + (lambda/2)*(sumTheta);

        return J;
    }

    //calculate the cost function J for Matrix Format
    public double JCalcMatrix(RealMatrix X, RealMatrix Theta, RealMatrix Y, double[][] R, double lambda, int Xl1, int Xl2, int Thetal1, int Thetal2) {
        double J = 0; //cost compute

        //cost function
        RealMatrix m1 = X.multiply(Theta.transpose());
        RealMatrix m2 = m1.subtract(Y);

        //element by element Multiplication of R times Matrix X*Theta'-Y (m2)
        for (int i = 0; i < R.length; i++) {
            for (int j = 0; j < R[i].length; j++) {
                m2.multiplyEntry(i, j, R[i][j]);
            }
        }

        //element by element Square
        for (int i = 0; i < R.length; i++) {
            for (int j = 0; j < R[i].length; j++) {
                m2.multiplyEntry(i, j, m2.getEntry(i, j));
            }
        }

        //sum into J
        for (int i = 0; i < R.length; i++) {
            for (int j = 0; j < R[i].length; j++) {
                J+=  m2.getEntry(i,j);
            }
        }
        J = 0.5*J;
        //add regularization params (J + (lambda/2)(sum(sum(X^2)) + sum(sum(Theta^2)))
        double sumX = 0;
        double sumTheta = 0;
        //square all X
        for (int i = 0; i < Xl1; i++) {
            for (int j = 0; j < Xl2; j++) {
                X.multiplyEntry(i, j, X.getEntry(i, j));
                sumX += X.getEntry(i,j);
            }
        }
        //square all Theta
        for (int i = 0; i < Thetal1; i++) {
            for (int j = 0; j < Thetal2; j++) {
                Theta.multiplyEntry(i, j, Theta.getEntry(i, j));
                sumTheta += Theta.getEntry(i,j);
            }
        }

        J += (lambda/2)*(sumX) + (lambda/2)*(sumTheta);

        return J;
    }

    public double[][] XgradCalc(double[][] Xdata, double[][] Thetadata, double[][] Ydata, double[][] R, double lambda, double[][] X_grad) {
        //convert data to workable matrices
        RealMatrix X = MatrixUtils.createRealMatrix(Xdata);
        RealMatrix Theta = MatrixUtils.createRealMatrix(Thetadata);
        RealMatrix Y = MatrixUtils.createRealMatrix(Ydata);

        //calculate Xgrad:
        RealMatrix m1 = X.multiply(Theta.transpose());
        RealMatrix m2 = m1.subtract(Y);
        //element by element Multiplication of R times Matrix X*Theta'-Y (m2)
        for (int i = 0; i < R.length; i++) {
            for (int j = 0; j < R[i].length; j++) {
                m2.multiplyEntry(i, j, R[i][j]);
            }
        }

        RealMatrix m3 = m2.multiply(Theta);
        m3 = m3.add(X.scalarMultiply(lambda)); //regularization

        for (int i = 0; i < Xdata.length; i++) {
            for (int j = 0; j < Xdata[i].length; j++) {
                X_grad[i][j] = m3.getEntry(i,j);
            }
        }

        return X_grad; //X_grad matrix - gradient matrix of X values for grad descent (partial derivatives)

    }

    public RealMatrix XgradCalcMatrix(RealMatrix X, RealMatrix Theta, RealMatrix Y, double[][] R, double lambda, RealMatrix X_grad) {

        //calculate Xgrad:
        RealMatrix m1 = X.multiply(Theta.transpose());
        RealMatrix m2 = m1.subtract(Y);
        //element by element Multiplication of R times Matrix X*Theta'-Y (m2)
        for (int i = 0; i < R.length; i++) {
            for (int j = 0; j < R[i].length; j++) {
                m2.multiplyEntry(i, j, R[i][j]);
            }
        }

        RealMatrix m3 = m2.multiply(Theta);
        m3 = m3.add(X.scalarMultiply(lambda)); //regularization

        X_grad = m3;

        return X_grad; //X_grad matrix - gradient matrix of X values for grad descent (partial derivatives)

    }

    public double[][] ThetagradCalc(double[][] Xdata, double[][] Thetadata, double[][] Ydata, double[][] R, double lambda, double[][] Theta_grad) {
        //convert data to workable matrices
        RealMatrix X = MatrixUtils.createRealMatrix(Xdata);
        RealMatrix Theta = MatrixUtils.createRealMatrix(Thetadata);
        RealMatrix Y = MatrixUtils.createRealMatrix(Ydata);

        //calculate
        RealMatrix m1 = X.multiply(Theta.transpose());
        RealMatrix m2 = m1.subtract(Y);
        //element by element Multiplication of R times Matrix X*Theta'-Y (m2)
        for (int i = 0; i < R.length; i++) {
            for (int j = 0; j < R[i].length; j++) {
                m2.multiplyEntry(i, j, R[i][j]);
            }
        }

        RealMatrix m3 = m2.transpose().multiply(X);
        m3 = m3.add(Theta.scalarMultiply(lambda)); //regularization


        for (int i = 0; i < Thetadata.length; i++) {
            for (int j = 0; j < Thetadata[i].length; j++) {
                Theta_grad[i][j] = m3.getEntry(i,j);
            }
        }

        return Theta_grad; //Theta_grad matrix - gradient matrix of Theta values for grad descent (partial derivatives)

    }

    public RealMatrix ThetagradCalcMatrix(RealMatrix X, RealMatrix Theta, RealMatrix Y, double[][] R, double lambda, RealMatrix Theta_grad) {

        //calculate
        RealMatrix m1 = X.multiply(Theta.transpose());
        RealMatrix m2 = m1.subtract(Y);
        //element by element Multiplication of R times Matrix X*Theta'-Y (m2)
        for (int i = 0; i < R.length; i++) {
            for (int j = 0; j < R[i].length; j++) {
                m2.multiplyEntry(i, j, R[i][j]);
            }
        }

        RealMatrix m3 = m2.transpose().multiply(X);
        m3 = m3.add(Theta.scalarMultiply(lambda)); //regularization

        Theta_grad = m3;

        return Theta_grad; //Theta_grad matrix - gradient matrix of Theta values for grad descent (partial derivatives)

    }

}
