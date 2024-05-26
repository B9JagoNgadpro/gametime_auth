package jagongadpro.autentikasi.config;

import jagongadpro.autentikasi.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableConfigurationProperties
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    AuthenticationProvider authenticationProvider;
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOriginPatterns(List.of("*"));
                    configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));
                    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
                    return configuration;
                }).and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/games/create").hasAnyAuthority("ROLE_PENJUAL")
                .requestMatchers("/api/games/get**").permitAll()
                .requestMatchers("/api/transaksi/**").hasAnyAuthority("ROLE_PEMBELI")

                .requestMatchers("/ulasan/create").hasAnyAuthority("ROLE_PEMBELI")
                .requestMatchers("/ulasan/{idUlasan}").hasAnyAuthority("ROLE_PEMBELI")
                .requestMatchers("/ulasan/user/{idUser}").hasAnyAuthority("ROLE_PEMBELI")
                .requestMatchers("/ulasan/game/{idGame}").hasAnyAuthority("ROLE_PEMBELI")
                .requestMatchers("/ulasan/edit/{idUlasan}").hasAnyAuthority("ROLE_PEMBELI")
                .requestMatchers("/ulasan/delete/{idUlasan}").hasAnyAuthority("ROLE_PEMBELI")

                .requestMatchers("/penilaian-produk/create").hasAnyAuthority("ROLE_PENJUAL")
                .requestMatchers("/penilaian-produk/{idTanggapan}").hasAnyAuthority("ROLE_PENJUAL")
                .requestMatchers("/penilaian-produk/user/{idPenjual}").hasAnyAuthority("ROLE_PENJUAL")
                .requestMatchers("/penilaian-produk/edit/{idTanggapan}").hasAnyAuthority("ROLE_PENJUAL")
                .requestMatchers("/penilaian-produk/delete/{idTanggapan}").hasAnyAuthority("ROLE_PENJUAL")
                
                .requestMatchers("/api/cart/add").hasAnyAuthority("ROLE_PEMBELI")
                .requestMatchers("/api/cart/increment").hasAnyAuthority("ROLE_PEMBELI")
                .requestMatchers("/api/cart/decrement").hasAnyAuthority("ROLE_PEMBELI")
                .requestMatchers("/api/cart/remove").hasAnyAuthority("ROLE_PEMBELI")
                .requestMatchers("/api/cart/update").hasAnyAuthority("ROLE_PEMBELI")
                .requestMatchers("/api/cart/view/{email}").hasAnyAuthority("ROLE_PEMBELI")
                .requestMatchers("/api/cart/clear/{email}").hasAnyAuthority("ROLE_PEMBELI")

                .requestMatchers("/api/games/search").permitAll()
                .requestMatchers("/api/games/filter").permitAll()
                .requestMatchers("/api/games/{id}").permitAll()
                .requestMatchers("/user/password/**").permitAll()
                .requestMatchers("/api/user/password/**").permitAll()
                .requestMatchers("/api/user/me").hasAnyAuthority("ROLE_PEMBELI", "ROLE_PENJUAL")
                .requestMatchers("/api/user/reduceBalance").hasAnyAuthority("ROLE_PEMBELI")
                .requestMatchers("/api/user/changeRole").hasAnyAuthority("ROLE_PEMBELI", "ROLE_PENJUAL")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}