import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Server implements Runnable {

	public void run() {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);


			server.createContext("/hello", exchange -> {
				exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
				exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
				exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

				// Handle preflight (OPTIONS) requests
				if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
					exchange.sendResponseHeaders(204, -1); // No content
					return;
				}

				String response = "Hello from Java!";
				exchange.sendResponseHeaders(200, response.length());
				try (OutputStream os = exchange.getResponseBody()) {
					os.write(response.getBytes());
				}
			});
			server.start();
			System.out.println("Server started at http://localhost:8080/hello");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
