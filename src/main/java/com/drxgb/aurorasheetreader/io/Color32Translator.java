package com.drxgb.aurorasheetreader.io;

/**
 * Traduz a cor para o padrão de 32-bit.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public final class Color32Translator extends ColorTranslator
{
	/*
	 * ===========================================================
	 * 			*** MÉTODOS IMPLEMENTADOS ***
	 * ===========================================================
	 */

	/**
	 * @see com.drxgb.aurorasheetreader.io.ColorTranslator#red(int)
	 */
	@Override
	public int red(int code)
	{
		return code & 0xFF;
	}


	/**
	 * @see com.drxgb.aurorasheetreader.io.ColorTranslator#green(int)
	 */
	@Override
	public int green(int code)
	{
		return (code >> 8) & 0xFF;
	}


	/**
	 * @see com.drxgb.aurorasheetreader.io.ColorTranslator#blue(int)
	 */
	@Override
	public int blue(int code)
	{
		return (code >> 16) & 0xFF;
	}

}
