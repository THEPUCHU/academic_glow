public class Event {
    private int id;
    private String name, category, date, time, venue, status;

    public Event(int id, String name, String category, String date, String time, String venue, String status) {
        this.id = id; this.name = name; this.category = category; this.date = date; 
        this.time = time; this.venue = venue; this.status = status;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    
    public String toJson(int participantCount) {
        return String.format("{\"id\":%d,\"name\":\"%s\",\"category\":\"%s\",\"date\":\"%s\",\"time\":\"%s\",\"venue\":\"%s\",\"status\":\"%s\",\"participantCount\":%d}",
                id, name, category, date, time, venue, status, participantCount);
    }
}