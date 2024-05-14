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

	@Bean
	public RouterFunction<ServerResponse> createUlasan() {
		return route("/ulasan/create").GET("/ulasan/create", http("http://34.168.24.170/")).build();
	}

	@Bean
	public RouterFunction<ServerResponse> getUlasanById() {
		return route("/ulasan/{idUlasan}").GET("/ulasan/{idUlasan}", http("http://34.168.24.170/")).build();
	}

	@Bean
	public RouterFunction<ServerResponse> getUlasanUser() {
		return route("/ulasan/user/{idUser}").GET("/ulasan/user/{idUser}", http("http://34.168.24.170/")).build();
	}

	@Bean
	public RouterFunction<ServerResponse> getUlasanGame() {
		return route("/ulasan/game/{idGame}").GET("/ulasan/game/{idGame}", http("http://34.168.24.170/")).build();
	}

	@Bean
	public RouterFunction<ServerResponse> editUlasan() {
		return route("/ulasan/edit/{idUlasan}").GET("/ulasan/edit/{idUlasan}", http("http://34.168.24.170/")).build();
	}

	@Bean
	public RouterFunction<ServerResponse> deleteUlasan() {
		return route("/ulasan/delete/{idUlasan}").GET("/ulasan/delete/{idUlasan}", http("http://34.168.24.170/")).build();
	}

	@Bean
	public RouterFunction<ServerResponse> createTanggapan() {
		return route("/penilaian-produk/create").GET("/penilaian-produk/create", http("http://34.168.24.170/")).build();
	}

	@Bean
	public RouterFunction<ServerResponse> getTanggapan() {
		return route("/penilaian-produk/{idTanggapan}").GET("/penilaian-produk/{idTanggapan}", http("http://34.168.24.170/")).build();
	}

	@Bean
	public RouterFunction<ServerResponse> getAllTanggapanPenjual() {
		return route("/penilaian-produk/user/{idPenjual}").GET("/penilaian-produk/user/{idPenjual}", http("http://34.168.24.170/")).build();
	}

	@Bean
	public RouterFunction<ServerResponse> editTanggapan() {
		return route("/penilaian-produk/edit/{idTanggapan}").GET("/penilaian-produk/edit/{idTanggapan}", http("http://34.168.24.170/")).build();
	}

	@Bean
	public RouterFunction<ServerResponse> deleteTanggapan() {
		return route("/penilaian-produk/delete/{idTanggapan}").GET("/penilaian-produk/delete/{idTanggapan}", http("http://34.168.24.170/")).build();
	}
}
