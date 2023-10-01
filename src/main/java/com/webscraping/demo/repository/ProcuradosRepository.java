package com.webscraping.demo.repository;
import com.webscraping.demo.model.OrigemDados;
import com.webscraping.demo.model.Procurados;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcuradosRepository extends JpaRepository<Procurados, Long> {
  List<Procurados> findByNomeContainingIgnoreCaseAndOrigemDadosAndCrime(String nome, OrigemDados origemDados, String crime);
  List<Procurados> findByOrigemDadosAndCrimeContainingIgnoreCase(OrigemDados origemDados, String crime);
  List<Procurados> findByNomeContainingIgnoreCaseAndCrimeContainingIgnoreCase(String nome, String crime);
  List<Procurados> findByNomeContainingIgnoreCaseAndOrigemDados(String nome, OrigemDados origemDados);
  List<Procurados> findByNomeContainingIgnoreCase(String nome);
  List<Procurados> findByOrigemDados(OrigemDados origemDados);
  List<Procurados> findByCrime(String crime);
}
