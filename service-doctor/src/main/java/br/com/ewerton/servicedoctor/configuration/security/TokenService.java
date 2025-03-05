package br.com.ewerton.servicedoctor.configuration.security;

import br.com.ewerton.servicedoctor.model.DoctorModel;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Serviço para gerenciar a criação e validação de tokens JWT.
 * Este serviço é responsável por gerar tokens JWT para médicos autenticados
 * e validar os tokens em requisições subsequentes para garantir que o usuário esteja autenticado.
 *
 * @author Ewerton Rodrigues
 * @version 1.0
 */
@Service
public class TokenService {

    /**
     * A chave secreta usada para assinar o token JWT.
     * A chave é configurada no arquivo de propriedades {@code application.properties}.
     */
    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Gera um token JWT para o médico fornecido.
     * O token contém informações como o email do médico e a data de expiração.
     *
     * @param doctorModel O modelo de médico contendo os dados do médico.
     * @return O token JWT gerado.
     * @throws RuntimeException Se ocorrer um erro ao gerar o token.
     */
    public String generateToken(DoctorModel doctorModel) {
        try {
            // Define o algoritmo de assinatura do token usando a chave secreta
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // Cria o token com as informações do médico e a data de expiração
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(doctorModel.getEmail())
                    .withExpiresAt(genExpiresTime()) // Define a expiração do token
                    .sign(algorithm); // Assina o token
            return token;
        } catch (JWTCreationException e) {
            // Lança uma exceção personalizada em caso de erro ao gerar o token
            throw new RuntimeException("Error while generating token", e);
        }
    }

    /**
     * Valida o token JWT fornecido.
     * O método verifica a assinatura do token e a data de expiração.
     *
     * @param token O token JWT a ser validado.
     * @return O email do médico (subject) se o token for válido, ou uma string vazia se o token for inválido.
     */
    public String validateToken(String token) {
        try {
            // Define o algoritmo de validação usando a chave secreta
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // Verifica e valida o token
            return JWT.require(algorithm)
                    .withIssuer("auth-api") // Verifica o emissor do token
                    .build()
                    .verify(token) // Verifica o token
                    .getSubject(); // Retorna o subject (email) do médico
        } catch (JWTVerificationException e) {
            // Retorna uma string vazia se o token for inválido ou expirado
            return "";
        }
    }

    /**
     * Gera o tempo de expiração do token, que é 2 horas a partir do momento atual.
     *
     * @return A data de expiração do token como um objeto {@link Instant}.
     */
    private Instant genExpiresTime() {
        // Define a expiração do token para 2 horas após o momento atual
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
