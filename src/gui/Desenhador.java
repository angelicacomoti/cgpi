package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import calculadores.CalculadorGenerico;
import calculadores.CirculoCalculador;
import calculadores.RetaCalculador;
import controladores.TipoDesenho;
import controladores.TipoPrimitivo;
import javafx.geometry.Rectangle2D;
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
	private Map<TipoPrimitivo, List<Object>> objetosDesenhados;  
	private Map<TipoPrimitivo, List<Integer>> indicesSelecionados;
	private Rectangle2D areaRecorte;
	
	public Desenhador(Canvas canvas) {
		this.diametro = 3;
		this.cor = Color.BLACK;
		this.canvas = canvas;
		this.inicilizarEstruturasManipulacaoDeDesenhos();
	}
	
	public Map<TipoPrimitivo, List<Object>> getObjetosDesenhados() {
		return objetosDesenhados;
	}

	public void setObjetosDesenhados(Map<TipoPrimitivo, List<Object>> objetosDesenhados) {
		this.objetosDesenhados = objetosDesenhados;
	}

	public Map<TipoPrimitivo, List<Integer>> getIndicesObjetosSelecionados() {
		return indicesSelecionados;
	}

	public void setIndicesObjetosSelecionados(Map<TipoPrimitivo, List<Integer>> indicesObjetosParaApagar) {
		this.indicesSelecionados = indicesObjetosParaApagar;
	}
	
	public Rectangle2D getAreaRecorte() {
		return areaRecorte;
	}

	public void setAreaRecorte(Rectangle2D areaRecorte) {
		this.areaRecorte = areaRecorte;
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


	public void inicilizarEstruturasManipulacaoDeDesenhos(){
		objetosDesenhados = new HashMap<>();
		indicesSelecionados = new HashMap<>();
		List<TipoPrimitivo> listEnum = Arrays.asList(TipoPrimitivo.values());
		
		for ( TipoPrimitivo tipoPrimitivo: listEnum) {
			objetosDesenhados.put(tipoPrimitivo, new ArrayList<>());
			indicesSelecionados.put(tipoPrimitivo, new ArrayList<>());
		}
	}
	
	public void desenharPrimitivoElastico(Ponto pontoInicial, Ponto pontoFinal, TipoDesenho tipoDesenho, boolean salvar) {
		switch(tipoDesenho) {
			case RETA_ELASTICA:
				desenharReta(pontoInicial,pontoFinal, salvar);
				break;
			case CIRCULO_ELASTICO:
				desenharCirculo(pontoInicial,pontoFinal, salvar);
				break;
			case RETANGULO_ELASTICO:
				//desenharRetangulo(pontoInicial,pontoFinal, salvar);
				break;
			case SELECIONAR_AREA_CLIPPING:
				//desenharAreaSelecao(pontoInicial, pontoFinal, salvar);
		}
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
	
	public void desenharObjetosArmazenados(Color novaCor){
		// apaga canvas
		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		//desenha todos os objetos
		objetosDesenhados.forEach((tipoPrimitivo, objetos) -> {
			for(Object desenho : objetos){
				//verifica se objeto esta selecionado
				boolean selecionado = (this.indicesSelecionados.get(tipoPrimitivo).contains(objetos.indexOf(desenho)))
						? true
						: false;
				Color cor;
				switch (tipoPrimitivo) {
					case RETA:
						Reta reta = (Reta) desenho;
						cor = (selecionado) ? novaCor : reta.getCor() ;
						this.desenharPontos(RetaCalculador.obterPontosAlgoritmoMidPoint(reta), cor);
						break;
					case CIRCULO:
						Circulo circulo = (Circulo) desenho;
						cor = (selecionado) ? novaCor : circulo.getCor() ;
						desenharPontos(CirculoCalculador.obterPontosAlgoritmoMidPoint(circulo), cor );
						break;
				}
			}
		});
	}
	
	public void limparObjetosSelecionados() {
		indicesSelecionados.forEach((tipoPrimitivo, indices)->{
			indices.clear();
		});
		desenharObjetosArmazenados(Color.WHITE);
	}
	
	public void salvarObjetoDesenhado(TipoPrimitivo tipoPrimitivo,Object primitivo) {
		this.objetosDesenhados.get(tipoPrimitivo).add(primitivo);
	}
	

}