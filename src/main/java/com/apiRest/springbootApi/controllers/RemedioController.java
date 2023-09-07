package com.apiRest.springbootApi.controllers;

import com.apiRest.springbootApi.exceptions.ResourceNotFoundException;
import com.apiRest.springbootApi.remedios.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/remedios")
public class RemedioController {

    @Autowired
    private RemedioRepository remedioRepository;

    @GetMapping
    public ResponseEntity<List<DadosListagemRemedio>> listar(){
        List<DadosListagemRemedio> obj = remedioRepository.findAllByAtivoTrue().stream().map(DadosListagemRemedio::new).toList();
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<DadosListagemRemedio>> listarPorId(@PathVariable Long id) {
        try {
            if (remedioRepository.existsById(id)){
                List<DadosListagemRemedio> obj = remedioRepository.findById(id).stream().map(DadosListagemRemedio::new).toList();
                return ResponseEntity.ok().body(obj);
            }else {
                throw new ResourceNotFoundException(id);
            }
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoRemedio> cadastrar(@RequestBody @Valid DadosCadastroRemedio dados, UriComponentsBuilder uriBuilder){
        Remedio remedios = new Remedio(dados);
        remedioRepository.save(remedios);

        URI uri = uriBuilder.path("/remedios/{id}").buildAndExpand(remedios.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoRemedio(remedios));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoRemedio> atualizar(@RequestBody @Valid DadosAtualizarRemedio dados){
        var remedios = remedioRepository.getReferenceById(dados.id());
        remedios.atualizarRemedio(dados);
        return ResponseEntity.ok(new DadosDetalhamentoRemedio(remedios));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        remedioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/inativar/{id}")
    @Transactional
    public ResponseEntity<Void> inativar(@PathVariable Long id){
        try {
            var remedios = remedioRepository.getReferenceById(id);
            remedios.inativar();
            return ResponseEntity.noContent().build();
        }catch (EntityNotFoundException e){
            throw new  ResourceNotFoundException(id);
        }
    }

    @PutMapping("/ativar/{id}")
    @Transactional
    public ResponseEntity<Void> ativar(@PathVariable Long id){
        try {
            var remedios = remedioRepository.getReferenceById(id);
            remedios.ativar();
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException(id);
        }

    }
}
