package gui;

import controladores.ControladorDeEventos;
import controladores.TipoDesenho;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import primitivos.Ponto;
import utils.AlertaCallback;
import utils.AlertaPersonalizado;

@SuppressWarnings("restriction")
public class TelaPrincipal {

	Ponto pontoSelecionado = null;
	
	private Stage palco;
	private MenuBar menu;
	private Menu desenhoPontoPonto;
	private MenuItem menuPontos;
	private MenuItem menuRetas;
	private MenuItem menuCirculos;
	private MenuItem menuCurvaDragao;
	private MenuItem opcaoGeral;
	

	private Menu opcoes;
	private MenuItem menuLimpar;
	
	
	private Canvas canvas;
	private ControladorDeEventos controladorDeEventos;
	
	public static int LARGURA_CANVAS = 1000;
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
		controladorDeEventos = new ControladorDeEventos(canvas);  
		
		// Painel para os componentes
        BorderPane pane = new BorderPane();
        
        //Criando Menu
        menu 							        = new MenuBar();
        desenhoPontoPonto 				        = new Menu("Desenho Ponto a Ponto");
        menuPontos 						        = new MenuItem("Pontos");
        menuRetas 						        = new MenuItem("Retas");
        menuCirculos 					        = new MenuItem("Circulos");
        menuCurvaDragao 					    = new MenuItem("Curva do Dragão");
        opcaoGeral		 					    = new MenuItem("Opção Geral");

        opcoes			 				        = new Menu("Opções");
        menuLimpar 						        = new MenuItem("Limpar");

    	desenhoPontoPonto.getItems().addAll(menuPontos,menuRetas,menuCirculos,menuCurvaDragao,opcaoGeral);
    	opcoes.getItems().addAll(menuLimpar);
    	
    	menu.getMenus().addAll(desenhoPontoPonto, opcoes);
    	
    	//Criando footer
    	GridPane grid = montarMenuOpcoesGraficas();
    	VBox menus = new VBox();
    	menus.getChildren().addAll(menu,grid);
    	menus.setMinHeight(60);
    	menus.setMaxHeight(60);
    	        
    	// atributos do painel
        pane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setCenter(canvas); // posiciona o componente de desenho
        pane.setTop(menus);
    	atribuirEventosAosComponentesGraficos();
        // cria e insere cena
        Scene scene = new Scene(pane);
        palco.setScene(scene);
        palco.show();
		
	}
	
	// Vincula??o dos componentes do MENU aos eventos declarados no ControladorDeEventos de componentes grasficos.
	private void atribuirEventosAosComponentesGraficos() {
		// menu
		this.menuRetas.setOnAction(e -> {
			controladorDeEventos.getEventoBasicoMenuDesenho(TipoDesenho.RETA);
		});

		this.menuPontos.setOnAction(e -> {
			controladorDeEventos.getEventoBasicoMenuDesenho(TipoDesenho.PONTO);
		});
		this.menuCirculos.setOnAction(e -> {
			controladorDeEventos.getEventoBasicoMenuDesenho(TipoDesenho.CIRCULO);
		});
		this.menuCurvaDragao.setOnAction(e -> {
			controladorDeEventos.setTipoDesenho(TipoDesenho.CURVA_DO_DRAGAO);
		});
		this.opcaoGeral.setOnAction(e -> {
			controladorDeEventos.setTipoDesenho(TipoDesenho.OPCAO_GERAL);
		});
		
		
		
		this.menuLimpar.setOnAction(e -> {
			AlertaPersonalizado.criarAlertaComCallback("A execucao dessa operacao resulta na perda de todos os dados desenhados.\n "
					+ "Deseja continuar?", new AlertaCallback() {				
						@Override
						public void alertaCallbak() {
							controladorDeEventos.limparCanvas();
						}
					});
		});
		
		// canvas
		canvas.setOnMouseMoved(event -> {
			palco.setTitle("(Posição do Cursor):" + " (" + (int) event.getX() + ", " + (int) event.getY() + ")");
		});
		canvas.setOnMousePressed(event -> {
			controladorDeEventos.onCanvasMousePressed(event);
		});

	}
	
	// Menu de cores e diametro das linhas

	@SuppressWarnings("restriction")
	private GridPane montarMenuOpcoesGraficas() {
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(5));
		grid.setHgap(5);
 
		// Color Picker
		ColorPicker colorPicker = new ColorPicker(Color.BLACK);
		colorPicker.setOnAction(e -> {
			controladorDeEventos.getDesenhador().setCor(colorPicker.getValue());
		});

		Spinner<Integer> diametroLinhas = new Spinner<Integer>();
		SpinnerValueFactory<Integer> diametros = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 3);
		diametroLinhas.setValueFactory(diametros);
		diametroLinhas.setMaxWidth(80);
		diametroLinhas.valueProperty().addListener(e -> {
			controladorDeEventos.getDesenhador().setDiametro(diametros.getValue());
		});
		
		
		grid.add(new Label("Cor: "), 0, 0);
		grid.add(colorPicker, 1, 0);
		grid.add(new Label("Espessura: "), 2, 0);
		grid.add(diametroLinhas, 3, 0);

		return grid;
	}
	
}