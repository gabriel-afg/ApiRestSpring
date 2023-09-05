package com.apiRest.springbootApi.controllers;

import com.apiRest.springbootApi.remedios.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/remedios")
public class RemedioController {
    @Autowired
    private RemedioRepository remedioRepository;

    @GetMapping
    public ResponseEntity<List<DadosListagemRemedio>> listar(){
        List<DadosListagemRemedio> obj = remedioRepository.findAll().stream().map(DadosListagemRemedio::new).toList();
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<DadosListagemRemedio>> listarPorId(@PathVariable Long id) {
        List<DadosListagemRemedio> obj = remedioRepository.findById(id).stream().map(DadosListagemRemedio::new).toList();
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroRemedio dados){
        remedioRepository.save(new Remedio(dados));
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizarRemedio dados){
        var remedios = remedioRepository.getReferenceById(dados.id());
        remedios.atualizarRemedio(dados);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        remedioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
