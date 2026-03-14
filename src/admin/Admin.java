package admin;

public class Admin {
    private String username;
    private String password;
    private final String role = "admin";

    public String getRole() {
        return role;
    }

    public Admin(){
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
