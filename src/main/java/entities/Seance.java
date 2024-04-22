package entities;

public class Seance {

    private Integer id;
    private String title;
    private String duree;
    private String lien;
    private String password;
    private Integer user_id;
    private Integer type_id;
    private String image;

    public Seance(Integer id, String title, String duree, String lien, String password, Integer user_id, Integer type_id, String image) {
        this.id = id;
        this.title = title;
        this.duree = duree;
        this.lien = lien;
        this.password = password;
        this.user_id = user_id;
        this.type_id = type_id;
        this.image =image;
    }
    public Seance( String title, String duree, String lien, String password, Integer user_id, Integer type_id, String image) {
        this.title = title;
        this.duree = duree;
        this.lien = lien;
        this.password = password;
        this.user_id = user_id;
        this.type_id = type_id;
        this.image =image;

    }
    public Seance(){
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getType_id() {
        return type_id;
    }

    public void setType_id(Integer type_id) {
        this.type_id = type_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Seance{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", duree='" + duree + '\'' +
                ", lien='" + lien + '\'' +
                ", password='" + password + '\'' +
                ", user_id=" + user_id +
                ", type_id=" + type_id +
                ", image='" + image + '\'' +
                '}';
    }
}
