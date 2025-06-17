package com.tntp.botwgameonly.classifier;

import java.util.BitSet;

import com.tntp.botwgameonly.Main;

public class SkipButtonClassifier implements Classifier {
    private static final BitSet STANDARD = BitSet.valueOf(new long[] { 2251250058129408L, -2304717113601294344L, -35747322109231105L, -1055531163680769L, -26388279095297L, -2170172621807158785L,
            -17732923596668929L, -4288095349358593L, -432768189278422913L, -63050394808352769L, -8725724281962497L, -2247401768214529L, -562881234206593L, 70931694667825153L });

    @Override
    public boolean classify(BitSet cleanedData) {
//        for (int i = 0; i < 30; i++) {
//            for (int j = 0; j < 30; j++) {
//                if (cleanedData.get(j * 30 + i)) {
//                    System.out.print(1);
//                } else {
//                    System.out.print(0);
//                }
//            }
//            System.out.println();
//        }
      //  final int threshold = 120;
        cleanedData.xor(STANDARD);
        int score = cleanedData.cardinality();
        // System.out.println(score);
        return score < Main.SKIP_BUTTON_THRESHOLD;

    }
}
