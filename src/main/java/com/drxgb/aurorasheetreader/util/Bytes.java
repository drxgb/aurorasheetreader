package com.drxgb.aurorasheetreader.util;

import java.util.ArrayList;
import java.util.List;

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
	 * Recebe o valor e converte para um conjunto de bytes.
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
	
	
	/**
	 * Recebe o texto e converte para um conjunto de bytes.
	 *
	 * @param value	Texto recebido
	 * @return		O conjunto de bytes.
	 * @throws NumberFormatException
	 * 				Quando o texto lido não é válido para
	 * 					ser convertido para número.
	 */
	public static byte[] makeArray(String value) throws NumberFormatException
	{
		byte[] bytes;
		List<Byte> data;
		int pos;
		int end;
		int len;
		byte b;
		String chunk;
		
		data = new ArrayList<>();
		len = value.length();
		pos = 0;
		
		while (pos < len - 1)
		{
			if (Characters.isWhitespace(value.charAt(pos)))
			{
				end = pos + 1;
			}
			else
			{
				end = pos + 2;
				chunk = value.substring(pos, end);
				b = Integer.valueOf(chunk, 16).byteValue();
				
				data.add(b);
			}
			
			pos = end;
		}
		
		bytes = new byte[data.size()];
		
		for (int i = 0; i < bytes.length; ++i)
		{
			bytes[i] = data.get(i);
		}
		
		return bytes;
	}
	
	
	/**
	 * Verifica se o texto é uma representação
	 * de um byte ou um conjunto de bytes.
	 *
	 * @param data	O texto a ser verificado.
	 * @return		Sinal do texto válido.
	 */
	public static boolean isValidByteString(String data)
	{
		int pos;
		int len;
		char[] chars;
		char ch;
		
		chars = data.toUpperCase().toCharArray();
		pos = 0;
		len = chars.length;
		
		while (pos < len - 1)
		{
			ch = chars[pos];
			
			if (Characters.isWhitespace(ch))
			{
				++pos;
				continue;
			}
			
			for (int i = 0; i < 2; ++i)
			{
				ch = chars[pos + i];
				
				if (! Characters.isHexValue(ch))
				{
					return false;
				}
			}
			
			pos += 2;
		}
		
		return true;
	}
}
