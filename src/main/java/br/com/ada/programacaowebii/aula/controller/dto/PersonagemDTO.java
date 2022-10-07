package br.com.ada.programacaowebii.aula.controller.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class PersonagemDTO {

    private InfoDTO info;
    private List<ResultDTO> results;


}
