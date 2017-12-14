package com.github.onsdigital.elasticutils.ml;

import com.github.onsdigital.elasticutils.ml.ranklib.models.Judgement;
import com.github.onsdigital.elasticutils.ml.ranklib.models.Judgements;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

/**
 * @author sullid (David Sullivan) on 14/12/2017
 * @project dp-elasticutils-ltr
 */
public class TestNDCG {

    private static final List<Judgement> judgementList;
    private static final float[] expected;
    private static final int QUERY_ID = 1;

    static {
        judgementList = new LinkedList<Judgement>() {{
           add(new Judgement(2.0f, QUERY_ID, 1));
           add(new Judgement(0.0f, QUERY_ID, 2));
           add(new Judgement(3.0f, QUERY_ID, 3));
           add(new Judgement(2.0f, QUERY_ID, 4));
        }};

        expected = new float[]{0.5f, 0.37500003f, 0.51923084f, 0.6057693f};
    }

    @Test
    public void testNDCG() {
        Judgements judgements = new Judgements(QUERY_ID, judgementList);

        float[] ndcg = judgements.normalisedDiscountedCumulativeGain();

        System.out.println(Arrays.toString(ndcg));
        System.out.println(Arrays.toString(expected));

        assertTrue(Arrays.equals(ndcg, expected));
    }

}
