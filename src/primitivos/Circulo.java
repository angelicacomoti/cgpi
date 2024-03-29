package primitivos;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import gui.TelaPrincipal;
import javafx.scene.paint.Color;

import java.io.Serializable;

@XmlRootElement(name = "Circulo")
public class Circulo implements Serializable {
	private int raio;
	private Ponto pontoOrigem;
	private Color cor;

	public Circulo(){}
	public Circulo(int raio, Ponto pontoOrigem, Color cor) {
		this.raio = raio;
		this.pontoOrigem = pontoOrigem;
		this.setCor(cor);
	}

	@XmlTransient
	public int getRaio() {
		return raio;
	}

	public void setRaio(int raio) {
		this.raio = raio;
	}


	@XmlElement(name = "Ponto")
	public Ponto getPontoOrigem() {
		return pontoOrigem;
	}

	public void setPontoOrigem(Ponto pontoOrigem) {
		this.pontoOrigem = pontoOrigem;
	}

	@XmlTransient
	public Color getCor() {
		return cor;
	}
	public void setCor(Color cor) {
		this.cor = cor;
	}

}
