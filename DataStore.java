import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class DataStore {
    private static DataStore instance;
    
    private List<Event> events = new CopyOnWriteArrayList<>();
    private List<Participant> participants = new CopyOnWriteArrayList<>();
    private List<Registration> registrations = new CopyOnWriteArrayList<>();

    private DataStore() {
        seedData();
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private void seedData() {
        // Seed Events
        events.add(new Event(1, "Oppana", "Dance", "2026-11-10", "09:00", "Main Stage", "Active"));
        events.add(new Event(2, "Kolkali", "Dance", "2026-11-10", "11:00", "Main Stage", "Active"));
        events.add(new Event(3, "Vattapat", "Dance", "2026-11-10", "14:00", "Auditorium", "Active"));
        events.add(new Event(4, "Group Song Girls", "Music", "2026-11-11", "09:30", "Seminar Hall", "Active"));
        events.add(new Event(5, "Group Song Boys", "Music", "2026-11-11", "11:30", "Seminar Hall", "Active"));
        events.add(new Event(6, "Drama", "Theatre", "2026-11-11", "15:00", "Open Air Theatre", "Active"));

        // Seed Participants
        addParticipant(new Participant(1, "Aysha", "36", "CSE", "S3"));
        addParticipant(new Participant(2, "Amina", "27", "CSE", "S3"));
        addParticipant(new Participant(3, "Safa", "33", "RAI", "S5"));
        addParticipant(new Participant(4, "Naveena", "17", "EEE", "S7"));
        addParticipant(new Participant(5, "Fidha", "12", "RAI", "S3"));
        addParticipant(new Participant(6, "Aardhra", "1", "ECE", "S1"));
        addParticipant(new Participant(7, "Sheez", "2", "MECH", "S3"));
        addParticipant(new Participant(8, "Karthik", "5", "EEE", "S3"));
        addParticipant(new Participant(9, "Shibin", "37", "CSE", "S5"));
        addParticipant(new Participant(10, "Roshan", "10", "ECE", "S7"));
        addParticipant(new Participant(11, "Kenz", "38", "CSE", "S3"));
        addParticipant(new Participant(12, "Musab", "10", "RAI", "S1"));
        addParticipant(new Participant(13, "Nihal", "5", "CIVIL", "S5"));

        // Seed Registrations (Team 1 for Event 1 & 2)
        int[] oppanaIds = {1, 2, 3, 4, 5, 6};
        for (int id : oppanaIds) registrations.add(new Registration(1, id));

        int[] kolkaliIds = {7, 8, 9, 10, 11, 12, 13};
        for (int id : kolkaliIds) registrations.add(new Registration(2, id));
    }

    public List<Event> getEvents() { return events; }
    public List<Participant> getParticipants() { return participants; }
    public List<Registration> getRegistrations() { return registrations; }

    public void addParticipant(Participant p) {
        if (p.getId() == 0) p.setId(participants.size() + 1);
        participants.add(p);
    }

    public void addRegistration(Registration r) {
        registrations.add(r);
    }
    
    public int getParticipantCountForEvent(int eventId) {
        return (int) registrations.stream().filter(r -> r.getEventId() == eventId).count();
    }
    
    public List<Participant> getParticipantsByEvent(int eventId) {
        List<Integer> participantIds = registrations.stream()
                .filter(r -> r.getEventId() == eventId)
                .map(Registration::getParticipantId)
                .collect(Collectors.toList());
                
        return participants.stream()
                .filter(p -> participantIds.contains(p.getId()))
                .collect(Collectors.toList());
    }
}