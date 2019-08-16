package controladores;

import calculadores.CirculoCalculador;
import calculadores.RetaCalculador;
import gui.Desenhador;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import primitivos.Circulo;
import primitivos.Ponto;
import primitivos.Reta;

public class ControladorDeEventos {

	private Canvas canvas;
	private TipoDesenho tipoDesenho;
	private Ponto pontoAtual;
	private WritableImage backup;
	private boolean fimElastico;
	private Desenhador desenhador;
	private boolean transformacaoEmAndamento;
	
	public ControladorDeEventos(Canvas canvas) {
		super();
		this.canvas = canvas;
		fimElastico = true;
		this.desenhador = new Desenhador(this.canvas);
	}

	public boolean isTransformacaoEmAndamento() {
		return transformacaoEmAndamento;
	}

	public void setTransformacaoEmAndamento(boolean transformacaoEmAndamento) {
		this.transformacaoEmAndamento = transformacaoEmAndamento;
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
				this.desenhador.desenharReta(pontoAtual,pt,fimElastico);
				break;
			case CIRCULO:
				this.desenhador.desenharCirculo(pontoAtual,pt,fimElastico);
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
				case SELECIONA_DESENHO:
					onCanvasMousePressedSelecionarPrimitivo(pontoClicado);
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
	
	public void limparCanvas() {
		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		this.desenhador.inicilizarEstruturasManipulacaoDeDesenhos();
		resetCanvas();
	}

	private void salvarCanvas(){
		// Capturando estado do canvas para desenhar sobre ele
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.WHITE);
		backup = canvas.snapshot(params, backup);
	}
	
	public void resetCanvas(){
		fimElastico = true;
		pontoAtual = null;
		setTransformacaoEmAndamento(false);
	}

	
	
}
