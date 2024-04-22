package entities;

public class TypeSeance {
    Integer id;
    String type;

    public TypeSeance() {

    }
    public TypeSeance(Integer id, String type) {
        this.id = id;
        this.type = type;
    }
    public TypeSeance(String type) {
        this.type = type;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }




}
