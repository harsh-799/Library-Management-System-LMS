package books;

import java.io.*;
import java.util.ArrayList;

public class BookRepo {

    private static final String FILE_PATH = "books/BooksData.ser";

    public void serializeData(ArrayList<Book> bookList){
        try {
            FileOutputStream fos = new FileOutputStream(FILE_PATH);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(bookList);

            oos.close();
            fos.close();
        } catch (IOException e) {
            System.out.println("Error while saving book data.");
        }
    }

    public ArrayList<Book> deserializeData(){
        try {
            FileInputStream fis = new FileInputStream(FILE_PATH);
            ObjectInputStream ois = new ObjectInputStream(fis);

            ArrayList<Book> bookList = (ArrayList<Book>) ois.readObject();

            ois.close();
            fis.close();
            return bookList;

        }catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}
