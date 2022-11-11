package org.um.feri.ears.algorithms.moo.nsga3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.util.Util;

/**
 * Created by ajnebro on 5/11/14.
 * Modified by Juanjo on 13/11/14
 * This implementation is based on the code of Tsung-Che Chiang
 * http://web.ntnu.edu.tw/~tcchiang/publications/nsga3cpp/nsga3cpp.htm
 */
public class ReferencePoint<Type extends Number> {
    public List<Double> position;
    private int memberSize;
    private List<Pair<MOSolutionBase<Type>, Double>> potentialMembers;

    public ReferencePoint() {
    }

    /**
     * Constructor
     */
    public ReferencePoint(int size) {
        position = new ArrayList<>();
        for (int i = 0; i < size; i++)
            position.add(0.0);
        memberSize = 0;
        potentialMembers = new ArrayList<>();
    }

    public ReferencePoint(ReferencePoint<Type> point) {
        position = new ArrayList<>(point.position.size());
        for (Double d : point.position) {
            position.add(d);
        }
        memberSize = 0;
        potentialMembers = new ArrayList<>();
    }

    public void generateReferencePoints(
            List<ReferencePoint> referencePoints,
            int numberOfObjectives,
            List<Integer> numberOfDivisions) {

        ReferencePoint refPoint = new ReferencePoint(numberOfObjectives);
        generateRecursive(referencePoints, refPoint, numberOfObjectives, numberOfDivisions.get(0), numberOfDivisions.get(0), 0);
    }

    private void generateRecursive(
            List<ReferencePoint> referencePoints,
            ReferencePoint refPoint,
            int numberOfObjectives,
            int left,
            int total,
            int element) {
        if (element == (numberOfObjectives - 1)) {
            refPoint.position.set(element, (double) left / total);
            referencePoints.add(new ReferencePoint(refPoint));
        } else {
            for (int i = 0; i <= left; i += 1) {
                refPoint.position.set(element, (double) i / total);

                generateRecursive(referencePoints, refPoint, numberOfObjectives, left - i, total, element + 1);
            }
        }
    }

    public List<Double> pos() {
        return this.position;
    }

    public int MemberSize() {
        return memberSize;
    }

    public boolean HasPotentialMember() {
        return potentialMembers.size() > 0;
    }

    public void clear() {
        memberSize = 0;
        this.potentialMembers.clear();
    }

    public void AddMember() {
        this.memberSize++;
    }

    public void AddPotentialMember(MOSolutionBase<Type> member_ind, double distance) {
        this.potentialMembers.add(new ImmutablePair<MOSolutionBase<Type>, Double>(member_ind, distance));
    }

    public MOSolutionBase<Type> FindClosestMember() {
        double minDistance = Double.MAX_VALUE;
        MOSolutionBase<Type> closetMember = null;
        for (Pair<MOSolutionBase<Type>, Double> p : this.potentialMembers) {
            if (p.getRight() < minDistance) {
                minDistance = p.getRight();
                closetMember = p.getLeft();
            }
        }

        return closetMember;
    }

    public MOSolutionBase<Type> RandomMember() {
        int index = this.potentialMembers.size() > 1 ? Util.rnd.nextInt(this.potentialMembers.size()) : 0;
        return this.potentialMembers.get(index).getLeft();
    }

    public void RemovePotentialMember(MOSolutionBase<Type> solution) {
        Iterator<Pair<MOSolutionBase<Type>, Double>> it = this.potentialMembers.iterator();
        while (it.hasNext()) {
            if (it.next().getLeft() == solution) {
                it.remove();
            }
        }
    }
}
