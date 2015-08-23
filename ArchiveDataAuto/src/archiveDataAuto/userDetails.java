package archiveDataAuto;

public class userDetails{
    private String user_id = null;
    private String password = null;

    public userDetails(String user_id, String password) {
        this.user_id = user_id;
        this.password = password;

    }

    public String getUser_id() {
        return user_id;
    }

    public String getPassword() {
       return password;
    }

}