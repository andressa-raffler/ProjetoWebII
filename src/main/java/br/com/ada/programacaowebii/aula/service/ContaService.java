package br.com.ada.programacaowebii.aula.service;

import br.com.ada.programacaowebii.aula.controller.vo.ContaVO;
import br.com.ada.programacaowebii.aula.model.Cliente;
import br.com.ada.programacaowebii.aula.model.Conta;
import br.com.ada.programacaowebii.aula.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    public Conta criarConta(Conta conta) {
        return this.contaRepository.saveAndFlush(conta);
    }

    public Conta atualizarContaPorId(Long id, ContaVO contaVO) {
        Optional<Conta> optionalConta = this.contaRepository.findById(id);
        if (optionalConta.isPresent()) {
            Conta conta = optionalConta.get();
            Conta contaAtualizado = new Conta();
            contaAtualizado.setId(conta.getId());
            contaAtualizado.setNumero(contaVO.getNumero());
            contaAtualizado.setSaldo(contaVO.getSaldo());
            contaAtualizado.setDataCriacao(contaVO.getDataCriacao());
            contaAtualizado.setCliente(contaVO.getCliente());
            return this.contaRepository.save(contaAtualizado);
        }
        return null;
    }

    public Optional<Conta> buscarContaPorNumero(Long numero) {
        //return this.contaRepository.buscarContaPorNumeroParametroNominal(numero);
        return this.contaRepository.findContaByNumero(numero);
    }

    public void removerContaPorId(Long id) {
        this.contaRepository.deleteById(id);
    }

    public List<Conta> listarContasPorCpf(Cliente cliente) {
        List<Conta> contas = this.contaRepository.findAll().stream()
                .filter(conta -> conta.getCliente().equals(cliente))
                .collect(Collectors.toList());
        return contas;
    }
}
