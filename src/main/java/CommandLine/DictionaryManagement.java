package CommandLine;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Collections;
import java.io.*;
import java.util.*;


public  class DictionaryManagement extends Dictionary {

    private static DictionaryManagement instance;
    static int number_of_words = 0;
    private static Trie trie = new Trie();

    static List<String> TargetDictionary = FXCollections.observableArrayList();
    public static final int wordsinlist = 10;

    private static final String IN_PATH = "src/main/resources/Vocab/dictionaries1.txt";
    private static final String OUT_PATH = "src/main/resources/Vocab/dictionaries_out.txt";

    public static void sortWordList() {
        Collections.sort(words, new Comparator<Word>() {
            @Override
            public int compare(Word word1, Word word2) {
                // So sánh theo thuộc tính target
                return word1.getWord_target().compareTo(word2.getWord_target());
            }
        });
    }

    public static void insertFromCommandLine() {
        int n;
        Scanner input = new Scanner(System.in);
        do {
            System.out.println("How many words you want to insert?");
            n = input.nextInt();
            number_of_words += n;
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
                //Word New_word = new Word(Word_target, Word_explain);
                addWord(Word_target, Word_explain);
            } else {
                System.out.println("Invalid input. Please enter both target and explain separated by a hyphen.");
                i--;
            }
        }
    }

/**
    public static void insertFromFile() {
        try {
            File inFile = new File(IN_PATH);
            FileReader fileReader = new FileReader(inFile);
            BufferedReader reader = new BufferedReader(fileReader);
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] wordsInLine = line.split(",");
                Word temp = new Word(wordsInLine[0], wordsInLine[1]);
                words.add(temp);

            }
            sortWordList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 */

    public static void insertFromFile() {
        try {
            Scanner scan = new Scanner(new File(IN_PATH));
            String line = scan.nextLine();
            BufferedReader reader = new BufferedReader(new FileReader(IN_PATH));
            while ((line = reader.readLine()) != null) {
                // tach string[] lam 2 de dua vao word_target va word_explain
                String[] parts = line.split("\t", 2);
                if (parts.length >= 2) {
                    String word_target = parts[0].trim();// trim() -> cat nhung dau cach o dau va cuoi
                    String word_explain = parts[1].trim();
                    Word word = new Word(word_target, word_explain);
                    words.add(word);
                } else {
                    System.out.println("ignoring line: " + line);
                }
            }
            reader.close();
        } catch (Exception e) {

        }
    }


    public static void addWord(String Word_target, String Word_explain) {
        Word_target = fixing_input(Word_target);
        Word_explain = fixing_input(Word_explain);
        Word New_word = new Word(Word_target, Word_explain);
        words.add(New_word);
        trie.insert(fixing_input(Word_target));
    }

    public static DictionaryManagement getInstance() {
        if (instance == null) {
            instance = new DictionaryManagement();
        }
        return instance;
    }
    public void removeFromDictionary(String remove_word) {
        remove_word = fixing_input(remove_word);
        Iterator<Word> iterator = words.iterator();
        while (iterator.hasNext()) {
            Word word = iterator.next();
            if (word.getWord_target().equals(remove_word)) {
                iterator.remove();
                trie.delete(remove_word);
                System.out.println("Delete " + remove_word + " succesfully");
                return;
            }
        }
        System.out.println("There is no word");

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



    public void dictionaryExportToFile()
    {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(OUT_PATH));
            for (Word word : words) {
                writer.write(word.getWord_target() + "\t" + word.getWord_explain() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int isContain(String str1, String str2) {
        for (int i = 0; i < Math.min(str1.length(), str2.length()); i++) {
            if (str1.charAt(i) > str2.charAt(i)) {
                return 1;
            } else if (str1.charAt(i) < str2.charAt(i)) {
                return -1;
            }
        }
        if (str1.length() > str2.length()) {
            return 1;
        }
        return 0;
    }

/**
 public static int binaryCheck(int start, int end, String word) {
 if (end < start) {
 return -1;
 }
 int mid = start + (end - start) / 2;
 int compareNext = word.compareTo(words.get(mid).getWord_target());
 if (mid == 0) {
 if (compareNext < 0) {
 return 0;
 } else if (compareNext > 0) {
 return binaryCheck(mid + 1, end, word);
 } else {
 return -1;
 }
 } else {
 int comparePrevious = word.compareTo(words.get(mid - 1).getWord_target());
 if (comparePrevious > 0 && compareNext < 0) {
 return mid;
 } else if (comparePrevious < 0) {
 return binaryCheck(start, mid - 1, word);
 } else if (compareNext > 0) {
 if (mid == words.size() - 1) {
 return words.size();
 }
 return binaryCheck(mid + 1, end, word);
 } else {
 return -1;
 }
 }
 }


 public static int binaryLookup(int start, int end, String word) {
 if (end < start) {
 return -1;
 }
 int mid = start + (end - start) / 2;
 int compare = isContain(word, words.get(mid).getWord_target());
 if (compare == -1) {
 return binaryLookup(start, mid - 1, word);
 } else if (compare == 1) {
 return binaryLookup(mid + 1, end, word);
 } else {
 return mid;
 }
 }

 */

    /**
     public static void showWordLookup(String word, int index) {
     if (index < 0) {
     //System.out.println((new Spelling.("src/main/java/big.txt")).correct(word.toLowerCase()));
     return;
     }
     ArrayList<Word> listWordSearching = new ArrayList<Word>();
     int j = index;
     while (j >= 0) {
     if (isContain(word, words.get(j).getWord_target()) == 0) {
     j--;
     } else {
     break;
     }
     }
     for (int i = j + 1; i <= index; i++) {
     Word temp = new Word(words.get(i).getWord_target(), words.get(i).getWord_explain());
     listWordSearching.add(temp);
     }

     for (int i = index + 1; i < words.size(); i++) {
     if (isContain(word, words.get(i).getWord_target()) == 0) {
     Word temp = new Word(words.get(i).getWord_target(), words.get(i).getWord_explain());
     listWordSearching.add(temp);
     } else {
     break;
     }
     }
     for (Word wordSearching : listWordSearching) {
     System.out.println(wordSearching.getWord_target());
     }
     }
     */

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

    public void dictionaryUpdate(String update_word)
    {
        Scanner input1 = new Scanner(System.in);
        Iterator<Word> iterator = words.iterator();
        while (iterator.hasNext()) {
            Word word = iterator.next();
            if (word.getWord_target().equals(fixing_input(update_word))) {
                System.out.println("Input new explain");
                String inputLine = input1.next();
                String Word_explain = fixing_input(inputLine);
                word.setWord_explain(Word_explain);
                System.out.println("Update successfully");
                return;
            }
        }
        System.out.println("No Word");
    }

    public ObservableList<String> getdictionary(String searchWord) {
        TargetDictionary = trie.autoComplete(fixing_input(searchWord));
        return (ObservableList<String>) TargetDictionary;
    }

    public static String dictionaryLookup(String look_up_word) {
        Iterator<Word> iterator = words.iterator();
        while (iterator.hasNext()) {
            Word word = iterator.next();
            if (word.getWord_target().equals(fixing_input(look_up_word))) {
                return word.getWord_explain();
            }
        }
        return "No Word";
    }

    public static int getPage() {
        if (TargetDictionary == null) {
            return 1;
        }
        int page = TargetDictionary.size();
        page = (page / wordsinlist) + 1;
        return page;
    }
}