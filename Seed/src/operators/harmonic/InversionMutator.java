package operators.harmonic;

import genetics.ChordContainer;
import genetics.MusicGenotype;
import genetics.MusicalContainer;
import olseng.ea.genetics.GeneticMutationOperator;
import util.MusicalKey;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Olav on 16.02.2016.
 */
public class InversionMutator extends GeneticMutationOperator<MusicGenotype> {

    public InversionMutator(double weight) {
        super(weight);
    }

    @Override
    public MusicGenotype mutate(MusicGenotype parent, Random rand) {
        MusicalContainer mc = parent.getDeepCopy();
        ChordContainer hg = mc.chordContainer;

        int chordIndex = rand.nextInt(hg.bars);
        byte[] chord = hg.getChord(chordIndex);
        int pitchCount = chord[chord.length - 1] != -1 ? chord.length : chord.length - 1;

        int pitch1 = rand.nextInt(pitchCount);
        int pitch2 = rand.nextInt(pitchCount);

        if(pitch1 == pitch2) {
            pitch2 = (pitch2 + 1) % pitchCount;
        }

        byte buffer = chord[pitch1];
        chord[pitch1] = chord[pitch2];
        chord[pitch2] = buffer;

        MusicGenotype mg = new MusicGenotype();
        mg.setData(mc);
        return mg;
    }

    @Override
    public boolean isApplicable(MusicGenotype genotype) {
        return true;
    }

    public static void main(String[] args) {
        MusicGenotype mg = new MusicGenotype();
        MusicalContainer mc = new MusicalContainer(2, new MusicalKey(0, MusicalKey.Mode.LOCRIAN));
        mc.init();
        mc.randomize(new Random());
        mg.setData(mc);

        for (int i = 0; i < mc.chordContainer.chords.length; i++) {
            System.out.println(Arrays.toString(mc.chordContainer.chords[i]));
        }

        InversionMutator mutator = new InversionMutator(1);

        for (int j = 0; j < 50; j++) {
            mg = mutator.mutate(mg, new Random());
            for (int i = 0; i < mc.chordContainer.chords.length; i++) {
                System.out.println(Arrays.toString(mc.chordContainer.chords[i]));
            }

        }



    }
}
