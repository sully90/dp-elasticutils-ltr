package com.github.onsdigital.elasticutils.ml.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author sullid (David Sullivan) on 12/12/2017
 * @project dp-elasticutils-ltr
 */
public class IOUtils {

    /**
     * Runs a python script externally to train test models
     * @return
     */
    public static int run() throws IOException, InterruptedException {
        Path resourceDirectory = getScriptPath();

        String cmd = "python3 train.py";
        System.out.println(cmd);
        final Process p = Runtime.getRuntime().exec(cmd, null, resourceDirectory.toFile());

        new Thread(new Runnable() {
            public void run() {
                BufferedReader stdInput = new BufferedReader(new
                        InputStreamReader(p.getInputStream()));

                BufferedReader stdError = new BufferedReader(new
                        InputStreamReader(p.getErrorStream()));

                System.out.println("stdout:\n");
                String s = null;
                try {
                    while ((s = stdInput.readLine()) != null) {
                        System.out.println(s);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("stderr:\n");
                try {
                    while ((s = stdError.readLine()) != null) {
                        System.out.println(s);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        p.waitFor();
        return p.exitValue();
    }

    private static Path getScriptPath() {
        Path resourceDirectory = Paths.get("src/test/resources");
        return resourceDirectory;
    }

    public static void main(String[] args) {
        try {
            int exitCode = run();
            System.out.println(exitCode);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
