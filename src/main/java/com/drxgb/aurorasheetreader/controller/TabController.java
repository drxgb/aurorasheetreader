package com.drxgb.aurorasheetreader.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import com.drxgb.aurorasheetreader.App;
import com.drxgb.aurorasheetreader.model.AuroraSheet;
import com.drxgb.aurorasheetreader.service.AuroraSheetManager;
import com.drxgb.aurorasheetreader.service.AuroraSheetRenderer;
import com.drxgb.aurorasheetreader.service.DataManager;
import com.drxgb.aurorasheetreader.service.RawDataViewBuilder;
import com.drxgb.aurorasheetreader.util.ColorMode;
import com.drxgb.aurorasheetreader.util.HexStringConverter;
import com.drxgb.aurorasheetreader.util.HexValueOperator;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * Controlador da área da aba de imagem.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public class TabController implements Initializable
{
	/*
	 * ===========================================================
	 * 			*** CONSTANTES ***
	 * ===========================================================
	 */
	
	private static final Integer HEX_BYTE_LENGTH = 2;
	private static final Integer HEX_16_LENGTH = 4;
	private static final Integer HEX_32_LENGTH = 6;
	
	private static final Integer BYTE_MAX = 0xFF;
	
	
	/*
	 * ===========================================================
	 * 			*** CONTROLES ***
	 * ===========================================================
	 */
	
	// Raiz
	@FXML private Parent panRoot;
	
	// Nome da aba
	@FXML private TextField txtName;
	
	// Tamanho da imagem
	@FXML private Spinner<Integer> spnWidth;
	@FXML private Spinner<Integer> spnHeight;
	
	// Modo de cores
	@FXML private RadioButton rdb32bit;
	@FXML private RadioButton rdb16bit;
	@FXML private Button btnApplyToPreview;
	
	private ToggleGroup tglColorModes;
	
	// Propriedades da cor
	@FXML private Spinner<Integer> spnIndex;
	@FXML private Spinner<Integer> spnRed;
	@FXML private Spinner<Integer> spnGreen;
	@FXML private Spinner<Integer> spnBlue;
	@FXML private TextField txtHexColor;
	@FXML private Rectangle rectColor;
	
	// Propriedades do pixel
	@FXML private Spinner<Integer> spnValue;
	@FXML private Spinner<Integer> spnX;
	@FXML private Spinner<Integer> spnY;
	
	// Prévia	
	@FXML private StackPane panPreview;
	@FXML private HBox panZoomButtons;
	
	private ToggleGroup tglZoomButtons;
	private Canvas cnvPreview;
	
	// Editor de dados brutos
	@FXML private StackPane panRawColor;
	@FXML private StackPane panPixel;
	
	private Label lbl32ColorIndex;
	private Label lbl16ColorIndex;
	private Label lblPixelIndex;
	private Label lblPixelPosition;
	
	
	/*
	 * ===========================================================
	 * 			*** ASSOCIAÇÕES ***
	 * ===========================================================
	 */
	
	private AuroraSheet auroraSheet;
	private AuroraSheetManager manager;
	private AuroraSheetRenderer renderer;
	
	private DataManager color32Manager;
	private DataManager color16Manager;
	private DataManager pixelManager;
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS IMPLEMENTADOS ***
	 * ===========================================================
	 */

	/**
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		auroraSheet = new AuroraSheet();
		manager = new AuroraSheetManager(auroraSheet);
		renderer = new AuroraSheetRenderer(auroraSheet);
		
		setupNameField();
		setupSizeControls();
		setupColorModeControls();
		setupColorPropertiesControls();
		setupPixelPropertiesControls();
		setupPreview();
		setupZoomButtons();
		setupRawDataPanes();
		
		txtName.requestFocus();
	}


	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ***
	 * ===========================================================
	 */	
	
	/**
	 * Redimensionar área da imagem.
	 */
	@FXML
	public void onBtnResizeAction()
	{
		int width;
		int height;

		width = spnWidth.getValue();
		height = spnHeight.getValue();

		manager.resize(width, height);
		pixelManager.syncRawData();
		btnApplyToPreview.setDisable(width == 0 || height == 0);

		updatePixelPositionSpinners();
	}
	
	
	/**
	 * Renderizar imagem de prévia.
	 */
	@FXML
	public void onBtnApplyToPreviewAction()
	{
		ObservableList<Node> nodes;
		double zoom;
		
		renderer.render(getColorModeSelected());
		
		cnvPreview = renderer.getCanvas();
		zoom = getPreviewZoom();
		nodes = panPreview.getChildren();
		
		cnvPreview.setScaleX(zoom);
		cnvPreview.setScaleY(zoom);
		nodes.clear();
		nodes.add(cnvPreview);
	}
	
	
	/**
	 * Aplicar as propriedades de cor à paleta.
	 */
	@FXML
	public void onBtnRefreshAction()
	{
		// TODO Aplicar as propriedades de cor à paleta.
	}
	
	
	/**
	 * Atualizar cor do pixel.
	 */
	@FXML
	public void onBtnSetPixelColorAction()
	{
		// TODO Atualizar cor do pixel.
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PRIVADOS ***
	 * ===========================================================
	 */
	
	/**
	 * Inicializa o campo do nome da aba.
	 */
	@SuppressWarnings("unused")
	private void setupNameField()
	{		
		txtName.textProperty().addListener((obs, oldValue, newValue) ->
		{
			Tab tab;
			String tabName;
			
			tab = getTab();
			tabName = newValue.isEmpty() ? App.UNTITLED : newValue;
			tab.setText(tabName);
		});
	}
	
	
	/**
	 * Inicializa os controles do tamanho da imagem.
	 */
	private void setupSizeControls()
	{
		final int MAX = Integer.MAX_VALUE;
		
		spnWidth.setValueFactory(makeHexSpinnerValueFactory(0, MAX));
		spnHeight.setValueFactory(makeHexSpinnerValueFactory(0, MAX));
		
		spnWidth.getEditor().setTextFormatter(makeHexFormatter(HEX_BYTE_LENGTH));
		spnHeight.getEditor().setTextFormatter(makeHexFormatter(HEX_BYTE_LENGTH));
	}
	
	
	/**
	 * Inicializa os controles do modo de cor.
	 */
	@SuppressWarnings("unused")
	private void setupColorModeControls()
	{
		ObservableList<Toggle> toggles;
		
		tglColorModes = new ToggleGroup();
		toggles = tglColorModes.getToggles();
		
		toggles.add(rdb32bit);
		toggles.add(rdb16bit);
		
		tglColorModes.selectedToggleProperty().addListener((obs, oldValue, newValue) ->
		{
			Integer oldIndex;
			Integer newIndex;
			Node oldNode;
			Node newNode;
			ObservableList<Node> nodes;
			
			oldIndex = (Integer) oldValue.getUserData();
			newIndex = (Integer) newValue.getUserData();
			nodes = panRawColor.getChildren();
			oldNode = nodes.get(oldIndex);
			newNode = nodes.get(newIndex);
			
			oldNode.setVisible(false);
			newNode.setVisible(true);
		});
		
		rdb32bit.setUserData(0);
		rdb16bit.setUserData(1);
		
		rdb32bit.getProperties().put("colorMode", ColorMode.COLOR_32_BIT);
		rdb16bit.getProperties().put("colorMode", ColorMode.COLOR_16_BIT);
	}


	/**
	 * Inicializa os controles da prévia.
	 */
	private void setupPreview()
	{
		InputStream is;
		Image img;
		BackgroundImage bgImg;
		Background bg;
		
		is = App.class.getResourceAsStream("img/bg0.png");
		img = new Image(is);
		bgImg = new BackgroundImage(img, null, null, null, null);
		bg = new Background(bgImg);
		
		panPreview.setBackground(bg);
	}
	
	
	/**
	 * Inicializa os botões de zoom da prévia.
	 */
	private void setupZoomButtons()
	{
		ToggleButton btn;
		ObservableList<Node> buttons;
		double i;
		
		tglZoomButtons = new ToggleGroup();
		buttons = panZoomButtons.getChildren();
		i = 0.0;
		
		for (Node node : buttons)
		{
			btn = (ToggleButton) node;
			
			btn.setUserData(Math.pow(2.0, i));
			btn.setOnAction(ev ->
			{
				Node target;
				Double z;
				
				target = (Node) ev.getTarget();
				z = (Double) target.getUserData();
				
				if (cnvPreview != null)
				{
					cnvPreview.setScaleX(z);
					cnvPreview.setScaleY(z);
				}
			});
			
			tglZoomButtons.getToggles().add(btn);
			++i;
		}
	}
	
	
	/**
	 * Inicializa os controles das propriedades da cor.
	 */
	private void setupColorPropertiesControls()
	{
		final int MIN = 0x00;
		final int MAX = BYTE_MAX;
		
		spnIndex.setValueFactory(makeIntegerSpinnerValueFactory(MIN, MAX));
		spnRed.setValueFactory(makeIntegerSpinnerValueFactory(MIN, MAX));
		spnGreen.setValueFactory(makeIntegerSpinnerValueFactory(MIN, MAX));
		spnBlue.setValueFactory(makeIntegerSpinnerValueFactory(MIN, MAX));
	}
	
	
	/**
	 * Inicializa os controles das propriedades do pixel.
	 */
	private void setupPixelPropertiesControls()
	{
		updatePixelPositionSpinners();
		
		spnValue.setValueFactory(makeHexSpinnerValueFactory(0, BYTE_MAX));
		spnValue.getEditor().setTextFormatter(makeHexFormatter(HEX_BYTE_LENGTH));
	}
	
	
	/**
	 * Inicializa o contêiner dos dados brutos.
	 */
	private void setupRawDataPanes()
	{
		RawDataViewBuilder builder;
		ObservableList<Node> rawColorChildren;
		ObservableList<Node> pixelChildren;
		Parent pan32BitData;
		Parent pan16BitData;
		Parent panPixelData;
		HBox pan32BitFooter;
		HBox pan16BitFooter;
		BorderPane panPixelFooter;
		
		final String COLOR_TITLE = "Raw color data";
		final String PIXEL_TITLE = "Pixel data";
		final String DATA_MANAGER = "dataManager";
		
		try
		{
			rawColorChildren = panRawColor.getChildren();
			pixelChildren = panPixel.getChildren();
			pan32BitFooter = (HBox) makeRawColorDataFooter();
			pan16BitFooter = (HBox) makeRawColorDataFooter();
			panPixelFooter = (BorderPane) makePixelDataFooter();
			builder = new RawDataViewBuilder();
			
			pan32BitData = builder
				.clear()
				.setTitle(COLOR_TITLE)
				.setBytes(auroraSheet.getColorData(ColorMode.COLOR_32_BIT))
				.setBytesPerGroup(4)
				.appendNode(pan32BitFooter)
				.makeResult();
			
			pan16BitData = builder
				.clear()
				.setTitle(COLOR_TITLE)
				.setBytes(auroraSheet.getColorData(ColorMode.COLOR_16_BIT))
				.setBytesPerGroup(2)
				.appendNode(pan16BitFooter)
				.makeResult();
			
			panPixelData = builder
				.clear()
				.setTitle(PIXEL_TITLE)
				.setBytes(auroraSheet.getPixelData())
				.setBytesPerGroup(1)
				.appendNode(panPixelFooter)
				.makeResult();
			
			pan16BitData.setVisible(false);
			
			rawColorChildren.add(pan32BitData);
			rawColorChildren.add(pan16BitData);
			pixelChildren.add(panPixelData);
			
			lbl32ColorIndex = (Label) pan32BitFooter.getChildren().get(1);
			lbl16ColorIndex = (Label) pan16BitFooter.getChildren().get(1);
			lblPixelIndex = (Label) ((HBox) panPixelFooter.getLeft()).getChildren().get(1);
			lblPixelPosition = (Label) ((HBox) panPixelFooter.getRight()).getChildren().get(1);

			color32Manager = (DataManager) pan32BitData.getProperties().get(DATA_MANAGER);
			color16Manager = (DataManager) pan16BitData.getProperties().get(DATA_MANAGER);
			pixelManager = (DataManager) panPixelData.getProperties().get(DATA_MANAGER);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Atualiza os spinners das coordenadas do pixel.
	 */
	private void updatePixelPositionSpinners()
	{
		int width;
		int height;
		
		width = spnWidth.getValue();
		height = spnHeight.getValue();
		
		spnX.setValueFactory(makeIntegerSpinnerValueFactory(0, width > 0 ? (width - 1) : 0));
		spnY.setValueFactory(makeIntegerSpinnerValueFactory(0, height > 0 ? (height - 1) : 0));
	}
	
	
	/**
	 * @return O rodapé da aba de dados brutos da cor.
	 */
	private Parent makeRawColorDataFooter()
	{
		HBox panFooter;
		Label lblTitle;
		Label lblValue;
		ObservableList<Node> nodes;
		
		panFooter = new HBox(4.0);
		lblTitle = new Label("Index:");
		lblValue = new Label("0 (00)");
		nodes = panFooter.getChildren();
		
		nodes.add(lblTitle);
		nodes.add(lblValue);
		
		return panFooter;
	}
	
	
	/**
	 * @return O rodapé da aba de dados dos pixels.
	 */
	private Parent makePixelDataFooter()
	{
		BorderPane panFooter;
		HBox panIndex;
		HBox panPosition;
		Label lblTitle;
		Label lblValue;
		ObservableList<Node> positionNodes;
		
		panFooter = new BorderPane();
		panIndex = (HBox) makeRawColorDataFooter();
		panPosition = new HBox(4.0);
		lblTitle = new Label("Position:");
		lblValue = new Label("(0, 0)");
		positionNodes = panPosition.getChildren();
		
		positionNodes.add(lblTitle);
		positionNodes.add(lblValue);
		panFooter.setLeft(panIndex);
		panFooter.setRight(panPosition);
		
		return panFooter;
	}
	
	
	/**
	 * Método fábrica que instancia a fábrica de valores
	 * inteiros em <code>Spinner</code>.
	 * 
	 * @see javafx.scene.control.Spinner
	 * 
	 * @param min	Valor mínimo
	 * @param max	Valor máximo
	 * @return	A fábrica de valores de <code>Spinner</code>.
	 */
	private SpinnerValueFactory<Integer> makeIntegerSpinnerValueFactory(int min, int max)
	{
		return new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max);
	}
	
	
	/**
	 * Método fábrica que instancia a fábrica de valores
	 * inteiros com representação hexadecimal em <code>Spinner</code>.
	 * 
	 * @see javafx.scene.control.Spinner
	 * 
	 * @param min	Valor mínimo
	 * @param max	Valor máximo
	 * @return	A fábrica de valores de <code>Spinner</code>.
	 */
	private SpinnerValueFactory<Integer> makeHexSpinnerValueFactory(int min, int max)
	{
		SpinnerValueFactory<Integer> factory;
		
		factory = makeIntegerSpinnerValueFactory(min, max);
		factory.setConverter(new HexStringConverter());
		
		return factory;
	}
	
	
	/**
	 * Cria o formatador de texto.
	 * 
	 * @param <T>	Tipo do conteúdo do formatador.
	 * @param limit	O limite de caracteres do texto.
	 * @return	O formatador de texto.
	 */
	private <T> TextFormatter<T> makeHexFormatter(int limit)
	{
		return new TextFormatter<>(new HexValueOperator(limit));
	}
	
	
	/**
	 * Recebe o valor atual do zoom da imagem da prévia.
	 * 
	 * @return O valor do zoom.
	 */
	private Double getPreviewZoom()
	{
		final Node btn = (Node) tglZoomButtons.getSelectedToggle();

		return (Double) btn.getUserData();
	}
	
	
	/**
	 * Recebe o modo de cores selecionado.
	 * 
	 * @return O modo de cores.
	 */
	private ColorMode getColorModeSelected()
	{
		final Node chk = (Node) tglColorModes.getSelectedToggle();
		
		return (ColorMode) chk.getProperties().get("colorMode");
	}
	
	
	/**
	 * @return A aba do contêiner.
	 */
	private Tab getTab()
	{
		return (Tab) panRoot.getProperties().get("tab");
	}
}
