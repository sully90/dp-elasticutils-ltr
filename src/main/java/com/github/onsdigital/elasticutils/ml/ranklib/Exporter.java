package com.github.onsdigital.elasticutils.ml.ranklib;

import com.github.onsdigital.elasticutils.ml.client.response.sltr.models.Fields;
import com.github.onsdigital.elasticutils.ml.client.response.sltr.models.LogEntry;
import com.github.onsdigital.elasticutils.ml.client.response.sltr.models.SltrDocument;
import com.github.onsdigital.elasticutils.ml.ranklib.models.Judgement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author sullid (David Sullivan) on 06/12/2017
 * @project dp-elasticutils-ltr
 *
 * Exports SltrDocuments to disk, to use with RankLib
 */
public class Exporter {

    private static String format(Judgement judgement, SltrDocument document) {
        StringJoiner joiner = new StringJoiner("\t");
        Fields fields = document.getFields();

        for (Map<String, List<LogEntry>> entry : fields.getLtrLogList()) {
            for (String key : entry.keySet()) {
                List<LogEntry> logEntries = entry.get(key);
                for (LogEntry logEntry : logEntries) {
                    String feature = String.format("%s:%s", logEntry.getName(), logEntry.getValue());
                    joiner.add(feature);
                }
            }
        }
        if (judgement.getComment() != null) {
            return String.format("%s\tqid:%s\t%s %s", judgement.getJudgement(), judgement.getQueryId(), joiner.toString(), judgement.getFormattedComment());
        } else {
            return String.format("%s\tqid:%s\t%s", judgement.getJudgement(), judgement.getQueryId(), joiner.toString());
        }
    }

    public static List<String> toRankLibFormat(Map<Judgement, SltrDocument> queryFeatureMap) {
        List<String> output = new LinkedList<>();

        for (Judgement judgement : queryFeatureMap.keySet()) {
            output.add(format(judgement, queryFeatureMap.get(judgement)));
        }

        return output;
    }

    public static void export(String filePath, Map<Judgement, SltrDocument> queryFeatureMap) throws IOException {
        Path path = Paths.get(filePath);
        export(path, queryFeatureMap);
    }

    public static void export(Path filePath, Map<Judgement, SltrDocument> queryFeatureMap) throws IOException {
        List<String> rankLibFormatted = toRankLibFormat(queryFeatureMap);
        Files.write(filePath, rankLibFormatted);
    }

}
