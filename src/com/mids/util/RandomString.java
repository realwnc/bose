package com.mids.util;

import java.util.Random;

/**
   * 产生随机字符串
   * */
public class RandomString
{
	private static Random randGen = null;
	private static char[] numbersAndLetters = null;
	private static Object initLock = new Object(); 
	
	
	public static final String GenString(int length) 
	{
		if (length < 1) {
			return null;
		}
		if (randGen == null) {
			synchronized (initLock) {
				if (randGen == null) {
					randGen = new Random();
					numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
					//numbersAndLetters = ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
					}
				}
			}
		char [] randBuffer = new char[length];
		for (int i=0; i<randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
			//randBuffer[i] = numbersAndLetters[randGen.nextInt(35)];
			}
		return new String(randBuffer);
	}
	
	public static final String GenerateRandStr(int length) 
	{
		if (length < 1) {
			return null;
		}
		StringBuffer generateRandStr = new StringBuffer(); 
        Random rand = new Random(); 
        char ch;
        for(int i=0;i<length;i++) 
        { 
            //randNum = Math.abs(rand.nextInt()) % 10 + 48 ; // 产生48到57的随机数(0-9的键位值) 
        	int randNum = Math.abs(rand.nextInt())%26+97; // 产生97到122的随机数(a-z的键位值) 
            ch = ( char ) randNum;
            generateRandStr.append( ch );
        } 
        return generateRandStr.toString(); 
	}
}
