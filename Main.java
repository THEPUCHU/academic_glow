import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        server.createContext("/", new StaticFileHandler());
        server.createContext("/api/events", new EventsApiHandler());
        server.createContext("/api/participants", new ParticipantsApiHandler());
        server.createContext("/api/register", new RegisterApiHandler());
        
        server.setExecutor(null);
        server.start();
        System.out.println("Academic Glow Server running on http://localhost:8080");
    }

    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            try {
                byte[] response = Files.readAllBytes(Paths.get("index.html"));
                t.getResponseHeaders().set("Content-Type", "text/html");
                t.sendResponseHeaders(200, response.length);
                OutputStream os = t.getResponseBody();
                os.write(response);
                os.close();
            } catch (Exception e) {
                String error = "404 - index.html not found";
                t.sendResponseHeaders(404, error.length());
                t.getResponseBody().write(error.getBytes());
                t.getResponseBody().close();
            }
        }
    }

    static class EventsApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            DataStore db = DataStore.getInstance();
            String jsonArray = "[" + db.getEvents().stream()
                .map(e -> e.toJson(db.getParticipantCountForEvent(e.getId())))
                .collect(Collectors.joining(",")) + "]";
            
            sendJsonResponse(t, jsonArray);
        }
    }

    static class ParticipantsApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String query = t.getRequestURI().getQuery();
            int eventId = Integer.parseInt(query.split("=")[1]);
            
            DataStore db = DataStore.getInstance();
            String jsonArray = "[" + db.getParticipantsByEvent(eventId).stream()
                .map(Participant::toJson)
                .collect(Collectors.joining(",")) + "]";
            
            sendJsonResponse(t, jsonArray);
        }
    }

    static class RegisterApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            if ("POST".equalsIgnoreCase(t.getRequestMethod())) {
                InputStream is = t.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> params = parseFormData(body);
                
                String name = params.get("name");
                String regNo = params.get("registerNumber");
                String dept = params.get("department");
                String sem = params.get("semester");
                int eventId = Integer.parseInt(params.get("eventId"));

                DataStore db = DataStore.getInstance();
                
                Participant participant = db.getParticipants().stream()
                    .filter(p -> p.getRegisterNumber().equals(regNo))
                    .findFirst()
                    .orElse(null);

                if (participant == null) {
                    participant = new Participant(0, name, regNo, dept, sem);
                    db.addParticipant(participant);
                }

                // Avoid duplicate registration
                boolean alreadyRegistered = db.getRegistrations().stream()
                    .anyMatch(r -> r.getEventId() == eventId && r.getParticipantId() == participant.getId());

                if (!alreadyRegistered) {
                    db.addRegistration(new Registration(eventId, participant.getId()));
                }

                sendJsonResponse(t, "{\"status\":\"success\"}");
            }
        }

        private Map<String, String> parseFormData(String formData) {
            return java.util.Arrays.stream(formData.split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(
                    a -> a[0], 
                    a -> a.length > 1 ? java.net.URLDecoder.decode(a[1], StandardCharsets.UTF_8) : ""
                ));
        }
    }

    private static void sendJsonResponse(HttpExchange t, String json) throws IOException {
        t.getResponseHeaders().set("Content-Type", "application/json");
        t.sendResponseHeaders(200, json.getBytes().length);
        OutputStream os = t.getResponseBody();
        os.write(json.getBytes());
        os.close();
    }
}