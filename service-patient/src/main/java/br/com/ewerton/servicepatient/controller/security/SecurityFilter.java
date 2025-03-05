package br.com.ewerton.servicepatient.controller.security;

import br.com.ewerton.servicepatient.repository.PatientRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de segurança personalizado para interceptação de requisições HTTP.
 * <p>
 * Este filtro é responsável por interceptar todas as requisições HTTP e validar o token JWT presente no cabeçalho "Authorization".
 * Caso o token seja válido, ele autentica o usuário no contexto de segurança da aplicação.
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PatientRepository patientRepository;

    /**
     * Intercepta a requisição HTTP e valida o token de autenticação.
     * <p>
     * Se o token for válido, a autenticação do usuário é realizada no contexto de segurança da aplicação.
     *
     * @param request     A requisição HTTP.
     * @param response    A resposta HTTP.
     * @param filterChain A cadeia de filtros para a requisição.
     * @throws ServletException Se ocorrer um erro durante a execução do filtro.
     * @throws IOException      Se ocorrer um erro de entrada/saída.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoveryToken(request);  // Recupera o token do cabeçalho "Authorization"
        if (token != null) {
            // Valida o token e obtém o email do paciente
            var email = tokenService.validateToken(token);
            UserDetails patient = patientRepository.findByEmail(email);  // Busca o paciente pelo email
            if (patient != null) {
                // Cria uma autenticação com as informações do paciente e define no contexto de segurança
                var authentication = new UsernamePasswordAuthenticationToken(patient, null, patient.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        // Continua o processamento da requisição
        filterChain.doFilter(request, response);
    }

    /**
     * Recupera o token JWT do cabeçalho "Authorization" da requisição.
     * <p>
     * O token é extraído do cabeçalho com o prefixo "Bearer ".
     *
     * @param request A requisição HTTP.
     * @return O token JWT ou null se o cabeçalho "Authorization" não estiver presente.
     */
    private String recoveryToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;  // Se o cabeçalho não estiver presente, retorna null
        return authHeader.replace("Bearer ", "");  // Remove o prefixo "Bearer " e retorna o token
    }
}
