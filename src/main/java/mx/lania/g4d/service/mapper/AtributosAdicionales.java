package mx.lania.g4d.service.mapper;

import java.util.List;
import mx.lania.g4d.domain.Atributo;

public class AtributosAdicionales {

    private String nombreAtributo;

    private String valor;

    public AtributosAdicionales() {}

    public AtributosAdicionales(String nombreAtributo, String valor) {
        this.nombreAtributo = nombreAtributo;
        this.valor = valor;
    }

    public String getNombreAtributo() {
        return nombreAtributo;
    }

    public void setNombreAtributo(String nombreAtributo) {
        this.nombreAtributo = nombreAtributo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
