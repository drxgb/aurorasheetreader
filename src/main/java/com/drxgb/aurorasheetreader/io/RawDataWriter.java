package com.drxgb.aurorasheetreader.io;

import java.util.Objects;
import java.util.Vector;

/**
 * Responsável por escrever dados brutos.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public class RawDataWriter
{
	/*
	 * ===========================================================
	 * 			*** ATRIBUTOS ***
	 * ===========================================================
	 */
	
	private int position;
	

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
	 * Cria um escritor de dados brutos.
	 *
	 * @param data	O conjunto dos dados brutos.
	 */
	public RawDataWriter(Vector<Byte> data)
	{
		Objects.requireNonNull(data);
		
		this.data = data;
		this.position = 0;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ***
	 * ===========================================================
	 */
	
	/**
	 * Verifica quantos dados faltam para escrever no conjunto.
	 *
	 * @return A quantidade de dados restantes.
	 */
	public int available()
	{
		return length() - position;
	}
	
	
	/**
	 * Escreve os bytes no conjunto de dados brutos.
	 *
	 * @param bytes	O conjunto de bytes a ser escrito.
	 */
	public void write(byte[] bytes)
	{
		for (int i = 0; i < bytes.length; ++i)
		{
			if (available() <= 0)
			{
				break;
			}

			data.set(position++, bytes[i]);
		}
	}
	
	
	/**
	 * Escreve os bytes no conjunto de dados brutos
	 * a partir de um posição inicial.
	 *
	 * @param offset		A posição inicial de escrita.
	 * @param bytes			O conjunto de bytes a ser escrito.
	 * @param saveLastPos	Recupera a posição anterior após a escrita.
	 */
	public void write(int offset, byte[] bytes, boolean saveLastPos)
	{
		Integer lastPos;
		
		lastPos = saveLastPos ? position : null;
		position = offset;
		write(bytes);
		
		if (lastPos != null)
		{
			position = lastPos;
		}
	}
	
	
	/**
	 * Escreve os bytes no conjunto de dados brutos
	 * a partir de um posição inicial.
	 *
	 * @param offset		A posição inicial de escrita.
	 * @param bytes			O conjunto de bytes a ser escrito.
	 * @param saveLastPos	Recupera a posição anterior após a escrita.
	 */
	public void write(int offset, byte[] bytes)
	{
		write(offset, bytes, false);
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PRIVADOS ***
	 * ===========================================================
	 */
	
	/**
	 * Recebe o tamanho dos dados de escrita.
	 *
	 * @return
	 */
	private int length()
	{
		return data.size();
	}
}
