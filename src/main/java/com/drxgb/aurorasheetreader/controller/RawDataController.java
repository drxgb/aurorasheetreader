package com.drxgb.aurorasheetreader.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
	 * 			*** MÉTODOS IMPLEMENTADOS ***
	 * ===========================================================
	 */
	
	/**
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
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
		// TODO Colar bytes.
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
}
