package com.drxgb.aurorasheetreader;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.drxgb.aurorasheetreader.util.Bytes;

/**
 * Testa a verificação de um string de bytes.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 */
class ByteStringTest
{
	@Test
	void test()
	{
		String a;
		String b;
		String c;
		String d;
		String e;
		
		a = "00 AA FD 4C 72 B6";
		b = "A45F210C";
		c = "ff 21 4c a0";
		d = "012 ABC";
		e = "Esse aqui vai dar bosta.";
		
		assertTrue(Bytes.isValidByteString(a));
		assertTrue(Bytes.isValidByteString(b));
		assertTrue(Bytes.isValidByteString(c));
		assertFalse(Bytes.isValidByteString(d));
		assertFalse(Bytes.isValidByteString(e));
	}
}
