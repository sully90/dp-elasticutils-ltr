package com.github.onsdigital.elasticutils.ml.ranklib.models;

import java.util.Arrays;
import java.util.List;

/**
 * @author sullid (David Sullivan) on 14/12/2017
 * @project dp-elasticutils-ltr
 *
 * Computes metrics to determine search performance.
 * See https://github.com/ONSdigital/dp-search/blob/master/search_relevance/README.md
 * for a description of each metric calculated below.
 */
public class Judgements {

    public static final float MAX_SCORE = 4.0f;

    private int queryId;
    private List<Judgement> judgementList;

    public Judgements(int queryId, List<Judgement> judgements) {
        this.queryId = queryId;

        // Ensure all judgements are for this queryId
        for (Judgement judgement : judgements) {
            if (judgement.getQueryId() != this.queryId) {
                throw new RuntimeException(String.format("Invalid judgement for queryID: %d : %s", this.queryId, judgement));
            }
        }
        this.judgementList = judgements;
    }

    public float[] cumulativeGain() {
        float[] cumulativeGain = new float[this.judgementList.size()];

        float total = 0.0f;
        for (int i = 0; i < cumulativeGain.length; i++) {
            total += this.judgementList.get(i).getJudgement();
            cumulativeGain[i] = total;
        }
        return cumulativeGain;
    }

    public float[] discountedCumulativeGain() {
        float[] discountedCumulativeGain = new float[this.judgementList.size()];

        float total = 0.0f;
        for (int i = 0; i < discountedCumulativeGain.length; i++) {
            Judgement judgement = this.judgementList.get(i);
            total += judgement.getJudgement() / Float.valueOf(judgement.getRank());
            discountedCumulativeGain[i] = total;
        }
        return discountedCumulativeGain;
    }

    /**
     * Computes the main Normalised Discounted Cumulative Gain (NDGC) metric of performance
     * @return
     */
    public float[] normalisedDiscountedCumulativeGain() {
        float[] normalisedDiscountedCumulativeGain = new float[this.judgementList.size()];

        float[] discountedCumulativeGain = this.discountedCumulativeGain();
        float[] idealDiscountedCumulativeGain = idealDiscountedCumulativeGain(normalisedDiscountedCumulativeGain.length);

        for (int i = 0; i < normalisedDiscountedCumulativeGain.length; i++) {
            // Cap the value at 1
            normalisedDiscountedCumulativeGain[i] = Math.min(1.0f,
                    discountedCumulativeGain[i] / idealDiscountedCumulativeGain[i]);
        }

        return normalisedDiscountedCumulativeGain;
    }

    /**
     *
     * @param num
     * @return
     *
     * Generates evenly spaced ideal judgements between MAX_SCORE and 0
     */
    public static float[] idealJudgement(int num) {
        float[] idealJudgement = new float[num];

        int i = 0;
        float increment = (1.0f / ((float) num - 1)) * num;
        for (float val = idealJudgement.length; val > 0; val-=increment) {
            idealJudgement[i] = (val / (float) num) * MAX_SCORE;
            i++;
        }
        return idealJudgement;
    }

    public static float[] idealDiscountedCumulativeGain(int num) {
        float[] idealGain = idealJudgement(num);
        float[] idealDiscountedCumulativeGain = new float[num];

        float total = 0.0f;
        for (int i = 0; i < idealGain.length; i++) {
            total += idealGain[i] / (float) (i+1);  // Compute idealDiscountedGain
            idealDiscountedCumulativeGain[i] = total; // Compute cumulativeIdealDiscountedGain
        }
        return idealDiscountedCumulativeGain;
    }

    public static void main(String[] args) {
        int num = 4;
        float[] idealJudgements = Judgements.idealJudgement(num);

        System.out.println(Arrays.toString(idealJudgements));
    }

}
