package com.drxgb.aurorasheetreader.util;

import java.util.function.UnaryOperator;

import javafx.scene.control.TextFormatter.Change;

/**
 * Filtra o limite de caracteres de um valor.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public class HexValueOperator implements UnaryOperator<Change>
{
	/*
	 * ===========================================================
	 * 			*** ATRIBUTOS ***
	 * ===========================================================
	 */
	
	private Integer limit;
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUTORES ***
	 * ===========================================================
	 */	

	/**
	 * @param limit	Quantidade máxima de caracteres.
	 */
	public HexValueOperator(Integer limit)
	{
		this.limit = limit;
	}


	/*
	 * ===========================================================
	 * 			*** MÉTODOS IMPLEMENTADOS ***
	 * ===========================================================
	 */

	/**
	 * @see java.util.function.Function#apply(java.lang.Object)
	 */
	@Override
	public Change apply(Change change)
	{
		String text;
		char ch;
		
		text = change.getControlNewText();
		
		if (text.length() > limit)
		{
			return null;
		}

		for (int i = 0; i < text.length(); ++i)
		{
			ch = text.toUpperCase().charAt(i);
			
			if (! (ch >= '0' && ch <= '9') && ! (ch >= 'A' && ch <= 'F'))
			{
				return null;
			}
		}
						
		return change;
	}

}
