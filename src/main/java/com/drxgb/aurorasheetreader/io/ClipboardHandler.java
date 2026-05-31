package com.drxgb.aurorasheetreader.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import com.drxgb.aurorasheetreader.java.io.HexStringOutputStream;
import com.drxgb.aurorasheetreader.util.Bytes;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

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
	
	private RawDataReader reader;
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
		reader = new RawDataReader(bytes);
		writer = new RawDataWriter(bytes);
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ***
	 * ===========================================================
	 */
	
	/**
	 * Cola o conteúdo ao conjunto de bytes a
	 * partir da posição solicitada.
	 *
	 * @param position	Posição inicial da colagem.
	 * @param content	Conteúdo a ser colado.
	 * @return			Se o processo de colagem foi bem sucedido.
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
	
	
	/**
	 * Copia todos os dados brutos para a área de transferência.
	 */
	public void copyAll()
	{
		Clipboard clipboard;
		ClipboardContent content;
		String result;
		int b;
		
		try (
			InputStream is = reader.asInputStream();
			OutputStream os = new HexStringOutputStream();
		)
		{
			while (is.available() > 0)
			{
				b = is.read();
				os.write(b);
			}
			
			result = os.toString();
			clipboard = Clipboard.getSystemClipboard();
			content = new ClipboardContent();
			
			content.putString(result);
			clipboard.setContent(content);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
