package com.drxgb.aurorasheetreader.javafx.util;

import com.drxgb.aurorasheetreader.util.NumberFormats;

import javafx.util.StringConverter;

/**
 * Conversor de <code>String</code> para uma representação
 * textual de um valor hexadecimal.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public class HexStringConverter extends StringConverter<Integer>
{
	/*
	 * ===========================================================
	 * 			*** CONSTANTES ***
	 * ===========================================================
	 */
	
	private static final Integer DEFAULT_DIGITS = 2;
	
	 
	/*
	 * ===========================================================
	 * 			*** ATRIBUTOS ***
	 * ===========================================================
	 */
	
	private Integer digits;
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUTORES ***
	 * ===========================================================
	 */	
	
	/**
	 * Cria um conversor especificando a quantidade de dígitos.
	 * 
	 * @param digits	Quantidade de dígitos.
	 */
	public HexStringConverter(Integer digits)
	{
		super();
		this.digits = digits;
	}


	/**
	 * Cria um conversor com a quantidade de dígitos padrão (2).
	 */
	public HexStringConverter()
	{
		this(DEFAULT_DIGITS);
	}


	/*
	 * ===========================================================
	 * 			*** MÉTODOS IMPLEMENTADOS ***
	 * ===========================================================
	 */

	/**
	 * @see javafx.util.StringConverter#toString(java.lang.Object)
	 */
	@Override
	public String toString(Integer value)
	{
		if (value == null)
		{
			return "";
		}
		
		return NumberFormats.hexValue(value, digits);
	}


	/**
	 * @see javafx.util.StringConverter#fromString(java.lang.String)
	 */
	@Override
	public Integer fromString(String string)
	{
		try
		{
			if (string == null || string.isEmpty())
			{
				return 0;
			}
			
			return Integer.parseInt(string, 16);
		} catch (NumberFormatException e)
		{
			return 0;
		}
	}

}
