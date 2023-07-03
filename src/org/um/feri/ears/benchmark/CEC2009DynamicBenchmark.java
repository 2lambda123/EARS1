package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.dynamic.cec2009.BasicFunction;
import org.um.feri.ears.problems.dynamic.cec2009.ChangeType;

/**
 * Generalized Dynamic Benchmark Generator
 * <p>
 * Original source code:
 * - https://github.com/P-N-Suganthan/CEC2009-Dynamic-Optimization
 * <p>
 * Two different instances of generators:
 * - Dynamic Composition Benchmark Generator for continuous problems
 * - Dynamic Rotation Benchmark Generator for continuous problems
 * <p>
 * Six types of changes of control parameters in GDBG:
 * - small step change
 * - large step change
 * - random change
 * - chaotic change
 * - recurrent change
 * - recurrent change with noisy
 * <p>
 * Two types of environmental changes:
 * - dimensional changes
 * - non-dimensional changes
 */

public class CEC2009DynamicBenchmark extends SOBenchmark<NumberSolution<Double>, NumberSolution<Double>, DoubleProblem, NumberAlgorithm> {

    public static MyRandom myRandom = new MyRandom();

    private final int numberOfPeaksOrFunctions = 10;
    private final Double heightNormalizeSeverity = 2000.0;  // the constant number for normalizing all basic functions with similar height
    private final Double minPeakHeight = 10.0, maxPeakHeight = 100.0;
    private final Double chaoticConstant = 3.67;    // value must be between 1.0 and 4.0
    private final int minDimensions = 5, maxDimensions = 15;
    private final int changeFrequency = 10000;  // number of evaluations between two successive changes
    private final int numberOfChanges = 60;  // the number of changes = how many times the problem will change
    private final Double gLowerLimit = -5.0, gUpperLimit = 5.0;    // search range
    private final int changeFrequencyPerDimension = 10000;

    public CEC2009DynamicBenchmark() {
        super();
        name = "Benchmark CEC 2009 Dynamic Optimization";
        dimension = 10;
    }

    @Override
    public void initAllProblems() {
        /*
            TODO:
                DynamicCompositionProblem
                    - problems for all basic functions (Sphere, Rastrigin, Griewank ...) - first for loop in C++
                    - problems for all change types (small step, large step, u_random ...) - first inner loop in C++
                    - two problems with different number of peaks (10 and 50) for Sphere - second inner loop in C++
         */
        DynamicCompositionProblem problem1 = new DynamicCompositionProblem("DynamicCompositionProblemSphereSmallStepPeaks10",
                dimension, // numberOfDimensions
                1, // numberOfGlobalOptima
                1, // numberOfObjectives
                0, // numberOfConstraints
                numberOfPeaksOrFunctions,
                minPeakHeight, maxPeakHeight,
                chaoticConstant,
                ChangeType.SMALL_STEP,
                0,  // periodicity
                true,   //false,  // isDimensionChanged
                minDimensions, maxDimensions,
                changeFrequency,
                5,   // heightSeverity
                heightNormalizeSeverity,
                gLowerLimit, gUpperLimit,
                BasicFunction.SPHERE
        );
        int maxEvaluations = calculateNumberOfEvaluations(problem1);
        addTask(problem1, stopCriterion, maxEvaluations, timeLimit, maxIterations);
    }

    private int calculateNumberOfEvaluations(DynamicProblem p) {
        int maxEvaluations = 0;

        if (p.isDimensionChanging()) {
            boolean isDimensionIncreasing = p.isDimensionIncreasing();
            int currentNumberOfDimensions = p.getNumberOfDimensions();

            for (int i = 0; i < numberOfChanges; i++) {
                maxEvaluations += (p.getChangeFrequency() * currentNumberOfDimensions);

                if (currentNumberOfDimensions == p.getMinDimensions()) {
                    isDimensionIncreasing = true;
                }
                if (currentNumberOfDimensions == p.getMaxDimensions()) {
                    isDimensionIncreasing = false;
                }
                if (isDimensionIncreasing) {
                    currentNumberOfDimensions++;
                } else {
                    currentNumberOfDimensions--;
                }
            }
        } else {
            maxEvaluations = p.getChangeFrequency() * p.getNumberOfDimensions() * numberOfChanges;
        }

        return maxEvaluations;
    }

    @Override
    protected void addTask(DoubleProblem problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations) {
        tasks.add(new DynamicTask<>(problem, stopCriterion, maxEvaluations, time, maxIterations, changeFrequencyPerDimension));
    }

    public static void main(String[] args) {
        //CEC2009DynamicOptimizationBenchmark benchmark = new CEC2009DynamicOptimizationBenchmark();
        //benchmark.initAllProblems();

        DynamicCompositionProblem problem1 = new DynamicCompositionProblem("DynamicCompositionProblemSphereSmallStepPeaks10",
                10, // numberOfDimensions
                1, // numberOfGlobalOptima
                1, // numberOfObjectives
                0, // numberOfConstraints
                10,
                10.0, 100.0,
                3.67,
                ChangeType.CHAOTIC,
                0,  // periodicity
                true,   //false,  // isDimensionChanged
                5, 15,
                10000,
                5,   // heightSeverity
                2000.0,
                -5.0, 5.0,
                BasicFunction.SPHERE
        );

        int changeCounter = 0;
        for (int g = 1; g <= problem1.getChangeFrequency() * 60; g++) {
            if (g % problem1.getChangeFrequency() == 0) {
                // generate a solution
                Double[] x = new Double[problem1.getNumberOfDimensions()];
                for (int i = 0; i < problem1.getNumberOfDimensions(); i++) {
                    x[i] = -5.0 + (5.0 - (-5.0)) * myRandom.nextDouble();
                }
                double fit = problem1.eval(x);
                System.out.println(fit);
                problem1.performChange(changeCounter++);

                //if (problem1.isDimensionChanging()) {
                //    problem1.changeDimension(changeCounter);
                //}
            }
        }
    }
}
