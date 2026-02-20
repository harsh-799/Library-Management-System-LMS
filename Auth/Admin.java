package Auth;

import java.io.Serializable;

public class Admin implements Serializable {
    private String username;
    private String password;
    private String role;

    public Admin(){
        this.role = "admin";
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }
}
