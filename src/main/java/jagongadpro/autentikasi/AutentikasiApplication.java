package jagongadpro.autentikasi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.function.ServerResponse;

import org.springframework.web.servlet.function.RouterFunction;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;



@EnableConfigurationProperties
@SpringBootApplication()
@CrossOrigin(origins = "*")
public class AutentikasiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutentikasiApplication.class, args);
	}
	@Bean
	public RouterFunction<ServerResponse> getAllGames() {
		return route("/api/games/get-all").GET("/api/games/get-all", http("http://35.240.130.147/")).build();
	}
	@Bean
	public RouterFunction<ServerResponse> createGames() {
		return route("/api/games/create").POST("/api/games/create", http("http://35.240.130.147/")).build();
	}
	@Bean
	public RouterFunction<ServerResponse> filterGames() {
		return route("/api/games/get").GET("/api/games/get", http("http://35.240.130.147/")).build();
	}
	@Bean
	public RouterFunction<ServerResponse> getGameById() {
		return route("/api/games/{id}").GET("/api/games/{id}", http("http://35.240.130.147/")).build();
	}


}
