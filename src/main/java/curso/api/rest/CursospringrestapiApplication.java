package curso.api.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;



@SpringBootApplication // start da aplicação
@EntityScan(basePackages = {"curso.api.rest.model"})//busca as entidade nesse pacote
@ComponentScan(basePackages = {"curso.*"})// cria as injeção de dependência
@EnableJpaRepositories(basePackages = {"curso.api.rest.repository"})//Interage cmbanco de dados 
@EnableTransactionManagement // transações no banco
@EnableWebMvc
@RestController // vai rodar rest e vai retornar um json
@EnableAutoConfiguration // spring vai configurar todo o projeto
@EnableCaching
public class CursospringrestapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CursospringrestapiApplication.class, args);
		System.out.println(new BCryptPasswordEncoder().encode("123"));
		
		
		
		Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); 
		System.out.println("Base 64: "+ Base64.getEncoder().encodeToString(key.getEncoded()));
		
	}
	
	 //@Override
     public void addCorsMappings(CorsRegistry registry) {
         registry.addMapping("/**") // aplica CORS a todos os endpoints
                 .allowedOrigins("http://localhost:3000", "https://seusite.com") // substitua pelas origens permitidas
                 .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // métodos permitidos
                 .allowedHeaders("*") // todos os headers são permitidos
                 .allowCredentials(true); // permite cookies/autenticação
     }

}
