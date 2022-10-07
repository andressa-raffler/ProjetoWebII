package br.com.ada.programacaowebii.aula.controller.dto;

import br.com.ada.programacaowebii.aula.model.Cliente;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContaDTO {

    private Long numero;
    private LocalDateTime dataCriacao;
    private BigDecimal saldo;
    private Cliente cliente;
}
