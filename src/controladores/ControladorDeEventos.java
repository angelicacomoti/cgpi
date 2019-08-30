package controladores;

import java.util.List;

import calculadores.CirculoCalculador;
import calculadores.CurvaDoDragaoCalculador;
import calculadores.RetaCalculador;
import gui.Desenhador;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import primitivos.Circulo;
import primitivos.Ponto;
import primitivos.Reta;

public class ControladorDeEventos {

	private int iteracoesCurvaDragao;
	private Canvas canvas;
	private TipoDesenho tipoDesenho;
	private Ponto pontoAtual;
	private boolean fimDesenho;
	private Desenhador desenhador;
	
	public ControladorDeEventos(Canvas canvas) {
		super();
		this.canvas = canvas;
		this.iteracoesCurvaDragao = 0;
		fimDesenho = true;
		this.desenhador = new Desenhador(this.canvas);
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
				this.desenhador.desenharReta(pontoAtual,pt,fimDesenho);
				break;
			case CIRCULO:
				this.desenhador.desenharCirculo(pontoAtual,pt,fimDesenho);
				break;
			default:
				throw new RuntimeException("Erro interno");
			}
			pontoAtual = null;
		}
	}
	
	public void onCanvasMousePressedSelecionarPrimitivo(Ponto pontoClicado){
	
		// Iterando sobre objetos j� desenhados
		this.desenhador.getObjetosDesenhados().forEach((tipoPrimitivo, desenhos)->{
			
			for(Object desenho : desenhos){
				double distancia = 0;
				// calcular distancia do ponto pro objeto
				switch (tipoPrimitivo) {
					case RETA:
						distancia = RetaCalculador.calcularDistanciaPontoReta(pontoClicado, (Reta)desenho);
						break;
					case CIRCULO:
						distancia = CirculoCalculador.calcularDistanciaPontoCirculo(pontoClicado, (Circulo) desenho);
						break;
				}
				//guarda objeto para remo��o posterior
				if (distancia < 7.00){
					//verifica se j� n�o existe na lista de remo��o
					if (!this.desenhador.getIndicesObjetosSelecionados().get(tipoPrimitivo).contains(desenhos.indexOf(desenho))){
						this.desenhador.getIndicesObjetosSelecionados().get(tipoPrimitivo).add(desenhos.indexOf(desenho));
					}
				}
			}
		});
		this.desenhador.desenharObjetosArmazenados(Color.DARKRED);
		resetCanvas();
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
		
		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

		int raio = 100;
		
		//circulo central
		Circulo circuloCentral = new Circulo(raio, pontoMedio, Color.GREEN);
		this.desenhador.desenharPontos(CirculoCalculador.obterPontosAlgoritmoMidPoint(circuloCentral), Color.GREEN);
		
		//circulo leste
		Ponto pontoLeste = new Ponto(pontoMedio.getx() + 100, pontoMedio.gety());
		this.desenhador.desenharPonto((int) Math.floor(pontoLeste.getx()), (int) Math.floor(pontoLeste.gety()), "", Color.BLUE);
	
		Circulo circuloLeste = new Circulo(raio, pontoLeste, Color.GREEN);
		this.desenhador.desenharPontos(CirculoCalculador.obterPontosAlgoritmoMidPoint(circuloLeste), Color.GREEN);
				
		//circulo oeste
		Ponto pontoOeste = new Ponto(pontoMedio.getx() - 100, pontoMedio.gety());
		this.desenhador.desenharPonto((int) Math.floor(pontoOeste.getx()), (int) Math.floor(pontoOeste.gety()), "", Color.BLUE);
		
		Circulo circuloOeste = new Circulo(raio, pontoOeste, Color.GREEN);
		this.desenhador.desenharPontos(CirculoCalculador.obterPontosAlgoritmoMidPoint(circuloOeste), Color.GREEN);
				
		//circulo nordeste
		Ponto pontoNordeste = new Ponto(pontoMedio.getx() + 50, pontoMedio.gety()-raio+12);
		this.desenhador.desenharPonto((int) Math.floor(pontoNordeste.getx()), (int) Math.floor(pontoNordeste.gety()), "", Color.BLUE);
			
		Circulo circuloNordeste = new Circulo(raio, pontoNordeste, Color.GREEN);
		this.desenhador.desenharPontos(CirculoCalculador.obterPontosAlgoritmoMidPoint(circuloNordeste), Color.GREEN);
			
		//circulo noroeste
		Ponto pontoNoroeste = new Ponto(pontoMedio.getx() - 50, pontoMedio.gety()-raio+12);
		this.desenhador.desenharPonto((int) Math.floor(pontoNoroeste.getx()), (int) Math.floor(pontoNoroeste.gety()), "", Color.BLUE);
			
		Circulo circuloNoroeste = new Circulo(raio, pontoNoroeste, Color.GREEN);
		this.desenhador.desenharPontos(CirculoCalculador.obterPontosAlgoritmoMidPoint(circuloNoroeste), Color.GREEN);
					
		//circulo nordeste
		Ponto pontoSuldeste = new Ponto(pontoMedio.getx() + 50, pontoMedio.gety()+raio-12);
		this.desenhador.desenharPonto((int) Math.floor(pontoSuldeste.getx()), (int) Math.floor(pontoSuldeste.gety()), "", Color.BLUE);
			
		Circulo circuloSuldeste = new Circulo(raio, pontoSuldeste, Color.GREEN);
		this.desenhador.desenharPontos(CirculoCalculador.obterPontosAlgoritmoMidPoint(circuloSuldeste), Color.GREEN);
			
		//circulo noroeste
		Ponto pontoSuldoeste = new Ponto(pontoMedio.getx() - 50, pontoMedio.gety()+raio-12);
		this.desenhador.desenharPonto((int) Math.floor(pontoSuldoeste.getx()), (int) Math.floor(pontoSuldoeste.gety()), "", Color.BLUE);
			
		Circulo circuloSuldoeste = new Circulo(raio, pontoSuldoeste, Color.GREEN);
		this.desenhador.desenharPontos(CirculoCalculador.obterPontosAlgoritmoMidPoint(circuloSuldoeste), Color.GREEN);
							
										
				

		Reta retaCentralLeste = new Reta(pontoMedio, pontoLeste, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaCentralLeste), Color.RED);
		
		Reta retaCentralOeste = new Reta(pontoMedio, pontoOeste, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaCentralOeste), Color.RED);
		
		Reta retaCentralNordeste = new Reta(pontoMedio, pontoNordeste, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaCentralNordeste), Color.RED);
		
		Reta retaCentralNoroeste = new Reta(pontoMedio, pontoNoroeste, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaCentralNoroeste), Color.RED);
		
		Reta retaCentralSuldeste = new Reta(pontoMedio, pontoSuldeste, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaCentralSuldeste), Color.RED);
		
		Reta retaCentralSuldoeste = new Reta(pontoMedio, pontoSuldoeste, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaCentralSuldoeste), Color.RED);
		
		
		Reta retaCircunferencia1 = new Reta(pontoNordeste, pontoLeste, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaCircunferencia1), Color.RED);
		
		Reta retaCircunferencia2 = new Reta(pontoNoroeste, pontoNordeste, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaCircunferencia2), Color.RED);
		
		Reta retaCircunferencia3 = new Reta(pontoOeste, pontoNoroeste, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaCircunferencia3), Color.RED);
		
		Reta retaCircunferencia4 = new Reta(pontoSuldoeste, pontoOeste, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaCircunferencia4), Color.RED);
		
		Reta retaCircunferencia5 = new Reta(pontoSuldeste, pontoSuldoeste, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaCircunferencia5), Color.RED);
		
		Reta retaCircunferencia6 = new Reta(pontoLeste, pontoSuldeste, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaCircunferencia6), Color.RED);
		
		

		Ponto pontoSul = new Ponto(pontoMedio.getx(), pontoMedio.gety() + 175);
		this.desenhador.desenharPonto((int) Math.floor(pontoSul.getx()), (int) Math.floor(pontoSul.gety()), "", Color.BLUE);
		
		Ponto pontoNorte = new Ponto(pontoMedio.getx(), pontoMedio.gety() - 175);
		this.desenhador.desenharPonto((int) Math.floor(pontoNorte.getx()), (int) Math.floor(pontoNorte.gety()), "", Color.BLUE);
		
		Reta retaExternoSul = new Reta(pontoMedio, pontoSul, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaExternoSul), Color.RED);
		
		Reta retaExternoNorte = new Reta(pontoMedio, pontoNorte, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaExternoNorte), Color.RED);
		
		
		
		
		Ponto pontoNordesteExterno = new Ponto(pontoMedio.getx() + 150, pontoMedio.gety()-raio+12);
		this.desenhador.desenharPonto((int) Math.floor(pontoNordesteExterno.getx()), (int) Math.floor(pontoNordesteExterno.gety()), "", Color.BLUE);
		
		Ponto pontoSuldesteExterno = new Ponto(pontoMedio.getx() + 150, pontoMedio.gety()+raio-12);
		this.desenhador.desenharPonto((int) Math.floor(pontoSuldesteExterno.getx()), (int) Math.floor(pontoSuldesteExterno.gety()), "", Color.BLUE);
		
		Ponto pontoNoroesteExterno = new Ponto(pontoMedio.getx() - 150, pontoMedio.gety()-raio+12);
		this.desenhador.desenharPonto((int) Math.floor(pontoNoroesteExterno.getx()), (int) Math.floor(pontoNoroesteExterno.gety()), "", Color.BLUE);
		
		Ponto pontoSuldoesteExterno = new Ponto(pontoMedio.getx() - 150, pontoMedio.gety()+raio-12);
		this.desenhador.desenharPonto((int) Math.floor(pontoSuldoesteExterno.getx()), (int) Math.floor(pontoSuldoesteExterno.gety()), "", Color.BLUE);


		Reta retaExterno1 = new Reta(pontoMedio, pontoNordesteExterno, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaExterno1), Color.RED);
		
		Reta retaExterno2 = new Reta(pontoMedio, pontoSuldesteExterno, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaExterno2), Color.RED);

		Reta retaExterno3 = new Reta(pontoMedio, pontoNoroesteExterno, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaExterno3), Color.RED);
		
		Reta retaExterno4 = new Reta(pontoMedio, pontoSuldoesteExterno, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaExterno4), Color.RED);
		
		


		Reta retaCircExterno1 = new Reta(pontoNorte, pontoNordesteExterno, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaCircExterno1), Color.RED);
		
		Reta retaCircExterno2 = new Reta(pontoNoroesteExterno, pontoNorte, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaCircExterno2), Color.RED);

		Reta retaCircExterno3 = new Reta(pontoSuldoesteExterno, pontoNoroesteExterno, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaCircExterno3), Color.RED);
		
		Reta retaCircExterno4 = new Reta(pontoSul, pontoSuldoesteExterno, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaCircExterno4), Color.RED);

		Reta retaCircExterno5 = new Reta(pontoSuldesteExterno, pontoSul, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaCircExterno5), Color.RED);
		
		Reta retaCircExterno6 = new Reta(pontoNordesteExterno, pontoSuldesteExterno, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaCircExterno6), Color.RED);

		
		Reta retaTriangulo11 = new Reta(pontoNorte, pontoSuldesteExterno, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaTriangulo11), Color.RED);

		Reta retaTriangulo12 = new Reta(pontoSuldesteExterno, pontoSuldoesteExterno, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaTriangulo12), Color.RED);
		
		Reta retaTriangulo13 = new Reta(pontoSuldoesteExterno, pontoNorte, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaTriangulo13), Color.RED);
		
		
		
		Reta retaTriangulo21 = new Reta(pontoSul, pontoNordesteExterno, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaTriangulo21), Color.RED);

		Reta retaTriangulo22 = new Reta(pontoNordesteExterno, pontoNoroesteExterno, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaTriangulo22), Color.RED);
		
		Reta retaTriangulo23 = new Reta(pontoNoroesteExterno, pontoSul, Color.RED);
		this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaTriangulo23), Color.RED);
		
		
		
	}
	
	private void desenharCurvaDoDragao() {
		if (iteracoesCurvaDragao <= 17) {
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
		Reta reta = new Reta(new Ponto(150, 400), new Ponto(600, 400), this.desenhador.getCor());
		CurvaDoDragaoCalculador calc = new CurvaDoDragaoCalculador(reta, this.iteracoesCurvaDragao);
		List<Reta> retasCurvaDragao;

		try {
			retasCurvaDragao = calc.getRetasCurva();
			for (Reta retaCalc : retasCurvaDragao) {
				this.desenhador.desenharPontos(RetaCalculador.obterPontos(retaCalc), this.desenhador.getCor());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void limparCanvas() {
		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		this.desenhador.inicilizarEstruturasManipulacaoDeDesenhos();
		resetCanvas();
	}

	private void salvarCanvas(){
		// Capturando estado do canvas para desenhar sobre ele
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.WHITE);
	}
	
	public void resetCanvas(){
		fimDesenho = true;
		pontoAtual = null;
	}

	
	
}
