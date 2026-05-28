package com.drxgb.aurorasheetreader.service;

import java.io.IOException;

import javafx.scene.control.Tab;

/**
 * Contrato para gerar abas.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
public interface TabFactory
{
	/**
	 * Gera uma nova aba.
	 * 
	 * @return A nova aba.
	 * @throws IOException	Quando não for possível gerar a aba.
	 */
	Tab makeTab() throws IOException;
}
