package curso.api.rest.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import curso.api.rest.model.Endereco;

@Repository
public interface EnderecoRepository  extends CrudRepository<Endereco, Long> {

}
