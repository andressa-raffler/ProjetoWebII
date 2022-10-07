package br.com.ada.programacaowebii.aula.controller;

import br.com.ada.programacaowebii.aula.controller.dto.ClienteDTO;
import br.com.ada.programacaowebii.aula.controller.vo.ClienteVO;
import br.com.ada.programacaowebii.aula.model.Cliente;
import br.com.ada.programacaowebii.aula.service.ClienteService;
import br.com.ada.programacaowebii.aula.service.RickAndMortyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private RickAndMortyService rickAndMortyService;

    @Operation(summary = "Criar cliente", tags = "cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "422", description = "Bad Request",
                    content = {
                        @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClienteDTO.class))
                    }
            )

    })
    @PostMapping("/cliente")
    public ResponseEntity<String> criarCliente(@Valid @RequestBody ClienteVO clienteVO){
        Cliente cliente = new Cliente();
        cliente.setNome(clienteVO.getNome());
        cliente.setCpf(clienteVO.getCpf());
        cliente.setDataNascimento(clienteVO.getDataNascimento());
        cliente.setApelido(rickAndMortyService.getPersonagemPorNomeCliente(clienteVO.getNome()));
        clienteService.criarCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<ClienteDTO> buscarClientePorId(@PathVariable("id") Long id) {
        Optional<Cliente> optionalCliente = this.clienteService.buscarClientePorId(id);
        if (optionalCliente.isPresent()) {
            Cliente cliente = optionalCliente.get();
            ClienteDTO clienteDTO = new ClienteDTO();
            clienteDTO.setNome(cliente.getNome());
            clienteDTO.setCpf(cliente.getCpf());
            clienteDTO.setDataNascimento(cliente.getDataNascimento());
            clienteDTO.setApelido(cliente.getApelido());
            return ResponseEntity.ok(clienteDTO);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/cliente/{id}")
    public ResponseEntity<ClienteDTO> atualizarCliente(@PathVariable("id") Long id, @RequestBody ClienteVO clienteVO) {
        Cliente cliente = this.clienteService.atualizarClientePorId(id, clienteVO);
        if (Objects.nonNull(cliente)) {
            ClienteDTO clienteDTO = new ClienteDTO();
            clienteDTO.setNome(cliente.getNome());
            clienteDTO.setCpf(cliente.getCpf());
            clienteDTO.setDataNascimento(cliente.getDataNascimento());
            clienteDTO.setApelido(rickAndMortyService.getPersonagemPorNomeCliente(cliente.getNome()));
            return ResponseEntity.ok().body(clienteDTO);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/cliente/{id}")
    public ResponseEntity<String> removerClientePorId(@PathVariable("id") Long id) {
        Optional<Cliente> optionalCliente = this.clienteService.buscarClientePorId(id);
        if (optionalCliente.isPresent()) {
            Cliente cliente = optionalCliente.get();
            this.clienteService.removerClientePorId(cliente.getId());
            return ResponseEntity.ok("Cliente removido!");
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/clientes")
    public ResponseEntity<List<ClienteDTO>> listarTodosClientes() {
        List<Cliente> clientes = this.clienteService.listarTodosClientes();
        if (clientes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        List<ClienteDTO> clienteDTOS = convertendoClienteInClienteDTO(clientes);
        return ResponseEntity.ok(clienteDTOS);
    }

    @GetMapping("/clientes-por-nome/{nome}")
    public ResponseEntity<List<ClienteDTO>> listarClientesPorNome(@PathVariable("nome") String nome) {
        List<Cliente> clientes = this.clienteService.listarClientesPorNome(nome);
        if (clientes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        List<ClienteDTO> clienteDTOS = convertendoClienteInClienteDTO(clientes);
        return ResponseEntity.ok(clienteDTOS);
    }

    @GetMapping("/clientes-por-nome/{nome}/ou-data-nascimento/{data-nascimento}")
    public ResponseEntity<List<ClienteDTO>> listarClientesPorNome(@PathVariable("nome") String nome,
                                                                  @PathVariable("data-nascimento") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataNascimento) {
        List<Cliente> clientes = this.clienteService.listarClientesPorNomeOuDataNascimentoOrdenadoPorNome(nome, dataNascimento);
        if (clientes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        List<ClienteDTO> clienteDTOS = convertendoClienteInClienteDTO(clientes);
        return ResponseEntity.ok(clienteDTOS);
    }

    @GetMapping("/clientes-nascidos-em/{data-inicial}/ate/{data-final}")
    public ResponseEntity<List<ClienteDTO>> listarClientesPorPeriodo(
            @PathVariable("data-inicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @PathVariable("data-final") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataFinal) {
        List<Cliente> clientes = this.clienteService.listarClientesPorPeriodo(dataInicial, dataFinal);
        if (clientes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        List<ClienteDTO> clienteDTOS = convertendoClienteInClienteDTO(clientes);
        return ResponseEntity.ok(clienteDTOS);
    }

    @GetMapping("/cliente-por-cpf/{cpf}")
    public ResponseEntity<ClienteDTO> buscarClientePorCpf(@PathVariable("cpf") String cpf) {
        Optional<Cliente> optionalCliente = this.clienteService.buscarClientePorCpf(cpf);
        if (optionalCliente.isPresent()) {
            Cliente cliente = optionalCliente.get();
            ClienteDTO clienteDTO = new ClienteDTO();
            clienteDTO.setNome(cliente.getNome());
            clienteDTO.setCpf(cliente.getCpf());
            clienteDTO.setDataNascimento(cliente.getDataNascimento());
            clienteDTO.setApelido(cliente.getApelido());
            return ResponseEntity.ok(clienteDTO);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PutMapping("/cliente-por-cpf")
    public ResponseEntity<ClienteDTO> atualizarClientePorCpf(@RequestBody ClienteVO clienteVO) {
        Cliente cliente = this.clienteService.atualizarCliente(clienteVO);
        if (Objects.nonNull(cliente)) {
            ClienteDTO clienteDTO = new ClienteDTO();
            clienteDTO.setNome(cliente.getNome());
            clienteDTO.setCpf(cliente.getCpf());
            clienteDTO.setDataNascimento(cliente.getDataNascimento());
            clienteDTO.setApelido(cliente.getApelido());
            return ResponseEntity.ok().body(clienteDTO);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/cliente-por-cpf/{cpf}")
    public ResponseEntity<String> removerClientePorId(@PathVariable("cpf") String cpf) {
        Optional<Cliente> optionalCliente = this.clienteService.buscarClientePorCpf(cpf);
        if (optionalCliente.isPresent()) {
            Cliente cliente = optionalCliente.get();
            this.clienteService.removerClientePorId(cliente.getId());
            return ResponseEntity.ok("Cliente removido!");
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private List<ClienteDTO> convertendoClienteInClienteDTO(List<Cliente> clientes) {
        List<ClienteDTO> clienteDTOS = clientes.stream()
                .map(cliente -> {
                    ClienteDTO clienteDTO = new ClienteDTO();
                    clienteDTO.setNome(cliente.getNome());
                    clienteDTO.setCpf(cliente.getCpf());
                    clienteDTO.setDataNascimento(cliente.getDataNascimento());
                    clienteDTO.setApelido(cliente.getApelido());
                    return clienteDTO;
                })
                //.filter(clienteDTO -> clienteDTO.getDataNascimento().isBefore(LocalDate.now())) //filtrando data de nascimento para antes da data atual
                .collect(Collectors.toList());
        return clienteDTOS;
    }

}
