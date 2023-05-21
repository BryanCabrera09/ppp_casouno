package com.api.ppp.back.controllers;

import com.api.ppp.back.daos.AuthorityRepository;
import com.api.ppp.back.models.Accion;
import com.api.ppp.back.models.Authority;
import com.api.ppp.back.models.TutorEmpresarial;
import com.api.ppp.back.models.TutorInstituto;
import com.api.ppp.back.services.TutorEmpresarialService;
import com.api.ppp.back.services.TutorInstitutoService;
import com.api.ppp.back.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.api.ppp.back.constant.Validate.isPasswordSecure;

@RestController
@RequestMapping("/tutorInstituto")
@CrossOrigin(origins = "*")
public class TutorInstitutoController {

	@Autowired
	private TutorInstitutoService service;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private UsuarioService usuarioService;

	// To list all records
	@GetMapping("/listar")
	public ResponseEntity<?> listar() {
		return ResponseEntity.ok().body(service.findAll());
	}

	// To find one record, specifically by a unique identifier (PK or ID)
	@GetMapping("/buscar/{id}")
	public ResponseEntity<?> buscarID(@PathVariable("id") Integer id) {
		Optional<TutorInstituto> current = service.findById(id);
		if (current.isPresent()) {
			return ResponseEntity.ok().body(current.get());
		}
		return ResponseEntity.notFound().build();
	}

	// To create a record
	@PostMapping("/crear")
	public ResponseEntity<?> crear(@RequestBody TutorInstituto entity, @RequestParam String rol) {
		try {
			if (!isPasswordSecure(entity.getUsuario().getPassword())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must be secure");
			}
			String hashPwd = passwordEncoder.encode(entity.getUsuario().getPassword());
			entity.getUsuario().setPassword(hashPwd);
			TutorInstituto tutor = service.save(entity);
			if (tutor.getUsuario().getId() > 0) {
				Authority role = new Authority();
				role.setName(rol);
				role.setUsuario(tutor.getUsuario());
				authorityRepository.save(role);
				return ResponseEntity.status(HttpStatus.CREATED).body(tutor);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred while saving the user");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An exception occured due to " + e.getMessage());
		}
	}

	// To find one record and update it, specifically by a unique identifier (PK or
	// ID)
	@PostMapping("/editar/{id}")
	public ResponseEntity<?> editar(@PathVariable("id") Integer id, @RequestBody TutorInstituto entity) {
		if (!isPasswordSecure(entity.getUsuario().getPassword())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must be secure");
		}
		Optional<TutorInstituto> optional = service.findById(id);
		if (optional.isPresent()) {
			String hashPwd = passwordEncoder.encode(entity.getUsuario().getPassword());
			entity.getUsuario().setPassword(hashPwd);
			TutorInstituto current = optional.get();
			current.setUsuario(entity.getUsuario());
			current.setIdDocente(entity.getIdDocente());
			return ResponseEntity.status(HttpStatus.CREATED).body(service.save(current));
		}
		return ResponseEntity.notFound().build();
	}

	// To find one record and delete it, specifically by a unique identifier (PK or
	// ID)
	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<Void> eliminarID(@PathVariable("id") Integer id) {
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/buscarxusuario/{id}")
	public ResponseEntity<?> buscarxUsuario(@PathVariable("id") Integer id) {
		Optional<TutorInstituto> current = Optional
				.ofNullable(service.tutorxUsuario(usuarioService.findById(id).orElse(null)));
		if (current.isPresent()) {
			return ResponseEntity.ok().body(current.get());
		}
		return ResponseEntity.notFound().build();
	}

}
