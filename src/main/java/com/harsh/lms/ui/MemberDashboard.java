package com.harsh.lms.ui;

import java.time.LocalTime;
import java.util.*;

import com.harsh.lms.exception.*;
import com.harsh.lms.model.Book;
import com.harsh.lms.model.BookIssued;
import com.harsh.lms.model.Member;
import com.harsh.lms.service.BookService;
import com.harsh.lms.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberDashboard {

    private Member member;
    private final Scanner sc = new Scanner(System.in);
    private final MemberService memberService;
    private final BookService bookService;
    private final BookDashboard bookDashboard;

    @Autowired
    public MemberDashboard(MemberService memberService, BookService bookService, BookDashboard bookDashboard) {
        this.memberService = memberService;
        this.bookService = bookService;
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

    private void showSearchBookMenu(){
        System.out.println("Searching By: ");
        System.out.println("1. By BookID");
        System.out.println("2. By Author");
        System.out.println("3. By Book Title");
        System.out.print("Enter choice: ");
    }

    private void searchBook(int searchChoice){
        switch(searchChoice){
            case 1:
                System.out.print("Enter the BookID: ");
                try {
                    int bookId = sc.nextInt();
                    Book foundBook = bookService.searchBookById(bookId);
                    bookDashboard.bookDetailsPrinterMember(foundBook.getBookId(), foundBook.getTitle(), foundBook.getAuthor(), foundBook.getTotalQty(), foundBook.getAvailableQty());
                } catch (InputMismatchException e){
                    System.out.println("Invalid input, Please enter Digit");
                } catch (InvalidBookException e) {
                    System.out.println("❌ "+e.getMessage());
                }
                break;

            case 2:
                System.out.println("Enter the Author Name: ");
                sc.nextLine();
                String authorName = sc.nextLine();
                try {
                    List<Book> foundBookByAuthorName = bookService.searchBookByAuthor(authorName);
                    foundBookByAuthorName.forEach(x -> bookDashboard.bookDetailsPrinterMember(x.getBookId(), x.getTitle(), x.getAuthor(), x.getTotalQty(), x.getAvailableQty()));

                } catch (BookNotFoundByAuthorException e) {
                    System.out.println("🔍 "+ e.getMessage() + " 📚");
                }
                break;

            case 3:
                System.out.print("Enter the Book Title: ");
                String bookTitle = sc.nextLine();
                try {
                    List<Book> foundBookByTitle = bookService.searchBookByTitle(bookTitle);
                    foundBookByTitle.forEach(x -> bookDashboard.bookDetailsPrinterMember(x.getBookId(), x.getTitle(), x.getAuthor(), x.getTotalQty(), x.getAvailableQty()));
                } catch (BookNotFoundByTitleException e) {
                    System.out.println("🔍 "+ e.getMessage() + " 📚");
                }

                break;

            default:
                System.out.println("Invalid Option, Select Between (1 - 3)");
                break;
        }
    }

    public void issuesPrinter(Book book){
        System.out.println("BookID: " + book.getBookId());
        System.out.println("Book Title: " + book.getTitle());
        System.out.println("Book Author: " + book.getAuthor());
        System.out.println(" ---- ");
    }

    public void printMemberDetails(int memberID, String fullname, String username, int bookIssued){
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
                try {
                    List<Book> allBooks = bookService.viewAllBooks(member.getRole());
                    System.out.println("Below are the Available books in library: ");
                    allBooks.forEach(b -> bookDashboard.bookDetailsPrinterMember(b.getBookId(), b.getTitle(), b.getAuthor(), b.getTotalQty(), b.getAvailableQty()));
                } catch (RuntimeException e) {
                    System.out.println("📚 " + e.getMessage() + " ❌");
                }
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

                System.out.print("📘 Enter Book ID: ");
                int bookId = sc.nextInt();

                try {
                    bookService.issueBook(bookId, member);
                    System.out.println("✅ Book issued successfully!");

                } catch (BookNotAvailableException e) {
                    System.out.println("⏰ "+e.getMessage() + " 📚. Kindly wait for a return 🔄. ");
                } catch (IssueLimitReachedException e) {
                    System.out.println("⚠️ "+ e.getMessage() + " 📚. Only 3 allowed at once.");
                } catch (InvalidBookException e) {
                    System.out.println("❌ "+ e.getMessage());
                } catch (BookAlreadyIssuedException e) {
                    System.out.println("⚠️ "+ e.getMessage());
                } catch (Exception e) {
                    System.out.println("❌ Something went wrong");
                }
                break;

            case 4:
                System.out.println("\n📤 Return a Book");

                System.out.print("📘 Enter Book ID: ");
                int bookIdForReturn = sc.nextInt();

                try {
                    bookService.returnBook(bookIdForReturn, member);
                    System.out.println("✅ Book returned successfully!");
                } catch (BookIssuedNotFoundException e) {
                    System.out.println("📚 " + e.getMessage() + " 🙅‍♂️");
                } catch (InvalidBookException e) {
                    System.out.println("❌ "+ e.getMessage());
                }
                break;

            case 5:
                System.out.println("\n📚 Your Issued Books:");
                System.out.println("---------------------------------------");

                try {
                    List<BookIssued> bookIssuedList =  memberService.getAllIssued(member);
                    bookIssuedList.forEach(b -> issuesPrinter(b.getBook()));
                } catch (BookIssuedNotFoundException e) {
                    System.out.println("📚 " + e.getMessage() + " 🙅‍♂️");
                }
                // System.out.println("📭 You have not issued any books.");
                break;

            case 6:
                System.out.println("\n👤 Your Profile");
                System.out.println("---------------------------------------");
                System.out.println("🆔 Member ID: " + member.getMemberId());
                System.out.println("📛 Name: " + member.getFullName());
                System.out.println("🔑 Username: " + member.getMemberCredentials().getUsername());
                System.out.println("📚 Books Issued: " + member.getIssuedBooks().size());
                break;

            case 7:
                System.out.println("\n🔐 Update Password");
                System.out.print("Enter the old password: ");
                String oldPassword = sc.next();
                System.out.print("Enter the new Password: ");
                String newPassword = sc.next();
                try {
                    memberService.updatePassword(member, oldPassword, newPassword);
                    System.out.println("🔑✅ Password updated successfully!");
                } catch (RuntimeException e) {
                    System.out.println("❌ "+e.getMessage());
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
        this.member = memberService.syncMemberOnLogin(member);

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