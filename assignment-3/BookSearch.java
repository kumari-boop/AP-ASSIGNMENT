import java.util.*;

public class BookSearch {
    public static void main(String[] args) {
        ArrayList<String> books = new ArrayList<>();

        books.add("The Great Gatsby");
        books.add("To Kill a Mockingbird");
        books.add("Introduction to Java");
        books.add("The Lord of the Rings");
        books.add("Java Data Structures");

        Scanner sc = new Scanner(System.in);

        System.out.print("Search word: ");
        String word = sc.nextLine().toLowerCase();

        boolean found = false;

        for (String b : books) {
            if (b.toLowerCase().contains(word)) {
                System.out.println(b);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching books found.");
        }

        sc.close();
    }
}