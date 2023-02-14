package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

public class FletcherPowell10 extends DoubleProblem {

    public double[][] a;
    public double[][] b;
    public double[] alpha;

    public FletcherPowell10() {
        super("FletcherPowell10", 10, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -PI));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, PI));

        a = new double[][]{
                {-79, 56, -62, -9, 92, 48, -22, -34, -39, -40, -95, -69, -20, -66, -98, -66, -67, 37, -83, -45},
                {91, -9, -18, -59, 99, -45, 88, -14, -29, 26, 71, -65, 19, 45, 88, 18, -11, -81, -10, 42},
                {-38, 8, -12, -73, 40, 26, -64, 29, -82, -32, -89, -3, 88, 98, 53, 58, 45, -39, 34, -23},
                {-78, -18, -49, 65, 66, -40, 88, -95, -57, 10, -98, -11, -16, -55, 33, 84, 21, -43, 45, 100},
                {-1, -43, 93, -18, -76, -68, -42, 22, 46, -14, 69, 27, -12, -26, 57, -13, 0, 1, 56, 17},
                {34, -96, 26, -56, -36, -85, -62, 13, 93, 78, -43, 96, 77, 65, -34, -52, 82, 18, -59, -55},
                {52, -46, -69, 99, -47, -72, -11, 55, -55, 91, -30, 7, -35, 23, -20, 55, 61, -39, -58, 13},
                {81, 47, 35, 55, 67, -13, 33, 14, 83, -42, 8, -45, -44, 12, 100, -9, -33, -11, 21, 14},
                {5, -43, -45, 46, 56, -94, -62, 52, 66, 55, -86, -29, -52, -71, -91, -46, 27, -27, 6, 67},
                {-50, 66, -47, -75, 89, -16, 82, 6, -85, -62, -30, 31, -7, -75, -26, -24, 46, -95, -71, -57},
                {24, 98, -50, 68, -97, -64, -24, 81, -59, -7, 85, -92, 2, 61, 52, -59, -91, 74, -99, -95},
                {-30, -63, -32, -90, -35, 44, -64, 57, 27, 87, -70, -39, -18, -89, 99, 40, 14, -58, -5, -42},
                {56, 3, 88, 38, -14, -15, 84, -9, 65, -20, -75, -37, 74, 66, -44, 72, 74, 90, -83, -40},
                {84, 1, 73, 43, 84, -99, -35, 24, -78, -58, 47, -83, 94, -86, -65, 63, -22, 65, 50, -40},
                {-21, -8, -48, 68, -91, 17, -52, -99, -23, 43, -8, -5, -98, -17, -62, -79, 60, -18, 54, 74},
                {35, 93, -98, -88, -8, 64, 15, 69, -65, -86, 58, -44, -9, -94, 68, -27, -79, -67, -35, -56},
                {-91, 73, 51, 68, 96, 49, 10, -13, -6, -23, 50, -89, 19, -67, 36, -97, 0, 3, 1, 39},
                {53, 66, 23, 10, -33, 62, -73, 22, -65, 37, -83, -65, 59, -51, -56, 98, -57, -11, -48, 88},
                {83, 48, 67, 27, 91, -33, -90, -34, 39, -36, -68, 17, -7, 14, 11, -10, 96, 98, -32, 56},
                {52, -52, -5, 19, -25, 15, -1, -11, 8, -70, -4, -7, -4, -6, 48, 88, 13, -56, 85, -65}
        };
        b = new double[][]{
                {-65, -11, 76, 78, 30, 93, -86, -99, -37, 52, -20, -10, -97, -71, 16, 9, -99, -84, 90, -18, -94},
                {59, 67, 49, -45, 52, -33, -34, 29, -39, -80, 22, 7, 3, -19, -15, 7, -83, -4, 84, -60, -4},
                {21, -23, -80, 86, 86, -30, 39, -73, -91, 5, 83, -2, -45, -54, -81, -8, 14, 83, 73, 45, 32},
                {-91, -75, 20, -64, -15, 17, -89, 36, -49, -2, 56, -6, 76, 56, 2, -68, -59, -70, 48, 2, 24},
                {-79, 99, -31, -8, -67, -72, -43, -55, 76, -57, 1, -58, 3, -59, 30, 32, 57, 29, 66, 50, -80},
                {-89, -35, -55, 75, 15, -6, -53, -56, -96, 87, -90, -93, 52, -86, -38, -55, -53, 94, 98, 4, -79},
                {-76, 45, 74, 12, -12, -69, 2, 71, 75, -60, -50, 23, 0, 6, 44, -82, 37, 91, 84, -15, -63},
                {-50, -88, 93, 68, 10, -13, 84, -21, 65, 14, 4, 92, 11, 67, -18, -51, 4, 21, -38, 75, -59},
                {-23, -95, 99, 62, -37, 96, 27, 69, -64, -92, -12, 87, 93, -19, -99, -92, -34, -77, 17, -72, 29},
                {-5, -57, -30, -6, -96, 75, 25, -6, 96, 77, -35, -10, 82, 82, 97, -39, -65, -8, 34, 72, 65},
                {85, -9, -14, 27, -45, 70, 55, 26, -87, -98, -25, -12, 60, -45, -24, -42, -88, -46, -95, 53, 28},
                {80, -47, 38, -6, 43, -59, 91, -41, 90, -63, 11, -54, 33, -61, 74, 96, 21, -77, -58, -75, -9},
                {-66, -98, -4, 96, -11, 88, -99, 5, 5, 58, -53, 52, -98, -97, 50, 49, 97, -62, 79, -10, -80},
                {80, -95, 82, 5, -68, -54, 64, -2, 5, 10, 85, -33, -54, -30, -65, 58, 40, -21, -84, -66, -11},
                {94, 85, -31, 37, -25, 60, 55, -13, 48, -23, -50, 84, -71, 54, 47, 18, -67, -30, 5, -46, 53},
                {-29, 54, -10, -68, -54, -24, -16, 21, 32, 33, -27, 48, 37, -61, 97, 45, -90, 87, -95, 85, 67},
                {76, -11, -48, 38, -7, 86, -55, 51, 26, 8, -96, 99, 69, -84, 41, 78, -53, 4, 29, 38, 16},
                {-8, 48, 95, 47, 39, -11, -72, -95, -17, 33, 65, 96, -52, -17, -22, -15, -91, -41, -16, 23, 14},
                {92, 87, 63, -63, -80, 96, -62, 71, -58, 17, -89, -35, -96, -79, 7, 46, -74, 88, 93, -44, 52},
                {-21, 35, 16, -17, 54, -22, -93, 27, 88, 0, -67, 94, -24, -30, -90, -5, -48, 45, -90, 32, -81},
                {-86, 31, -80, -79, -5, 11, -20, 9, 52, -38, 67, 64, -49, 23, -86, 39, -97, 76, 10, 81, 20}
        };
        alpha = new double[]{
                -2.7910,
                2.5623,
                -1.0429,
                0.5097,
                -2.8096,
                1.1883,
                2.0771,
                -2.9926,
                0.0715,
                0.4142,
                -2.5010,
                1.7731,
                1.6473,
                0.4934,
                2.1038,
                -1.9930,
                0.3813,
                -2.2144,
                -2.5572,
                2.9449};
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double[] A = new double[numberOfDimensions];
        double[] B = new double[numberOfDimensions];

        for (int i = 0; i < numberOfDimensions; i++) {
            A[i] = 0;
            B[i] = 0;
            for (int j = 0; j < numberOfDimensions; j++) {
                A[i] = A[i] + (a[i][j] * sin(alpha[j]) + b[i][j] * cos(alpha[j]));
                B[i] = B[i] + (a[i][j] * sin(x[j]) + b[i][j] * cos(x[j]));
            }
        }

        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += pow(A[i] - B[i], 2);
        }

        return fitness;
    }
}
