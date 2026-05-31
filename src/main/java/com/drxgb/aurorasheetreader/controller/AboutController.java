package com.drxgb.aurorasheetreader.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.drxgb.aurorasheetreader.App;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

/**
 * Controlador da tela Sobre.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public class AboutController implements Initializable
{
	/*
	 * ===========================================================
	 * 			*** CONTROLES ***
	 * ===========================================================
	 */
	
	@FXML private Label lblTitle;
	@FXML private Label lblVersion;
	@FXML private Label lblJavaVersion;
	@FXML private Label lblJavaFxVersion;
	@FXML private Label lblCopyright;
	
	
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
		lblTitle.setText(App.NAME);
		lblVersion.setText(App.VERSION_STRING);
		lblJavaVersion.setText(System.getProperty("java.version"));
		lblJavaFxVersion.setText(System.getProperty("javafx.runtime.version"));

		setupCopyright();
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ***
	 * ===========================================================
	 */
	
	
	/**
	 * Abre a página do link clicado.
	 *
	 * @param e	Evento disparado.
	 */
	@FXML
	public void onHlkAuthorSiteAction(ActionEvent e)
	{
		Hyperlink link;
		HostServices hs;
		String text;
		
		link = (Hyperlink) e.getTarget();
		hs = App.getInstance().getHostServices();
		text = link.getText();
		
		hs.showDocument(text);
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PRIVADOS ***
	 * ===========================================================
	 */
	
	/**
	 * Escreve o conteúdo de direitos autorais.
	 */
	private void setupCopyright()
	{
		StringBuilder sb;
		
		sb = new StringBuilder();
		
		sb.append('©').append(' ')
			.append(App.RELEASE_YEAR)
			.append(" - ")
			.append("made by Dr.XGB")
			.toString();
		
		lblCopyright.setText(sb.toString());
	}
}
