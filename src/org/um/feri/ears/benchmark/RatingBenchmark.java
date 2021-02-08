package org.um.feri.ears.benchmark;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.graphing.recording.GraphDataRecorder;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriterion;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.rating.Game;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Util;

public abstract class RatingBenchmark extends RatingBenchmarkBase<Task, Algorithm, AlgorithmEvalResult> {

	/**
	 *
	 * @param task
	 * @param allSingleProblemRunResults
	 */
	protected void runOneProblem(Task task, BankOfResults allSingleProblemRunResults) {
		long start = 0;
		long duration = 0;
		for (Algorithm al : listOfAlgorithmsPlayers) {
			reset(task); // number of evaluations
			try {
				start = System.nanoTime();

				GraphDataRecorder.SetContext(al, task);
				DoubleSolution bestByALg = al.execute(task);

				duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
				al.addRunDuration(duration, duration - task.getEvaluationTimeMs());
				if (printSingleRunDuration)
					System.out.println(al.getID() + ": " + duration / 1000.0);
				reset(task); // for one eval!
				if (task.isFeasible(bestByALg.getVariables())) {

					results.add(new AlgorithmEvalResult(bestByALg, al, task.getNumberOfEvaluations()));
					allSingleProblemRunResults.add(task, bestByALg, al);

				} else {
					System.err.println(al.getAlgorithmInfo().getAcronym() + " result " + bestByALg
							+ " is out of intervals! For task:" + task.getProblemName());
					results.add(new AlgorithmEvalResult(null, al, task.getNumberOfEvaluations())); // this can be done parallel - asynchrony
				}
			} catch (StopCriterionException e) {
				System.err.println(
						al.getAlgorithmInfo().getAcronym() + " StopCriterionException for:" + task + "\n" + e);
				results.add(new AlgorithmEvalResult(null, al, task.getNumberOfEvaluations()));
			}
		}
	}

	@Override
	public void registerAlgorithm(Algorithm al) {
		listOfAlgorithmsPlayers.add(al);
	}

	@Override
	public void registerAlgorithms(ArrayList<Algorithm> algorithms) {
		listOfAlgorithmsPlayers.addAll(algorithms);

	}

	class FitnessComparator implements Comparator<AlgorithmEvalResult> {
		Task t;

		public FitnessComparator(Task t) {
			this.t = t;
		}

		@Override
		public int compare(AlgorithmEvalResult arg0, AlgorithmEvalResult arg1) {

			if (arg0.getBest() != null) {
				if (arg1.getBest() != null) {

					// first better
					if (t.isFirstBetter(arg0.getBest(), arg1.getBest())) {
						return -1;
					}
					// second better
					if (t.isFirstBetter(arg1.getBest(), arg0.getBest())) {
						return 1;
					}
				} else
					return -1; // second is null
			} else if (arg1.getBest() != null)
				return 1; // first null
			return 0; // both null or equal
		}
	}

	protected void setWinLoseFromResultList(ResultArena arena, Task t) {
		AlgorithmEvalResult win;
		AlgorithmEvalResult lose;
		FitnessComparator fc;
		fc = new FitnessComparator(t);
		Collections.sort(results, fc); // best first
		for (int i = 0; i < results.size() - 1; i++) {
			win = results.get(i);
			for (int j = i + 1; j < results.size(); j++) {
				lose = results.get(j);
				if (resultEqual(win.best, lose.best)) { // Special for this
														// benchmark
					if (debugPrint)
						System.out.println("draw of " + win.getAl().getID() + " ("
								+ Util.df3.format(win.getBest().getEval()) + ", feasable=" + win.getBest().areConstraintsMet()
								+ ") against " + lose.getAl().getID() + " (" + Util.df3.format(lose.getBest().getEval())
								+ ", feasable=" + lose.getBest().areConstraintsMet() + ") for " + t.getProblemName());
					arena.addGameResult(Game.DRAW, win.getAl().getAlgorithmInfo().getAcronym(),
							lose.getAl().getAlgorithmInfo().getAcronym(), t.getProblemName());
				} else {
					if (win.getAl() == null) {
						System.out.println("NULL ID " + win.getClass().getName());
					}
					if (win.getBest() == null) {
						System.out.println(win.getAl().getID() + " NULL");
					}
					if (lose.getAl() == null) {
						System.out.println("NULL ID " + lose.getClass().getName());
					}
					if (lose.getBest() == null) {
						System.out.println(lose.getAl().getID() + " NULL");
					}
					if (debugPrint)
						System.out.println("win of " + win.getAl().getID() + " ("
								+ Util.df3.format(win.getBest().getEval()) + ", feasable=" + win.getBest().areConstraintsMet()
								+ ") against " + lose.getAl().getID() + " (" + Util.df3.format(lose.getBest().getEval())
								+ ", feasable=" + lose.getBest().areConstraintsMet() + ") for " + t.getProblemName());
					arena.addGameResult(Game.WIN, win.getAl().getAlgorithmInfo().getAcronym(),
							lose.getAl().getAlgorithmInfo().getAcronym(), t.getProblemName());
				}

			}
		}
	}

	public boolean resultEqual(DoubleSolution a, DoubleSolution b) {
		if ((a == null) && (b == null))
			return true;
		if (a == null)
			return false;
		if (b == null)
			return false;
		if (!a.areConstraintsMet() && b.areConstraintsMet())
			return false;
		if (a.areConstraintsMet() && !b.areConstraintsMet())
			return false;
		if (!a.areConstraintsMet() && !b.areConstraintsMet())
			return true;

		// TODO global optimum (requires number of evaluations)
		// if global optimum first check if draw then compare number of
		// evaluations
		/*
		 * if(stopCriterion == EnumStopCriterion.GLOBAL_OPTIMUM_OR_EVALUATIONS) {
		 * // if results are equal check number of evaluations
		 * if(Math.abs(a.getEval()-b.getEval())<draw_limit){ } }
		 */

		if (Math.abs(a.getEval() - b.getEval()) < drawLimit)
			return true;
		return false;
	}

	protected abstract void registerTask(Problem p, EnumStopCriterion sc, int eval, long time, int maxIterations,
										 double epsilon);


	/**
	 * Run the benchmark with default number of runs
	 *
	 * @param arena needs to be filed with players and their ratings
	 * @param allSingleProblemRunResults
	 * @param repetition
	 */
	@Override
	public void run(ResultArena arena, BankOfResults allSingleProblemRunResults) {
		duelNumber = numberOfRuns;
		addParameter(EnumBenchmarkInfoParameters.NUMBER_OF_DUELS, "" + numberOfRuns);
		long start = System.nanoTime();
		for (Task t : listOfProblems) {
			if (debugPrint) System.out.println("Current problem: " + t.getProblemName());
			for (int i = 0; i < numberOfRuns; i++) {
				if (debugPrint) System.out.println("Current duel: " + (i + 1));
				runOneProblem(t, allSingleProblemRunResults);
				setWinLoseFromResultList(arena, t);
				results.clear();
			}
		}
		addParameter(EnumBenchmarkInfoParameters.BENCHMARK_RUN_DURATION,
				"" + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
		if (isCheating()) {
			System.out.println("The reset count does not match!");
		}

		// Recalculate ratings after tournament
		// arena.recalcRatings();

		if (displayRatingIntervalChart) {
			arena.calculteRatings();
			displayRatingIntervalsChart(arena.getPlayers());
		}

	}

}
