package com.drxgb.aurorasheetreader.util;

/**
 * O modo de cores normalmente usado para <code>AuroraSheet</code>.
 * 
 * @author Dr.XGB
 * @version 1.0.0
 * @see com.drxgb.aurorasheetreader.model.AuroraSheet
 */
public enum ColorMode
{
	COLOR_16_BIT(16),
	COLOR_32_BIT(32);
	
	
	private final int code;
	
	
	private ColorMode(int code)
	{
		this.code = code;
	}
	
	
	public int getCode()
	{
		return code;
	}
}
