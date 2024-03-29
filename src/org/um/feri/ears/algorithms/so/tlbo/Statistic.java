package org.um.feri.ears.algorithms.so.tlbo;

import org.um.feri.ears.problems.NumberSolution;

import java.util.ArrayList;

public class Statistic {
    private ArrayList<StatisticGeneration> a;
    private long evalCount;
    private StatisticGeneration current_g;
    private int updatedByTeacher;
    private int updatedByLearner;
    private static final int INIT_GEN_ID = -1;

    public boolean isBestFeasible() {
        return a.get(a.size() - 1).best.areConstraintsMet();
    }

    public long getEvalCount() {
        return evalCount;
    }

    public int getUpdatedByTeacher() {
        return updatedByTeacher;
    }

    public int getUpdatedByLearner() {
        return updatedByLearner;
    }

    public NumberSolution getBest() {
        return a.get(a.size() - 1).best;
    }

    public Statistic() {
        super();
        evalCount = 0;
        a = new ArrayList<StatisticGeneration>();
        current_g = new StatisticGeneration(INIT_GEN_ID, 0); //
        a.add(current_g);
        updatedByTeacher = 0;
        updatedByLearner = 0;

    }

    public void incUpdateByTeacher() {
        updatedByTeacher++;
        current_g.incUpdateByTeacher();
    }

    public void incUpdateByLearner() {
        updatedByLearner++;
        current_g.incUpdateByLearner();
    }

    public int getNumberOfActualGeneration() {
        return a.size() - 1;  //-1 because init generation is also in!
    }

    public void newGeneration(int id) {
        current_g = new StatisticGeneration(id, evalCount); //
        a.add(current_g);
    }

    public void incEval() {
        evalCount++;
        current_g.incEval();
    }

    public StatisticGeneration getCurrent_g() {
        return current_g;
    }

    public void setCurrent_g(StatisticGeneration current_g) {
        this.current_g = current_g;
    }


}
