package olseng.ea.genetics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olavo on 2016-01-11.
 */
public abstract class Phenotype<T, G extends Genotype> {

    private G genotype = null;
    private T representation = null;

    protected float[] fitnessValues;

    private int rank = Integer.MAX_VALUE;
    public int dominatedByCount = 0;
    public List<Phenotype<?,?>> dominatedSet;
    public float crowdingDistance = 0;
    public boolean isEvaluated = false;

    public Phenotype(G genotype) {
        this.genotype = genotype;
    }

    public void initFitnessValues(int valueCount) {
        this.fitnessValues = new float[valueCount];
    }

    public void setRepresentation(T representation) {
        this.representation = representation;
    }

    public T getRepresentation() {
        return representation;
    }

    public G getGenotype() {
        return this.genotype;
    }

    public int getFitnessCount() {
        return fitnessValues.length;
    }

    public void setFitnessValue(int index, float value) {
        fitnessValues[index] = value;
    }

    public float getFitnessValue(int index) {
        return fitnessValues[index];
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return this.rank;
    }

    public void initializeDominatedSet() {
        this.dominatedSet = new ArrayList<>();
    }

    public Phenotype getDominatedPhenotype(int index) {
        return this.dominatedSet.get(index);
    }

    public abstract boolean isEqualTo(Phenotype<T, G> p2);
}
