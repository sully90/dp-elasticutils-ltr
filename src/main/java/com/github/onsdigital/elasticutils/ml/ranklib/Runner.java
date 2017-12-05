package com.github.onsdigital.elasticutils.ml.ranklib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 *
 * A simple class to run RankLib from the command line.
 * TODO
 * In the future, move to using the RankLib jar as a library and doing everything in memory
 */
public class Runner {

    private int model;
    private String judgementWithFeaturesFile;
    private String modelOutput;

    public Runner(int model, String judgementWithFeaturesFile, String modelOutput) {
        this.model = model;
        this.judgementWithFeaturesFile = judgementWithFeaturesFile;
        this.modelOutput = modelOutput;
    }

    public String readModel() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(this.modelOutput)));
        return content;
    }

    public int run() throws IOException, InterruptedException {
        String cmd = getCmd(this.model, this.judgementWithFeaturesFile, this.modelOutput);
        final Process p = Runtime.getRuntime().exec(cmd);

        new Thread(new Runnable() {
            public void run() {
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = null;

                try {
                    while ((line = input.readLine()) != null)
                        System.out.println(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        p.waitFor();
        return p.exitValue();
    }

    private static String getCmd(int model, String judgementWithFeaturesFile, String modelOutput) throws IOException {
        String cmd = String.format("java -jar %s -ranker %s -train %s -save %s -frate 1.0",
                getPathToRankLib(),
                model,
                judgementWithFeaturesFile,
                modelOutput);
        return cmd;
    }

    private static String getPathToRankLib() throws IOException {
        URL url = Runner.class.getResource("/lib/RankLib-2.8.jar");
        if (url == null) {
            throw new IOException("Unable to locate RankLib-2.8.jar in the classpath");
        }
        return url.getPath();
    }

    public static void main(String[] args) {
        int model = 6;
        String baseDir = "/Users/sullid/idea/elasticsearch-learning-to-rank/demo/";
        String judgementsWithFeatures = baseDir + "sample_judgements_wfeatures.txt";
        String modelOutput = baseDir + "model_java.txt";
        Runner runner = new Runner(model, judgementsWithFeatures, modelOutput);
        try {
            runner.run();

            System.out.println(runner.readModel());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
