package com.drxgb.aurorasheetreader.util;

import java.io.IOException;

import com.drxgb.aurorasheetreader.App;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * Utilitáio para carregar as telas.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public abstract class ViewLoader
{
	/**
	 * Carrega a visão da tela solicitada.
	 * 
	 * @param path	O caminho do arquivo sem a extensão.
	 * @return		A instância dos componentes da tela.
	 * @throws IOException	Quando o arquivo da visão não for encontrado.
	 */
	public static Parent load(String path) throws IOException
	{
		String fxml;
		FXMLLoader loader;
		
		fxml = new StringBuilder()		
				.append("/resources/view/")
				.append(path)
				.append(".fxml")
				.toString();
		loader = new FXMLLoader(App.class.getResource(fxml));
		
		return loader.load();
	}
}
