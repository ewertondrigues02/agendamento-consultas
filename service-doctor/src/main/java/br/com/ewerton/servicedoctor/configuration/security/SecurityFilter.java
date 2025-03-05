package br.com.ewerton.servicedoctor.configuration.security;

import br.com.ewerton.servicedoctor.repository.DoctorRepository;
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
 * Filtro de segurança personalizado para a aplicação.
 * Este filtro é responsável por recuperar o token JWT da requisição,
 * validar o token, obter os detalhes do usuário (médico) a partir do repositório
 * e configurar a autenticação no contexto de segurança do Spring.
 * O filtro é aplicado antes da execução da requisição para garantir que as requisições
 * autenticadas sejam processadas corretamente.
 *
 * @author Ewerton Rodrigues
 * @version 1.0
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {

    /**
     * Serviço responsável por gerenciar a validação do token JWT.
     */
    @Autowired
    private TokenService tokenService;

    /**
     * Repositório para acessar dados dos médicos na base de dados.
     */
    @Autowired
    private DoctorRepository doctorRepository;

    /**
     * Método que executa a lógica do filtro de segurança para cada requisição.
     * Recupera o token JWT da requisição, valida o token, busca o usuário associado ao token
     * no repositório de médicos e configura a autenticação no contexto de segurança do Spring.
     *
     * @param request     A requisição HTTP.
     * @param response    A resposta HTTP.
     * @param filterChain A cadeia de filtros que será executada após este filtro.
     * @throws ServletException Se ocorrer um erro durante o processamento da requisição.
     * @throws IOException      Se ocorrer um erro de entrada/saída durante o processamento da requisição.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Recupera o token JWT da requisição
        var token = this.recoveryToken(request);
        if (token != null) {
            // Valida o token JWT e obtém o email do usuário associado ao token
            var email = tokenService.validateToken(token);
            // Busca o médico associado ao email no repositório
            UserDetails doctor = doctorRepository.findByEmail(email);
            if (doctor != null) {
                // Cria um objeto de autenticação com os detalhes do médico e as permissões
                var authentication = new UsernamePasswordAuthenticationToken(doctor, null, doctor.getAuthorities());
                // Configura a autenticação no contexto de segurança do Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        // Continua a execução da cadeia de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Recupera o token JWT do cabeçalho Authorization da requisição.
     * O token é extraído do cabeçalho como "Bearer <token>".
     *
     * @param request A requisição HTTP.
     * @return O token JWT, ou null se o cabeçalho Authorization não estiver presente.
     */
    private String recoveryToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
