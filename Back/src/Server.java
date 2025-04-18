import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.json.*;

public class Server implements Runnable {
	private final Connection con;

	Server(Connection con) {
		this.con = con;
	}

	public void run() {
		if (con == null) {
			System.out.println("Connection is null");
		}

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

			server.createContext("/tables", exchange -> {
				exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
				exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
				exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

				if ("OPTIONS".equals(exchange.getRequestMethod())) {
					exchange.sendResponseHeaders(204, -1); // No content
					return;
				}

				JsonObjectBuilder builder = Json.createObjectBuilder();
				builder.add("Key", "Tables");

				Statement tables = Main.getTables();

				System.out.println(tables.toString());
				try {
					ResultSet rs = tables.getResultSet();
					if (rs == null) {
						System.out.println("Result Set null.");
					}

					int i = 1;
					while (rs.next()) {
						builder.add(String.valueOf(i++), rs.getString(1));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				JsonObject response = builder.build();
				byte[] bytes = response.toString().getBytes(StandardCharsets.UTF_8);

				exchange.sendResponseHeaders(200, bytes.length);
				try (OutputStream os = exchange.getResponseBody()) {
					os.write(bytes);
				}
				exchange.close();
			});

			server.start();
			System.out.println("Server started at http://localhost:8080/hello");



		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendTables() {

	}
}
