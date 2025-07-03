package curso.api.rest.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import curso.api.rest.model.Endereco;
import curso.api.rest.model.Telefone;
import curso.api.rest.model.Usuario;
import curso.api.rest.model.UsuarioDTO;
import curso.api.rest.repository.EnderecoRepository;
import curso.api.rest.repository.UsuarioRepository;
import javax.validation.Valid;





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
	@Cacheable("cacheusuario")
	public ResponseEntity<UsuarioDTO> init(@PathVariable(value = "id") long id) {
		//public ResponseEntity<Usuario> init(@PathVariable(value = "id") long id) {
		//

		Optional<Usuario> usuario = usuarioRepository.findById(id);

		return new ResponseEntity(new UsuarioDTO(usuario.get()), HttpStatus.OK); ///
		//return new ResponseEntity(usu.get(), HttpStatus.OK);

	}

	@GetMapping(value = "/todos")
	//@Cacheable("cachetodos")
	@CacheEvict(value="cachetodos", allEntries = true)
	@CachePut("cachetodos")
	public ResponseEntity<List<Usuario>> findByAllId() throws InterruptedException {

		Thread.sleep(6000);
		
		List<Usuario> usuAll = (List<Usuario>) usuarioRepository.findAll();

		return new ResponseEntity<List<Usuario>>(usuAll, HttpStatus.OK);
	}
	

	@PostMapping(value = "/cad_usuario")
	public ResponseEntity<Usuario> salvar(@RequestBody @Valid Usuario usuario) throws Exception {

		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);

		}	
		
		
		
		//Consumindo API externa
		URL url = new URL("https://viacep.com.br/ws/"+usuario.getCep()+"/json/");
		URLConnection connection = url.openConnection();
		InputStream is = connection.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
		
		
		String cep="";
		StringBuilder jsonCep= new StringBuilder();
		
		while((cep=br.readLine())!=null) {
			jsonCep.append(cep);
			
			
			
		}
		
		
		System.out.println("cep:"+jsonCep.toString());
		
		Usuario userAux = new Gson().fromJson(jsonCep.toString(), Usuario.class);
		
		usuario.setCep(userAux.getCep());
		usuario.setLogradouro(userAux.getLogradouro());
		usuario.setComplemento(userAux.getComplemento());
		usuario.setBairro(userAux.getBairro());
		usuario.setLocalidade(userAux.getLocalidade());
		usuario.setUf(userAux.getUf());
		
		
		
		String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhaCriptografada);				
		Usuario usuarioS = usuarioRepository.save(usuario);

		return new ResponseEntity<Usuario>(usuarioS, HttpStatus.OK);

	}
	

	@PostMapping(value = "/cadEnd_Endereco")
	public ResponseEntity<Endereco> cadstrar(@RequestBody Endereco endereco) {

		Endereco cadEnd = enderecoRepository.save(endereco);

		return new ResponseEntity<Endereco>(cadEnd, HttpStatus.OK);
	}

	@PutMapping(value = "/atualizar_usuario")
	public ResponseEntity<Usuario> atualizar_Usuario(@RequestBody Usuario usuario) {

		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {

			usuario.getTelefones().get(pos).setUsuario(usuario);
	}
		
		Usuario usuarioTemporario = usuarioRepository.findUserByLogin(usuario.getLogin());
		if(!usuarioTemporario.getSenha().equals(usuario.getSenha())) {
			
			String senhaCryptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhaCryptografada);
			
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
