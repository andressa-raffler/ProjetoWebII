package br.com.ada.programacaowebii.aula.controller.vo;

import br.com.ada.programacaowebii.aula.model.Cliente;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContaVO {

    @NotNull
    @Pattern(regexp="\\d{8}", message = "ATENÇÃO: Deve ser informado 8 dígitos!")
    private Long numero;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataCriacao;
    @DecimalMax(message = "Valores com 2 casas decimais apenas", value = "2")
    @NotBlank
    private BigDecimal saldo;
    private Cliente cliente;

}
