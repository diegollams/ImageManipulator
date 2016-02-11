package com.thebitcorps.imagemanipulator.helpers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by diegollams on 2/6/16.
 */
public class BitmapTrasformer {

	public static final int HALF_PIXEL_VALUE = 125;
	public static final int MAX_PIXEL_VALUE = 255;
	public static final int MIN_PIXEL_VALUE = 0;
	public static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
														 int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);


		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}


	public static Bitmap getBitmapFromUri(Uri uri,int reqWidth, int reqHeight){
		File file = new File(uri.getPath());
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), getOptions(reqWidth, reqHeight));
		return bitmap;
	}

	/**
	 *
	 * @param reqWidth
	 * @param reqHeight
	 * @return {@link android.graphics.BitmapFactory.Options } object with mutable
	 */
	@NonNull
	private static BitmapFactory.Options getOptions(int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
		options.inMutable  = true;
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		return options;
	}

	/**
	 * Method that decode a bitmap form a byte[] with dimension that fit reqHeight and reqWidth
	 * @param data the byte array the bitmap will be create
	 * @param reqWidth the max value of width that the bitmap can be
	 * @param reqHeight the max value of height that the bitmap can be
	 * @return a bitmap with dimension that fits the reqWidth and reqHeight
	 */
	public static Bitmap decodeSampledBitmapFromData(byte[] data
														 ,int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeByteArray(data, 0, data.length, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		options.inMutable = true;
		return BitmapFactory.decodeByteArray(data, 0, data.length, options);
	}

	/**
	 *
	 * @param value
	 * @return
	 */
	private static int boundPixelValue(int value){
		if(value > MAX_PIXEL_VALUE){
			return MAX_PIXEL_VALUE;
		}
		else if(value < MIN_PIXEL_VALUE){
			return MIN_PIXEL_VALUE;
		}
		else{
			return value;
		}

	}
	/**
	 * will transform bitmap so every pixel will be in grayscale form
	 * @param bitmap a bitmap will all channels in the same value
	 */
	public static void grayScale(Bitmap bitmap){
		for (int x = 0; x < bitmap.getWidth(); x++) {
			for (int y = 0; y < bitmap.getHeight(); y++) {
				int pixel = bitmap.getPixel(x,y);
				bitmap.setPixel(x, y,RGBHelper.getBetterGrayScaleColor(pixel));
			}
		}
	}

	/**
	 * will transform the bitmap so every pixel will be inverse, first will get the max value in the image for every  color channel
	 * then apply {@link #inverseMethod(Bitmap, int, int, int)} with the max value of every channel
	 * @param bitmap
	 */
	@Deprecated
	public static void inverseMaxCanal(Bitmap bitmap){
		int maxRed = 0,maxGreen = 0,maxBlue = 0;
		for (int x = 0; x < bitmap.getWidth(); x++) {
			for (int y = 0; y < bitmap.getHeight(); y++) {
				RGBHelper pixel = new RGBHelper(bitmap.getPixel(x,y));
				if(pixel.getRed() > maxRed) maxRed = pixel.getRed();
				if(pixel.getGreen() > maxGreen) maxGreen= pixel.getGreen();
				if(pixel.getBlue() > maxBlue) maxBlue = pixel.getBlue();
			}
		}
		inverseMethod(bitmap, maxRed, maxGreen, maxBlue);
	}

	/**
	 * get the max channel value and substract every pixel channel so we get the inverse of every pixel
	 * @param bitmap the bitmap that will be inverse
	 * @param maxRed value that the red channel of every pixel will be substract
	 * @param maxGreen value that the green channel of every pixel will be substract
	 * @param maxBlue value that the green channel of every pixel will be substract
	 */
	private static void inverseMethod(Bitmap bitmap, int maxRed, int maxGreen, int maxBlue) {
		for (int x = 0; x < bitmap.getWidth(); x++) {
			for (int y = 0; y < bitmap.getHeight(); y++) {
				RGBHelper pixel = new RGBHelper(bitmap.getPixel(x,y));
				int newPixel = RGBHelper.createPixel(maxRed - pixel.getRed(), maxGreen - pixel.getGreen(), maxBlue - pixel.getBlue(), pixel.getAlpha());
				bitmap.setPixel(x,y,newPixel);
			}
		}
	}

	/**
	 * will transform the bitmap so every pixel will be inverse,
	 * then apply {@link #inverseMethod(Bitmap, int, int, int)} with the {@value #MAX_PIXEL_VALUE} for  every channel
	 * @param bitmap the bitmap that will be covert
	 */
	public static void inverseNormal(Bitmap bitmap){
		inverseMethod(bitmap,MAX_PIXEL_VALUE,MAX_PIXEL_VALUE,MAX_PIXEL_VALUE);
	}

	/**
	 * transform bitmap to black and white pixels
	 * @param bitmap the bitmap that will be covert
	 */
	public static void binarization(Bitmap bitmap){
		for (int x = 0; x < bitmap.getWidth(); x++) {
			for (int y = 0; y < bitmap.getHeight(); y++) {
				int grayScalePixel = RGBHelper.getGrayScaleColor(bitmap.getPixel(x, y));
				if(RGBHelper.getRed(grayScalePixel) > HALF_PIXEL_VALUE){
					bitmap.setPixel(x,y,RGBHelper.createPixel(MAX_PIXEL_VALUE,MAX_PIXEL_VALUE,MAX_PIXEL_VALUE,MAX_PIXEL_VALUE));
				}
				else{
					bitmap.setPixel(x,y,RGBHelper.createPixel(MIN_PIXEL_VALUE,MIN_PIXEL_VALUE,MIN_PIXEL_VALUE,MIN_PIXEL_VALUE));
				}
			}
		}
	}

	/**
	 *
	 * @param bitmap
	 * @param brightness
	 */
	public static void changeBrightness(Bitmap bitmap,int brightness){
		for (int x = 0; x < bitmap.getWidth(); x++) {
			for (int y = 0; y < bitmap.getHeight(); y++) {
//				RGBHelper pixel = new RGBHelper(bitmap.getPixel(x, y));
				int pixel  = bitmap.getPixel(x,y);
				int red  = boundPixelValue(RGBHelper.getRed(pixel) + brightness);
				int green = boundPixelValue(RGBHelper.getGreen(pixel) + brightness);
				int blue = boundPixelValue(RGBHelper.getBlue(pixel) + brightness);
				bitmap.setPixel(x,y,RGBHelper.createPixel(red,green,blue,RGBHelper.getAlpha(pixel)));
			}
		}
	}

	/**
	 *
	 * @param bitmap
	 * @param contrast
	 */
	public static void changeContrast(Bitmap bitmap,float contrast){
		for (int x = 0; x < bitmap.getWidth(); x++) {
			for (int y = 0; y < bitmap.getHeight(); y++) {
//				RGBHelper pixel = new RGBHelper(bitmap.getPixel(x, y));
				int pixel  = bitmap.getPixel(x,y);
				int red  = boundPixelValue((int) (RGBHelper.getRed(pixel) * contrast));
				int green = boundPixelValue((int) (RGBHelper.getGreen(pixel) * contrast));
				int blue = boundPixelValue((int) (RGBHelper.getBlue(pixel) * contrast));
				bitmap.setPixel(x,y,RGBHelper.createPixel(red,green,blue,RGBHelper.getAlpha(pixel)));
			}
		}
	}

	public static void changeContrastBetter(Bitmap bitmap,int contrast){
		final float newContrast = (259 * (contrast + 255)) / (255 * (259 - contrast));
		for (int x = 0; x < bitmap.getWidth(); x++) {
			for (int y = 0; y < bitmap.getHeight(); y++) {
//				RGBHelper pixel = new RGBHelper(bitmap.getPixel(x, y));
				int pixel  = bitmap.getPixel(x,y);
				int red  = boundPixelValue((int) ((RGBHelper.getRed(pixel) * newContrast)-128) + 128);
				int green = boundPixelValue((int)((RGBHelper.getGreen(pixel) * newContrast)-128) + 128);
				int blue = boundPixelValue((int) ((RGBHelper.getBlue(pixel) * newContrast)-128) + 128);
				bitmap.setPixel(x,y,RGBHelper.createPixel(red,green,blue,RGBHelper.getAlpha(pixel)));
			}
		}
	}


}
