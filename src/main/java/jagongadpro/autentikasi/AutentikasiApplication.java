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

	String cart;
	String filter;


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
		return route("/api/games/get").GET("/api/games/get", http("http://localhost:9090")).build();
	}
	@Bean
	public RouterFunction<ServerResponse> getGameById() {
		return route("/api/games/{id}").GET("/api/games/{id}", http("http://localhost:9090")).build();
	}

	@Bean
	public RouterFunction<ServerResponse> clearCart() {
		return route("/api/cart/clear/{email}").GET("/api/cart/clear/{email}", http("http://35.213.132.17/api/cart/clear/{email}")).build();
	}


	@Bean
	public RouterFunction<ServerResponse> searchGames() {
		return route().GET("/api/games/search", http("http://34.87.89.120:8080/api/games/search")).build();
	}

	@Bean
	public RouterFunction<ServerResponse> filterGamesWahyu() {
		return route("/api/games/filter").GET("/api/games/filter", http("http://34.87.89.120:8080/api/games/filter")).build();
	}
	@Bean
	public RouterFunction<ServerResponse> getGameByIdWahyu() {
		return route("/api/games/{id}").GET("/api/games/{id}", http("http://34.87.89.120:8080/api/games/{id}")).build();
	}
}
