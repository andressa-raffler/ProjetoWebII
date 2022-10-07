package br.com.ada.programacaowebii.aula.controller.vo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RickAndMortyVO {
    private String nome;
    private String personagem;

}
