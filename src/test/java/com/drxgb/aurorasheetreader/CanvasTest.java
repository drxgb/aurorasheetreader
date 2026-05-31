package com.drxgb.aurorasheetreader;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Testa a renderização de um Canvas.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public class CanvasTest extends Application
{
	/**
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage stage) throws Exception
	{
		Scene scene;
		StackPane root;
		SwingNode canvas;
		
		final int width = 80;
		final int height = 60;
		
		canvas = new SwingNode();
		root = new StackPane(canvas);
		scene = new Scene(root, 800.0, 600.0);
		
		SwingUtilities.invokeLater(() ->
		{
			PixelCanvas panel;
			int[] buf;
			double scale;
			
			panel = new PixelCanvas(width, height);
			buf = makeColors(width, height);
			scale = 8.0;
			
			panel.setPixels(buf);
			panel.setScale(scale);
			panel.repaint();
			
			canvas.setContent(panel);
		});
		
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Canvas test");
		stage.show();
	}

	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	
	private static int[] makeColors(int width, int height)
	{
		int[] colors;
		double c;
		
		colors = new int[width * height];
		
		for (int i = 0; i < colors.length; ++i)
		{
			c = (double) (0x00FFFFFF / colors.length) * i;
			colors[i] = (int) c;
		}
		
		return colors;
	}
	
	
	private static class PixelCanvas extends JPanel
	{
		private static final long serialVersionUID = 1L;
		
		private final BufferedImage canvasImage;
		private double scale;
		
		public PixelCanvas(int width, int height)
		{
			canvasImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			scale = 1.0;
		}
		
		
		public void setScale(double scale)
		{
			int width;
			int height;
			Dimension size;
			
			this.scale = scale;
			width = (int) scale * canvasImage.getWidth();
			height = (int) scale * canvasImage.getHeight();
			size = new Dimension(width, height);
			
			setPreferredSize(size);
			setMaximumSize(size);
			revalidate();
		}
		
		
		public void setPixels(int[] buffer)
		{
			int x;
			int y;
			int width;
			int color;
			
			width = canvasImage.getWidth();
			
			for (int i = 0; i < buffer.length; ++i)
			{
				x = i % width;
				y = i / width;
				color = buffer[i];
				
				canvasImage.setRGB(x, y, color);
			}
		}
		
		
		@Override
		protected void paintComponent(Graphics g)
		{
			Graphics2D g2d;
			
			super.paintComponent(g);
			g2d = (Graphics2D) g;
			
			g2d.scale(scale, scale);
			g2d.drawImage(canvasImage, 0, 0, null);
			g2d.dispose();
		}
	}
}
