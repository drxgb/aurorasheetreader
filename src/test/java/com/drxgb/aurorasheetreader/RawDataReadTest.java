package com.drxgb.aurorasheetreader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.drxgb.aurorasheetreader.io.RawDataReader;

/**
 * Testa a leitura de dados brutos.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
class RawDataReadTest
{
	private Vector<Byte> bytes;
	
	
	/**
	 * Inicializa os dados bturos.
	 *
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception
	{		
		bytes = new Vector<>();
		
		bytes.add((byte) 0xFF);
		bytes.add((byte) 0xEE);
		bytes.add((byte) 0xDD);
		bytes.add((byte) 0x00);
		
		bytes.add((byte) 0xCC);
		bytes.add((byte) 0xBB);
		bytes.add((byte) 0xAA);
		bytes.add((byte) 0x00);
		
		bytes.add((byte) 0x99);
		bytes.add((byte) 0x88);
		bytes.add((byte) 0x77);
		bytes.add((byte) 0x00);
		
		bytes.add((byte) 0x66);
		bytes.add((byte) 0x55);
		bytes.add((byte) 0x44);
		bytes.add((byte) 0x00);
		
		bytes.add((byte) 0x33);
		bytes.add((byte) 0x22);
		bytes.add((byte) 0x11);
		bytes.add((byte) 0x00);
	}


	@Test
	public void test()
	{
		RawDataReader reader;
		StringBuilder esb;
		
		int a, b, c, d, e;
		
		final int numOfBytes = 4;
		
		esb = new StringBuilder();
		reader = new RawDataReader(bytes);
		
		a = reader.read(numOfBytes);
		b = reader.read(numOfBytes);
		c = reader.read(numOfBytes);
		d = reader.read(numOfBytes);
		e = reader.read(numOfBytes);
		
		for (Byte by : bytes)
		{
			esb.append(by);
		}
		
		assertEquals(0x00DDEEFF, a);
		assertEquals(0x00AABBCC, b);
		assertEquals(0x00778899, c);
		assertEquals(0x00445566, d);
		assertEquals(0x00112233, e);
	}

}
