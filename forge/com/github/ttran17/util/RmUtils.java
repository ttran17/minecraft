package com.github.ttran17.util;

import java.io.File;
import java.util.logging.Logger;

public class RmUtils {
    
    private static final Logger logger = Logger.getLogger(RmUtils.class.getName());

    public static void main(String[] args) {
        File dir = new File("/home/ttran/Projects/forge-1.5.2/mcp/src/minecraft");
        
        String pattern = ".class";
        
        remove(dir, pattern);
    }
    
    public static void remove(File dir, String pattern) {
        File[] files = dir.listFiles();
        for (File file : files) {
            
            if (file.isDirectory()) {
            	remove(file, pattern);
            }
                    
            if (file.getPath().contains(pattern)) {                
                if (file.getPath().endsWith(pattern)) {
                    logger.warning("Deleting " + file.getAbsolutePath());
                }                
                
                boolean deleted = file.delete();
                if (!deleted) {
                    logger.warning("Unable to delete: " + file.getAbsolutePath());
                }
            }
        }
    }
    
}

