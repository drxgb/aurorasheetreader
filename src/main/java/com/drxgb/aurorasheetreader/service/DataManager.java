package com.drxgb.aurorasheetreader.service;

import java.util.Vector;

import com.drxgb.aurorasheetreader.util.NumberFormats;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

/**
 * Responsável por gerenciar os dados brutos.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public class DataManager
{
	/*
	 * ===========================================================
	 * 			*** ATRIBUTOS ***
	 * ===========================================================
	 */
	
	private Integer bytesPerGroup;
	
	
	/*
	 * ===========================================================
	 * 			*** ASSOCIAÇÕES ***
	 * ===========================================================
	 */
	
	private Vector<Byte> bytes;
	private ObservableList<Node> nodes;
	
	
	/*
	 * ===========================================================
	 * 			*** CONSTRUTORES ***
	 * ===========================================================
	 */
	
	/**
	 * Cria o gerenciador de dados.
	 * 
	 * @param bytes			Conjunto de bytes
	 * @param nodes			Conjunto de nós do contêiner
	 * @param bytesPerGroup	Quantidade de bytes agrupados
	 */
	public DataManager(Vector<Byte> bytes, ObservableList<Node> nodes, Integer bytesPerGroup)
	{
		this.bytes = bytes;
		this.nodes = nodes;
		this.bytesPerGroup = bytesPerGroup;
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PÚBLICOS ***
	 * ===========================================================
	 */	
	
	/**
	 * Sincroniza os dados brutos entre o vetor e o contêiner.
	 */
	public void syncRawData()
	{
		HBox panGroup;
		Label lblByte;
		int i;
		
		i = 0;
		panGroup = null;

		nodes.clear();
		
		for (Byte b : bytes)
		{
			lblByte = makeByteLabel(b);
			
			if (bytesPerGroup > 1)
			{
				if (i % bytesPerGroup == 0)
				{
					panGroup = new HBox(2.0);
					nodes.add(panGroup);
				}
				
				panGroup.getChildren().add(lblByte);
			}
			else
			{
				nodes.add(lblByte);
			}
			
			++i;
		}
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PRIVADOS ***
	 * ===========================================================
	 */
	
	/**
	 * Cria uma <code>Label</code> do byte.
	 * 
	 * @param b	Valor do byte
	 * @return	O componente do byte.
	 */
	private Label makeByteLabel(byte b)
	{
		Label label;
		String text;
		Font font;
		
		text = NumberFormats.hexValue(b);
		label = new Label(text);
		font = new Font("Consolas", 16.0);
		
		label.setFont(font);
		
		return label;
	}
}
