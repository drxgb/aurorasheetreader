package com.drxgb.aurorasheetreader.java.io;

import java.io.IOException;
import java.io.OutputStream;

import com.drxgb.aurorasheetreader.util.NumberFormats;

/**
 * Responsável por escrever bytes convertidos para <code>String</code>.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public class HexStringOutputStream extends OutputStream
{
	/*
	 * ===========================================================
	 * 			*** ATRIBUTOS ***
	 * ===========================================================
	 */
	
	private StringBuilder builder;
	

	/*
	 * ===========================================================
	 * 			*** CONSTRUTORES ***
	 * ===========================================================
	 */

	/**
	 * Cria um escritor de fluxo.
	 */
	public HexStringOutputStream()
	{
		super();
		builder = new StringBuilder();
	}


	/*
	 * ===========================================================
	 * 			*** MÉTODOS IMPLEMENTADOS ***
	 * ===========================================================
	 */

	/**
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException
	{
		String value;
		
		value = NumberFormats.hexValue(b);
		builder.append(value).append(' ');
	}

	
	/*
	 * ===========================================================
	 * 			*** TO STRING ***
	 * ===========================================================
	 */
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return builder.toString().trim();
	}
}
