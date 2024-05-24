package jagongadpro.autentikasi;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import org.springframework.web.servlet.function.ServerResponse;

import org.springframework.web.servlet.function.RouterFunction;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;



@EnableConfigurationProperties
@SpringBootApplication()
public class AutentikasiApplication {

	@Value("${app.game}")
	String game;

	public static void main(String[] args) {
		SpringApplication.run(AutentikasiApplication.class, args);
	}
	@Bean
	public RouterFunction<ServerResponse> getAllGames() {
		return route("/api/games/get-all").GET("/api/games/get-all", http(game)).build();
	}
	@Bean
	public RouterFunction<ServerResponse> createGames() {
		return route("/api/games/create").POST("/api/games/create", http(game)).build();
	}
	@Bean
	public RouterFunction<ServerResponse> filterGames() {
		return route("/api/games/get").GET("/api/games/get", http(game)).build();
	}
	@Bean
	public RouterFunction<ServerResponse> getGameById() {
		return route("/api/games/{id}").GET("/api/games/{id}", http(game)).build();
	}


}
