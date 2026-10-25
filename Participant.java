public class Participant {
    private int id;
    private String name, registerNumber, department, semester;

    public Participant(int id, String name, String registerNumber, String department, String semester) {
        this.id = id; this.name = name; this.registerNumber = registerNumber; 
        this.department = department; this.semester = semester;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getRegisterNumber() { return registerNumber; }

    public String toJson() {
        return String.format("{\"id\":%d,\"name\":\"%s\",\"registerNumber\":\"%s\",\"department\":\"%s\",\"semester\":\"%s\"}",
                id, name, registerNumber, department, semester);
    }
}