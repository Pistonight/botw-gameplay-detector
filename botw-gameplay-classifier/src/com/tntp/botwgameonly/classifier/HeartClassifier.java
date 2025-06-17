package com.tntp.botwgameonly.classifier;

import java.util.BitSet;

import com.tntp.botwgameonly.Main;

public class HeartClassifier implements Classifier {
    // The standard heart images
    private static final BitSet QUARTER_HEART = BitSet
            .valueOf(new long[] { 0L, 1065151889408L, -576460477962387329L, -36028779872649201L, 1150669705864806400L, 71494644151353344L, 4362862143143936L });
    private static final BitSet HALF_HEART = BitSet
            .valueOf(new long[] { 0L, 1065151889408L, -576460477962387329L, -36028728333041633L, -2251782635913209L, 9222809091196059649L, 2305702272799014912L });
    private static final BitSet THREE_QUARTER_HEART = BitSet.valueOf(new long[] { 0L, 1065151889408L, -576460477962387329L, -36028728333041633L, -2251782635913209L, 9222809091196059649L,
            2305702272799014912L, 71776119195172864L, 1108307722878976L, 16492674448384L, 206158430656L, 4L, });
    private static final BitSet FULL_HEART = BitSet.valueOf(new long[] { 0L, 1065151889408L, -576460477962387329L, -36028728333041633L, -2251782635913209L, 9222809091196059649L, 2305702272799014912L,
            72053196125626368L, 1125831189462912L, -2305825419175100418L, -144114913332166145L, 2287828614990790663L, 65011712L, });

    @Override
    public boolean classify(BitSet cleanedData) {

        if (check(cleanedData, QUARTER_HEART)) {
            return true;
        }
        if (check(cleanedData, HALF_HEART)) {
            return true;
        }
        if (check(cleanedData, THREE_QUARTER_HEART)) {
            return true;
        }
        if (check(cleanedData, FULL_HEART)) {
            return true;
        }
        return false;

    }

    private boolean check(BitSet input, BitSet standard) {
      //  final int threshold = 130;
        BitSet s = (BitSet) input.clone();
        s.xor(standard);
        // System.out.println(s.cardinality());
        return s.cardinality() < Main.HEART_THRESHOLD;
    }
}
