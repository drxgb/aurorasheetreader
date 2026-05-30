package com.drxgb.aurorasheetreader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.drxgb.aurorasheetreader.io.RawDataWriter;
import com.drxgb.aurorasheetreader.util.Bytes;

/**
 * Testa a escrita de dados brutos.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
class RawDataWriteTest
{
	private Vector<Byte> data;
	private Vector<Byte> expected;
	

	@BeforeEach
	public void setUp() throws Exception
	{
		data = new Vector<>();
		expected = new Vector<>();
		
		expected.add((byte) 0xFF);
		expected.add((byte) 0xEE);
		expected.add((byte) 0xDD);
		expected.add((byte) 0xCC);
		expected.add((byte) 0xBB);
		expected.add((byte) 0xAA);
		expected.add((byte) 0x99);
		expected.add((byte) 0x88);
		expected.add((byte) 0x77);
		expected.add((byte) 0x66);
		expected.add((byte) 0x55);
		expected.add((byte) 0x44);
		expected.add((byte) 0x33);
		expected.add((byte) 0x22);
		expected.add((byte) 0x11);
		
		data.setSize(expected.size());
	}


	@Test
	public void test()
	{
		RawDataWriter writer;
		byte[] bytes;
		
		writer = new RawDataWriter(data);
		bytes = new byte[] {
			(byte) 0xFF,
			(byte) 0xEE,
			(byte) 0xDD,
			(byte) 0xCC,
			(byte) 0xBB,
			(byte) 0xAA,
			(byte) 0x99,
			(byte) 0x88,
		};
		
		writer.write(bytes);
		writer.write(Bytes.makeArray(0x44556677, Integer.BYTES));
		writer.write(Bytes.makeArray(0x2233, Short.BYTES));
		writer.write(Bytes.makeArray(0x11, Byte.BYTES));
		
		assertEquals(expected, data);
	}

}
