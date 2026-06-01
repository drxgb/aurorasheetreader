package com.drxgb.aurorasheetreader.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import javax.swing.SwingUtilities;

import com.drxgb.aurorasheetreader.io.ColorTranslator;
import com.drxgb.aurorasheetreader.io.RawDataReader;
import com.drxgb.aurorasheetreader.javax.swing.PixelCanvas;
import com.drxgb.aurorasheetreader.model.AuroraSheet;
import com.drxgb.aurorasheetreader.util.ColorMode;

import javafx.embed.swing.SwingNode;

/**
 * Responsável por gerar a imagem de <code>AuroraSheet</code>.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 * @see com.drxgb.aurorasheetreader.model.AuroraSheet
 */
public class AuroraSheetRenderer
{
	/*
	 * ===========================================================
	 * 			*** ATRIBUTOS ***
	 * ===========================================================
	 */
	
	private int[] pixelBuf;
	private List<Integer> palette;
	
	
	/*
	 * ===========================================================
	 * 			*** ASSOCIAÇÕES ***
	 * ===========================================================
	 */
	
	private AuroraSheet auroraSheet;
	private SwingNode canvas;

	 
	/*
	 * ===========================================================
	 * 			*** CONSTRUTORES ***
	 * ===========================================================
	 */
	
	/**
	 * Cria um renderizador de AuroraSheet.
	 *
	 * @param auroraSheet	A representação da imagem.
	 */
	public AuroraSheetRenderer(AuroraSheet auroraSheet)
	{
		Objects.requireNonNull(auroraSheet);
		
		this.auroraSheet = auroraSheet;
		this.canvas = null;
	}	
	
	 
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ***
	 * ===========================================================
	 */
	
	/**
	 * Gera a imagem contida em <code>AuroraSheet</code>.
	 * 
	 * @param mode	O modo de cores a ser renderizado.
	 * @param scale	Escalonamento da imagem.
	 */
	public void render(ColorMode mode, double scale)
	{		
		canvas = new SwingNode();
		
		refreshPalette(mode);
		refreshPixelBuffer();
		
		SwingUtilities.invokeLater(drawCanvas(scale));
	}
	
	
	/**
	 * Modifica o escalonamento da imagem.
	 *
	 * @param scale	Escalonamento.
	 */
	public void setScale(double scale)
	{
		if (canvas != null)
		{
			SwingUtilities.invokeLater(drawCanvas(scale));
		}
	}
	
	
	/*
	 * ===========================================================
	 * 			*** GETTERS ***
	 * ===========================================================
	 */
	
	/**
	 * Recebe o auroraSheet.
	 *
	 * @return O auroraSheet.
	 */
	public AuroraSheet getAuroraSheet()
	{
		return auroraSheet;
	}


	/**
	 * Recebe o canvas.
	 *
	 * @return O canvas.
	 */
	public SwingNode getCanvas()
	{
		return canvas;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PRIVADOS ***
	 * ===========================================================
	 */
	
	/**
	 * Cria uma paleta de cores para ser usada
	 * no desenho da imagem.
	 *
	 * @param mode	Modo de cores (16-bit ou 32-bit).
	 */
	private void refreshPalette(ColorMode mode)
	{
		Vector<Byte> colorBytes;
		RawDataReader reader;
		ColorTranslator translator;
		Integer color;
		
		int numOfBytes;
		int colorCode;
		
		translator = ColorTranslator.makeColorTranslator(mode);
		colorBytes = auroraSheet.getColorData(mode);
		reader = new RawDataReader(colorBytes);
		numOfBytes = mode.getBytes();
		palette = new ArrayList<>();
		
		while (reader.available() > 0)
		{
			colorCode = reader.read(numOfBytes);
			color = translator.translate(colorCode);

			palette.add(color);
		}
	}
	
	
	/**
	 * Atualiza os dados do pixel para renderização.
	 */
	private void refreshPixelBuffer()
	{
		Vector<Byte> pixels;
		int width;
		int height;
		int i;
		
		pixels = auroraSheet.getPixelData();
		width = auroraSheet.getWidth();
		height = auroraSheet.getHeight();
		i = 0;
		pixelBuf = new int[width * height];
		
		for (Byte b : pixels)
		{
			pixelBuf[i++] = palette.get(Byte.toUnsignedInt(b));
		}
	}
	
	
	/**
	 * Ação para desenhar o conteúdo à imagem.
	 *
	 * @param scale		Escalonamento.
	 * @return			Callback do desenho da imagem.
	 */
	private Runnable drawCanvas(double scale)
	{
		final int width;
		final int height;

		if (canvas == null)
		{
			return null;
		}
		
		width = auroraSheet.getWidth();
		height = auroraSheet.getHeight();
		
		return () ->
		{
			PixelCanvas content;
			
			if (auroraSheet.isEmpty())
			{
				return;
			}
			
			content = new PixelCanvas(width, height);
			content.setPixels(pixelBuf);
			content.setScale(scale);
			content.render();
			
			canvas.setContent(content);
		};
	}
}
