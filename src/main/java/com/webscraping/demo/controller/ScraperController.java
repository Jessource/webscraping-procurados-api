package com.webscraping.demo.controller;

import com.webscraping.demo.model.OrigemDados;
import com.webscraping.demo.model.Procurados;
import com.webscraping.demo.repository.ProcuradosRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/procurados")
public class ScraperController {
    @Autowired
    ProcuradosRepository procuradosRepository;

    @GetMapping
    public ResponseEntity<List<Procurados>> getAll(
        @RequestParam(name = "nome", required = false) String nome, 
        @RequestParam(name = "origemDados", required = false) OrigemDados origemDados, 
        @RequestParam(name = "crime", required = false) String crime
    ) {
        if (nome != null && origemDados != null && crime != null) {
            return ResponseEntity.ok(procuradosRepository.findByNomeContainingIgnoreCaseAndOrigemDadosAndCrime(nome, origemDados, crime));
        }
        if (nome == null && origemDados != null && crime != null) {
            return ResponseEntity.ok(procuradosRepository.findByOrigemDadosAndCrimeContainingIgnoreCase(origemDados, crime));
        }
        if (nome != null && origemDados == null && crime != null) {
            return ResponseEntity.ok(procuradosRepository.findByNomeContainingIgnoreCaseAndCrimeContainingIgnoreCase(nome, crime));
        }
        if (nome != null && origemDados != null && crime == null) {
            return ResponseEntity.ok(procuradosRepository.findByNomeContainingIgnoreCaseAndOrigemDados(nome, origemDados));
        }
        if (nome != null) {
            return ResponseEntity.ok(procuradosRepository.findByNomeContainingIgnoreCase(nome));
        }
        if (origemDados != null) {
            return ResponseEntity.ok(procuradosRepository.findByOrigemDados(origemDados));
        }
        if (crime != null) {
            return ResponseEntity.ok(procuradosRepository.findByCrime(crime));
        }
        return ResponseEntity.ok(procuradosRepository.findAll());
    }
}
