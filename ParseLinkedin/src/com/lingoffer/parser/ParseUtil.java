package com.lingoffer.parser;

import java.io.*;
import java.util.*;

/**
 * Created by siyu on 2017/7/23.
 */
public class ParseUtil {
    public static Map<String, Integer> loadFromFile(File f) {
        Map<String, Integer> item = null;
        try {
            item = new HashMap<>();
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) !=null) {
                int code = Integer.parseInt(line.substring(line.length() - 1));
                String name = line.substring(0, line.length() - 1);
                line.toLowerCase();
                line.trim();
                item.put(name, code);
                //System.out.println(item.toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

}
