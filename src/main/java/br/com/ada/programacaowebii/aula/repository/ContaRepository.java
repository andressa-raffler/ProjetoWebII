package br.com.ada.programacaowebii.aula.repository;

import br.com.ada.programacaowebii.aula.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    public Optional<Conta> findContaByNumero(Long numero);

    //@Query("SELECT c FROM Conta c WHERE c.numero = :numero")
    //public Optional<Conta> buscarContaPorNumeroParametroNominal(@Param("numero") Long numero);
}
