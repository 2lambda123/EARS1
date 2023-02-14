package org.um.feri.ears.problems;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.comparator.DominanceComparator;

import javax.annotation.CheckReturnValue;
import java.util.ArrayList;
import java.util.List;

public abstract class DoubleProblem extends NumberProblem<Double> {

    DominanceComparator<Double> dominanceComparator = new DominanceComparator<>();

    public DoubleProblem(String name, int numberOfDimensions, int numberOfGlobalOptima, int numberOfObjectives, int numberOfConstraints) {
        super(name, numberOfDimensions, numberOfGlobalOptima, numberOfObjectives, numberOfConstraints);
    }

    @Override
    public void makeFeasible(NumberSolution<Double> solution) {
        setFeasible(solution.getVariables());

    }

    @Override
    public boolean isFeasible(NumberSolution<Double> solution) {
        return isFeasible(solution.getVariables());
    }

    @Override
    public boolean isFirstBetter(NumberSolution<Double> solution1, NumberSolution<Double> solution2) {
        //dominance comparator with information about minimization of each objective
        return -1 == dominanceComparator.compare(solution1,solution2);
    }

    @Override
    public NumberSolution<Double> getRandomSolution() {

        List<Double> x = new ArrayList<>();
        for (int j = 0; j < numberOfDimensions; j++) {
            x.add(Util.nextDouble(lowerLimit.get(j), upperLimit.get(j)));
        }

        return new NumberSolution<>(numberOfObjectives, x);
    }

    /**
     * Generates random feasible variables
     *
     * @return random variables
     */
    public double[] getRandomVariables() {
        double[] x = new double[numberOfDimensions];
        for (int j = 0; j < numberOfDimensions; j++) {
            x[j] = Util.nextDouble(lowerLimit.get(j), upperLimit.get(j));
        }
        return x;
    }

    public double[] getInterval() {
        double[] interval = new double[upperLimit.size()];
        for (int i = 0; i < interval.length; i++) {
            interval[i] = upperLimit.get(i) - lowerLimit.get(i);
        }
        return interval;
    }

    /**
     * Sets every variable in {@code double[] x} feasible.
     *
     * @param x vector to be set to feasible
     * @return vector containing feasible variables
     */
    public void setFeasible(List<Double> x) {
        for (int i = 0; i < x.size(); i++) {
            x.set(i, setFeasible(x.get(i), i));
        }
    }

    public void setFeasible(double[] x) {
        for (int i = 0; i < x.length; i++) {
            x[i] = setFeasible(x[i], i);
        }
    }

    /**
     * Checks if the {@code value} is inside upper and lower limit for the {@code dimension}.
     * If the {@code value} is greater than upper limit it is set to upper limit.
     * If the {@code value} is smaller than lower limit it is set to lower limit.
     * If the {@code value} is inside the interval, the original value is returned.
     *
     * @param value     to be set feasible
     * @param dimension of the interval
     * @return feasible value
     */
    @CheckReturnValue
    public double setFeasible(double value, int dimension) {
        return Math.max(Math.min(value, upperLimit.get(dimension)), lowerLimit.get(dimension));
    }

    /**
     * Checks if the {@code value} is inside upper and lower limit for the {@code dimension}.
     *
     * @param value     to be checked if feasible
     * @param dimension of the interval
     * @return true if value feasible, false otherwise
     */
    public boolean isFeasible(double value, int dimension) {
        return (value >= lowerLimit.get(dimension) && value <= upperLimit.get(dimension));
    }

    /**
     * Checks if the provided array is inside the interval given by the upper and lower limits
     *
     * @param x array to be checked
     * @return true if the array is inside interval, false otherwise
     */
    public boolean isFeasible(List<Double> x) {
        for (int i = 0; i < numberOfDimensions; i++) {
            if (!isFeasible(x.get(i), i))
                return false;
        }
        return true;
    }

    //TODO throw error if number of constraints bigger than 0, make generic?
    /**
     * Override this method if the problem has constraints.
     *
     * @param x variables for which the constraints will be evaluated
     * @return computed constraints
     */
    public double[] evaluateConstrains(double[] x) {
        return new double[0];
    }

    public final double[] evaluateConstrains(List<Double> x) {
        return evaluateConstrains(x.stream().mapToDouble(i -> i).toArray());
    }


    //TODO replace with accessor pattern


    @Override
    public void evaluate(NumberSolution<Double> solution) {
        solution.setObjective(0, eval(solution.getVariables()));
    }

    /**
     * Implements the problem's fitness function.
     *
     * @param x variables to evaluate
     * @return fitness value
     */
    public double eval(double[] x) {return Double.MAX_VALUE;}

    public final double eval(List<Double> x) {
        return eval(x.stream().mapToDouble(i -> i).toArray());
    }

    public final double eval(Double[] x) {
        return eval(ArrayUtils.toPrimitive(x));
    }
}
