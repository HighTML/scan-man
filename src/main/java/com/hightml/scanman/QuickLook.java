package com.hightml.scanman;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * Please enter description here.
 * <p/>
 * User: marcel
 * Date: 08/02/15
 * Time: 00:15
 * <p/>
 * Copyright by HighTML.
 */
@Getter
@Setter
@Slf4j
public class QuickLook implements Runnable {
    private static QuickLook instance = new QuickLook();
    private File file;
    private Process process;

    public static QuickLook getInstance(){
        return instance;
    }

    private boolean isStarted() {
        return process!=null;
    }

    private void stop() {
        process.destroy();
        process = null;
        log.debug("Destroyed Process");
    }

    public static void close() {
        if (getInstance().isStarted()) {
            getInstance().stop();
        }
    }

    public static void lookOrClose(File file) {
        if (instance.isStarted()) {
            instance.stop();
            return;
        }


        instance.setFile(file);


        (new Thread(instance)).start();
    }


    @Override
    public void run() {

        String filename = file.getAbsolutePath();


        try {
            process = Runtime.getRuntime().exec("qlmanage -p "+filename);
            process.waitFor();
        } catch (Exception e) {
            log.error("During qlmanage",e);
        }
    }
}
