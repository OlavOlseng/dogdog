package genetics;

import olseng.ea.genetics.Phenotype;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Olav on 02.03.2016.
 */
public class MusicPhenotype extends Phenotype<MusicalContainer, MusicGenotype> {

    public ArrayList<ArrayList<Integer>> melodyIntervals = new ArrayList<>();
    public ArrayList<ArrayList<Byte>> melodyPitches = new ArrayList<>();

    public MusicPhenotype(MusicGenotype genotype) {
        super(genotype);
        for (int i = 0; i < genotype.getData().bars; i++) {
            melodyIntervals.add(new ArrayList<>());
            melodyPitches.add(new ArrayList<>());
        }
    }

    @Override
    public String toString() {
        return /**getRepresentation().toString() +**/ " -> " + Arrays.toString(this.fitnessValues) + ", Rank: " + getRank() + ", CD: " + crowdingDistance;
    }

}
