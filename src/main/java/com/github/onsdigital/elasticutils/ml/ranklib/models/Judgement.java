package com.github.onsdigital.elasticutils.ml.ranklib.models;

import java.util.Random;

/**
 * @author sullid (David Sullivan) on 06/12/2017
 * @project dp-elasticutils-ltr
 */
public class Judgement {

    private int judgement;
    private int queryId;
    private String comment;

    public Judgement(int judgement, int queryId) {
        if (judgement < 0 || judgement > 4) {
            throw new UnsupportedOperationException("Judgement must be between 0 and 4");
        }
        this.judgement = judgement;
        this.queryId = queryId;
    }

    public Judgement(int judgement, int queryId, String comment) {
        this(judgement, queryId);
        this.comment = comment;
    }

    public int getJudgement() {
        return judgement;
    }

    public int getQueryId() {
        return queryId;
    }

    public String getComment() {
        return comment;
    }

    public String getFormattedComment() {
        return String.format("# %s", this.comment);
    }

    public static Judgement randomJudgement(int queryId) {
        return randomJudgement(queryId, null);
    }

    public static Judgement randomJudgement(int queryId, String comment) {
        Random random = new Random();
        int max = 4;
        int min = 0;
        int judgement = random.nextInt((max - min) + 1) + min;
        return new Judgement(judgement, queryId, comment);
    }
}
