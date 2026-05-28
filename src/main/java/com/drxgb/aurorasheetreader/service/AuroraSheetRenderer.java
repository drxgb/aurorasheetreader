package com.drxgb.aurorasheetreader.service;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import com.drxgb.aurorasheetreader.model.AuroraSheet;
import com.drxgb.aurorasheetreader.util.ColorMode;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;

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
	 * 			*** ASSOCIAÇÕES ***
	 * ===========================================================
	 */
	
	private AuroraSheet auroraSheet;
	private Canvas canvas;

	 
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
	 * @param	mode	O modo de cores a ser renderizado.
	 */
	public void render(ColorMode mode)
	{
		List<Integer> palette;
		Vector<Byte> pixels;
		PixelWriter writer;
		PixelFormat<IntBuffer> format;
		int[] buffer;
		int width;
		int height;
		int i;
		
		clearCanvas();
		
		palette = makePalette(mode);
		pixels = auroraSheet.getPixelData();
		writer = canvas.getGraphicsContext2D().getPixelWriter();
		format = PixelFormat.getIntArgbInstance();
		
		width = auroraSheet.getWidth();
		height = auroraSheet.getHeight();
		buffer = new int[width * height];
		i = 0;
		
		for (Byte b : pixels)
		{
			buffer[i++] = palette.get((int) b);
		}
		
		writer.setPixels(0, 0, width, height, format, buffer, 0, width);
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
	public Canvas getCanvas()
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
	 * @return	A paleta de cores.
	 */
	private List<Integer> makePalette(ColorMode mode)
	{
		List<Integer> palette;
		Vector<Byte> colorBytes;
		Iterator<Byte> it;
		Integer color;
		
		int r, g, b;
		int numOfBytes;
		int colorCode;
		byte current;
		
		r = g = b = 0;
		colorBytes = auroraSheet.getColorData(mode);
		numOfBytes = mode.getCode() / 8;
		palette = new ArrayList<>();
		it = colorBytes.iterator();
		
		while (it.hasNext())
		{
			colorCode = (int) it.next();
			
			for (int i = 1; i < numOfBytes; ++i)
			{
				if (it.hasNext())
				{
					current = it.next();
					colorCode += ((int) current) << (i * 8);
				}
			}
			
			if (mode == ColorMode.COLOR_32_BIT)
			{
				r = colorCode & 0xFF;
				g = (colorCode >> 8) & 0xFF;
				b = (colorCode >> 16) & 0xFF;
			}
			else if (mode == ColorMode.COLOR_16_BIT)
			{
				//
			}
			
			color = 0xFF000000;
			color += r << 16;
			color += g << 8;
			color += b;
			
			palette.add(color);
		}
		
		return palette;
	}
	
	
	/**
	 * Limpa a imagem.
	 */
	private void clearCanvas()
	{
		double width;
		double height;
		
		width = auroraSheet.getWidth().doubleValue();
		height = auroraSheet.getHeight().doubleValue();
		
		canvas = new Canvas(width, height);
	}
}
