package com.github.onsdigital.elasticutils.ml.ranklib.models;

import java.util.Random;

/**
 * @author sullid (David Sullivan) on 06/12/2017
 * @project dp-elasticutils-ltr
 */
public class Judgement {

    private float judgement;
    private int queryId;
    private int rank;
    private String comment;

    public Judgement(float judgement, int queryId, int rank) {
        if (judgement < 0 || judgement > 4) {
            throw new UnsupportedOperationException("Judgement must be between 0 and 4");
        }
        this.judgement = judgement;
        this.queryId = queryId;
        this.rank = rank;
    }

    public Judgement(float judgement, int queryId, int rank, String comment) {
        this(judgement, queryId, rank);
        this.comment = comment;
    }

    public float getJudgement() {
        return judgement;
    }

    public int getQueryId() {
        return queryId;
    }

    public int getRank() {
        return rank;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFormattedComment() {
        return String.format("# %s", this.comment);
    }

    public static Judgement randomJudgement(int queryId, int rank) {
        return randomJudgement(queryId, rank, null);
    }

    public static Judgement randomJudgement(int queryId, int rank, String comment) {
        Random random = new Random();
        int max = 4;
        int min = 0;
        int judgement = random.nextInt((max - min) + 1) + min;
        return new Judgement(judgement, queryId, rank, comment);
    }
}
