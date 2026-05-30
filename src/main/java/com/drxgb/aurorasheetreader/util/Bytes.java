package com.drxgb.aurorasheetreader.util;

/**
 * Utilitário para manipulação de bytes.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public abstract class Bytes
{
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ESTÁTICOS ***
	 * ===========================================================
	 */
	
	/**
	 * Recebe o valor e converte para um array de bytes.
	 *
	 * @param value 	Valor recebido.
	 * @param len		Quantidade de bytes.
	 * @return			O conjunto de bytes.
	 */
	public static byte[] makeArray(Number value, int len)
	{
		byte[] bytes;
		
		if (len > 8)
		{
			len = 8;
		}
		
		bytes = new byte[len];
		
		for (int i = 0; i < len; ++i)
		{
			bytes[i] = (byte) ((value.intValue() >> (i * 8)) & 0xFF);
		}
		
		return bytes;
	}
}
