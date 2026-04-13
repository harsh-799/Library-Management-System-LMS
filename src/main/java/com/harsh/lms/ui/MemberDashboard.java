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