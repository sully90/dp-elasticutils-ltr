package com.github.onsdigital.elasticutils.ml.ranklib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

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

}
