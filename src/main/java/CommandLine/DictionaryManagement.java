package CommandLine;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Collections;
import java.io.*;
import java.util.*;

public  class DictionaryManagement extends Dictionary {

    private static Dictionary dictionary = new Dictionary();

    private static Trie trie = new Trie();

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
    public static void insertFromFile(Dictionary dictionary, String path) {
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // tach string[] lam 2 de dua vao word_target va word_explain
                String[] parts = line.split("\t", 2);
                if (parts.length >= 2) {
                    String word_target = parts[0].trim();// trim() -> cat nhung dau cach o dau va cuoi
                    String word_explain = parts[1].trim();
                    Word word = new Word(word_target, word_explain);
                    dictionary.add(word);
                }
            }
            bufferedReader.close();
        }
            catch(IOException e){
                System.out.println("An error occur with file: " + e);
            } catch(Exception e){
                System.out.println("Something went wrong: " + e);
            }
        }

    /* Thêm tu */
    public static void addWord(Word word, String path) {
        try (FileWriter fileWriter = new FileWriter(path, true);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {
             writer.write(word.getWord_target() + "\t" + word.getWord_explain());
             writer.newLine();
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


    public void dictionaryExportToFile(Dictionary dictionary, String path) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            for (Word word : dictionary) {
                writer.write(word.getWord_target() + "\t" + word.getWord_explain());
                writer.newLine();
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