package org.um.feri.ears.algorithms.gp;


import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.operators.SinglePointCrossover;
import org.um.feri.ears.operators.SingleTreeNodeMutation;
import org.um.feri.ears.operators.TournamentSelection;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.comparator.ProblemComparator;

import java.util.ArrayList;
import java.util.List;

public class DefaultGPAlgorithm extends GPAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize; //Usually around 500

    @AlgorithmParameter(name = "crossover probability")
    private double crossoverProbability;

    @AlgorithmParameter(name = "mutation probability")
    private double mutationProbability;

    @AlgorithmParameter(name = "number of tournaments")
    private int numberOfTournaments;

    private ArrayList<ProgramSolution<Double>> population;

    private ProblemComparator<ProgramSolution<Double>> comparator;
    private TournamentSelection<ProgramSolution<Double>, ProgramProblem<Double>> selectionOperator;
    private SinglePointCrossover<Double> singlePointCrossover;
    private SingleTreeNodeMutation<Double> singleTreeNodeMutation;

    public DefaultGPAlgorithm(){
        this(50, 0.1, 0.9, 2);
    }

    public DefaultGPAlgorithm(int popSize, double crossoverProbability, double mutationProbability, int numberOfTournaments) {
        this.popSize = popSize;
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability = mutationProbability;
        this.numberOfTournaments = numberOfTournaments;

        this.singlePointCrossover = new SinglePointCrossover<>(this.crossoverProbability);
        this.singleTreeNodeMutation = new SingleTreeNodeMutation<>(this.mutationProbability);

        au = new Author("marko", "marko.smid2@um.si");
        ai = new AlgorithmInfo("DGP", "Default GP Algorithm",
                ""
        );
    }

    @Override
    public ProgramSolution<Double> execute(Task<ProgramSolution<Double>, ProgramProblem<Double>> task) throws StopCriterionException {
        this.task = task;
        comparator = new ProblemComparator<>(task.problem);
        selectionOperator = new TournamentSelection<>(this.numberOfTournaments, comparator);
        // Population inicialization and evaluation
        initPopulation();

        while (!task.isStopCriterion()) {
            System.out.println("Generation: " + this.task.getNumberOfIterations());
            ProgramSolution<Double>[] parents = new ProgramSolution[2];
            List<ProgramSolution<Double>> selectedIndividuals = new ArrayList<>(population.size());
            // Selection and Crossover
            for (int i = 0; i < this.popSize / 2; i++) {
                parents[0] = selectionOperator.execute(population, task.problem);
                parents[1] = selectionOperator.execute(population, task.problem);

                //selectedIndividuals.add(parents[0]);
                //selectedIndividuals.add(parents[1]);
                //TODO -> uredi da križanje ne bo preseglo višino drevesa
                try {
                    ProgramSolution<Double>[] newSolution = singlePointCrossover.execute(parents, task.problem);
                    selectedIndividuals.add(newSolution[0]);
                    selectedIndividuals.add(newSolution[1]);
                } catch (Exception ex) {
                    selectedIndividuals.add(parents[0]);
                    selectedIndividuals.add(parents[1]);
                }
            }
            // Mutation
            for (int i = 0; i < selectedIndividuals.size(); i++) {
                try {
                    selectedIndividuals.set(i,singleTreeNodeMutation.execute(selectedIndividuals.get(i), task.problem));
                } catch (Exception ex) { }
            }

            // Evaluate
            population = new ArrayList<>(selectedIndividuals);
            for (int i = 0; i < this.population.size(); i++) {
                this.task.eval(this.population.get(i));

                if (this.task.isStopCriterion())
                    break;
            }
            if (this.task.isStopCriterion())
                break;

            this.task.incrementNumberOfIterations();
        }
        this.population.sort(this.comparator);
        return this.population.get(0);
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }

    /**
     * Initialize @popSize individuals and evaluate them. Best random generated solution is saved to @best
     * */
    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            ProgramSolution<Double> newSolution = task.getRandomEvaluatedSolution();

            population.add(newSolution);
            if (task.isStopCriterion())
                break;
        }
    }
}
