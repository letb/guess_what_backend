package base.dataSets;

import com.google.gson.JsonObject;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user", schema = "public")
public class UserDataSet implements Serializable {
    private static final long serialVersionUID = -8706689714326132798L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "login")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "score")
    private int score;

    public UserDataSet() {
    }

    public UserDataSet(String name, String password, String email) {
        this.setId(-1);
        this.setName(name);
        this.setPassword(password);
        this.setEmail(email);
        this.setScore(0);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public int getScore() {
        return score;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public JsonObject getJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", 1);
        jsonObject.addProperty("login", this.getName());
        jsonObject.addProperty("email", this.getEmail());
        return jsonObject;
    }
}
