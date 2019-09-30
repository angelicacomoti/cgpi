package gui;

import controladores.ControladorDeEventos;
import controladores.TipoDesenho;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import utils.AlertaCallback;
import utils.AlertaPersonalizado;

@SuppressWarnings("restriction")
public class TelaPrincipal {

	private Stage palco;
	private VBox menu;

	private HBox desenhoPontoPonto;
	private Button pontos;
	private Button retas;
	private Button circulos;

	private HBox desenhoFigura;
	private Button curvaDragao;
	private Button opcaoGeral;

	private HBox opcoes;
	private Button limpar;
	private Button redesenhar;
	private HBox opcoes2;
	private Button undo;
	private Button redo;

	
	private Canvas canvas;
	private Canvas canvasLittle;
	private ControladorDeEventos controladorDeEventos;
	
	public static int LARGURA_CANVAS = 1220;
	public static int ALTURA_CANVAS = 700;
					

	public TelaPrincipal(Stage palco) {
		this.palco = palco;
		desenharTela();
	}

	public void desenharTela(){
			
		palco.setWidth(LARGURA_CANVAS);
		palco.setHeight(ALTURA_CANVAS);
		palco.setResizable(false);

		//criando Canvas
		canvas = new Canvas(palco.getWidth(), palco.getHeight());

		//criando canvas pequeno
		BorderPane paneLittle = new BorderPane();
		paneLittle.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		canvasLittle = new Canvas(208, 145); // proporçao: divide o maior por 4,8
		paneLittle.setCenter(canvasLittle);

		controladorDeEventos = new ControladorDeEventos(canvas, canvasLittle);

		// Painel para os componentes
        BorderPane pane = new BorderPane();
        
        //Criando Menu
        menu = montarMenuOpcoesButton();
        menu.getChildren().addAll(paneLittle);
		menu.setMaxWidth(230);
		menu.setMinWidth(230);
    	        
    	// atributos do painel
        pane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setCenter(canvas); // posiciona o componente de desenho
        pane.setLeft(menu);
    	atribuirEventosAosComponentesGraficos();
        // cria e insere cena
        Scene scene = new Scene(pane);
        palco.setScene(scene);
        palco.show();
		
	}
	
	// Vincula??o dos componentes do MENU aos eventos declarados no ControladorDeEventos de componentes grasficos.
	private void atribuirEventosAosComponentesGraficos() {
		// menu
		this.retas.setOnAction(e -> {
			controladorDeEventos.getEventoBasicoMenuDesenho(TipoDesenho.RETA);
		});
		this.pontos.setOnAction(e -> {
			controladorDeEventos.getEventoBasicoMenuDesenho(TipoDesenho.PONTO);
		});
		this.circulos.setOnAction(e -> {
			controladorDeEventos.getEventoBasicoMenuDesenho(TipoDesenho.CIRCULO);
		});
		this.curvaDragao.setOnAction(e -> {
			controladorDeEventos.setTipoDesenho(TipoDesenho.CURVA_DO_DRAGAO);
		});
		this.opcaoGeral.setOnAction(e -> {
			controladorDeEventos.setTipoDesenho(TipoDesenho.OPCAO_GERAL);
		});
		this.limpar.setOnAction(e -> {
			AlertaPersonalizado.criarAlertaComCallback("A execucao dessa operacao resulta na perda de todos os dados desenhados.\n "
					+ "Deseja continuar?", new AlertaCallback() {				
						@Override
						public void alertaCallbak() {
							controladorDeEventos.limparCanvas();
						}
					});
		});

		this.redesenhar.setOnAction(e -> {
			controladorDeEventos.redesenharCanvas();
		});
		this.undo.setOnAction(e -> {
			controladorDeEventos.undoLastDesenho();
		});
		this.redo.setOnAction(e -> {
			controladorDeEventos.redoLastDesenho();
		});
		
		// canvas
		canvas.setOnMouseMoved(event -> {
			palco.setTitle("(Posição do Cursor):" + " (" + (int) event.getX() + ", " + (int) event.getY() + ")");
		});
		canvas.setOnMousePressed(event -> {
			controladorDeEventos.onCanvasMousePressed(event);
		});

	}

	private VBox montarMenuOpcoesButton(){
		VBox menu = new VBox();
		menu.getChildren().addAll(new Label("Desenho Ponto a Ponto "), criarPrimeiraLinha());
		menu.getChildren().addAll(new Label("Desenho Figuras"), criarSegundaLinha());
		menu.getChildren().addAll(new Label("Opções "), criarTerceiraLinha(), criarQuartaLinha());
		menu.getChildren().addAll(new Label(""), criarOpcaoCor(), new Label(""), criarOpcaoEspessura());
		menu.setSpacing(10);

		menu.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY)));
		menu.setPadding(new Insets(10, 10, 15, 10));
		return menu;
	}

	private HBox criarPrimeiraLinha(){
		desenhoPontoPonto = new HBox();
		desenhoPontoPonto.setSpacing(10);
		pontos = criarButton("Pontos");
		retas = criarButton("Retas");
		circulos = criarButton("Circulos");
		desenhoPontoPonto.getChildren().addAll(pontos, retas, circulos);
		return desenhoPontoPonto;
	}

	private HBox criarSegundaLinha(){
		desenhoFigura = new HBox();
		desenhoFigura.setSpacing(10);
		curvaDragao = criarButton("Cv. Dragão");
		opcaoGeral = criarButton("Opção Geral");
		desenhoFigura.getChildren().addAll(curvaDragao, opcaoGeral);
		return desenhoFigura;
	}

	private HBox criarTerceiraLinha(){
		opcoes = new HBox();
		opcoes.setSpacing(10);
		limpar = criarButton("Limpar");
		redesenhar = criarButton("Redesenhar");
		opcoes.getChildren().addAll(limpar, redesenhar);
		return opcoes;
	}

	private HBox criarQuartaLinha(){
		opcoes2 = new HBox();
		opcoes2.setSpacing(10);
		undo = criarButton("Undo");
		redo = criarButton("Redo");
		opcoes2.getChildren().addAll(undo, redo);
		return opcoes2;
	}

	private Button criarButton(String text){
		Button btn = new Button(text);
		btn.setMinHeight(20);
		btn.setMinWidth(43);
		btn.setFont(new Font(12));
		return btn;
	}

	@SuppressWarnings("restriction")
	private HBox criarOpcaoCor(){
		HBox hbox = new HBox();
		ColorPicker colorPicker = new ColorPicker(Color.BLACK);
		colorPicker.setOnAction(e -> {
			controladorDeEventos.getDesenhador().setCor(colorPicker.getValue());
		});
		hbox.getChildren().addAll(new Label("Cor: "), colorPicker);
		return hbox;
	}

	@SuppressWarnings("restriction")
	private HBox criarOpcaoEspessura(){
		HBox hbox = new HBox();
		Spinner<Integer> diametroLinhas = new Spinner<Integer>();
		SpinnerValueFactory<Integer> diametros = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 2);
		diametroLinhas.setValueFactory(diametros);
		diametroLinhas.setMaxWidth(80);
		diametroLinhas.valueProperty().addListener(e -> {
			controladorDeEventos.getDesenhador().setDiametro(diametros.getValue());
		});
		hbox.getChildren().addAll(new Label("Espessura: "), diametroLinhas);
		return hbox;
	}
	
}