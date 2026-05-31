package com.drxgb.aurorasheetreader.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.drxgb.aurorasheetreader.service.ClipboardHandler;
import com.drxgb.aurorasheetreader.service.DataViewManager;

import javafx.application.Platform;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;

/**
 * Contêiner do editor de de dados brutos.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public class RawDataController implements Initializable
{
	/*
	 * ===========================================================
	 * 			*** CONTROLES ***
	 * ===========================================================
	 */
	
	@FXML private Parent panRoot;
	@FXML private ScrollPane panScroll;
	@FXML private Label lblTitle;
	@FXML private BorderPane panHeader;
	@FXML private TilePane panBody;
	
	
	/*
	 * ===========================================================
	 * 			*** ASSOCIAÇÕES ***
	 * ===========================================================
	 */
	
	private ClipboardHandler clipboard;
	
	
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
		clipboard = null;
		setupRootProperties();
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ***
	 * ===========================================================
	 */	
	
	/**
	 * Copiar todos os bytes.
	 */
	@FXML
	public void onBtnCopyAllAction()
	{
		// TODO Copiar todos os bytes.
	}
	
	
	/**
	 * Colar bytes.
	 */
	@FXML
	public void onBtnPasteAction()
	{
		DataViewManager manager;
		String content;
		int position;
		
		manager = getDataManager();
		position = manager.getIndex();
		content = Clipboard.getSystemClipboard().getString();
		
		if (getClipboard().paste(position, content))
		{
			Platform.runLater(() -> manager.syncRawData());
		}
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PRIVADOS ***
	 * ===========================================================
	 */
	
	/**
	 * Inicializa as propriedades da raiz.
	 */
	private void setupRootProperties()
	{
		ObservableMap<Object, Object> rootProperties;
		
		rootProperties = panRoot.getProperties();
		
		rootProperties.put("header", panHeader);
		rootProperties.put("body", panBody);
		rootProperties.put("scroll", panScroll);
		rootProperties.put("dataManager", null);
	}
	
	
	/**
	 * Recebe o gerenciador de dados.
	 *
	 * @return O gerenciador de dados.
	 */
	private DataViewManager getDataManager()
	{
		return (DataViewManager) panRoot.getProperties().get("dataManager");
	}
	
	
	/**
	 * Recebe o manipulador da area de transferência.
	 *
	 * @return O manipulador.
	 */
	private ClipboardHandler getClipboard()
	{
		DataViewManager manager;
		
		if (clipboard == null)
		{
			manager = getDataManager();
			clipboard = new ClipboardHandler(manager.getBytes());
		}
		
		return clipboard;
	}
}
