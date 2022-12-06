package pt.ipca.io;

import org.json.simple.JSONValue;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JSON {

    private static final int TAB = 4;

    public static void save(ArrayList list, String path) {
        String json = JSONValue.toJSONString(list);
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
