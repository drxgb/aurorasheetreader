package com.drxgb.aurorasheetreader.util;

/**
 * Utilitário para caracteres.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public abstract class Characters
{
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ESTÁTICOS ***
	 * ===========================================================
	 */
	
	/**
	 * Verifica se o caractere representa um número hexadecimal.
	 *
	 * @param ch	O caractere a ser verificado.
	 * @return		Sinal do caractere válido.
	 */
	public static boolean isHexValue(char ch)
	{
		return (ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F');
	}
	
	
	/**
	 * Verifica se o caracter é um espaço em branco.
	 *
	 * @param ch	Caractere a ser verificado.
	 * @return		Sinal do caractere em branco.
	 */
	public static boolean isWhitespace(char ch)
	{
		return ch == ' ' || ch == '\r' || ch == '\n' || ch == '\t';
	}
}
