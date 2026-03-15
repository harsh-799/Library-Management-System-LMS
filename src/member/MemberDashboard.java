package member;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.ListIterator;
import java.util.Scanner;
import books.BookOperations;
import books.Book;

public class MemberDashboard {

    public Member member;
    private final Scanner sc = new Scanner(System.in);
    private final MemberOperations memberOperations = new MemberOperations();
    private final BookOperations bookOperations = new BookOperations();

    public String getGreeting(){
        LocalTime lt = LocalTime.now();
        int currTime = lt.getHour();

        if (currTime >= 9 && currTime < 12){
            return "Morning";
        } else if (currTime >= 12 && currTime < 17){
            return "Afternoon";
        } else if (currTime >= 17 && currTime < 22){
            return "Evening";
        } else {
            return "It's Closed";
        }
    }

    public void showMenu(){
        System.out.println("\n---------------------------------------");
        System.out.println("📚 Member Options");
        System.out.println("---------------------------------------");
        System.out.println("1️⃣  View Available Books");
        System.out.println("2️⃣  Search Books");
        System.out.println("3️⃣  Issue Book");
        System.out.println("4️⃣  Return Book");
        System.out.println("5️⃣  View My Issued Books");
        System.out.println("6️⃣  View My Profile");
        System.out.println("7️⃣  Update Password");
        System.out.println("8️⃣  Logout");

        System.out.print("\n👉 Enter your choice: ");
    }

    public void showSearchBookMenu(){
        System.out.println("Searching By: ");
        System.out.println("1. By BookID");
        System.out.println("2. By Author");
        System.out.println("3. By Book Title");
        System.out.print("Enter choice: ");
    }

    public void searchBook(int searchChoice){
        switch(searchChoice){
            case 1:
                System.out.print("Enter the BookID: ");
                try {
                    int bookId = sc.nextInt();
                    bookOperations.searchBook(bookId);
                } catch (InputMismatchException e){
                    System.out.println("Invalid input");
                }
                break;

            case 2:
                System.out.println("Enter the Author Name: ");
                String authorName = sc.nextLine();
                bookOperations.searchBookByAuthor(authorName);
                break;

            case 3:
                System.out.print("Enter the Book Title: ");
                String bookTitle = sc.nextLine();
                bookOperations.searchBookByTitle(bookTitle);
                break;

            default:
                System.out.println("Invalid Option.");
                break;
        }
    }

    public void resultPrinter(Book book) {
        System.out.println("Book Found in the Library: ");
        System.out.println("BookID: " + book.getBookId());
        System.out.println("Book Title: " + book.getTitle());
        System.out.println("Book Author: " + book.getAuthor());
        if (book.getAvailableQty() > 0) {
            System.out.println("Status: Available");
        } else {
            System.out.println("Status: Not Available");
        }
    }

    public void issuesPrinter(Book book){
        System.out.println("BookID: " + book.getBookId());
        System.out.println("Book Title: " + book.getTitle());
        System.out.println("Book Author: " + book.getAuthor());
        System.out.println(" ---- ");
    }

    public void printMemberDetails(Long memberID, String fullname, String username, String bookIssued){
        System.out.println("MemberID: " + memberID);
        System.out.println("FullName: " + fullname);
        System.out.println("userName: " + username);
        System.out.println("Book Issued: " + bookIssued);
        System.out.println("-----           -----");
    }

