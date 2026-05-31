package com.drxgb.aurorasheetreader.service;

import java.util.Vector;

import com.drxgb.aurorasheetreader.io.RawDataWriter;
import com.drxgb.aurorasheetreader.util.Bytes;

/**
 * Responsável por manipular os dados da área de transferência
 * do sistema operacional para copiar e colar dados.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public class ClipboardHandler
{
	/*
	 * ===========================================================
	 * 			*** ASSOCIAÇÕES ***
	 * ===========================================================
	 */
	
	private RawDataWriter writer;
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUTORES ***
	 * ===========================================================
	 */

	/**
	 * Cria o manipulador da área de transferência.
	 *
	 * @param bytes	Conjunto de bytes.
	 */
	public ClipboardHandler(Vector<Byte> bytes)
	{		
		writer = new RawDataWriter(bytes);
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ***
	 * ===========================================================
	 */
	
	public boolean paste(int position, String content)
	{
		byte[] data;
		
		try
		{
			if (Bytes.isValidByteString(content))
			{
				data = Bytes.makeArray(content);
				writer.write(position, data);
				
				return true;
			}
		}
		catch (NumberFormatException e)
		{}
		
		return false;
	}
}
