package gui;

import java.util.List;

import calculadores.CalculadorGenerico;
import calculadores.CirculoCalculador;
import calculadores.RetaCalculador;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import primitivos.Circulo;
import primitivos.Ponto;
import primitivos.PontoGr;
import primitivos.Reta;


@SuppressWarnings("restriction")
public class Desenhador {

	private Color cor;
	private int diametro;
	private Canvas canvas;
	
	public Desenhador(Canvas canvas) {
		this.diametro = 2;
		this.cor = Color.BLACK;
		this.canvas = canvas;
	}
	
	public Color getCor() {
		return cor;
	}


	public void setCor(Color cor) {
		this.cor = cor;
	}


	public int getDiametro() {
		return diametro;
	}


	public void setDiametro(int diametro) {
		this.diametro = diametro;
	}
	
	public void desenharReta(Ponto pontoInicial, Ponto pontoFinal, boolean salvar) {
		Reta reta = new Reta(pontoInicial, pontoFinal, cor);
		desenharPontos(RetaCalculador.obterPontosAlgoritmoMidPoint(reta), cor);
	}
	
	public void desenharCirculo(Ponto pontoInicial, Ponto pontoFinal, boolean salvar) {
		Ponto pontoMedio = CalculadorGenerico.obterPontoMedio(pontoInicial, pontoFinal);
		int raio = CirculoCalculador.obterRaio(pontoMedio, pontoFinal);
		Circulo circulo = new Circulo(raio, pontoMedio, cor);
		desenharPontos(CirculoCalculador.obterPontosAlgoritmoMidPoint(circulo), cor);
	}
	

	public void desenharPontos(List<Ponto> pontos, Color cor) {
		for (Ponto p : pontos) {
			desenharPonto((int) Math.floor(p.getx()), (int) Math.floor(p.gety()), "", cor);
		}
	}
	
	public void desenharPonto(int x, int y) {
		this.desenharPonto(x, y, "", cor);
	}

	public void desenharPonto(int x, int y, String nome, Color cor) {
		PontoGr p;
		// Cria um ponto
		p = new PontoGr(x, y, cor, nome, diametro);
		// Desenha o ponto
		p.desenharPonto(canvas.getGraphicsContext2D());
	}
}