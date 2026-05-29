package com.drxgb.aurorasheetreader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.drxgb.aurorasheetreader.io.ColorTranslator;
import com.drxgb.aurorasheetreader.util.ColorMode;

/**
 * Testa a tradução de cores para o formato 32-bit e 16-bit.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
class ColorTranslateTest
{
	private List<Integer> data32;
	private List<Integer> data16;
	private List<Integer> expected;
	

	/**
	 * Inicializa os dados.
	 *
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception
	{
		setupData32();
		setupData16();
		setupExpectedData();
	}


	@Test
	public void test()
	{
		ColorTranslator translator32;
		ColorTranslator translator16;
		int result32;
		int result16;
		int e;
		
		translator32 = ColorTranslator.makeColorTranslator(ColorMode.COLOR_32_BIT);
		translator16 = ColorTranslator.makeColorTranslator(ColorMode.COLOR_16_BIT);
		
		for (int i = 0; i < expected.size(); ++i)
		{
			e = expected.get(i);
			result32 = translator32.translate(data32.get(i));
			result16 = translator16.translate(data16.get(i));
			
			assertEquals(e, result32);
			assertEquals(e, result16);
		}
	}

	
	/**
	 * Prepara os dados da cor de 32-bit.
	 */
	private void setupData32()
	{
		data32 = new ArrayList<>();
		
		data32.add(0x0000FF);
		data32.add(0x00FF00);
		data32.add(0xFF0000);
		data32.add(0x00FFFF);
		data32.add(0xFFFF00);
		data32.add(0xFF00FF);
	}
	
	
	/**
	 * Prepara os dados da cor de 16-bit.
	 */
	private void setupData16()
	{
		data16 = new ArrayList<>();
		
		data16.add(0xF800);
		data16.add(0x07E0);
		data16.add(0x001F);
		data16.add(0xFFE0);
		data16.add(0x07FF);
		data16.add(0xF81F);
	}
	
	
	/**
	 * Prepara os dados esperados para 32-bit.
	 */
	private void setupExpectedData()
	{
		expected = new ArrayList<>();
		
		expected.add(0xFFFF0000);
		expected.add(0xFF00FF00);
		expected.add(0xFF0000FF);
		expected.add(0xFFFFFF00);
		expected.add(0xFF00FFFF);
		expected.add(0xFFFF00FF);
	}
}
