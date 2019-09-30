package controladores;

import java.util.ArrayList;
import java.util.List;

import calculadores.CirculoCalculador;
import calculadores.CurvaDoDragaoCalculador;
import calculadores.RetaCalculador;
import gui.Desenhador;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import primitivos.Circulo;
import primitivos.Ponto;
import primitivos.Reta;

public class ControladorDeEventos {

	private int iteracoesCurvaDragao;
	private Canvas canvas;
	private Canvas canvasLittle;
	private TipoDesenho tipoDesenho;
	private Ponto pontoAtual;
	private boolean fimDesenho;
	private Desenhador desenhador;
	private boolean limpou = false;
	
	public ControladorDeEventos(Canvas canvas, Canvas canvasLittle) {
		super();
		this.canvas = canvas;
		this.canvasLittle = canvasLittle;
		this.iteracoesCurvaDragao = 0;
		fimDesenho = true;
		this.desenhador = new Desenhador(this.canvas, this.canvasLittle);
	}
	
	public Desenhador getDesenhador() {
		return desenhador;
	}

	public void setDesenhador(Desenhador desenhador) {
		this.desenhador = desenhador;
	}

	public void setTipoDesenho(TipoDesenho tipoDesenho) {
		this.tipoDesenho = tipoDesenho;
		resetCanvas();
	}
	
	public void onCanvasMousePressed(MouseEvent event) {

		if(this.limpou){
			this.limpou = false;
			this.desenhador.limparObjetosArmazenados();
		}

		Ponto pontoClicado = new Ponto(event.getX(), event.getY());
		if (tipoDesenho != null){
			onCanvasMousePressedDesenho(event, pontoClicado);
		}
	}
	
	private void onMousePressedPrimitivosBasicos(Ponto pt) {

		if (pontoAtual == null) {
			pontoAtual = pt;
		} else {
			switch (tipoDesenho) {
			case RETA:
				this.desenhador.desenharReta(pontoAtual,pt);
				break;
			case CIRCULO:
				this.desenhador.desenharCirculo(pontoAtual,pt);
				break;
			default:
				throw new RuntimeException("Erro interno");
			}
			pontoAtual = null;
		}
	}
	
	private void onCanvasMousePressedDesenho(MouseEvent event, Ponto pontoClicado){
		if (event.getButton() == MouseButton.PRIMARY ) {			
			//Definir qual desenho ser� feito
			switch (tipoDesenho) {
				case OPCAO_GERAL:
					desenharOpcaoGeral(pontoClicado);
					break;
				case CURVA_DO_DRAGAO:
					desenharCurvaDoDragao();
					break;
				case PONTO:
					this.desenhador.desenharPonto((int) Math.floor(event.getX()), (int) Math.floor(event.getY()));
					break;
				case RETA:
				case CIRCULO:
					onMousePressedPrimitivosBasicos(pontoClicado);
					break;
			}
		}
	}
	
		
	public void getEventoBasicoMenuDesenho(TipoDesenho desenho) {
		tipoDesenho = desenho;
		resetCanvas();
	}
	
	private void desenharOpcaoGeral(Ponto pontoMedio) {

		int raio = 100;

		List<Circulo> circulosCircunferencia = new ArrayList<>();
		Circulo circuloCentral = new Circulo(raio, pontoMedio, Color.GREEN);
		circulosCircunferencia.add(circuloCentral);

		List<Ponto> pontos = determinarPontos(pontoMedio, raio);
		desenharPontosDesenhoGeral(pontos);

		circulosCircunferencia.addAll(determinarCirculos(pontos, raio));
		desenharCirculosDesenhoGeral(circulosCircunferencia);

		List<Reta> retas = determinarRetasCirculoCentral(pontoMedio, pontos);
		desenharRetasDesenhoGeral(retas);

		List<Ponto> pontosExtremos = determinarPontosExtremos(pontoMedio, raio);
		desenharPontosDesenhoGeral(pontosExtremos);

		List<Reta> retasExtremas = determinarRetasExtremas(pontoMedio, pontosExtremos);
		desenharRetasDesenhoGeral(retasExtremas);

		List<Reta> retasExtremas2 = determinarRetasExtremas2(pontosExtremos);
		desenharRetasDesenhoGeral(retasExtremas2);

		this.desenhador.setCor(Color.BLACK);
		this.desenhador.terminouFiguraGeral(TipoDesenho.OPCAO_GERAL);
	}

	private List<Ponto> determinarPontos(Ponto pontoMedio, int raio){
		List<Ponto> pontos = new ArrayList<>();
		pontos.add(new Ponto(pontoMedio.getx() + 100, pontoMedio.gety())); //leste
		pontos.add(new Ponto(pontoMedio.getx() + 50, pontoMedio.gety()-raio+12)); //nordeste
		pontos.add(new Ponto(pontoMedio.getx() - 50, pontoMedio.gety()-raio+12)); //noroeste
		pontos.add(new Ponto(pontoMedio.getx() - 100, pontoMedio.gety())); //oeste
		pontos.add(new Ponto(pontoMedio.getx() - 50, pontoMedio.gety()+raio-12)); //sudoeste
		pontos.add(new Ponto(pontoMedio.getx() + 50, pontoMedio.gety()+raio-12)); //sudeste
		return pontos;
	}

	private void desenharPontosDesenhoGeral(List<Ponto> pontos){
		for(Ponto ponto : pontos){
			this.desenhador.setCor(Color.BLUE);
			this.desenhador.desenharPonto((int) Math.floor(ponto.getx()), (int) Math.floor(ponto.gety()), "", desenhador.getCor());
		}
	}

