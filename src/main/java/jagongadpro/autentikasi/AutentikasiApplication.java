package jagongadpro.autentikasi;

import jagongadpro.autentikasi.model.User;
import jagongadpro.autentikasi.model.UserNotFoundException;
import jagongadpro.autentikasi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Collection;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;



@EnableConfigurationProperties
@SpringBootApplication()
public class AutentikasiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutentikasiApplication.class, args);
	}
	@Bean
	public RouterFunction<ServerResponse> getAllGames() {
		return route("/api/games/get-all").GET("/api/games/get-all", http("http://localhost:9090")).build();
	}
	@Bean
	public RouterFunction<ServerResponse> createGames() {
		return route("/api/games/create").POST("/api/games/create", http("http://localhost:9090")).build();
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
	public RouterFunction<ServerResponse> getCart() {
		return route("/api/cart/view/{email}").GET("/api/cart/view/{email}", http("http://35.213.132.17/api/cart/view/{email}")).build();
	}

	@Bean
	public RouterFunction<ServerResponse> addToCart() {
		return route("/api/cart/add").GET("/api/cart/add", http("http://35.213.132.17/api/cart/add")).build();
	}

	@Bean
	public RouterFunction<ServerResponse> removeFromCart() {
		return route("/api/cart/remove").GET("/api/cart/remove", http("http://35.213.132.17/api/cart/remove")).build();
	}


	@Bean
	public RouterFunction<ServerResponse> updateItemCart() {
		return route("/api/cart/update").GET("/api/cart/update", http("http://35.213.132.17/api/cart/update")).build();
	}

	@Bean
	public RouterFunction<ServerResponse> clearCart() {
		return route("/api/cart/clear/{email}").GET("/api/cart/clear/{email}", http("http://35.213.132.17/api/cart/clear/{email}")).build();
	}

}
