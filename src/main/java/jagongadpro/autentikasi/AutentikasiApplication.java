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

	@Value("${app.cart}")
	String cart;

	@Value("${app.filter}")
	String filter;

	@Value("${app.ulasan}")
	String ulasan;


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
	@Bean
	public RouterFunction<ServerResponse> getCart() {
		return route("/api/cart/view/{email}").GET("/api/cart/view/{email}", http(cart)).build();
	}

	@Bean
	public RouterFunction<ServerResponse> addToCart() {
		return route("/api/cart/add").GET("/api/cart/add", http(cart)).build();
	}

	@Bean
	public RouterFunction<ServerResponse> removeFromCart() {
		return route("/api/cart/remove").GET("/api/cart/remove", http(cart)).build();
	}


	@Bean
	public RouterFunction<ServerResponse> updateItemCart() {
		return route("/api/cart/update").GET("/api/cart/update", http(cart)).build();
	}

	@Bean
	public RouterFunction<ServerResponse> clearCart() {
		return route("/api/cart/clear/{email}").GET("/api/cart/clear/{email}", http(cart)).build();
	}



	@Bean
	public RouterFunction<ServerResponse> createUlasan() {
		return route("/ulasan/create").GET("/ulasan/create", http(ulasan)).build();
	}

	@Bean
	public RouterFunction<ServerResponse> getUlasanById() {
		return route("/ulasan/{idUlasan}").GET("/ulasan/{idUlasan}", http(ulasan)).build();
	}

	@Bean
	public RouterFunction<ServerResponse> getUlasanUser() {
		return route("/ulasan/user/{idUser}").GET("/ulasan/user/{idUser}", http(ulasan)).build();
	}

	@Bean
	public RouterFunction<ServerResponse> getUlasanGame() {
		return route("/ulasan/game/{idGame}").GET("/ulasan/game/{idGame}", http(ulasan)).build();
	}

	@Bean
	public RouterFunction<ServerResponse> editUlasan() {
		return route("/ulasan/edit/{idUlasan}").GET("/ulasan/edit/{idUlasan}", http(ulasan)).build();
	}

	@Bean
	public RouterFunction<ServerResponse> deleteUlasan() {
		return route("/ulasan/delete/{idUlasan}").GET("/ulasan/delete/{idUlasan}", http(ulasan)).build();
	}

	@Bean
	public RouterFunction<ServerResponse> createTanggapan() {
		return route("/penilaian-produk/create").GET("/penilaian-produk/create", http(ulasan)).build();
	}

	@Bean
	public RouterFunction<ServerResponse> getTanggapan() {
		return route("/penilaian-produk/{idTanggapan}").GET("/penilaian-produk/{idTanggapan}", http(ulasan)).build();
	}

	@Bean
	public RouterFunction<ServerResponse> getAllTanggapanPenjual() {
		return route("/penilaian-produk/user/{idPenjual}").GET("/penilaian-produk/user/{idPenjual}", http(ulasan)).build();
	}

	@Bean
	public RouterFunction<ServerResponse> editTanggapan() {
		return route("/penilaian-produk/edit/{idTanggapan}").GET("/penilaian-produk/edit/{idTanggapan}", http(ulasan)).build();
	}

	@Bean
	public RouterFunction<ServerResponse> deleteTanggapan() {
		return route("/penilaian-produk/delete/{idTanggapan}").GET("/penilaian-produk/delete/{idTanggapan}", http(ulasan)).build();
	}
	@Bean
	public RouterFunction<ServerResponse> filterGamesWahyu() {
		return route("/api/games/filter").GET("/api/games/filter", http(filter)).build();
	}
	@Bean
	public RouterFunction<ServerResponse> getGameByIdWahyu() {
		return route("/api/games/{id}").GET("/api/games/{id}", http(filter)).build();
	}
	@Bean
	public RouterFunction<ServerResponse> searchGames() {
		return route().GET("/api/games/search", http(filter)).build();
	}


}
