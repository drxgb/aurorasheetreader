package com.drxgb.aurorasheetreader.controller;

import java.io.IOException;

import com.drxgb.aurorasheetreader.App;
import com.drxgb.aurorasheetreader.service.AuroraSheetTabFactory;
import com.drxgb.aurorasheetreader.service.TabFactory;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

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
}
