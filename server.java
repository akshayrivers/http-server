import java.io.*;
import java.net.*;

public class server {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);

        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Client connected");

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream();

                // Read the request
                String requestLine = in.readLine();
                if (requestLine == null) {
                    continue;
                }
                
                System.out.println("Request: " + requestLine);

                // Determine the HTTP method
                String[] requestParts = requestLine.split(" ");
                String method = requestParts[0];
                String path = requestParts[1];

                // Handle different HTTP methods
                switch (method) {
                    case "GET":
                        handleGet(out, path);
                        break;
                    case "POST":
                        handlePost(in, out,path);
                        break;
                    case "PUT":
                        handlePut(in, out);
                        break;
                    case "DELETE":
                        handleDelete(out, path);
                        break;
                    default:
                        sendNotAllowedResponse(out);
                        break;
                }
            }
        }
    }
   // Handles GET requests for different routes
   private static void handleGet(OutputStream out, String path) throws IOException {
    if (path.equals("/")) {
        // Root route
        String response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\n" +
                "Welcome to the Home Page!";
        out.write(response.getBytes("UTF-8"));
    } else if (path.equals("/about")) {
        // /about route
        String response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\n" +
                "This is the About Page.";
        out.write(response.getBytes("UTF-8"));
    } else {
        // 404 - Not Found
        sendNotFoundResponse(out);
    }
}

// Handles POST requests for different routes
private static void handlePost(BufferedReader in, OutputStream out, String path) throws IOException {
    if (path.equals("/submit")) {
        // Simulate processing POST data
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            body.append(line).append("\n");
        }

        // Respond to the POST request
        String response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\n" +
                "POST request received at /submit with body:\n" + body;
        out.write(response.getBytes("UTF-8"));
    } else {
        sendNotFoundResponse(out);
    }
}


    private static void handlePut(BufferedReader in, OutputStream out) throws IOException {
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            body.append(line).append("\n");
        }
        String response = "HTTP/1.1 200 OK\r\n\r\n" + "PUT request received with body: \n" + body;
        out.write(response.getBytes("UTF-8"));
    }

    private static void handleDelete(OutputStream out, String path) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n\r\n" + "DELETE request received for path: " + path;
        out.write(response.getBytes("UTF-8"));
    }

        // Send 404 Not Found
        private static void sendNotFoundResponse(OutputStream out) throws IOException {
            String response = "HTTP/1.1 404 Not Found\r\nContent-Type: text/plain\r\n\r\n" +
                    "404 Page Not Found";
            out.write(response.getBytes("UTF-8"));
        }
    
        // Send 405 Method Not Allowed
    private static void sendNotAllowedResponse(OutputStream out) throws IOException {
        String response = "HTTP/1.1 405 Method Not Allowed\r\n\r\n" + "Method not allowed";
        out.write(response.getBytes("UTF-8"));
    }
}
