package com.drxgb.aurorasheetreader.util;

import java.util.HexFormat;

/**
 * Utilitário para formatação de números.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public abstract class NumberFormats
{
	/*
	 * ===========================================================
	 * 			*** CONSTANTES ***
	 * ===========================================================
	 */
	
	private static final int DEFAULT_DIGITS = 2;
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ***
	 * ===========================================================
	 */
	
	/**
	 * Recebe a representação textual de um número hexadecimal.
	 * 
	 * @param value		Valor a ser tratado
	 * @param digits	Quantidade de dígitos
	 * @return			O valor convertido.
	 */
	public static String hexValue(Number value, int digits)
	{
		HexFormat format;
		
		format = HexFormat.of();
		return format.toHexDigits(value.longValue(), digits).toUpperCase();
	}
	
	
	/**
	 * Recebe a representação textual de um número hexadecimal
	 * no padrão de dois dígitos.
	 * 
	 * @param value		Valor a ser tratado
	 * @return			O valor convertido.
	 */
	public static String hexValue(Number value)
	{
		return hexValue(value, DEFAULT_DIGITS);
	}
	
	
	/**
	 * Recebe a representação textual de um número hexadecimal.
	 * 
	 * @param value		Valor a ser tratado
	 * @param digits	Quantidade de dígitos
	 * @return			O valor convertido.
	 */
	public static String hexValue(String value, int digits)
	{
		return hexValue(Integer.parseInt(value, 16), digits);
	}
	
	
	/**
	 * Recebe a representação textual de um número hexadecimal
	 * no padrão de dois dígitos.
	 * 
	 * @param value		Valor a ser tratado
	 * @return			O valor convertido.
	 */
	public static String hexValue(String value)
	{
		return hexValue(value, DEFAULT_DIGITS);
	}
}
