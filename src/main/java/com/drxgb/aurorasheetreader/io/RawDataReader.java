package com.drxgb.aurorasheetreader.io;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;
import java.util.Vector;

/**
 * Responsável por ler dados brutos.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public class RawDataReader
{
	/*
	 * ===========================================================
	 * 			*** ATRIBUTOS ***
	 * ===========================================================
	 */
	
	private int position;
	private int length;
	
	
	/*
	 * ===========================================================
	 * 			*** ASSOCIAÇÕES ***
	 * ===========================================================
	 */
	
	private Vector<Byte> data;
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUTORES ***
	 * ===========================================================
	 */

	/**
	 * Cria um leitor de dados brutos.
	 *
	 * @param data	O conjunto dos dados brutos.
	 */
	public RawDataReader(Vector<Byte> data)
	{
		Objects.requireNonNull(data);
		
		this.data = data;
		this.position = 0;
		this.length = data.size();
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ***
	 * ===========================================================
	 */
	
	/**
	 * Verifica quantos dados faltam para ler no conjunto.
	 *
	 * @return A quantidade de dados restantes.
	 */
	public int available()
	{
		return length - position;
	}

	
	/**
	 * Lê os dados e converte para um valor numérico
	 * a partir da posição atual de leitura.
	 *
	 * @param len	Quantidade de caracteres para ler.
	 * @return	O valor recebido pela leitura.
	 */
	public int read(int len)
	{
		int result;
		int end;
		int value;
		int i;
		byte b;
		
		result = 0;
		end = position + len;
		i = 0;
		
		if (end > length)
		{
			end = length;
		}
		
		while (position < end)
		{
			b = data.get(position);
			value = ((int) b & 0xFF) << (i * 8);
			result += value;
			
			++i;
			++position;
		}
		
		return result;
	}
	
	
	/**
	 * Lê os dados e converte para um valor numérico
	 * a partir do início da leitura.
	 *
	 * @param offset		Posição inicial da leitura.
	 * @param len			Quantidade de caracteres para ler.
	 * @param saveLastPos	Se for <code>true</code> a posiçao
	 * 							de leitura voltará ao estado anterior.
	 * @return				O valor recebido pela leitura.
	 */
	public int read(int offset, int len, boolean saveLastPos)
	{
		Integer lastPos;
		int result;
		
		lastPos = saveLastPos ? position : null;
		position = offset;		
		result = read(len);
		
		if (lastPos != null)
		{
			position = lastPos;
		}
		
		return result;
	}
	
	
	/**
	 * Lê os dados e converte para um valor numérico
	 * a partir do início da leitura.
	 *
	 * @param offset	Posição inicial da leitura.
	 * @param len		Quantidade de caracteres para ler.
	 * @return			O valor recebido pela leitura.
	 */
	public int read(int offset, int len)
	{
		return read(offset, len, false);
	}
	
	
	/**
	 * Lê os dados e converte para um valor numérico
	 * a partir da posição atual de leitura.
	 *
	 * @return	O valor recebido pela leitura.
	 */
	public int read()
	{
		return read(1);
	}
	
	
	/**
	 * Converte os dados para um <code>InputStream</code>.
	 *
	 * @return O fluxo de bytes para leitura.
	 * @see java.io.InputStream
	 */
	public InputStream asInputStream()
	{
		byte[] buf;
		
		buf = new byte[length];
		
		for (int i = 0; i < length; ++i)
		{
			buf[i] = data.get(i);
		}
		
		return new ByteArrayInputStream(buf);
	}
}
