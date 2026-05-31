package com.drxgb.aurorasheetreader.java.util;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import javafx.scene.control.TextFormatter.Change;

/**
 * Responsável por formatar valores numéricos.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public class NumericValueOperator implements UnaryOperator<Change>
{
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
		if (Pattern.matches("\\d+", change.getControlNewText()))
		{
			return change;
		}
		
		return null;
	}
}
