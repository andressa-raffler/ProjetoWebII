package br.com.ada.programacaowebii.aula.controller;

import br.com.ada.programacaowebii.aula.controller.dto.ClienteDTO;
import br.com.ada.programacaowebii.aula.controller.dto.ContaDTO;
import br.com.ada.programacaowebii.aula.controller.vo.ContaVO;
import br.com.ada.programacaowebii.aula.model.Cliente;
import br.com.ada.programacaowebii.aula.model.Conta;
import br.com.ada.programacaowebii.aula.service.ClienteService;
import br.com.ada.programacaowebii.aula.service.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ContaControlller {

    @Autowired
    private ContaService contaService;

    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Criar conta", tags = "conta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ContaDTO.class))
                    }
            )

    })
    @PostMapping("/conta")
    public ResponseEntity<Void> criarConta(@Valid @RequestHeader(value = "cpf") String cpf, @RequestBody ContaVO contaVO) {
        if (clienteService.buscarClientePorCpf(cpf).isPresent()) {
            Conta conta = new Conta();
            conta.setNumero(contaVO.getNumero());
            conta.setSaldo(contaVO.getSaldo());
            conta.setDataCriacao(contaVO.getDataCriacao());
            conta.setCliente(clienteService.buscarClientePorCpf(cpf).get());
            contaService.criarConta(conta);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Operation(summary = "Atualizar conta", tags = "conta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ContaDTO.class))
                    }
            )

    })
    @PutMapping("/conta")
    public ResponseEntity<ContaDTO> atualizarConta(@PathVariable("id") Long id, @Valid @RequestBody ContaVO contaVO) {
        Conta conta = this.contaService.atualizarContaPorId(id, contaVO);
        if (Objects.nonNull(conta)) {
            ContaDTO contaDTO = new ContaDTO();
            contaDTO.setNumero(conta.getNumero());
            contaDTO.setSaldo(conta.getSaldo());
            contaDTO.setDataCriacao(conta.getDataCriacao());
            contaDTO.setCliente(conta.getCliente());
            return ResponseEntity.ok().body(contaDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Buscar conta pelo numero", tags = "conta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ContaDTO.class))
                    }
            )

    })
    @GetMapping("/conta-por-numero/{numero}")
    public ResponseEntity<ContaDTO> buscarContaPeloNumero(@PathVariable("numero") Long numero) {
        Optional<Conta> optionalConta = this.contaService.buscarContaPorNumero(numero);
        if (optionalConta.isPresent()) {
            Conta conta = optionalConta.get();
            ContaDTO contaDTO = new ContaDTO();
            contaDTO.setNumero(conta.getNumero());
            contaDTO.setSaldo(conta.getSaldo());
            contaDTO.setDataCriacao(conta.getDataCriacao());
            contaDTO.setCliente(conta.getCliente());
            return ResponseEntity.ok(contaDTO);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Remover conta pelo numero", tags = "conta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ContaDTO.class))
                    }
            )

    })
    @DeleteMapping("/conta-por-numero/{numero}")
    public ResponseEntity<String> removerContaPeloNumero(@PathVariable("numero") Long numero) {
        Optional<Conta> optionalConta = this.contaService.buscarContaPorNumero(numero);
        if (optionalConta.isPresent()) {
            Conta conta = optionalConta.get();
            this.contaService.removerContaPorId(conta.getId());
            return ResponseEntity.ok("Conta removido!");
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Listar contas pelo cpf do cliente", tags = "conta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ContaDTO.class))
                    }
            )

    })
    @GetMapping("/contas-por-cpf/{cpf}")
    public ResponseEntity<List<ContaDTO>> listarContasPorCpf(@PathVariable("cpf") String cpf) {
        Optional<Cliente> cliente = clienteService.buscarClientePorCpf(cpf);
        if(cliente.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        List<Conta> contas = this.contaService.listarContasPorCpf(cliente.get());
        if (contas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        List<ContaDTO> contaDTOS = convertendoContaInContaDTO(contas);
        return ResponseEntity.ok(contaDTOS);
    }

    private List<ContaDTO> convertendoContaInContaDTO(List<Conta> contas) {
        List<ContaDTO> contaDTOS = contas.stream()
                .map(conta -> {
                    ContaDTO contaDTO = new ContaDTO();
                    contaDTO.setNumero(conta.getNumero());
                    contaDTO.setSaldo(conta.getSaldo());
                    contaDTO.setDataCriacao(conta.getDataCriacao());
                    contaDTO.setCliente(conta.getCliente());
                    return contaDTO;
                })
                .collect(Collectors.toList());
        return contaDTOS;
    }

}
