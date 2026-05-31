package com.drxgb.aurorasheetreader.util;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;

/**
 * Utilitário para as telas roláveis.
 * 
 * @author Dr.XGB
 * @author Dr.XGB
 */
public abstract class Scrolls
{
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ESTÁTICOS ***
	 * ===========================================================
	 */
	
	/**
	 * Atualiza a posição da tela caso o elemento esteja fora
	 * da área de visão.
	 *
	 * @param pane	A tela.
	 * @param node	O nó que terá sua posição testada.
	 */
	public static void trackNodePosition(ScrollPane pane, Node node)
	{
		Bounds contentBounds;
		Bounds viewportBounds;
		Bounds nodeBounds;
		
		double contentSize;
		double viewportSize;
		double nodeMinPos;
		double nodeMaxPos;
		double scrollPos;
		double currentValue;
		double newValue;
		
		contentBounds = pane.getContent().getLayoutBounds();
		viewportBounds = pane.getViewportBounds();
		nodeBounds = node.getBoundsInParent();
			
		if (pane.getHbarPolicy() != ScrollBarPolicy.NEVER)
		{
			newValue = -1.0;
			currentValue = pane.getHvalue();
			viewportSize = viewportBounds.getWidth();
			contentSize = contentBounds.getWidth();
			nodeMinPos = nodeBounds.getMinX();
			nodeMaxPos = nodeBounds.getMaxX();
			scrollPos = currentValue * (contentSize - viewportSize);
			
			if (nodeMaxPos < scrollPos)
			{
				newValue = (scrollPos - viewportSize) / (contentSize - viewportSize);
			}
			else if (nodeMaxPos >= scrollPos + viewportSize)
			{				
				newValue = nodeMinPos / (contentSize - viewportSize);
			}
			
			if (newValue != -1.0)
			{
				newValue = Double.min(Double.max(newValue, 0.0), 1.0);
				pane.setHvalue(newValue);
			}
		}
		
		if (pane.getVbarPolicy() != ScrollBarPolicy.NEVER)
		{
			newValue = -1.0;
			currentValue = pane.getVvalue();
			viewportSize = viewportBounds.getHeight();
			contentSize = contentBounds.getHeight();
			nodeMinPos = nodeBounds.getMinY();
			nodeMaxPos = nodeBounds.getMaxY();
			scrollPos = currentValue * (contentSize - viewportSize);
			
			if (nodeMaxPos < scrollPos)
			{
				newValue = (scrollPos - viewportSize) / (contentSize - viewportSize);
			}
			else if (nodeMaxPos >= scrollPos + viewportSize)
			{				
				newValue = nodeMinPos / (contentSize - viewportSize);
			}
			
			if (newValue != -1.0)
			{
				newValue = Double.min(Double.max(newValue, 0.0), 1.0);
				pane.setVvalue(newValue);
			}
		}
	}
}
