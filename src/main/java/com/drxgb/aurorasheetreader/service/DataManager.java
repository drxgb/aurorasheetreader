package com.drxgb.aurorasheetreader.service;

import java.util.Vector;

import com.drxgb.aurorasheetreader.io.RawDataReader;
import com.drxgb.aurorasheetreader.util.Bytes;
import com.drxgb.aurorasheetreader.util.NumberFormats;
import com.drxgb.aurorasheetreader.util.Scrolls;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
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
	
	private int bytesPerGroup;
	private int selectPosition;
	
	
	/*
	 * ===========================================================
	 * 			*** ASSOCIAÇÕES ***
	 * ===========================================================
	 */
	
	private Vector<Byte> bytes;
	private ObservableList<Node> nodes;
	private RawDataReader reader;
	
	private ScrollPane scroll;
	
	
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
		if (bytesPerGroup != 1 && bytesPerGroup != 2 && bytesPerGroup != 4)
		{
			String msg;
			
			msg = new StringBuilder()
				.append("Number of bytes per group must be 1, 2 or 4. ")
				.append(bytesPerGroup)
				.append(" was given.")
				.toString();
			
			throw new IllegalArgumentException(msg);
		}
		
		this.bytes = bytes;
		this.nodes = nodes;
		this.bytesPerGroup = bytesPerGroup;
		
		reader = new RawDataReader(bytes);
		selectPosition = 0;
		scroll = null;
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
				
				if (panGroup != null)
				{					
					panGroup.getChildren().add(lblByte);
				}
			}
			else
			{
				nodes.add(lblByte);
			}
			
			++i;
		}
		
		if (! nodes.isEmpty())
		{
			setSelectPosition(selectPosition);
		}
	}
	
	
	/**
	 * Atualiza uma parte dos dados brutos.
	 *
	 * @param index		Índice do grupo de dados.
	 */
	public void updateSingleData(int index)
	{
		Pane pane;
		Label label;
		ObservableList<Node> labels;
		byte[] _bytes;
		int value;
		
		value = reader.read(index * bytesPerGroup, bytesPerGroup);
		_bytes = Bytes.makeArray(value, bytesPerGroup);
		
		if (bytesPerGroup == 1)
		{
			label = (Label) nodes.get(index);
			writeLabel(label, _bytes[0]);
			
			return;
		}
		
		pane = (Pane) nodes.get(index);
		labels = pane.getChildren();
		
		for (int i = 0; i < bytesPerGroup; ++i)
		{
			label = (Label) labels.get(i);
			writeLabel(label, _bytes[i]);
		}
	}
	
	
	/*
	 * ===========================================================
	 * 			*** GETTERS E SETTERS ***
	 * ===========================================================
	 */
	
	/**
	 * Recebe a posição selecionada.
	 *
	 * @return Posição selecionada.
	 */
	public int getSelectedPosition()
	{
		return selectPosition;
	}
	
	
	/**
	 * Recebe o scroll.
	 *
	 * @return O scroll.
	 */
	public ScrollPane getScroll()
	{
		return scroll;
	}

	
	/**
	 * Define o scroll.
	 *
	 * @param scroll O scroll.
	 */
	public void setScroll(ScrollPane scroll)
	{
		this.scroll = scroll;
	}


	/**
	 * Modifica a posição da seleção.
	 *
	 * @param position	Posição da seleção.
	 */
	public void setSelectPosition(int position)
	{
		deselect(selectPosition);
		select(position);
		
		selectPosition = position;
	}
	
	
	/**
	 * Atualiza a posição da tela rolável, verificando se
	 * a posição selecionada se encontra fora da tela
	 * para ajustar a posição da visualização.
	 */
	public void updateScrollPosition()
	{
		Node current;
		
		if (scroll != null)
		{
			current = nodes.get(selectPosition);
			Scrolls.trackNodePosition(scroll, current);
		}
	}
	
	
	/*
	 * ===========================================================
	 * 			*** MÉTODOS PRIVADOS ***
	 * ===========================================================
	 */
	
	/**
	 * Remove a seleção da região dos dados.
	 *
	 * @param position	Posição da seleção.
	 */
	private void deselect(int position)
	{
		changeNodeBackground(position, null);
	}
	
	
	/**
	 * Seleciona a região dos dados.
	 *
	 * @param position	Posição da seleção.
	 */
	private void select(int position)
	{
		Background bg;
		Color color;
		
		color = Color.color(0.75, 0.94, 1.0, 0.95);
		bg = Background.fill(color);
		
		changeNodeBackground(position, bg);
	}
	
	
	/**
	 * Troca o fundo do nó.
	 *
	 * @param position	Posição da seleção.
	 * @param bg		Fundo a ser aplicado.
	 */
	private void changeNodeBackground(int position, Background bg)
	{
		Pane pane;
		Label label;
		
		if (bytesPerGroup == 1)
		{
			label = (Label) nodes.get(position);
			label.setBackground(bg);
		}
		else
		{
			pane = (Pane) nodes.get(position);
			pane.setBackground(bg);
		}
	}
	
	
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
	
	
	/**
	 * Altera o texto de label.
	 *
	 * @param label	O controle <code>Label</code>.
	 * @param b		Valor a ser modificado.
	 */
	private void writeLabel(Label label, byte b)
	{
		label.setText(NumberFormats.hexValue(b));
	}
}
