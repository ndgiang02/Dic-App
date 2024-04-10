package CommandLine;

import java.util.ArrayList;

public class Dictionary extends ArrayList<Word>{
    private static Dictionary instance;

    public static Dictionary dictionary = new Dictionary();

    public static Dictionary getInstance() {
        if (instance == null) {
            instance = new Dictionary();
        }
        return instance;
    }
}
