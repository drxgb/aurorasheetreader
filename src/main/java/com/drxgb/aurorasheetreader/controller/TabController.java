package com.drxgb.aurorasheetreader.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import com.drxgb.aurorasheetreader.App;
import com.drxgb.aurorasheetreader.io.ColorTranslator;
import com.drxgb.aurorasheetreader.java.util.HexValueOperator;
import com.drxgb.aurorasheetreader.java.util.NumericValueOperator;
import com.drxgb.aurorasheetreader.javafx.util.HexStringConverter;
import com.drxgb.aurorasheetreader.model.AuroraSheet;
import com.drxgb.aurorasheetreader.service.AuroraSheetManager;
import com.drxgb.aurorasheetreader.service.AuroraSheetRenderer;
import com.drxgb.aurorasheetreader.service.DataManager;
import com.drxgb.aurorasheetreader.service.RawDataViewBuilder;
import com.drxgb.aurorasheetreader.util.ColorMode;
import com.drxgb.aurorasheetreader.util.NumberFormats;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
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
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;

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
	 * 			*** ATRIBUTOS ***
	 * ===========================================================
	 */
	
	private boolean lockChainedChanges = false;
	
	
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
	@FXML private TitledPane panPixelProperties;
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
		App.getScene().setCursor(Cursor.WAIT);
		panRoot.setDisable(true);
		
		Platform.runLater(() ->
		{			
			int width;
			int height;
			boolean isEmpty;
			
			width = spnWidth.getValue();
			height = spnHeight.getValue();
			
			manager.resize(width, height);
			pixelManager.syncRawData();
			
			isEmpty = auroraSheet.isEmpty();
			
			updatePixelPositionSpinners();
			
			btnApplyToPreview.setDisable(isEmpty);
			panPixelProperties.setDisable(isEmpty);			
			panRoot.setDisable(false);

			App.getScene().setCursor(Cursor.DEFAULT);
		});
	}
	
	
	/**
	 * Renderizar imagem de prévia.
	 */
	@FXML
	public void onBtnApplyToPreviewAction()
	{
		App.getScene().setCursor(Cursor.WAIT);
		panRoot.setDisable(true);
		
		Platform.runLater(() ->
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
			panRoot.setDisable(false);
			App.getScene().setCursor(Cursor.DEFAULT);
		});
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
		
		spnWidth.setValueFactory(makeHexSpinnerValueFactory(0, MAX, HEX_32_LENGTH));
		spnHeight.setValueFactory(makeHexSpinnerValueFactory(0, MAX, HEX_32_LENGTH));
		
		spnWidth.getEditor().setTextFormatter(makeHexFormatter(HEX_32_LENGTH));
		spnHeight.getEditor().setTextFormatter(makeHexFormatter(HEX_32_LENGTH));
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
			DataManager dataManager;
			ColorMode mode;
			Integer oldIndex;
			Integer newIndex;
			Node oldNode;
			Node newNode;
			Label lblIndex;
			ObservableList<Node> nodes;
			int position;
			
			oldIndex = (Integer) oldValue.getUserData();
			newIndex = (Integer) newValue.getUserData();
			mode = (ColorMode) newValue.getProperties().get("colorMode");
			dataManager = getColorDataManager(mode);
			nodes = panRawColor.getChildren();
			oldNode = nodes.get(oldIndex);
			newNode = nodes.get(newIndex);
			position = spnIndex.getValue();
			lblIndex = getColorIndexLabel(mode);
			
			oldNode.setVisible(false);
			newNode.setVisible(true);
			
			updateTextFieldHexFromData();
			updateRgbSpinners();
			updateColorRect();
			updateIndexLabel(lblIndex, position);
			dataManager.setSelectPosition(position);
			dataManager.updateScrollPosition();
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
	@SuppressWarnings("unused")
	private void setupColorPropertiesControls()
	{		
		spnIndex.setValueFactory(makeIntegerSpinnerValueFactory(0, BYTE_MAX));
		spnRed.setValueFactory(makeIntegerSpinnerValueFactory(0, BYTE_MAX));
		spnGreen.setValueFactory(makeIntegerSpinnerValueFactory(0, BYTE_MAX));
		spnBlue.setValueFactory(makeIntegerSpinnerValueFactory(0, BYTE_MAX));
		
		spnIndex.getEditor().setTextFormatter(makeNumericFormatter());
		spnIndex
			.valueProperty()
			.addListener((obs, oldValue, newValue) ->
			{
				DataManager dataManager;
				
				dataManager = getColorDataManager();
				
				updateTextFieldHexFromData(newValue);
				updateRgbSpinners();
				updateColorRect();
				updateIndexLabel(getColorIndexLabel(), newValue);
				dataManager.setSelectPosition(newValue);
				dataManager.updateScrollPosition();
			});
		
		spnRed.valueProperty().addListener(makeTextFieldHexChangeListener());
		spnGreen.valueProperty().addListener(makeTextFieldHexChangeListener());
		spnBlue.valueProperty().addListener(makeTextFieldHexChangeListener());
		
		spnRed.getEditor().setTextFormatter(makeNumericFormatter());
		spnGreen.getEditor().setTextFormatter(makeNumericFormatter());
		spnBlue.getEditor().setTextFormatter(makeNumericFormatter());
		
		txtHexColor
			.textProperty()
			.addListener((obs, oldValue, newValue) ->
			{
				if (lockChainedChanges)
				{
					return;
				}
				
				lockChainedChanges = true;
				
				updateRgbSpinners();
				updateRawColorData();
				updateColorRect();
				
				lockChainedChanges = false;
			});
		
		updateTextFieldHexFromData();
		updateRgbSpinners();
	}
	
	
	/**
	 * Inicializa os controles das propriedades do pixel.
	 */
	@SuppressWarnings("unused")
	private void setupPixelPropertiesControls()
	{
		updatePixelPositionSpinners();
		
		spnX.valueProperty().addListener((obs, oldValue, newValue) ->
		{
			updatePixelDataPosition(newValue, spnY.getValue());
			updateTextFieldPixelValue(newValue);
		});
		
		spnY.valueProperty().addListener((obs, oldValue, newValue) ->
		{
			updatePixelDataPosition(spnX.getValue(), newValue);
			updateTextFieldPixelValue(newValue);
		});
		
		spnValue.setValueFactory(makeHexSpinnerValueFactory(0, BYTE_MAX, HEX_BYTE_LENGTH));
		spnValue.getEditor().setTextFormatter(makeHexFormatter(HEX_BYTE_LENGTH));
		spnValue.valueProperty().addListener((obs, oldValue, newValue) ->
		{
			int x;
			int y;
			int width;
			int index;
			
			if (! auroraSheet.isEmpty())
			{
				x = spnX.getValue();
				y = spnY.getValue();
				width = auroraSheet.getWidth();
				index = (y * width) + (x % width);
				
				manager.setPixelFromIndex(index, newValue.byteValue());
				pixelManager.updateSingleData(index);
			}
		});
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
	 * Atualiza os spinners das cores vermelho, verde e azul.
	 */
	private void updateRgbSpinners()
	{
		ColorTranslator translator;
		HexStringConverter converter;
		ColorMode mode;
		int value;		
		int r;
		int g;
		int b;
		
		mode = getColorModeSelected();
		converter = new HexStringConverter();
		translator = ColorTranslator.makeColorTranslator(mode);
		value = converter.fromString(txtHexColor.getText());
		r = translator.red(value);
		g = translator.green(value);
		b = translator.blue(value);
		
		spnRed.getValueFactory().setValue(r);
		spnGreen.getValueFactory().setValue(g);
		spnBlue.getValueFactory().setValue(b);
	}
	
	
	/**
	 * Atualiza o formatador de texto do campo
	 * hexadecimal da cor de acordo com o modo
	 * de cor selecionado pelo usuário.
	 * 
	 * @param index Índice a ser atualizado.
	 */
	private void updateTextFieldHexFromData(int index)
	{
		ColorMode mode;
		Integer value;
		int digits;
		int len;
		StringConverter<Integer> converter;
		
		mode = getColorModeSelected();
		len = mode == ColorMode.COLOR_32_BIT
			? HEX_32_LENGTH
			: HEX_16_LENGTH;
		
		digits = mode == ColorMode.COLOR_32_BIT ? 6 : 4;
		converter = new HexStringConverter(digits);
		value = manager.getColorFromIndex(index, mode);
		
		txtHexColor.setTextFormatter(makeHexFormatter(len));
		txtHexColor.setText(converter.toString(value));
	}
	
	
	/**
	 * Atualiza o formatador de texto do campo
	 * hexadecimal da cor de acordo com o modo
	 * de cor selecionado pelo usuário.
	 */
	private void updateTextFieldHexFromData()
	{
		updateTextFieldHexFromData(spnIndex.getValue());
	}
	
	
	/**
	 * Atualiza o valor do campo de
	 * texto da cor hexadecimal.
	 */
	private void updateTextFieldHexFromRgb()
	{
		ColorTranslator translator;
		HexStringConverter converter;
		String text;
		ColorMode mode;
		int r;
		int g;
		int b;
		int value;
		int digits;
		
		mode = getColorModeSelected();
		digits = mode == ColorMode.COLOR_32_BIT ? 6 : 4;
		translator = ColorTranslator.makeColorTranslator(mode);
		converter = new HexStringConverter(digits);
		r = spnRed.getValue();
		g = spnGreen.getValue();
		b = spnBlue.getValue();
		value = (r << 16) | (g << 8) | b;
		value = translator.translate(value);
		text = converter.toString(value);
		
		txtHexColor.setText(text);
	}
	
	
	/**
	 * Atualiza a cor do retângulo da prévia da cor.
	 */
	private void updateColorRect()
	{
		Color color;
		double r;
		double g;
		double b;
		
		r = ((double) spnRed.getValue()) * (100.0 / 255.0) / 100.0;
		g = ((double) spnGreen.getValue()) * (100.0 / 255.0) / 100.0;
		b = ((double) spnBlue.getValue()) * (100.0 / 255.0) / 100.0;
		color = Color.color(r, g, b);
		
		rectColor.setFill(color);
	}
	
	
	/**
	 * Atualiza o paleta de cores dos dados brutos.
	 */
	private void updateRawColorData()
	{
		int index;
		String value;
		ColorMode mode;
		
		index = spnIndex.getValue();
		value = txtHexColor.getText();
		mode = getColorModeSelected();
		
		manager.setColorFromIndex(index, value, mode);
		getColorDataManager().updateSingleData(index);
	}
	
	
	/**
	 * Atualiza a posição dos dados brutos do pixel.
	 *
	 * @param x	Posição X
	 * @param y	Posição Y
	 */
	private void updatePixelDataPosition(int x, int y)
	{
		int width;
		int index;
		
		width = auroraSheet.getWidth();
		index = (y * width) + x;
		
		pixelManager.setSelectPosition(index);
		pixelManager.updateScrollPosition();
		updateIndexLabel(lblPixelIndex, index);
		updatePositionLabel(lblPixelPosition, x, y);
	}
	
	
	/**
	 * Atualiza o valor do campo do valor do pixel.
	 *
	 * @param index	O índice.
	 */
	private void updateTextFieldPixelValue(int index)
	{
		int value;
		
		value = manager.getPixelFromIndex(index);
		spnValue.getValueFactory().setValue(value);
	}
	
	
	/**
	 * Atualiza a texto do índice no rodapé dos dados brutos.
	 * 
	 * @param label	Label.
	 * @param index	Índice.
	 */
	private void updateIndexLabel(Label label, int index)
	{
		String text;

		text = new StringBuilder()
			.append(index)
			.append(' ')
			.append('(')
			.append(NumberFormats.hexValue(index))
			.append(')')
			.toString();
		
		label.setText(text);
	}
	
	
	/**
	 * Atualiza o label da posição.
	 *
	 * @param label
	 * @param x
	 * @param y
	 */
	private void updatePositionLabel(Label label, int x, int y)
	{
		String text;
		
		text = new StringBuilder()
			.append('(')
			.append(x)
			.append(',')
			.append(' ')
			.append(y)
			.append(')')
			.toString();
		
		label.setText(text);
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
		lblValue = new Label();
		nodes = panFooter.getChildren();
		
		updateIndexLabel(lblValue, 0);
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
		lblValue = new Label();
		positionNodes = panPosition.getChildren();
		
		positionNodes.add(lblTitle);
		positionNodes.add(lblValue);
		panFooter.setLeft(panIndex);
		panFooter.setRight(panPosition);
		
		updatePositionLabel(lblValue, 0, 0);
		
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
	 * @param min		Valor mínimo
	 * @param max		Valor máximo
	 * @param limit		Quantidade de caracters
	 * @return	A fábrica de valores de <code>Spinner</code>.
	 */
	private SpinnerValueFactory<Integer> makeHexSpinnerValueFactory(int min, int max, int limit)
	{
		SpinnerValueFactory<Integer> factory;
		
		factory = makeIntegerSpinnerValueFactory(min, max);
		factory.setConverter(new HexStringConverter(limit));
		
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
	 * Cria o formatador de texto.
	 *
	 * @param <T>	Tipo de conteúdo do formatador.
	 * @return		O formatador de texto.
	 */
	private <T> TextFormatter<T> makeNumericFormatter()
	{
		return new TextFormatter<>(new NumericValueOperator());
	}
	
	
	/**
	 * Callback que atualiza o campo de terxto da cor
	 * hexadecimal e os dados brutos a cor.
	 *
	 * @return
	 */
	@SuppressWarnings("unused")
	private ChangeListener<Integer> makeTextFieldHexChangeListener()
	{
		return (obs, oldValue, newValue) ->
		{
			if (lockChainedChanges)
			{
				return;
			}
			
			lockChainedChanges = true;
			
			updateTextFieldHexFromRgb();
			updateRawColorData();
			updateColorRect();
			
			lockChainedChanges = false;
		};
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
	 * Recebe o gerenciador de dados de acordo
	 * com o modo de cor.
	 *
	 * @param mode	O modo de cor.
	 * @return		O gerenciador de dados.
	 */
	private DataManager getColorDataManager(ColorMode mode)
	{
		switch (mode)
		{
			case COLOR_16_BIT: return color16Manager;
			case COLOR_32_BIT: return color32Manager;
		}
		
		return null;
	}
	
	
	/**
	 * Recebe o gerenciador de dados de
	 * acordo com o modo de cor selecionado.
	 * 
	 * @return	O gerenciador de dados.
	 */
	private DataManager getColorDataManager()
	{
		return getColorDataManager(getColorModeSelected());
	}
	
	
	/**
	 * Recebe a label do índice de cor de acordo com
	 * o modo de cor.
	 *
	 * @param mode	O modo de cor.
	 * @return		O label do índice.
	 */
	private Label getColorIndexLabel(ColorMode mode)
	{
		switch (mode)
		{
			case COLOR_16_BIT: return lbl16ColorIndex;
			case COLOR_32_BIT: return lbl32ColorIndex;
		}
		
		return null;
	}
	
	
	/**
	 * Recebe a label do índice de cor de acordo com
	 * o modo de cor selecionado.
	 *
	 * @return		O label do índice.
	 */
	private Label getColorIndexLabel()
	{
		return getColorIndexLabel(getColorModeSelected());
	}
	
	
	/**
	 * @return A aba do contêiner.
	 */
	private Tab getTab()
	{
		return (Tab) panRoot.getProperties().get("tab");
	}
}
