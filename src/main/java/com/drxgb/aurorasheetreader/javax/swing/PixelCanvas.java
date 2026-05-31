package com.drxgb.aurorasheetreader.javax.swing;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Representa a imagem de resolução pixelada.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public class PixelCanvas extends JPanel
{
	/*
	 * ===========================================================
	 * 			*** CONSTANTES ***
	 * ===========================================================
	 */
	
	private static final long serialVersionUID = 1L;

	
	/*
	 * ===========================================================
	 * 			*** ATRIBUTOS ***
	 * ===========================================================
	 */

	private Canvas canvas;
	private BufferedImage img;
	private double scale;
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUTORES ***
	 * ===========================================================
	 */
	
	/**
	 * Cria uma imagem pixelada.
	 *
	 * @param width		Largura
	 * @param height	Altura
	 */
	public PixelCanvas(int width, int height)
	{
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		canvas = new Canvas();
		scale = 1.0;
		
		add(canvas);
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ***
	 * ===========================================================
	 */
	
	/**
	 * Renderiza a imagem.
	 */
	public void render()
	{
		int width;
		int height;
		Dimension size;
		
		width = (int) scale * img.getWidth();
		height = (int) scale * img.getHeight();
		size = new Dimension(width, height);
		
		setPreferredSize(size);
		setMaximumSize(size);
		revalidate();
		repaint();
	}
	
	
	/**
	 * Redimensiona o escalonamento da imagem.
	 * 
	 * @param scale	Escalonamento.
	 */
	public void setScale(double scale)
	{		
		if (scale < 1.0)
		{
			scale = 1.0;
		}

		this.scale = scale;
	}
	
	
	/**
	 * Define o conjunto de pixels da imagem.
	 *
	 * @param buffer	Os dados das cores de cada pixel.
	 */
	public void setPixels(int[] buffer)
	{
		int x;
		int y;
		int width;
		int color;
		
		width = img.getWidth();
		
		for (int i = 0; i < buffer.length; ++i)
		{
			x = i % width;
			y = i / width;
			color = buffer[i];
			
			img.setRGB(x, y, color);
		}
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PROTEGIDOS ***
	 * ===========================================================
	 */	
	
	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2d;
		
		super.paintComponent(g);

		g2d = (Graphics2D) g.create();
		
		g2d.scale(scale, scale);
		g2d.drawImage(img, 0, 0, null);
		
		canvas.update(g2d);
		g2d.dispose();
	}
}
