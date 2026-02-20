package Auth;

import java.io.*;

public class CredRepo {

    Admin adminCredentials = null;

    public void serialize(Admin admin){
        try{
            FileOutputStream fos = new FileOutputStream("Auth/Credentials.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(admin);
            System.out.println("Credentials serialized properly..");
            oos.close();
            fos.close();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public Admin deserialize(){
        try {
            FileInputStream fis = new FileInputStream("Auth/Credentials.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);

            adminCredentials = (Admin) ois.readObject();

        } catch(ClassNotFoundException e){
            System.out.println(e);
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }catch (IOException e){
            System.out.println(e);
        }

        return adminCredentials;
    }
}
