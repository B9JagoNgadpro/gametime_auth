package jagongadpro.autentikasi.config;

import jagongadpro.autentikasi.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class  JwtAuthenticationFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;



    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
      void testDoFilterInternal_ValidToken_AuthenticationSet() throws Exception {
        String token = "valid_token";
        String userEmail = "user@example.com";
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(userDetailsService, times(1)).loadUserByUsername(userEmail);
        verify(jwtService, times(1)).isTokenValid(token, userDetails);
        verify(filterChain, times(1)).doFilter(request, response);
        verify(request, times(1)).getHeader("Authorization");
        }

    @Test
      void testDoFilterInternal_UserNameFailed() throws Exception {
        String token = "invalid_token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(jwtService, never()).isTokenValid(anyString(), any(UserDetails.class));
        verify(filterChain, times(1)).doFilter(request, response);
        verify(request, times(1)).getHeader("Authorization");

    }
    @Test
      void testDoFilterInternal_AuthHeaderNull() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
    }
    @Test
      void testDoFilterInternal_AuthHeaderNotBearer() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Starts");
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
      void testDoFilterInternal_InvalidToken() throws Exception {
        String token = "valid_token";
        String userEmail = "user@example.com";
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(false);


        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(userDetailsService, times(1)).loadUserByUsername(userEmail);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(request, times(1)).getHeader("Authorization");

    }

    @Test
      void testDoFilterInternal_NotAutenticated() throws Exception {
        String token = "valid_token";
        String userEmail = "user@example.com";


        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(userEmail);
        Authentication authentication = Mockito.mock(Authentication.class);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(userDetailsService, never()).loadUserByUsername(userEmail);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(request, times(1)).getHeader("Authorization");

    }
    @Test
      void headerAuthorizationNotFounf() throws Exception {
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);


    }
}
