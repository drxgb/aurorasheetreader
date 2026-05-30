package com.drxgb.aurorasheetreader.io;

import com.drxgb.aurorasheetreader.util.ColorMode;

/**
 * Responsável por traduzir o código de cores de acordo
 * com o seu modo.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public abstract class ColorTranslator
{
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ESTÁTICOS ***
	 * ===========================================================
	 */
	
	/**
	 * Cria um tradutor de acordo com o modo de cores.
	 *
	 * @param mode	O modo de cor.
	 * @return O tradutor de cores.
	 */
	public static ColorTranslator makeColorTranslator(ColorMode mode)
	{
		switch (mode)
		{
			case COLOR_16_BIT: return new Color16Translator();
			case COLOR_32_BIT: return new Color32Translator();
		}
		
		return null;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ***
	 * ===========================================================
	 */
	
	/**
	 * Traduz o código de cor para o modo padrão.
	 *
	 * @param code	O código da cor a ser traduzido.
	 * @return	O código da cor traduzido.
	 */
	public final int translate(int code)
	{
		int color;
		int r;
		int g;
		int b;
		
		r = red(code);
		g = green(code);
		b = blue(code);
		
		color = 0xFF000000;
		color |= r << 16;
		color |= g << 8;
		color |= b;
		
		return color;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ABSTRATOS ***
	 * ===========================================================
	 */
	
	/**
	 * @param code	Código da cor.
	 * @return Código da cor vermelha.
	 */
	public abstract int red(int code);
	
	
	/**
	 * @param code	Código da cor.
	 * @return Código da cor verde.
	 */
	public abstract int green(int code);
	
	
	/**
	 * @param code	Código da cor.
	 * @return Código da cor azul.
	 */
	public abstract int blue(int code);
}
