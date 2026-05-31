package com.drxgb.aurorasheetreader.controller;

import java.io.IOException;

import com.drxgb.aurorasheetreader.App;
import com.drxgb.aurorasheetreader.service.AuroraSheetTabFactory;
import com.drxgb.aurorasheetreader.service.TabFactory;
import com.drxgb.aurorasheetreader.util.ViewLoader;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controlador da tela principal da aplicação.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public class MainController
{
	/*
	 * ===========================================================
	 * 			*** CONTROLES ***
	 * ===========================================================
	 */
	
	@FXML private TabPane tbpImages;
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ***
	 * ===========================================================
	 */	
	
	/**
	 * Ação disparada ao clicar no botão "New Tab".
	 * 
	 * @throws IOException 
	 */
	@FXML
	public void onBtnNewTabAction() throws IOException
	{
		Tab tab;
		String name;
		TabFactory factory;
		ObservableList<Tab> tabs;
		
		name = App.UNTITLED;		
		factory = new AuroraSheetTabFactory();
		tabs = tbpImages.getTabs();
		tab = factory.makeTab();
		
		tab.setText(name);
		tabs.add(tab);
		tbpImages.getSelectionModel().select(tab);
	}
	
	
	/**
	 * Encerrar a aplicação.
	 */
	@FXML
	public void onMnitCloseAction()
	{
		Platform.exit();
	}
	
	
	/**
	 * Abrir a janela Sobre.
	 *
	 * @throws IOException
	 */
	@FXML
	public void onMnitAboutAction() throws IOException
	{
		Stage mainStage;
		Stage aboutStage;
		Scene scene;
		Parent root;
		
		mainStage = App.getWindow();
		aboutStage = new Stage();
		root = ViewLoader.load("AboutView");
		scene = new Scene(root, 320.0, 240.0);
		
		aboutStage.setTitle("About");
		aboutStage.getIcons().addAll(mainStage.getIcons());
		aboutStage.setScene(scene);
		aboutStage.initOwner(mainStage);
		aboutStage.initModality(Modality.WINDOW_MODAL);
		aboutStage.setResizable(false);
		aboutStage.showAndWait();
	}
}
