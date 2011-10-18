package com.compomics.sigpep.report;

import com.google.common.io.Files;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * This class is a
 */
public class SignatureTransitionMassMatrixReader {
    private static Logger logger = Logger.getLogger(SignatureTransitionMassMatrixReader.class);

    private final File iFile;
    private boolean iParseBarcode = true;
    private boolean iParseTarget = true;
    private boolean iParseBackground = false;

    ArrayList<String[]> iBarcode = new ArrayList<String[]>();
    ArrayList<String[]> iTarget = new ArrayList<String[]>();
    ArrayList<String[]> iBackground = new ArrayList<String[]>();

    /**
     * Construct a new Reader to parse a SignatureTransitionMassMatrix File.
     *
     * @param aFile
     */
    public SignatureTransitionMassMatrixReader(File aFile){
        iFile = aFile;
        parse();
    }

    public void parse() {
        try {
            BufferedReader lReader = Files.newReader(iFile, Charset.defaultCharset());
            String line = null;
            while((line = lReader.readLine()) != null){

                if(line.startsWith("bc") && isParseBarcode()){
                    iBarcode.add(line.split("\\t"));
                    logger.debug(line);
                    logger.debug("adding " + iBarcode.get(0).length + " barcode elements");
                }else if(line.startsWith("tg") && isParseTarget()){
                    iTarget.add(line.split("\\t"));
                    logger.debug(line);
                    logger.debug("adding " + iTarget.get(0).length + " target elements");
                }else if(line.startsWith("bg") && isParseBackground()){
                    iBackground.add(line.split("\\t"));
                    logger.debug(line);
                    logger.debug("adding " + iBackground.get(0).length + " background elements");
                }else if(isParseBackground() == false){
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

    }

    public boolean isParseBackground() {
        return iParseBackground;
    }

    public boolean isParseBarcode() {
        return iParseBarcode;
    }

    public boolean isParseTarget() {
        return iParseTarget;
    }

    public void setParseBackground(boolean aParseBackground) {
        iParseBackground = aParseBackground;
    }

    public void setParseBarcode(boolean aParseBarcode) {
        iParseBarcode = aParseBarcode;
    }

    public void setParseTarget(boolean aParseTarget) {
        iParseTarget = aParseTarget;
    }

    public ArrayList<String[]> getBackground() {
        return iBackground;
    }

    public ArrayList<String[]> getBarcode() {
        return iBarcode;
    }

    public ArrayList<String[]> getTarget() {
        return iTarget;
    }
}
