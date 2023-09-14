package wam2.finals.filevault;

public class PasswordObj {

    private int ID;
    private String website;
    private String email;
    private String password;


    public PasswordObj(int ID, String website, String email, String password) {
        this.ID = ID;
        this.website = website;
        this.email = email;
        this.password = password;
    }

    public PasswordObj(String website, String email, String password) {
        this.website = website;
        this.email = email;
        this.password = password;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
