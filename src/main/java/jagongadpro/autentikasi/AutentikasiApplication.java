package jagongadpro.autentikasi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@EnableConfigurationProperties
@SpringBootApplication
public class AutentikasiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutentikasiApplication.class, args);
	}

	@Bean
	public RouterFunction<ServerResponse> getAllGames() {
		return route().GET("/api/games/get-all", http("http://localhost:9090")).build();
	}

	@Bean
	public RouterFunction<ServerResponse> createGames() {
		return route().POST("/api/games/create", http("http://localhost:9090")).build();
	}

	@Bean
	public RouterFunction<ServerResponse> filterGames() {
		return route().GET("/api/games/filter", http("http://34.87.89.120:8080/api/games/filter")).build();
	}

	@Bean
	public RouterFunction<ServerResponse> getGameById() {
		return route().GET("/api/games/{id}", request -> {
			String id = request.pathVariable("id");
			String uri = "http://34.87.89.120:8080/api/games/" + id;
			return http(uri).handle(request);
		}).build();
	}

	@Bean
	public RouterFunction<ServerResponse> searchGames() {
		return route().GET("/api/games/search", http("http://34.87.89.120:8080/api/games/search")).build();
	}
}