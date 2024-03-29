package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F4 extends CEC2015 {

    public F4(int d) {
        super("F04 Schwefel's Function", d, 4);
    }

    @Override
    public double eval(double[] x) {
        return Functions.schwefel_func(x, numberOfDimensions, OShift, M, 1, 0) + 100.0 * funcNum;
    }
}
