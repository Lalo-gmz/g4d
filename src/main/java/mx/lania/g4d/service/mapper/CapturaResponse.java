package mx.lania.g4d.service.mapper;

import java.time.Instant;
import java.util.List;
import mx.lania.g4d.domain.Funcionalidad;
import mx.lania.g4d.domain.Proyecto;

public class CapturaResponse {

    public List<Funcionalidad> funcionalidadList;

    public Instant fecha;

    public Proyecto proyecto;

    public CapturaResponse() {}

    public List<Funcionalidad> getFuncionalidadList() {
        return funcionalidadList;
    }

    public void setFuncionalidadList(List<Funcionalidad> funcionalidadList) {
        this.funcionalidadList = funcionalidadList;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }
}
