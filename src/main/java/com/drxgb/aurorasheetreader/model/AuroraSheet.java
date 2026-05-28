package com.drxgb.aurorasheetreader.model;

import java.util.Vector;

import com.drxgb.aurorasheetreader.util.ColorMode;

/**
 * Representa a imagem RPG_RT.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public class AuroraSheet
{
	/*
	 * ===========================================================
	 * 			*** CONSTANTES ***
	 * ===========================================================
	 */
	
	public static final Byte NULL_BYTE = 0;

	private static final Integer MAX_COLORS = 0x100;
	
	
	/*
	 * ===========================================================
	 * 			*** ATRIBUTOS ***
	 * ===========================================================
	 */
	
	private Integer width;
	private Integer height;
	
	
	/*
	 * ===========================================================
	 * 			*** ASSOCIAÇÕES ***
	 * ===========================================================
	 */
	
	private Vector<Byte> color32Data;
	private Vector<Byte> color16Data;
	private Vector<Byte> pixelData;
	

	/*
	 * ===========================================================
	 * 			*** CONSTRUTORES ***
	 * ===========================================================
	 */

	/**
	 * Cria uma folha de imagem com o tamanho especificado.
	 * 
	 * @param width		Largura
	 * @param height	Altura
	 */
	public AuroraSheet(Integer width, Integer height)
	{
		initColorData();
		initPixelData();
		resize(width, height);
	}


	/**
	 * Cria uma folha de imagem vazia.
	 */
	public AuroraSheet()
	{
		this(0, 0);
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ***
	 * ===========================================================
	 */
	
	/**
	 * Redimensiona a folha de imagem.
	 * 
	 * @param width		Largura
	 * @param height	Altura
	 */
	public void resize(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		resizePixelData();
	}
	
	
	/*
	 * ===========================================================
	 * 			*** GETTERS ***
	 * ===========================================================
	 */

	/**
	 * @return A Largura.
	 */
	public Integer getWidth()
	{
		return width;
	}


	/**
	 * @return A Altura
	 */
	public Integer getHeight()
	{
		return height;
	}


	/**
	 * Recebe o conjunto de cores.
	 *
	 * @param mode	O modo de cor.
	 * @return O conjunto de cores.
	 */
	public Vector<Byte> getColorData(ColorMode mode)
	{
		switch (mode)
		{
			case COLOR_32_BIT: return color32Data;
			case COLOR_16_BIT: return color16Data;
		}
		
		return null;
	}


	/**
	 * @return O conjunto de pixels
	 */
	public Vector<Byte> getPixelData()
	{
		return pixelData;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PRIVADOS ***
	 * ===========================================================
	 */
	
	/**
	 * Inicializa os dados das cores.
	 */
	private void initColorData()
	{
		color32Data = new Vector<Byte>(MAX_COLORS * 4);
		color16Data = new Vector<Byte>(MAX_COLORS * 2);
		
		for (int i = 0; i < MAX_COLORS; ++i)
		{
			color32Data.add(i + 0, NULL_BYTE);
			color32Data.add(i + 1, NULL_BYTE);
			color32Data.add(i + 2, NULL_BYTE);
			color32Data.add(i + 3, NULL_BYTE);
			
			color16Data.add(i + 0, NULL_BYTE);
			color16Data.add(i + 1, NULL_BYTE);
		}
	}
	
	
	/**
	 * Inicializa os dados dos pixels.
	 */
	private void initPixelData()
	{
		pixelData = new Vector<Byte>();
	}
	
	
	/**
	 * Redimensiona o vetor de pixels.
	 */
	private void resizePixelData()
	{
		pixelData.setSize(width * height);
	}
}
