package controladores;

import primitivos.PontoGr;

import java.util.List;

public class ObjetoDesenhado {

    private TipoDesenho tipo;
    private List<PontoGr> pontos;

    public ObjetoDesenhado(TipoDesenho tipo, List<PontoGr> pontos) {
        this.tipo = tipo;
        this.pontos = pontos;
    }

    public TipoDesenho getTipo() {
        return tipo;
    }

    public void setTipo(TipoDesenho tipo) {
        this.tipo = tipo;
    }

    public List<PontoGr> getPontos() {
        return pontos;
    }

    public void setPontos(List<PontoGr> pontos) {
        this.pontos = pontos;
    }
}
