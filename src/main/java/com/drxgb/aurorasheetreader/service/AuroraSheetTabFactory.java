package com.drxgb.aurorasheetreader.service;

import java.io.IOException;

import com.drxgb.aurorasheetreader.util.ViewLoader;

import javafx.scene.Parent;
import javafx.scene.control.Tab;

/**
 * Responsável por gerar a aba das folhas de dados.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public class AuroraSheetTabFactory implements TabFactory
{

	/**
	 * @throws IOException 
	 * @see com.drxgb.aurorasheetreader.service.TabFactory#makeTab()
	 */
	@Override
	public Tab makeTab() throws IOException
	{
		Tab tab;
		Parent root;
		
		root = ViewLoader.load("TabView");
		tab = new Tab();
		
		root.getProperties().put("tab", tab);
		tab.setContent(root);
		tab.setClosable(true);
		
		return tab;
	}

}
