package curso.api.rest.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Endereco;
import curso.api.rest.model.Telefone;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.EnderecoRepository;
import curso.api.rest.repository.UsuarioRepository;

//@CrossOrigin
@RestController // serve para dizer que um serviço restfull/arquitetura
@RequestMapping(value = "/usuario")
public class IndexController {

	@Autowired // apring
	// se fosse CDI seria @Inject
	private UsuarioRepository usuarioRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	// Serviço RSETFULL
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> init(@PathVariable(value = "id") long id) {
		//

		Optional<Usuario> usu = usuarioRepository.findById(id);

		return new ResponseEntity(usu.get(), HttpStatus.OK); ///

	}

	@GetMapping(value = "/todos")
	public ResponseEntity<List<Usuario>> findByAllId() {

		List<Usuario> usuAll = (List<Usuario>) usuarioRepository.findAll();

		return new ResponseEntity<List<Usuario>>(usuAll, HttpStatus.OK);
	}

	@PostMapping(value = "/cad_usuario")
	public ResponseEntity<Usuario> salvar(@RequestBody Usuario usuario) {

		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);

		}

		Usuario usuarioS = usuarioRepository.save(usuario);

		return new ResponseEntity<Usuario>(usuarioS, HttpStatus.OK);

	}

	@PostMapping(value = "/cadEnd_Endereco")
	public ResponseEntity<Endereco> cadstrar(@RequestBody Endereco endereco) {

		Endereco cadEnd = enderecoRepository.save(endereco);

		return new ResponseEntity<Endereco>(cadEnd, HttpStatus.OK);
	}

	@PutMapping(value = "/atu_Usuario")
	public ResponseEntity<Usuario> atualizar_Usuario(@RequestBody Usuario usuario) {

		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {

			usuario.getTelefones().get(pos).setUsuario(usuario);

			//
		}

		Usuario usu = usuarioRepository.save(usuario);
		return new ResponseEntity<Usuario>(usu, HttpStatus.OK);

	}

	@PutMapping(value = "/AtualizarEnd")
	public ResponseEntity<Endereco> Ataulizarcadstrar(@RequestBody Endereco endereco) {

		Endereco cadEnd = enderecoRepository.save(endereco);

		return new ResponseEntity<Endereco>(cadEnd, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}/del_enderco")
	public ResponseEntity<Endereco> deleteEnd(@PathVariable(value = "id") long id) {

		enderecoRepository.deleteById(id);

		return new ResponseEntity<Endereco>(HttpStatus.OK);

	}

	@DeleteMapping(value = "/{id}/del_Usuario")
	public ResponseEntity<Usuario> DeleteId(@PathVariable(value = "id") long id) {

		usuarioRepository.deleteById(id);

		return new ResponseEntity<Usuario>(HttpStatus.OK);

	}

}
