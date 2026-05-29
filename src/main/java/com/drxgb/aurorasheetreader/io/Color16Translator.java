package com.drxgb.aurorasheetreader.io;

/**
 * Traduz a cor para o padrão de 16-bit.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public final class Color16Translator extends ColorTranslator
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
	protected int red(int code)
	{
		return ((code & 0xF800) >> 11) * 0xFF / 0x1F;
	}


	/**
	 * @see com.drxgb.aurorasheetreader.io.ColorTranslator#green(int)
	 */
	@Override
	protected int green(int code)
	{
		return ((code & 0x07E0) >> 5) * 0xFF / 0x3F;
	}


	/**
	 * @see com.drxgb.aurorasheetreader.io.ColorTranslator#blue(int)
	 */
	@Override
	protected int blue(int code)
	{
		return (code & 0x1F) * 0xFF / 0x1F;
	}
}
