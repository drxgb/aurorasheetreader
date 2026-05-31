package com.drxgb.aurorasheetreader.service;

import java.io.IOException;
import java.util.Vector;

import com.drxgb.aurorasheetreader.util.ViewLoader;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

/**
 * Responsável por montar a tela dos dados
 * brutos de forma dinâmica.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public class RawDataViewBuilder
{
	/*
	 * ===========================================================
	 * 			*** ATRIBUTOS ***
	 * ===========================================================
	 */
	
	private String title;
	private Integer bytesPerGroup;
	
	
	/*
	 * ===========================================================
	 * 			*** ASSOCIAÇÕES ***
	 * ===========================================================
	 */
	
	private Vector<Byte> bytes;
	private ObservableList<Node> extraNodes;
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUTORES ***
	 * ===========================================================
	 */
	
	/**
	 * Cria o montador de tela de dados brutos.
	 */
	public RawDataViewBuilder()
	{
		extraNodes = FXCollections.observableArrayList();
		clear();
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ***
	 * ===========================================================
	 */
	
	/**
	 * Limpa os dados do montador.
	 */
	public RawDataViewBuilder clear()
	{
		title = "";
		bytesPerGroup = 1;
		bytes = null;
		
		extraNodes.clear();
		
		return this;
	}
	
	
	/**
	 * Insere um nó extra ao montador.
	 * 
	 * @param node	O nó extra a ser inserido.
	 */
	public RawDataViewBuilder appendNode(Node node)
	{
		getExtraNodes().add(node);
		return this;
	}
	
	
	/**
	 * Gera o resultado da montagem da tela.
	 * 
	 * @return O resultado da tela montada.
	 * @throws IOException Quando não for possível montar a tela.
	 */
	public Parent makeResult() throws IOException
	{
		VBox root;
		BorderPane header;
		TilePane body;
		ScrollPane scroll;
		Label lblTitle;
		DataViewManager dataManager;
		ObservableMap<Object, Object> rootProperties;
		
		root = (VBox) ViewLoader.load("RawDataView");
		rootProperties = root.getProperties();
		header = (BorderPane) rootProperties.get("header");
		body = (TilePane) rootProperties.get("body");
		scroll = (ScrollPane) rootProperties.get("scroll");
		
		// Cabeçalho
		if (! title.isEmpty())
		{			
			lblTitle = new Label(title);
			header.setLeft(lblTitle);
		}
		
		// Corpo
		if (bytesPerGroup > 1)
		{
			body.setHgap(8.0);
		}
		if (bytes != null)
		{
			dataManager = new DataViewManager(bytes, body.getChildren(), bytesPerGroup);
			
			Platform.runLater(() -> dataManager.syncRawData());
			dataManager.setScroll(scroll);
			rootProperties.put("dataManager", dataManager);
		}
		
		// Nós extras
		for (Node node : extraNodes)
		{
			root.getChildren().add(node);
		}
		
		return root;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** GETTERS ***
	 * ===========================================================
	 */

	/**
	 * @return O título do cabeçalho da tela.
	 */
	public String getTitle()
	{
		return title;
	}


	/**
	 * @return A quantidade de bytes por grupo.
	 */
	public Integer getBytesPerGroup()
	{
		return bytesPerGroup;
	}


	/**
	 * @return Os nós a serem adicionados à tela.
	 */
	public ObservableList<Node> getExtraNodes()
	{
		return extraNodes;
	}

	
	/*
	 * ===========================================================
	 * 			*** SETTERS ***
	 * ===========================================================
	 */
	 
	/**
	 * @param title Título do montador.
	 */
	public RawDataViewBuilder setTitle(String title)
	{
		if (title == null)
		{
			title = "";
		}
		
		this.title = title;
		return this;
	}
	
	
	/**
	 * @param bytes	O conjunto de bytes
	 */
	public RawDataViewBuilder setBytes(Vector<Byte> bytes)
	{
		this.bytes = bytes;
		return this;
	}


	/**
	 * @param bytesPerGroup A quantidade de bytes por grupo.
	 */
	public RawDataViewBuilder setBytesPerGroup(Integer bytesPerGroup)
	{
		if (bytesPerGroup < 1)
		{
			bytesPerGroup = 1;
		}
		else if (bytesPerGroup > 4)
		{
			bytesPerGroup = 4;
		}
		
		this.bytesPerGroup = bytesPerGroup;
		return this;
	}
}
