import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

class Node {
    String name;
    Node next;

    public Node(String name) {
        this.name = name;
        this.next = null;
    }
}

class NameList {
    Node[] index;

    public NameList() {
        index = new Node[26];
        for (int i = 0; i < 26; i++) {
            index[i] = null;
        }
    }

    private static int getLetterValue(char ch) {
        return ch - 'a';
    }

    public void insert(String name) {
        int idx = getLetterValue(name.charAt(0));
        Node newNode = new Node(name);
        if (index[idx] == null) {
            index[idx] = newNode;
        } else {
            Node current = index[idx];
            Node prev = null;
            while (current != null && current.name.compareTo(name) < 0) {
                prev = current;
                current = current.next;
            }
            if (prev == null) {
                newNode.next = index[idx];
                index[idx] = newNode;
            } else {
                newNode.next = prev.next;
                prev.next = newNode;
            }
        }
    }

    public void displayList() {
        for (int i = 0; i < 26; i++) {
            Node current = index[i];
            while (current != null) {
                System.out.println(current.name);
                current = current.next;
            }
        }
    }

    public int calculateNameCode(String name) {
        int idx1 = getLetterValue(name.charAt(0));
        int idx2 = getLetterValue(name.charAt(1));
        int idx3 = getLetterValue(name.charAt(2));
        return (idx1 * 26 * 26) + (idx2 * 26) + idx3;
    }


    public int countNamesWithStartingLetter(char letter) {
        int indexToCount = getLetterValue(letter);
        int count = 0;
        Node node = index[indexToCount];
        while (node != null && node.name.charAt(0) == letter) {
            count++;
            node = node.next;
        }
        return count;
    }

    public void printNamesStartingWithLetter(char letter) {
        int indexToPrint = getLetterValue(letter);
        Node printNode = index[indexToPrint];
        boolean found = false;
        while (printNode != null && printNode.name.charAt(0) == letter) {
            System.out.println(printNode.name);
            found = true;
            printNode = printNode.next;
        }
        if (!found) {
            System.out.println("No names found starting with '" + letter + "'.");
        }
    }

    public void deleteName(String nameToDelete) {
        int indexToDelete = calculateNameCode(nameToDelete) % 7070;

        int idx = indexToDelete % 26;
        Node current = index[idx];
        Node prev = null;
        while (current != null && !current.name.equalsIgnoreCase(nameToDelete)) {
            prev = current;
            current = current.next;
        }
        if (current != null) {
            if (prev == null) {
                index[idx] = current.next;
            } else {
                prev.next = current.next;
            }
            System.out.println(nameToDelete + " has been deleted from the list.");
        } else {
            System.out.println(nameToDelete + " not found in the list.");
        }
    }


    public static class Main {
        public static void main(String[] args) {
            NameList nameList = new NameList();
            Scanner scanner = new Scanner(System.in);
            String source = "/Users/aakriti.thapa/Desktop/cmps 390/namelist.txt";

            // Read names from the file
            try {
                BufferedReader reader = new BufferedReader(new FileReader(source));
                String line;
                while ((line = reader.readLine()) != null) {
                    String name = line.trim().toLowerCase();
                    if (name.matches("[a-z]{3,}")) nameList.insert(name);
                }
                System.out.println("Names loaded successfully!");
            } catch (IOException e) {
                System.out.println("Error reading the file: " + e.getMessage());
                return;
            }

            char choice;
            do {
                System.out.println("Menu:");
                System.out.println("1. Display the list");
                System.out.println("2. Request the length of the list");
                System.out.println("3. Delete a single name from the list");
                System.out.println("4. Request the length of a section of the list");
                System.out.println("5. Print names starting with a specific letter");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.next().charAt(0);

                switch (choice) {
                    case '1' -> nameList.displayList();
                    case '2' -> {
                        int length = 0;
                        for (int i = 0; i < 26; i++) {
                            Node current = nameList.index[i];
                            while (current != null) {
                                length++;
                                current = current.next;
                            }
                        }
                        System.out.println("Length of the list: " + length);
                    }
                    case '3' -> {
                        System.out.print("Enter the name to delete: ");
                        String nameToDelete = scanner.next().trim().toLowerCase();
                        nameList.deleteName(nameToDelete);
                    }
                    case '4' -> {
                        System.out.print("Enter the starting letter: ");
                        char letter = scanner.next().charAt(0);
                        int count = nameList.countNamesWithStartingLetter(letter);
                        System.out.println("Number of names starting with '" + letter + "': " + count);
                    }
                    case '5' -> {
                        System.out.print("Enter the letter to print names: ");
                        char printLetter = scanner.next().charAt(0);
                        nameList.printNamesStartingWithLetter(printLetter);
                    }
                    case '6' -> System.out.println("Exiting program. Goodbye!");
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != '6');
        }
    }
}
}
