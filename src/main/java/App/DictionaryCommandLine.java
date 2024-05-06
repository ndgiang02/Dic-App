package App;

import CommandLine.DictionaryManagement;
import CommandLine.GameCommandLine;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DictionaryCommandLine extends DictionaryManagement {

    public static void main(String[] args) throws IOException {
        dictionaryAdvanced();
    }

    public static void dictionaryAdvanced() {
        try {
            Scanner sc = new Scanner(System.in);
            boolean exit = false;
            int get;
            while (!exit) {
                System.out.print("Welcome to Dictionary Application!\n"
                        + "[0] Exit\n"
                        + "[1] Add\n"
                        + "[2] Remove\n"
                        + "[3] Update\n"
                        + "[4] Display\n"
                        + "[5] Lookup\n"
                        + "[6] Search\n"
                        + "[7] Game\n"
                        + "[8] Import from file\n"
                        + "[9] Export to file\n"
                        + "Your action: ");
                get = sc.nextInt();
                if (get <= 9 && get >= 0) {
                    switch (get) {
                        case 0:
                            System.out.println("Exit successfully");
                            exit = true;
                            break;
                        case 1:
                            insertFromCommandLine();
                            break;
                        case 2:
                            System.out.print("Input delete word: ");
                            String deleteWord = sc.next();
                            int deleteIndex = searchWord(dictionary, deleteWord);
                            if (deleteIndex != -1) {
                                removeWord(dictionary, deleteIndex);
                                System.out.println("Word deleted successfully.");
                            } else {
                                System.out.println("Word not found in the dictionary.");
                            }
                            break;
                        case 3:
                            System.out.print("Input word to update: ");
                            String updateWord = sc.next();
                            int updateIndex = searchWord(dictionary, updateWord);
                            if (updateIndex != -1) {
                                System.out.print("Input new meaning: ");
                                String newMeaning = sc.next();
                                updateWord(dictionary, updateIndex, newMeaning);
                                System.out.println("Word updated successfully.");
                            } else {
                                System.out.println("Word not found in the dictionary.");
                            }
                            break;
                        case 4:
                            showAllWords();
                            break;
                        case 5:
                            System.out.print("Look up: ");
                            String lookUpWord = sc.next();
                            System.out.println("Look up return: " + dictionaryLookup(lookUpWord));
                            break;
                        case 6:
                            System.out.print("Search word: ");
                            String Search = sc.next();
                            searchWord(dictionary, Search);
                          //  System.out.println("Word searched successfully." + );
                            break;
                        case 7:
                            GameCommandLine.MutilChoice();
                            break;
                        case 8:
                            insertFromFile(dictionary, "src/main/resources/data/test.txt");
                            System.out.println("Imported from file successfully.");
                            break;
                        case 9:
                            dictionaryExportToFile(dictionary, "src/main/resources/data/test.txt");
                            System.out.println("E to file successfully");
                            break;
                    }
                } else {
                    System.out.println("Invalid input, please select from 0-9\n" + "Your action:");
                }
            }
            sc.close();
        } catch (InputMismatchException e) {
            System.out.println("Wrong input");
            dictionaryAdvanced();
        }
    }

}

