import java.awt.*;
import java.awt.font.FontRenderContext;
import java.util.ArrayList;
import java.awt.geom.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.*;

public class WordConverter {

	public static void initCanvas() {
		StdDraw.setCanvasSize(700, 170);
		StdDraw.setPenColor(new Color(255,255,255));
		StdDraw.setFont(new Font("Arial", Font.PLAIN, 100));
	}

	public static void main(String[] args) {
		initCanvas();

		int startingWord = Integer.parseInt(args[0]);

		In in = new In("source.txt");
		String source = in.readAll();

		// line splitter, inserts <br>'s
		int maxCols = 64;
		String[] paragraphs = source.split("<p>");
		for (int i = 0; i < paragraphs.length; i++) {
			boolean jumpMade = false;
			int cols = 0;
			for (int j = 0; j < paragraphs[i].length(); j++) {
				if (!jumpMade) {
					j += maxCols;
					jumpMade = true;
				}
				if (j >= paragraphs[i].length())
					j = paragraphs[i].length()-1;
				if (jumpMade && paragraphs[i].charAt(j) == ' ') {
					String beg = paragraphs[i].substring(0, j);
					String end = paragraphs[i].substring(j+1);
					paragraphs[i] = beg + "<br>" + end;
					jumpMade = false;
				}
			}
		}

		// image generator
		int wordCounter = 0;
		System.out.println(source);
		for (int i = 0; i < paragraphs.length; i++) {
			String[] lines = paragraphs[i].split("<br>");
			for (int j = 0; j < lines.length; j++) {
				String[] words = lines[j].split("\\s+");
				for (int k = 0; k < words.length; k++) {
					if (wordCounter >= startingWord) {
						String filename = "p" + i + "l" + j + "w" + k+ ".png";
						generateImage(words[k], filename);
					}
					System.out.println(wordCounter++);
				}
			}
		}

	}

	public static Color black = new Color(0, 0, 0);
	public static Color white = new Color(255, 255, 255);
	public static Color blank = new java.awt.Color(0,0,100,0);

	public static void generateImage(String text, String filename) {
		StdDraw.clear(black);
		StdDraw.textLeft(0.0, 0.5, text);
		StdDraw.show();
		StdDraw.save(filename);
		Picture pic = new Picture(filename);
		int newWidth = 0;
		for (int i = pic.width()-1; i > 0; i--) {
			for (int j = 0; j < pic.height(); j++) {
				if (!pic.get(i,j).equals(black)) {
					newWidth = i;
					break;
				}
			}
			if (newWidth != 0)
				break;
		}

		// create image that supports transparancy
		BufferedImage onscreenImage  = new BufferedImage(newWidth, 170, BufferedImage.TYPE_INT_ARGB);
		File file = new File(filename);
		try { ImageIO.write(onscreenImage, "png", file); }
		catch (IOException e) { e.printStackTrace(); }

		// copy image to properly sized image while converting black to blank
		Picture finalPic = new Picture(filename);
		for (int i = 0; i < finalPic.width(); i++) {
			for (int j = 0; j < finalPic.height(); j++) {
				if (pic.get(i,j).equals(black)) {
					finalPic.set(i, j, blank);
					// System.out.println(blank.getAlpha() + "   " + finalPic.get(i,j).getAlpha());
				}
				else {
					finalPic.set(i, j, pic.get(i,j));
				}
			}
		}
		finalPic.save(filename);
	}

	/*
	public static void generateImage(String text, String filename) {
		StdDraw.setCanvasSize(700, 170);
		StdDraw.setPenColor(new Color(255,255,255));
		StdDraw.setFont(new Font("Arial", Font.PLAIN, 100));

		StdDraw.clear(new Color(0,0,0));
		StdDraw.textLeft(0.0, 0.5, text);
		StdDraw.show();
		StdDraw.save(filename);

		Picture pic = new Picture(filename);
		Color color = new Color(255,255,255);
		boolean done = false;
		for (int i = pic.width()-1; i > 0; i--) {
			for (int j = 0; j < pic.height(); j++) {
				if (pic.get(i,j).equals(color)) {
					StdDraw.setCanvasSize(i, 170);
					StdDraw.setPenColor(new Color(255,255,255));
					StdDraw.setFont(new Font("Arial", Font.PLAIN, 100));
					StdDraw.clear(new Color(0,0,0));
					StdDraw.textLeft(0.0, 0.5, text);
					StdDraw.show();
					done = true;
					break;
				}
			}
			if (done)
				break;
		}

		StdDraw.clear(new Color(0,0,0));
		StdDraw.textLeft(.0, .5, text);
		StdDraw.save(filename);

		pic = new Picture(filename);
		color = new Color(255,255,255);
		Color blank = new Color(0,0,0,0);
		Color black = new Color(0,0,0);

		for (int i = 0; i < pic.width(); i++) {
			for (int j = 0; j < pic.height(); j++) {
				if (pic.get(i,j).equals(black))
					pic.set(i,j,blank);
			}
		}				
		pic.save(filename);


	}
	*/
}