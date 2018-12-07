package bean;

public class Login {
    private String id_;
    private String username;
    private String password;
    private String flag;

    public Login() {
        super();
    }

    public Login(String id_, String username, String password, String flag) {
        this.id_ = id_;
        this.username = username;
        this.password = password;
        this.flag = flag;
    }

    public String getId_() {
        return id_;
    }

    public void setId_(String id_) {
        this.id_ = id_;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "我的属性为"+username;
    }
}