    public boolean handleChoice(){
        showMenu();

        int choices;

        try {
            choices = sc.nextInt();
        }catch(InputMismatchException e){
            System.out.println("Invalid input. Please enter a number.");
            sc.nextLine(); // clear wrong input
            return false;
        }

        switch (choices){
            case 1:
                System.out.println("Below are the Available books in library: ");
                bookOperations.viewAllBooks(member.getRole());
                break;

            case 2:
                showSearchBookMenu();
                int searchChoice = sc.nextInt();
                sc.nextLine();
                System.out.println();
                searchBook(searchChoice);
                break;

            case 3:
                System.out.println("\n📥 Issue a Book");

                if (member.getMemberIssuedBookIds().size() == 3){
                    System.out.println("⚠️ Limit reached! You have already issued 3 books 📚. Only 3 allowed at once.");
                    break;
                }

                System.out.print("📘 Enter Book ID: ");
                int bookId = sc.nextInt();

                boolean alreaadyIssued = false;

                for (Integer bookIDs: member.getMemberIssuedBookIds()){
                    if (bookIDs == bookId){
                        alreaadyIssued = true;
                        break;
                    }
                }

                if (alreaadyIssued) {
                    System.out.println("⚠ You have already issued this book.");
                    break;
                }

                boolean bookIssuedStatus = bookOperations.issueBook(bookId, member.getMemberId());

                if (bookIssuedStatus){
                    member.setMemberIssuedBookIds(bookId);
                    member.setTotalBooksIssued(member.getTotalBooksIssued()+1);
                    System.out.println("✅ Book issued successfully!");
                }
                break;

            case 4:
                System.out.println("\n📤 Return a Book");

                if (member.getMemberIssuedBookIds().isEmpty()){
                    System.out.println("📚 No book has been issued yet 🙅‍♂️");
                    break;
                }

                System.out.print("📘 Enter Book ID: ");
                int bookIdForReturn = sc.nextInt();
                boolean bookReturnStatus = bookOperations.returnBook(bookIdForReturn, member.getMemberId());

                if (bookReturnStatus){
                    ListIterator<Integer> listIterator = member.getMemberIssuedBookIds().listIterator();

                    while (listIterator.hasNext()){
                        Integer currBookId = listIterator.next();

                        if (currBookId == bookIdForReturn){
                            listIterator.remove();
                        }
                    }
                    member.setTotalBooksIssued(member.getTotalBooksIssued() - 1);
                    System.out.println("✅ Book returned successfully!");
                    break;
                }
                System.out.println("❌ Invalid Book ID or return failed.");
                break;

            case 5:
                System.out.println("\n📚 Your Issued Books:");
                System.out.println("---------------------------------------");
                ArrayList<Integer> issuedBook = member.getMemberIssuedBookIds();

                if (!issuedBook.isEmpty()) {
                    for (Integer bookID: issuedBook){
                        issuesPrinter(bookOperations.searchBookForIssuedBooks(bookID));
                    }
                    break;
                }
                System.out.println("📭 You have not issued any books.");
                break;

            case 6:
                System.out.println("\n👤 Your Profile");
                System.out.println("---------------------------------------");
                System.out.println("🆔 Member ID: " + member.getMemberId());
                System.out.println("📛 Name: " + member.getFullName());
                System.out.println("🔑 Username: " + member.getUsername());
                System.out.println("📚 Books Issued: " + member.getTotalBooksIssued());
                break;

            case 7:
                System.out.println("\n🔐 Update Password");
                System.out.print("Enter the old password: ");
                String oldPassword = sc.next();
                if (member.getPassword().equals(oldPassword)) {
                    System.out.print("Enter the new Password: ");
                    String newPassword = sc.next();
                    memberOperations.updatePassword(newPassword, member);
                }
                break;

            case 8:
                return true;

            default:
                System.out.println("Invalid Option");
                break;
        }
        return false;
    }

    public void start(Member member) {
        this.member = member;

        // Sync the Data from the DB.
        this.member = memberOperations.syncMemberOnLogin(member);

        String greeting = getGreeting();

        if (greeting.equals("It's Closed")){
            System.out.println("⏰ Library is currently closed.");
            return;
        }

        System.out.println("\n=======================================");
        System.out.println("👤  MEMBER DASHBOARD  📋");
        System.out.println("=======================================\n");

        System.out.println("👋 Welcome, " + member.getFullName() + "!");
        System.out.println("☀ Good " + greeting + "!\n");

        boolean exit = false;
        while (!exit){
            exit = handleChoice();
        }
        System.out.println("\n👋 Logged out successfully.");
        System.out.println("See you next time, " + member.getFullName() + "!\n");
    }
}
