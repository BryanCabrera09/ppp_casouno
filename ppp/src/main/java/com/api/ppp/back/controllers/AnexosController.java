package com.api.ppp.back.controllers;

import com.api.ppp.back.models.Anexos;
import com.api.ppp.back.models.Practica;
import com.api.ppp.back.services.AnexoService;
import com.api.ppp.back.services.PracticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/anexos")
@CrossOrigin(origins="*")
public class AnexosController {

    @Autowired
    private AnexoService service;

    @Autowired
    private PracticaService practicaService;

    // To list all records
    @GetMapping("/listar")
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok().body(service.findAll());
    }

    // To find one record, specifically by a unique identifier (PK or ID)
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarID(@PathVariable("id") Integer id) {
        Optional<Anexos> current = service.findById(id);
        if(current.isPresent()) {
            return ResponseEntity.ok().body(current.get());
        }
        return ResponseEntity.notFound().build();
    }

    // To create a record
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody Anexos entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    // To find one record and update it, specifically by a unique identifier (PK or ID)
    @PostMapping("/editar/{id}")
    public ResponseEntity<?> editar(@PathVariable("id") Integer id, @RequestBody Anexos entity) {
        Optional<Anexos> optional = service.findById(id);
        if(optional.isPresent()) {
            Anexos current = optional.get();
            current.setUrl(entity.getUrl());
            current.setPractica(entity.getPractica());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(current));
        }
        return ResponseEntity.notFound().build();
    }

    // To find one record and delete it, specifically by a unique identifier (PK or ID)
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarID(@PathVariable("id") Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/guardarpdf")
    public ResponseEntity<String> guardarDocumento(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id")Integer id) {
        try {
            Optional<Anexos> optional = service.findById(id);
            if (optional.isPresent()) {
                Anexos current = optional.get();
                current.setUrl(archivo.getBytes());
                service.save(current);
                return ResponseEntity.ok("El documento se ha guardado correctamente. "+current.getUrl().length);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el documento.");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el documento.");
    }

    @GetMapping("/mostrarpdf/{id}")
    public ResponseEntity<byte[]> obtenerDocumento(@PathVariable("id") Integer id) {
        Optional<Anexos> optional = service.findById(id);
        if (optional.isPresent()) {
            Anexos current = optional.get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            switch (current.getTipo()){
                case 1:
                    headers.setContentDisposition(ContentDisposition.attachment().filename("DesignacionTutorISTA.pdf").build());
                    break;
                case 2:
                    headers.setContentDisposition(ContentDisposition.attachment().filename("DesignacionTutorEmpresarial.pdf").build());
                    break;
                case 3:
                    headers.setContentDisposition(ContentDisposition.attachment().filename("Anexo.pdf").build());
                    break;
                default:
                    headers.setContentDisposition(ContentDisposition.attachment().filename(current.getId()+".pdf").build());
                    break;
            }
            return new ResponseEntity<>(current.getUrl(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/listarxtipo")
    public ResponseEntity<?> listar(@RequestParam("idpractica")Integer id,@RequestParam("tipo")Integer tipo) {
        Practica practica=practicaService.findById(id).orElse(null);
        return ResponseEntity.ok().body(service.anexoTipo(practica,tipo));
    }

}
