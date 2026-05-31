package com.drxgb.aurorasheetreader.javafx.util;

import com.drxgb.aurorasheetreader.java.util.HexValueOperator;
import com.drxgb.aurorasheetreader.java.util.NumericValueOperator;

import javafx.application.Platform;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;

/**
 * Utilitário para aplicar regras a componentes de interface gráfica.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public abstract class Constraints
{
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ESTÁTICOS ***
	 * ===========================================================
	 */
	
	/**
	 * Seleciona o conteúdo do campo de texto quando o foco
	 * é solocitado pelo usuário.
	 *
	 * @param txt	O campo de texto.
	 */
	@SuppressWarnings("unused")
	public static void selectTextOnFocus(TextInputControl txt)
	{
		txt.focusedProperty().addListener((obs, wasFocused, isFocused) ->
		{
			if (isFocused)
			{
				Platform.runLater(txt::selectAll);
			}
		});
	}
	
	
	/**
	 * Define o formatador de texto do campo para aceitar
	 * somente valores hexadecimais.
	 *
	 * @param txt		O campo de texto.
	 * @param digits	Limite de dígitos do campo.
	 */
	public static void setHexTextFormatter(TextInputControl txt, int digits)
	{
		txt.setTextFormatter(new TextFormatter<>(new HexValueOperator(digits)));
	}
	
	
	/**
	 * Define o formatador de texto do campo para aceitar
	 * somente valores numéricos.
	 *
	 * @param txt		O campo de texto.
	 */
	public static void setNumberTextFormatter(TextInputControl txt)
	{
		txt.setTextFormatter(new TextFormatter<>(new NumericValueOperator()));
	}
	
	
	/**
	 * Define a fábrica de valor do <code>Spinner</code>
	 * exibindo o valor em hexadecimal.
	 *
	 * @param spn		O <code>Spinner</code>.
	 * @param min		Valor mínimo.
	 * @param max		Valor máximo.
	 * @param limit		Limite caracteres do campo de texto.
	 */
	public static void setHexSpinnerValueFactory(Spinner<Integer> spn, int min, int max, int limit)
	{
		SpinnerValueFactory<Integer> factory;
		
		factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max);
		factory.setConverter(new HexStringConverter(limit));
		
		spn.setValueFactory(factory);
	}
	
	
	/**
	 * Define a fábrica de valor do <code>Spinner</code>
	 * exibindo somente números.
	 *
	 * @param spn	O <code>Spinner</code>.
	 * @param min	Valor mínimo.
	 * @param max	Valor máximo.
	 */
	public static void setIntegerSpinnerValueFactory(Spinner<Integer> spn, int min, int max)
	{
		spn.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max));
	}
}
