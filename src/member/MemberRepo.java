package member;

import java.io.*;
import java.util.ArrayList;

public class MemberRepo {

    private static final String FILE_PATH = "member/MemberData.ser";

    public void serialize(ArrayList<Member> members){
        try {
            FileOutputStream fos = new FileOutputStream(FILE_PATH);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(members);
            oos.close();
            fos.close();
        }catch (IOException e){
            System.out.println("Error while saving the data");
        }
    }

    public ArrayList<Member> deserialize(){
        ArrayList<Member> members = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(FILE_PATH);
            ObjectInputStream ois = new ObjectInputStream(fis);
            members = (ArrayList<Member>) ois.readObject();

             ois.close();
             fis.close();
        } catch (IOException  | ClassNotFoundException e){
        }

        return members;
    }
}
