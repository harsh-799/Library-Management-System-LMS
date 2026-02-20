package Admin;

import java.time.LocalTime;
import java.util.Scanner;

import Books.Book;
import Books.BookOperations;
import Member.Member;
import Member.MemberOperations;

public class AdminDashboard {

    private final MemberOperations memberOperations = new MemberOperations();
    private final BookOperations bookOperations = new BookOperations();
    private final Scanner sc = new Scanner(System.in);

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

    private void showMenu(){
        System.out.println("\n=======================================");
        System.out.println("🛠  ADMIN DASHBOARD  📋");
        System.out.println("=======================================");
        System.out.println("1️⃣  Create New Member");
        System.out.println("2️⃣  Delete Member");
        System.out.println("3️⃣  View All Members");
        System.out.println("4️⃣  View Specific Member");
        System.out.println("5️⃣  Add Books");
        System.out.println("6️⃣  Remove Books");
        System.out.println("7️⃣  View Books");
        System.out.println("8️⃣  Logout");
        System.out.print("\n👉 Enter your choice: ");

    }

    public boolean handleLibraryOption(){
        showMenu();
        int choice = sc.nextInt();
        switch(choice){
            case 1:
                System.out.println("\n👤 Creating New Member");
                System.out.println("----------------------------");
                sc.nextLine();
                System.out.print("Enter Full Name: ");
                String fullName = sc.nextLine();

                System.out.print("Enter Username: ");
                String userName = sc.nextLine();
                System.out.print("Enter Password: ");
                String password = sc.next();

                Member member = new Member(fullName,userName,password);
                memberOperations.addMember(member);
                break;

            case 2:
                System.out.print("Enter the memberID to be deleted: ");
                long memberId = sc.nextLong();
                sc.nextLine();

                Member deletedMember = memberOperations.deleteMember(memberId);
                if (deletedMember != null){
                    System.out.println("\uD83D\uDDD1 Member named " + deletedMember.getFullName() + " with ID:  " + deletedMember.getMemberId()+ "is deleted successfully.");
                } else {
                    System.out.println("❌ No member found with that ID.");
                }
                break;

            case 3:
                memberOperations.getAllMembers();
                break;

            case 4:
                System.out.print("Enter the member ID to be Searched: ");
                long memID = sc.nextLong();
                System.out.println();
                Member resultMember = memberOperations.getMemberByID(memID);
                memberOperations.printMemberDetails(resultMember);
                break;

            case 5:
                System.out.println("\n📚 Adding New Book");
                System.out.println("----------------------------");
                System.out.print("Enter BookID: ");
                int bookId = sc.nextInt();
                sc.nextLine();
                System.out.print("Enter Title: ");
                String title = sc.nextLine();
                System.out.print("Enter Author: ");
                String author = sc.nextLine();
                System.out.print("Enter Total Qty: ");
                int totalQty = sc.nextInt();

                Book book = new Book(bookId,title,author,totalQty);
                bookOperations.addNewBook(book);
                break;

            case 6:
                System.out.print("Enter BookID to be Removed: ");
                int bookId1 = sc.nextInt();
                Book removedBook = bookOperations.removeBook(bookId1);
                if (removedBook != null){
                    System.out.println("🗑 Book removed successfully.");
                    System.out.println("Removed Book Details are: ");
                    System.out.println("BookID: "+removedBook.getBookId());
                    System.out.println("Title: "+removedBook.getTitle());
                    System.out.println("Author: "+removedBook.getAuthor());
                }
                break;

            case 7:
                bookOperations.viewAllBooksAdmin();
                break;

            case 8:
                return true;

            default:
                System.out.println("Invalid option. Try Again..");
                break;
        }
        return false;
    }

    public void start() {
        String greeting = getGreeting();

        if (greeting.equals("It's Closed")){
            System.out.println("\n🛠 Welcome Admin!");
            System.out.println("☀ Good " + greeting + "!\n");
            return;
        }

        System.out.println("\n🛠 Welcome Admin!");
        System.out.println("☀ Good " + greeting + "!\n");

        boolean exit = false;
        while (!exit){
            exit = handleLibraryOption();
        }
        System.out.println("\n👋 Logged out successfully.");
        System.out.println("Have a great day Admin!\n");
    }
}
