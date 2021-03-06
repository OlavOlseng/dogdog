package genetics;

import olseng.ea.genetics.DevelopmentalMethod;

import java.util.HashMap;

/**
 * Created by Olav on 02.03.2016.
 */
public class MusicDevelopmentalMethod implements DevelopmentalMethod<MusicGenotype, MusicPhenotype> {

    @Override
    public MusicPhenotype develop(MusicGenotype genotype) {
        MusicPhenotype p = new MusicPhenotype(genotype);
        p.setRepresentation(genotype.getData());

        processMelodicIntervalsAndPitches(p);
        processMelodicRhythmsAndPatterns(p);
        processWholeBarRhythmicPatterns(p);
        processHalfBarRhythmicPatterns(p);
        processWholeBarRestPatterns(p);

        return p;
    }

    private void processMelodicRhythmsAndPatterns(MusicPhenotype p) {
        //Durations are sorted where 1/16 is in position 0, 1/1 is in position 15.
        int[] durations;

        int pitchIndex = 0;
        int restIndex = 0;
        int lastPosition = 0;
        int currentPosition = 0;
        boolean isRest = false;
        boolean lastIsRest = true; //Set to true to not trigger on the first note

        while (pitchIndex < p.pitchPositions.size() || restIndex < p.restPositions.size()) {

            if (p.pitchPositions.size() <= pitchIndex) {
                currentPosition = p.restPositions.get(restIndex);
                durations = p.restDurations;
                isRest = true;
                restIndex++;
            }
            else if (p.restPositions.size() <= restIndex) {
                currentPosition = p.pitchPositions.get(pitchIndex);
                durations = p.pitchDurations;
                isRest = false;
                pitchIndex++;
            }
            else if (p.pitchPositions.get(pitchIndex) > p.restPositions.get(restIndex)) {
                currentPosition = p.pitchPositions.get(pitchIndex);
                durations = p.pitchDurations;
                isRest = false;
                pitchIndex++;
            }
            else {
                currentPosition = p.restPositions.get(restIndex);
                durations = p.restDurations;
                isRest = true;
                restIndex++;
            }

            int duration = currentPosition - lastPosition - 1; // -1 to get proper index in array.
            if (!isRest && !lastIsRest) {
                p.sequentialPitchesDurations.add(duration + 1);
            }
            lastIsRest = isRest;
            if (duration <= 0) {
                continue;
            }
            while(duration > 15) {
                durations[15]++;
                duration -= 15;
            }
            durations[duration]++;
            lastPosition = currentPosition;
        }
        //Finish off final note.
        int duration = p.getRepresentation().melodyContainer.melody.length - lastPosition - 1;
        if (!isRest && !lastIsRest) {
            p.sequentialPitchesDurations.add(duration + 1);
        }
        if (isRest) {
            durations = p.restDurations;
        }
        else {
            durations = p.pitchDurations;
        }
        while(duration > 15) {
            durations[15]++;
            duration -= 15;
        }
        durations[duration]++;
    }

    private void processMelodicIntervalsAndPitches(MusicPhenotype p) {
        MelodyContainer mc = p.getRepresentation().melodyContainer;
        //find first pitch
        int barSize = mc.MELODY_BAR_SUBDIVISION * mc.MELODY_FOURTH_SUBDIVISION;
        int lastPitchIndex = mc.melody[0];
        for (int bar = 0; bar < mc.bars; bar++) {
            for (int currentPitchIndex = bar * barSize; currentPitchIndex < (bar + 1) * barSize; currentPitchIndex++) {

                if (mc.melody[currentPitchIndex] < MelodyContainer.MELODY_RANGE_MIN) {
                    if (mc.melody[currentPitchIndex] == MelodyContainer.MELODY_REST) {
                        p.restPositions.add(currentPitchIndex);
                    }
                    continue;
                }
                //New index is a pitch.
                p.melodyPitches.get(bar).add(mc.melody[currentPitchIndex]);
                p.pitchPositions.add(currentPitchIndex);
                if (mc.melody[lastPitchIndex] < MelodyContainer.MELODY_RANGE_MIN) {
                    lastPitchIndex = currentPitchIndex;
                    continue;
                }
                //At this point, both variables must be pitches.
                p.melodyIntervals.get(bar).add(mc.melody[currentPitchIndex] - mc.melody[lastPitchIndex]);
                lastPitchIndex = currentPitchIndex;
            }
        }
    }

    private void processWholeBarRhythmicPatterns(MusicPhenotype p) {
        p.wholeMeasureRhythmicPatterns = new HashMap<>();
        byte[] melody = p.getRepresentation().melodyContainer.melody;
        int barValue = 0;
        for (int i = 0; i < melody.length; i++) {
            if (melody[i] >= MelodyContainer.MELODY_RANGE_MIN) {
                barValue = barValue | 1;
            }
            if ((i + 1) % 16 == 0 && i != 0) {
                p.sequentialMeasurePatterns[i / 16] = barValue;
                if (p.wholeMeasureRhythmicPatterns.containsKey(barValue)) {
                    p.wholeMeasureRhythmicPatterns.put(barValue, p.wholeMeasureRhythmicPatterns.get(barValue) + 1);
                } else {
                    p.wholeMeasureRhythmicPatterns.put(barValue, 1);
                }
                barValue = 0;
            }
            barValue = barValue << 1;
        }
    }

    private void processWholeBarRestPatterns(MusicPhenotype p) {
        p.wholeMeasureRestPatterns = new HashMap<>();
        byte[] melody = p.getRepresentation().melodyContainer.melody;
        int barValue = 0;
        for (int i = 0; i < melody.length; i++) {
            if (melody[i] == MelodyContainer.MELODY_REST) {
                barValue = barValue | 1;
            }
            if ((i + 1) % 16 == 0 && i != 0) {
                p.sequentialMeasureRestPatterns[i / 16] = barValue;
                if (p.wholeMeasureRestPatterns.containsKey(barValue)) {
                    p.wholeMeasureRestPatterns.put(barValue, p.wholeMeasureRestPatterns.get(barValue) + 1);
                } else {
                    p.wholeMeasureRestPatterns.put(barValue, 1);
                }
                barValue = 0;
            }
            barValue = barValue << 1;
        }
    }

    private void processHalfBarRhythmicPatterns(MusicPhenotype p) {
        p.halfMeasureRhythmicPatterns = new HashMap<>();
        byte[] melody = p.getRepresentation().melodyContainer.melody;
        int barValue = 0;
        for (int i = 0; i < melody.length; i++) {
            if (melody[i] >= MelodyContainer.MELODY_RANGE_MIN) {
                barValue = barValue | 1;
            }
            if ((i + 1) % 8 == 0 && i != 0) {
                p.sequentialHalfMeasurePatterns[i / 8] = barValue;
                if (p.halfMeasureRhythmicPatterns.containsKey(barValue)) {
                    p.halfMeasureRhythmicPatterns.put(barValue, p.halfMeasureRhythmicPatterns.get(barValue) + 1);
                } else {
                    p.halfMeasureRhythmicPatterns.put(barValue, 1);
                }
                barValue = 0;
            }
            barValue = barValue << 1;
        }
    }
}