	private List<Circulo> determinarCirculos(List<Ponto> pontos, int raio){
		List<Circulo> circulos = new ArrayList<>();
		for(Ponto ponto : pontos){
			circulos.add(new Circulo(raio, ponto, Color.GREEN));
		}
		return circulos;
	}

	private void desenharCirculosDesenhoGeral(List<Circulo> circulos){
		for(Circulo circulo : circulos){
			this.desenhador.setCor(Color.GREEN);
			this.desenhador.desenharPontos(CirculoCalculador.obterPontosAlgoritmoMidPoint(circulo), desenhador.getCor(), TipoDesenho.OPCAO_GERAL);
		}
	}

	private List<Reta> determinarRetasCirculoCentral(Ponto pontoMedio, List<Ponto> pontos){
		List<Reta> retas = new ArrayList<>();
		for(Ponto ponto : pontos){
			retas.add(new Reta(pontoMedio, ponto, Color.RED));
		}
		return retas;
	}

	private void desenharRetasDesenhoGeral(List<Reta> retas){
		for(Reta reta : retas){
			this.desenhador.setCor(Color.RED);
			this.desenhador.desenharPontos(RetaCalculador.obterPontos(reta), desenhador.getCor(), TipoDesenho.OPCAO_GERAL);
		}
	}

	private List<Ponto> determinarPontosExtremos(Ponto pontoMedio, int raio){
		List<Ponto> pontos = new ArrayList<>();
		pontos.add(new Ponto(pontoMedio.getx(), pontoMedio.gety() - 175)); //norte
		pontos.add(new Ponto(pontoMedio.getx() - 150, pontoMedio.gety()-raio+12)); //noroeste
		pontos.add(new Ponto(pontoMedio.getx() - 150, pontoMedio.gety()+raio-12)); //suldoeste
		pontos.add(new Ponto(pontoMedio.getx(), pontoMedio.gety() + 175)); //sul
		pontos.add(new Ponto(pontoMedio.getx() + 150, pontoMedio.gety()+raio-12)); //suldeste
		pontos.add(new Ponto(pontoMedio.getx() + 150, pontoMedio.gety()-raio+12)); //nordeste
		return pontos;
	}

	private List<Reta> determinarRetasExtremas(Ponto pontoMedio, List<Ponto> pontos){
		List<Reta> retas = new ArrayList<>();
		for(Ponto ponto : pontos){
			retas.add(new Reta(pontoMedio, ponto, Color.RED));
		}
		return retas;
	}

	private List<Reta> determinarRetasExtremas2(List<Ponto> pontos){
		List<Reta> retas = new ArrayList<>();

		retas.add(new Reta(pontos.get(0), pontos.get(5), Color.RED));
		retas.add(new Reta(pontos.get(1), pontos.get(0), Color.RED));
		retas.add(new Reta(pontos.get(2), pontos.get(1), Color.RED));
		retas.add(new Reta(pontos.get(3), pontos.get(2), Color.RED));
		retas.add(new Reta(pontos.get(4), pontos.get(3), Color.RED));
		retas.add(new Reta(pontos.get(5), pontos.get(4), Color.RED));

		retas.add(new Reta(pontos.get(0), pontos.get(4), Color.RED));
		retas.add(new Reta(pontos.get(4), pontos.get(2), Color.RED));
		retas.add(new Reta(pontos.get(2), pontos.get(0), Color.RED));

		retas.add(new Reta(pontos.get(3), pontos.get(5), Color.RED));
		retas.add(new Reta(pontos.get(5), pontos.get(1), Color.RED));
		retas.add(new Reta(pontos.get(1), pontos.get(3), Color.RED));

		return retas;
	}

	private void desenharCurvaDoDragao() {
		if (iteracoesCurvaDragao <= 14) {
			preencherCanvasCurvaDoDragao();
			this.iteracoesCurvaDragao += 1;
		} else {
			Alert alerta = new Alert(AlertType.WARNING, "A aplicaçãoo atingiu o máximo de iterações possíveis.",
					ButtonType.FINISH);
			alerta.show();
		}
	}
	
	private void preencherCanvasCurvaDoDragao() {

		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		canvasLittle.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

		this.desenhador.limparCurvaDragaoTemp();

		Reta reta = new Reta(new Ponto(150, 400), new Ponto(600, 400), this.desenhador.getCor());
		CurvaDoDragaoCalculador calc = new CurvaDoDragaoCalculador(reta, this.iteracoesCurvaDragao);
		List<Reta> retasCurvaDragao;

		try {
			retasCurvaDragao = calc.getRetasCurva();
			for (Reta retaCalc : retasCurvaDragao) {
				this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaCalc), this.desenhador.getCor(), TipoDesenho.CURVA_DO_DRAGAO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void limparCanvas() {
		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		canvasLittle.getGraphicsContext2D().clearRect(0, 0, canvasLittle.getWidth(), canvasLittle.getHeight());
		limpou = true;
		resetCanvas();
	}

	public void redesenharCanvas() {
		this.desenhador.desenharObjetosArmaznados();
		this.limpou = false;
	}

	public void resetCanvas(){
		fimDesenho = true;
		pontoAtual = null;
	}

	public void undoLastDesenho(){
		this.desenhador.popDesenho();

		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		canvasLittle.getGraphicsContext2D().clearRect(0, 0, canvasLittle.getWidth(), canvasLittle.getHeight());

		this.desenhador.desenharObjetosArmaznados();
	}

	public void redoLastDesenho(){
		this.desenhador.pushDesenho();

		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		canvasLittle.getGraphicsContext2D().clearRect(0, 0, canvasLittle.getWidth(), canvasLittle.getHeight());

		this.desenhador.desenharObjetosArmaznados();
	}
	
	
}
