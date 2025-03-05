package br.com.ewerton.servicepatient.controller.security;

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
 * Configuração de segurança para a aplicação.
 * <p>
 * Esta classe define as configurações de segurança da aplicação, incluindo a definição das permissões de acesso para diferentes endpoints,
 * a configuração do filtro de segurança personalizado, a gestão de autenticação e a política de sessões.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private SecurityFilter securityFilter;

    /**
     * Define o filtro de segurança e configura as permissões de acesso para os endpoints da aplicação.
     * <p>
     * A configuração desabilita o CSRF, define que a aplicação usará a autenticação sem estado (stateless),
     * e define regras de autorização para os diferentes tipos de requisições HTTP.
     *
     * @param httpSecurity Configuração do HttpSecurity para definir as regras de segurança.
     * @return O SecurityFilterChain configurado.
     * @throws Exception Se ocorrer algum erro na configuração de segurança.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf -> csrf.disable())  // Desabilita CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Define a política de sessão como sem estado
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/patients/auth/login").permitAll()  // Permite acesso ao endpoint de login
                        .requestMatchers(HttpMethod.POST, "/patients/auth/register").permitAll()  // Permite acesso ao endpoint de registro
                        .requestMatchers(HttpMethod.POST, "/patients").permitAll()  // Permite acesso ao endpoint de criação de pacientes
                        .requestMatchers(HttpMethod.GET, "/patients").permitAll()  // Permite acesso ao endpoint de listagem de pacientes
                        .requestMatchers(HttpMethod.GET, "/patients/{id}").permitAll()  // Permite acesso ao endpoint de detalhes do paciente
                        .requestMatchers(HttpMethod.DELETE, "/patients/{id}").permitAll()  // Permite acesso ao endpoint de exclusão de paciente
                        .requestMatchers(HttpMethod.POST, "/patients/schedules").permitAll()  // Permite acesso ao endpoint de agendamento de paciente
                        .anyRequest().authenticated())  // Exige autenticação para todas as demais requisições
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)  // Adiciona o filtro de segurança personalizado antes do filtro de autenticação padrão
                .build();
    }

    /**
     * Define o AuthenticationManager responsável por autenticar os usuários.
     *
     * @param authenticationConfiguration Configuração de autenticação para obter o AuthenticationManager.
     * @return O AuthenticationManager configurado.
     * @throws Exception Se ocorrer algum erro ao obter o AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Define o encoder de senha usando o algoritmo BCrypt, para garantir a criptografia segura das senhas dos usuários.
     *
     * @return O PasswordEncoder configurado com BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
