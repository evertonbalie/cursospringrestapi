package curso.api.rest.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import curso.api.rest.model.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long >{

	
	@Query("select u from Usuario u where u.login=?1")
	Usuario findUserByLogin(String login);
	
	
	
}
