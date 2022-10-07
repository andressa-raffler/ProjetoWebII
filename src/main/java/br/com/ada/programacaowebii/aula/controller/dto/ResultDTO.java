package br.com.ada.programacaowebii.aula.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResultDTO {
    private Long Id;
    private String name;
    private String status;
    private String species;
    private String type;
    private String gender;
    private OriginDTO origin;
    private LocationDTO location;
    private String image;
    private List<String> episodes;


}
