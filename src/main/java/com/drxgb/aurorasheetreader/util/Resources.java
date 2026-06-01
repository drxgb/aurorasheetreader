package com.drxgb.aurorasheetreader.util;

import java.io.IOException;
import java.io.InputStream;

import com.drxgb.aurorasheetreader.App;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;

/**
 * Utilitáio para os recursos.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public abstract class Resources
{
	/*
	 * ===========================================================
	 * 			*** CONSTANTES ***
	 * ===========================================================
	 */
	
	private static final String BASE_PATH = "/resources/";
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ESTÁTICOS ***
	 * ===========================================================
	 */
	
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
				.append(BASE_PATH)
				.append("view/")
				.append(path)
				.append(".fxml")
				.toString();
		loader = new FXMLLoader(App.class.getResource(fxml));
		
		return loader.load();
	}
	
	
	/**
	 * Carrega uma imagem.
	 *
	 * @param path	Caminho da imagem.
	 * @return		A imagem gerada.
	 */
	public static Image loadImage(String path)
	{
		StringBuilder sb;
		InputStream is;
		
		try
		{
			sb = new StringBuilder().append(BASE_PATH).append(path);
			is = App.class.getResourceAsStream(sb.toString());
			
			return new Image(is);
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
