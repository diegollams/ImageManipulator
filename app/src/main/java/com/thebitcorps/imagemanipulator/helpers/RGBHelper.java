package com.thebitcorps.imagemanipulator.helpers;

import android.graphics.Color;

/**
 * Created by diegollams on 2/6/16.
 */
public class RGBHelper {

	public int color;
	public RGBHelper(int color) {
		this.color = color;

	}

//	public int getColor() {
//		return color;
//	}
//
//	public void setRed(int red){
//		this.color =  RGBHelper.createPixel(red,getGreen(),getBlue(),getAlpha());
//	}
//	public void setGreen(int green){
//		this.color =  RGBHelper.createPixel(getRed(),green,getBlue(),getAlpha());
//	}
//	public void setBlue(int blue){
//		this.color =  RGBHelper.createPixel(getRed(),getGreen(),blue,getAlpha());
//	}

	public int getBlue() {
		return RGBHelper.getBlue(color);
	}

	public int getRed() {
		return RGBHelper.getRed(color);
	}

	public int getGreen() {
		return RGBHelper.getBlue(color);
	}
	public int getAlpha(){
		return RGBHelper.getAlpha(color);
	}

	public static int getBetterGrayScaleColor(int color){
//		sqrt( 0.299*R^2 + 0.587*G^2 + 0.114*B^2 )
		int gray = (int) (0.2126*RGBHelper.getRed(color) +  0.7152*RGBHelper.getGreen(color) + 0.0722*RGBHelper.getBlue(color));
		return RGBHelper.createPixel(gray, gray, gray, gray);

	}
	public static int getGrayScaleColor(int color){
		int gray = (int) (RGBHelper.getRed(color) + RGBHelper.getGreen(color) + RGBHelper.getBlue(color) / 3);
		return RGBHelper.createPixel(gray, gray, gray, gray);

	}

	public static int createPixel(int red,int green,int blue,int alpha){
		return (alpha << 24) | (red << 16) | (green << 8) | blue;
	}
	public static int getRed(int color){
		return color >> 16 & 0xFF;
	}
	public static int getBlue(int color){
		return color & 0xFF;
	}
	public static int getGreen(int color){
		return color >> 8 & 0xFF;
	}
	public static int getAlpha(int color){
		return color >>> 24;
	}
}
