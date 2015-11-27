package sk.styk.martin.bakalarka.decompile;

import brut.androlib.Androlib;
import brut.androlib.AndrolibException;
import brut.androlib.ApkDecoder;
import brut.androlib.ApkOptions;
import brut.directory.DirectoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by Martin Styk on 23.11.2015.
 */
public class ApkDecompiler {

    private static final Logger logger = LoggerFactory.getLogger(ApkDecompiler.class);
    private static ApkDecompiler instance = null;
    public static String TEMP_FOLDER_UNZIP = "D:\\Projects\\temp\\decompiled";

    private File apkFile;

    private ApkDecompiler() {
        // Exists only to defeat instantiation.
    }

    public static ApkDecompiler getInstance(File apkFile) {
        if (apkFile == null) {
            throw new IllegalArgumentException("apkFile null");
        }

        if (instance == null) {
            instance = new ApkDecompiler();
            installFramework(); //we need to install framework, otherwise some apk cannot be decompiled (arsc cannot decompile)
        }
        instance.apkFile = apkFile;
        return instance;
    }


    public void decompile() {
        try {
            decompileIt();
        } catch (AndrolibException e) {
            logger.error("Decompilation of apk " + apkFile.getName() + " failed with " + e.toString());
        }
    }

    private void decompileIt() throws AndrolibException {

        ApkDecoder decoder = new ApkDecoder();
        decoder.setApkFile(apkFile);
        decoder.setForceDelete(true);
        decoder.setDecodeSources((short) 0);

        File outDirectory = new File(TEMP_FOLDER_UNZIP);
        outDirectory.mkdirs();


        try {
            decoder.setOutDir(outDirectory);
        } catch (AndrolibException e) {
            e.printStackTrace();
        }
        logger.info("Starting decompilation of apk " + apkFile.getName());
        try {
            decoder.decode();
        } catch (DirectoryException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Succesfully finished decompilation of apk " + apkFile.getName());
    }

    private static void installFramework() {

        logger.info("Installing framework-res.apk");

        ApkOptions apkOptions = new ApkOptions();
        try {
            new Androlib(apkOptions).installFramework(new File("lib" + File.separator + "framework-res.apk"));
        } catch (AndrolibException e) {
            logger.warn("Installing framework-res.apk FAILED");
        }
    }
}
