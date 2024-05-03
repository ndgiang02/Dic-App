
package CommandLine;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Collections;
import java.io.*;
import java.util.*;

public  class DictionaryManagement extends Dictionary {

    public static Dictionary dictionary = new Dictionary();

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

    public static int searchWord(Dictionary dictionary, String keyWord) {
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

    public static void deleteWord(Dictionary dictionary, int index, String path) {
        try {
            dictionary.remove(index);
            trie = new Trie();
            setTrie(dictionary);
            dictionaryExportToFile(dictionary, path);
        } catch (NullPointerException e) {
            System.out.println("Null Exception.");
        }
    }


    public static void dictionaryExportToFile(Dictionary dictionary, String path) {
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

    public static void dictionaryUpdate(Dictionary dictionary, int index, String meaning, String path) {
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

    public static void setTrie(Dictionary dictionary) {
        try {
            for (Word word : dictionary) trie.insert(word.getWord_target());
        } catch (NullPointerException e) {
            System.out.println("Something went wrong: " + e);
        }
    }

    public static void insertFromCommandLine() {
        int n;
        Scanner input = new Scanner(System.in);
        do {
            System.out.println("How many words you want to insert?");
            n = input.nextInt();
            if (n <= 0) {
                System.out.println("Invalid input. Please enter valid number.");
            }
        } while (n <= 0);
        input.nextLine();
        for (int i = 0; i < n; i++) {
            System.out.print("Enter Word " + (i + 1) + " target-explain: ");
            String inputLine = input.next();
            String[] parts = inputLine.split("-", 2);
            if (parts.length >= 2) {
                String Word_target = fixing_input(parts[0]);
                String Word_explain = fixing_input(parts[1]);
                Word word = new Word(Word_target, Word_explain);
                dictionary.add(word);
            } else {
                System.out.println("Invalid input. Please enter both target and explain separated by a hyphen.");
                i--;
            }
        }
    }

    public static String fixing_input(String inputStr) {
        return inputStr.toLowerCase();
    }

    public static int getPage() {
        if (TargetDictionary == null) {
            return 1;
        }
        int page = TargetDictionary.size();
        page = (page / wordsinlist) + 1;
        return page;
    }

    public static void showAllWords() {
        Scanner input = new Scanner(System.in);
        System.out.printf("%-7s | %-20s | %-30s%n", "N0", "English", "Vietnamese");
        boolean exit = false;
        int page = 0;
        sortWordList();
        int get;

        while (!exit) {
            for (int i = 0; i < wordsinlist; i++) {
                if (page * wordsinlist + i == TargetDictionary.size()) {
                    break;
                }
                System.out.printf("%-7d | %-20s | %-30s%n", i + 1, TargetDictionary.get(page * wordsinlist + i)
                        , dictionaryLookup(TargetDictionary.get(page * wordsinlist + i)));
            }
            System.out.println("Page " + (page + 1) + " out of " + getPage());
            System.out.println("[0] Exit "
                    + "[1] Down page "
                    + "[2] Up page\n"
                    + "Your action:");
            get = input.nextInt();
            if (get >= 0 && get <= 2) {
                switch (get) {
                    case 0:
                        exit = true;
                        break;
                    case 1:
                        if (page == 0) {
                            break;
                        }
                        page--;
                        break;
                    case 2:
                        if (page >= getPage() - 1) {
                            break;
                        }
                        page++;
                        break;
                }
            } else {
                System.out.println("Invalid input, please select from 0-2\n" + "Your action:");
            }
        }
    }
}
