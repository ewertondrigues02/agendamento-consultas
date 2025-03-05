package br.com.ewerton.servicedoctor.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuração de segurança da aplicação.
 * Esta classe configura as regras de segurança, incluindo autenticação e autorização,
 * além de configurar o filtro de segurança personalizado.
 * A autenticação é feita por JWT e a criação de sessões é desativada para garantir a
 * statelessness da aplicação.
 *
 * @author Ewerton Rodrigues
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    /**
     * Filtro de segurança personalizado para a aplicação.
     * Este filtro é adicionado à cadeia de filtros do Spring Security.
     */
    @Autowired
    SecurityFilter securityFilter;

    /**
     * Configura o filtro de segurança para a aplicação.
     * Define as regras de segurança para os endpoints, desabilita o CSRF,
     * e especifica que as requisições devem ser stateless (sem sessões).
     * Além disso, configura a autorização para as rotas específicas e aplica o filtro
     * de segurança personalizado antes do filtro de autenticação padrão.
     *
     * @param httpSecurity Objeto utilizado para configurar as regras de segurança HTTP.
     * @return Configuração do filtro de segurança.
     * @throws Exception Se ocorrer um erro na configuração do filtro de segurança.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf -> csrf.disable()) // Desabilita a proteção CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Define a política de sessão stateless
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v3/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**").permitAll() // Permite acesso público aos recursos do Swagger
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll() // Permite login sem autenticação
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll() // Permite registro de novos usuários sem autenticação
                        .requestMatchers(HttpMethod.POST, "/doctors").hasRole("ADMIN") // Restringe acesso ao endpoint de criação de médicos apenas para administradores
                        .anyRequest().authenticated()) // Exige autenticação para todas as outras requisições
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // Adiciona o filtro de segurança personalizado
                .build();
    }

    /**
     * Configura o AuthenticationManager para a autenticação de usuários.
     * O AuthenticationManager é usado para realizar a autenticação das requisições.
     *
     * @param authenticationConfiguration Configuração de autenticação do Spring Security.
     * @return O AuthenticationManager configurado.
     * @throws Exception Se ocorrer um erro durante a configuração do AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configura o PasswordEncoder para codificar as senhas dos usuários.
     * O BCryptPasswordEncoder é utilizado para garantir a segurança das senhas.
     *
     * @return Um objeto PasswordEncoder que usa o algoritmo BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
