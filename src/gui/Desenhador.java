package gui;

import java.util.ArrayList;
import java.util.List;

import calculadores.CalculadorGenerico;
import calculadores.CirculoCalculador;
import calculadores.RetaCalculador;
import controladores.ObjetoDesenhado;
import controladores.TipoDesenho;
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
	private Canvas canvasLittle;
	private List<ObjetoDesenhado> objetosDesenhados = new ArrayList<>();
	private List<ObjetoDesenhado> objetosApagados = new ArrayList<>();

	private List<PontoGr> objetoTempFiguraGeral = new ArrayList<>();
	private List<PontoGr> objetoTempCurvaDragao = new ArrayList<>();


	
	public Desenhador(Canvas canvas, Canvas canvasLittle) {
		this.diametro = 2;
		this.cor = Color.BLACK;
		this.canvas = canvas;
		this.canvasLittle = canvasLittle;
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
	
	public void desenharReta(Ponto pontoInicial, Ponto pontoFinal) {
		Reta reta = new Reta(pontoInicial, pontoFinal, cor);
		desenharPontos(RetaCalculador.obterPontosAlgoritmoMidPoint(reta), cor, TipoDesenho.RETA);
	}
	
	public void desenharCirculo(Ponto pontoInicial, Ponto pontoFinal) {
		Ponto pontoMedio = CalculadorGenerico.obterPontoMedio(pontoInicial, pontoFinal);
		int raio = CirculoCalculador.obterRaio(pontoMedio, pontoFinal);
		Circulo circulo = new Circulo(raio, pontoMedio, cor);
		desenharPontos(CirculoCalculador.obterPontosAlgoritmoMidPoint(circulo), cor, TipoDesenho.CIRCULO);
	}
	

	public void desenharPontos(List<Ponto> pontos, Color cor, TipoDesenho tipoDesenho) {
		armazenarObjetoDesenhados(tipoDesenho, pontos);
		for (Ponto p : pontos) {
			desenharPonto((int) Math.floor(p.getx()), (int) Math.floor(p.gety()), "", cor);
		}
	}
	
	public void desenharPonto(int x, int y) {
		List<Ponto> pontos = new ArrayList<Ponto>();
		pontos.add(new Ponto(x, y));
		armazenarObjetoDesenhados(TipoDesenho.PONTO, pontos);
		this.desenharPonto(x, y, "", cor);

	}

	public void desenharPonto(int x, int y, String nome, Color cor) {
		PontoGr p = new PontoGr(x, y, cor, nome, diametro);
		p.desenharPonto(canvas.getGraphicsContext2D());

		PontoGr pLittle = new PontoGr((int) Math.floor(x/4.8), (int) Math.floor(y/4.8), cor, nome, diametro);
		pLittle.desenharPonto(canvasLittle.getGraphicsContext2D());

	}

	public void desenharObjetosArmaznados(){
		for(ObjetoDesenhado objeto : objetosDesenhados){
			for(PontoGr ponto : objeto.getPontos()) {
				ponto.desenharPonto(canvas.getGraphicsContext2D());

				PontoGr pontoLittle = new PontoGr((int) Math.floor(ponto.getx()/4.8), (int) Math.floor(ponto.gety()/4.8), ponto.getCor(), "", ponto.getDiametro());
				pontoLittle.desenharPonto(canvasLittle.getGraphicsContext2D());
			}
		}
	}

	private void armazenarObjetoDesenhados(TipoDesenho tipo, List<Ponto> pontos){
		ObjetoDesenhado desenho = new ObjetoDesenhado(tipo, transformarPontosEmPontosGR(pontos));
		if(tipo.equals(TipoDesenho.OPCAO_GERAL)){
			this.objetoTempFiguraGeral.addAll(desenho.getPontos());
		}
		else{
			this.objetosDesenhados.add(desenho);
		}
	}

	private List<PontoGr> transformarPontosEmPontosGR(List<Ponto> pontos){
		List<PontoGr> pontosGr = new ArrayList<>();
		for (Ponto p : pontos) {
			pontosGr.add(new PontoGr((int) Math.floor(p.getx()), (int) Math.floor(p.gety()), cor, "", diametro));
		}
		return pontosGr;
	}

	public void limparObjetosArmazenados(){
		this.objetosDesenhados = new ArrayList<>();
	}

	public void popDesenho(){
		int tamanhoLista = objetosDesenhados.size() - 1;
		this.objetosApagados.add(objetosDesenhados.get(tamanhoLista));
		this.objetosDesenhados.remove(tamanhoLista);
	}

	public void pushDesenho(){
		int tamanhoLista = objetosApagados.size() - 1;
		this.objetosDesenhados.add(objetosApagados.get(tamanhoLista));
		this.objetosApagados.remove(tamanhoLista);
	}

	public void terminouFiguraGeral(TipoDesenho tipo){
		ObjetoDesenhado desenho = new ObjetoDesenhado(tipo, this.objetoTempFiguraGeral);
		this.objetosDesenhados.add(desenho);
		this.objetoTempFiguraGeral = new ArrayList<>();
	}

	public void limparCurvaDragaoTemp(){
		this.objetoTempCurvaDragao = new ArrayList<>();
	}

}