package carsharing.company;

public class Company {
    private String name;
    private int id;


    public Company(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public Company(String name) {
        this.name = name;
    }
    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }
    @SuppressWarnings("unused")
    public int getId() {
        return id;
    }
    @SuppressWarnings("unused")
    public void setId(int id) {
        this.id = id;
    }
    @SuppressWarnings("unused")
    public void setName(String name) {
        this.name = name;
    }
}
