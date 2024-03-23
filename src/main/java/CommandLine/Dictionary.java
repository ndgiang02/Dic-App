package CommandLine;

import java.util.ArrayList;

public class Dictionary {
    public static ArrayList<Word> words = new ArrayList<>();

    private static Dictionary instance;

    public static Dictionary getInstance() {
        if (instance == null) {
            instance = new Dictionary();
        }
        return instance;
    }
}
