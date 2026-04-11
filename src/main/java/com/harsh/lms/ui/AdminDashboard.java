package com.harsh.lms.ui;


import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

import com.harsh.lms.exception.InvalidBookException;
import com.harsh.lms.exception.LibraryHasNoMembersException;
import com.harsh.lms.exception.MemberNotFoundException;
import com.harsh.lms.model.Admin;
import com.harsh.lms.model.Book;
import com.harsh.lms.model.Member;
import com.harsh.lms.model.MemberCredentials;
import com.harsh.lms.service.BookService;
import com.harsh.lms.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminDashboard {

    private final MemberService memberService;
    private final BookService bookService;
    private final MemberDashboard memberDashboard;
    private final BookDashboard bookDashboard;
    private final Scanner sc = new Scanner(System.in);
    private Admin admin;

    @Autowired
    public AdminDashboard(MemberService memberService, BookService bookService, MemberDashboard memberDashboard, BookDashboard bookDashboard) {
        this.memberService = memberService;
        this.bookService = bookService;
        this.memberDashboard = memberDashboard;
        this.bookDashboard = bookDashboard;
    }

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

                MemberCredentials memberCredentials = new MemberCredentials();
                Member member = new Member();

                memberCredentials.setUsername(userName);
                memberCredentials.setPassword(password);

                member.setFullName(fullName);

                memberCredentials.setMember(member);
                member.setMemberCredentials(memberCredentials);

                try {
                    memberService.addMember(member);
                    System.out.println("✅ Member created successfully!\n Below are the credentials: ");
                    System.out.println("username : " + member.getMemberCredentials().getUsername());
                    System.out.println("password : " + member.getMemberCredentials().getPassword());
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }
                break;

            case 2:
                System.out.print("Enter the memberID to be deleted: ");
                int memberId = sc.nextInt();
                sc.nextLine();

                try {
                    memberService.removeMember(memberId);
                    System.out.println("\uD83D\uDDD1 Member with MemberID:  " + memberId+ " is deleted successfully.");
                } catch (MemberNotFoundException e) {
                    System.out.println("❌ " + e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 3:
                try {
                    List<Member> libraryMembers = memberService.getAllMembers();

                    libraryMembers.forEach(mem -> memberDashboard.printMemberDetails(mem.getMemberId(), mem.getFullName(), mem.getMemberCredentials().getUsername(), mem.getIssuedBooks().size()));
                } catch (LibraryHasNoMembersException e) {
                    System.out.println("📚 " + e.getMessage() + " 😔");
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
                break;

            case 4:
                System.out.print("Enter the member ID to be Searched: ");
                int memID = sc.nextInt();
                try {
                    Member searchedMember = memberService.getMemberByID(memID);
                    memberDashboard.printMemberDetails(searchedMember.getMemberId(), searchedMember.getFullName(), searchedMember.getMemberCredentials().getUsername(), searchedMember.getIssuedBooks().size());
                } catch (RuntimeException e) {
                    System.out.println("❌ "+ e.getMessage());
                }
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
                try {
                    bookService.addNewBook(book);
                } catch (RuntimeException e) {
                    System.out.println("📕 " + e.getMessage() + " ❌");
                }
                System.out.println("✅ Book added successfully!");
                break;

            case 6:
                System.out.print("Enter BookID to be Removed: ");
                int bookIdForRemoving = sc.nextInt();
                try {
                    bookService.removeBook(bookIdForRemoving);
                    System.out.println("Book with BookID "+bookIdForRemoving+" removed from DB..");
                } catch (InvalidBookException e) {
                    System.out.println("❌ " + e.getMessage());
                }
                break;

            case 7:
                try {
                    List<Book> allBooks = bookService.viewAllBooks(admin.getRole());
                    allBooks.forEach(b -> bookDashboard.bookDetailsPrinterAdmin(b.getBookId(), b.getTitle(), b.getAuthor(), b.getTotalQty(), b.getAvailableQty()));
                } catch (RuntimeException e) {
                    System.out.println("📚 " + e.getMessage() + " ❌");
                }
                break;

            case 8:
                return true;

            default:
                System.out.println("Invalid option. Try Again..");
                break;
        }
        return false;
    }

    public void start(Admin admin) {
        this.admin = admin;
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
