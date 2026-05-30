package com.drxgb.aurorasheetreader.service;

import java.util.Objects;
import java.util.Vector;

import com.drxgb.aurorasheetreader.io.RawDataReader;
import com.drxgb.aurorasheetreader.io.RawDataWriter;
import com.drxgb.aurorasheetreader.model.AuroraSheet;
import com.drxgb.aurorasheetreader.util.Bytes;
import com.drxgb.aurorasheetreader.util.ColorMode;
import com.drxgb.aurorasheetreader.util.HexStringConverter;

/**
 * Responsável por gerenciar um <code>AuroraSheet</code>
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public class AuroraSheetManager
{
	/*
	 * ===========================================================
	 * 			*** ASSOCIAÇÕES ***
	 * ===========================================================
	 */
	
	private AuroraSheet auroraSheet;
	private RawDataReader color32Reader;
	private RawDataReader color16Reader;
	private RawDataWriter color32Writer;
	private RawDataWriter color16Writer;
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUTORES ***
	 * ===========================================================
	 */

	/**
	 * @param auroraSheet	A imagem a ser gerenciada.
	 */
	public AuroraSheetManager(AuroraSheet auroraSheet)
	{
		Vector<Byte> color32Data;
		Vector<Byte> color16Data;
		
		Objects.requireNonNull(auroraSheet);
		
		color32Data = auroraSheet.getColorData(ColorMode.COLOR_32_BIT);
		color16Data = auroraSheet.getColorData(ColorMode.COLOR_16_BIT);
		
		this.auroraSheet = auroraSheet;
		this.color32Reader = new RawDataReader(color32Data);
		this.color16Reader = new RawDataReader(color16Data);
		this.color32Writer = new RawDataWriter(color32Data);
		this.color16Writer = new RawDataWriter(color16Data);
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ***
	 * ===========================================================
	 */
	
	/**
	 * Redimensiona a imagem e atualiza o conjunto de pixels.
	 * 
	 * @param width		Largura.
	 * @param height	Altura.
	 */
	public void resize(int width, int height)
	{		
		auroraSheet.resize(width, height);		
		fillNullBytes(auroraSheet.getPixelData());
	}
	
	
	/**
	 * Lê a cor do <code>AuroraSheet</code> do
	 * índice solicitado.
	 *
	 * @param index	Índice da paleta de cores.
	 * @param mode	Modo de cor.
	 * @return		O valor da cor.
	 */
	public int getColorFromIndex(int index, ColorMode mode)
	{
		RawDataReader reader;
		int len;
		
		reader = getColorReader(mode);
		len = mode.getBytes();
		
		return reader.read(index * len, len, true);
	}
	
	
	/**
	 * Escreve a cor ao <code>AuroraSheet</code>
	 * no índice solicitado.
	 *
	 * @param index	Índice da paleta de cores.
	 * @param value	Valor da cor a ser escrito.
	 * @param mode	Modo de cor.
	 */
	public void setColorFromIndex(int index, int value, ColorMode mode)
	{
		RawDataWriter writer;
		int len;
		byte[] bytes;
		
		writer = getColorWriter(mode);
		len = mode.getBytes();
		bytes = Bytes.makeArray(value, len);
		
		writer.write(index * len, bytes);
	}
	
	
	/**
	 * Escreve a cor ao <code>AuroraSheet</code>
	 * no índice solicitado.
	 *
	 * @param index	Índice da paleta de cores.
	 * @param value	Valor da cor a ser escrito.
	 * @param mode	Modo de cor.
	 */
	public void setColorFromIndex(int index, String value, ColorMode mode)
	{
		HexStringConverter converter;
		int color;
		
		converter = new HexStringConverter();
		color = converter.fromString(value);
		
		setColorFromIndex(index, color, mode);
	}
	
	
	/*
	 * ===========================================================
	 * 			*** GETTERS ***
	 * ===========================================================
	 */
	
	/**
	 * Recebe o leitor de cores.
	 *
	 * @param mode	Modo de cor.
	 * @return		O leitor de cores de acordo com o modo solicitado.
	 */
	public RawDataReader getColorReader(ColorMode mode)
	{
		switch (mode)
		{
			case COLOR_16_BIT: return color16Reader;
			case COLOR_32_BIT: return color32Reader;
		}
		
		return null;
	}
	
	
	/**
	 * Recebe o escritor de cores.
	 *
	 * @param mode	Modo de cor.
	 * @return		O escritor de cores de acordo com o modo solicitado.
	 */
	public RawDataWriter getColorWriter(ColorMode mode)
	{
		switch (mode)
		{
		case COLOR_16_BIT: return color16Writer;
		case COLOR_32_BIT: return color32Writer;
		}
		
		return null;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PRIVADOS ***
	 * ===========================================================
	 */
	
	/**
	 * Preenche os espaços nulos por um byte vazio.
	 * 
	 * @param bytes	Conjunto de bytes.
	 */
	private void fillNullBytes(Vector<Byte> bytes)
	{		
		for (int i = 0; i < bytes.size(); ++i)
		{
			if (bytes.get(i) == null)
			{
				bytes.set(i, AuroraSheet.NULL_BYTE);
			}
		}
	}
}
