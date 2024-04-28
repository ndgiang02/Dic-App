package CommandLine;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Collections;
import java.io.*;
import java.util.*;

import static Constant.Constant.IN_PATH;


public  class DictionaryManagement extends Dictionary {
    private static DictionaryManagement instance;
    static int number_of_words = 0;
    private static Trie trie = new Trie();

    static List<String> TargetDictionary = FXCollections.observableArrayList();
    public static final int wordsinlist = 10;

    public static void sortWordList() {
        Collections.sort(dictionary, new Comparator<Word>() {
            @Override
            public int compare(Word word1, Word word2) {
                // So sánh theo thuộc tính target
                return word1.getWord_target().compareTo(word2.getWord_target());
            }
        });
    }

    /* Doc File Tu Dien */
    public static void insertFromFile() {
        try {
            String line ;
            BufferedReader reader = new BufferedReader(new FileReader(IN_PATH));
            while ((line = reader.readLine()) != null) {
                // tach string[] lam 2 de dua vao word_target va word_explain
                String[] parts = line.split("\t", 2);
                if (parts.length >= 2) {
                    String word_target = parts[0].trim();// trim() -> cat nhung dau cach o dau va cuoi
                    String word_explain = parts[1].trim();
                    Word word = new Word(word_target, word_explain);
                    dictionary.add(word);
                } else {
                    System.out.println("ignoring line: " + line);
                }
            }
            sortWordList();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /* Thêm tu */
    public static void addWord(Word word, String path) {
        try (FileWriter fileWriter = new FileWriter(path, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(word.getWord_target() + "\t" + word.getWord_explain());
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.out.println("IOException.");
        } catch (NullPointerException e) {
            System.out.println("Null Exception.");
        }
    }

    public int searchWord(Dictionary dictionary, String keyWord) {
        try {
            sortWordList();
            int left = 0;
            int right = dictionary.size() - 1;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                int res = dictionary.get(mid).getWord_target().compareTo(keyWord);
                if (res == 0) return mid;
                if (res <= 0) left = mid + 1;
                else right = mid - 1;
            }
        } catch (NullPointerException e) {
            System.out.println("Null Exception.");
        }
        return -1;
    }

    public static DictionaryManagement getInstance() {
        if (instance == null) {
            instance = new DictionaryManagement();
        }
        return instance;
    }


    public void deleteWord(Dictionary dictionary, int index, String path) {
        try {
            dictionary.remove(index);
            trie = new Trie();
            setTrie(dictionary);
            dictionaryExportToFile(dictionary, path);
        } catch (NullPointerException e) {
            System.out.println("Null Exception.");
        }
    }

    public static String fixing_input(String input) {
        if (input.equals("")) {
            return "";
        }
        input = input.toLowerCase();
        input = input.trim();
        char firstChar = input.charAt(0);
        char upperFirstChar = Character.toUpperCase(firstChar);
        return upperFirstChar + input.substring(1);
    }


    public void dictionaryExportToFile(Dictionary dictionary, String path) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            for (Word word : dictionary) {
                writer.write(word.getWord_target() + "\t" + word.getWord_explain() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dictionaryUpdate(Dictionary dictionary, int index, String meaning, String path) {
        try {
            dictionary.get(index).setWord_explain(meaning);
            dictionaryExportToFile(dictionary, path);
        } catch (NullPointerException e) {
            System.out.println("Null Exception.");
        }
    }


    public static ObservableList<String> dictionaryLookup(String look_up_word) {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            List<String> results = trie.autoComplete(look_up_word);
            if (results != null) {
                int length = Math.min(results.size(), 15);
                for (int i = 0; i < length; i++) list.add(results.get(i));
            }
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e);
        }
        return list;
    }

    public void setTrie(Dictionary dictionary) {
        try {
            for (Word word : dictionary) trie.insert(word.getWord_target());
        } catch (NullPointerException e) {
            System.out.println("Something went wrong: " + e);
        }
    }
}