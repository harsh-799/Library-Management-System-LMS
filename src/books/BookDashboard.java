package books;

public class BookDashboard {
    private void printCommonBookDetails(int bookId, String title, String author, int totalQty) {
        System.out.println("Book ID: " + bookId);
        System.out.println("Book Title: " + title);
        System.out.println("Book Author: " + author);
        System.out.println("Book TotalQty: " + totalQty);
    }

    public void bookDetailsPrinterAdmin(int bookId, String title, String author, int totalQty, int availQty) {
        printCommonBookDetails(bookId, title, author, totalQty);
        System.out.println("Book AvailQty: " + availQty);
        System.out.println("------");
    }

    public void bookDetailsPrinterMember(int bookId, String title, String author, int totalQty, int availQty) {
        printCommonBookDetails(bookId, title, author, totalQty);
        if (availQty > 0) {
            System.out.println("Status: Available ✅");
        } else {
            System.out.println("Status: Out of Stock ❌");
        }
        System.out.println("------");
    }
}
