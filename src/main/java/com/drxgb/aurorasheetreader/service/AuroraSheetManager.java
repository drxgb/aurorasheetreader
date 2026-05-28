package com.drxgb.aurorasheetreader.service;

import java.util.Objects;
import java.util.Vector;

import com.drxgb.aurorasheetreader.model.AuroraSheet;

/**
 * Responsável por gerenciar um <code>AuroraSheet</code>
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public class AuroraSheetManager
{
	/*
	 * ===========================================================
	 * 			*** ASSOCIAÇÕES ***
	 * ===========================================================
	 */
	
	private AuroraSheet auroraSheet;
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUTORES ***
	 * ===========================================================
	 */

	/**
	 * @param auroraSheet	A imagem a ser gerenciada.
	 */
	public AuroraSheetManager(AuroraSheet auroraSheet)
	{
		Objects.requireNonNull(auroraSheet);
		this.auroraSheet = auroraSheet;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ***
	 * ===========================================================
	 */
	
	/**
	 * Redimensiona a imagem e atualiza o conjunto de pixels.
	 * 
	 * @param width		Largura.
	 * @param height	Altura.
	 */
	public void resize(int width, int height)
	{		
		auroraSheet.resize(width, height);		
		fillNullBytes(auroraSheet.getPixelData());				
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PRIVADOS ***
	 * ===========================================================
	 */
	
	/**
	 * Preenche os espaços nulos por um byte vazio.
	 * 
	 * @param bytes	Conjunto de bytes.
	 */
	private void fillNullBytes(Vector<Byte> bytes)
	{		
		for (int i = 0; i < bytes.size(); ++i)
		{
			if (bytes.get(i) == null)
			{
				bytes.set(i, AuroraSheet.NULL_BYTE);
			}
		}
	}
}
