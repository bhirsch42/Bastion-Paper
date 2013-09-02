import java.awt.*;
import java.awt.font.FontRenderContext;
import java.util.ArrayList;
import java.awt.geom.*;
public class WordConverter {

	public static void initCanvas() {
		StdDraw.setCanvasSize(700, 170);

	}

	public static void main(String[] args) {
		initCanvas();

		In in = new In("source.txt");
		String source = in.readAll();

		String[] paragraphs = source.split("<p>");
		for (int i = 0; i < paragraphs.length; i++) {
			String[] lines = paragraphs[i].split("<br>");
			for (int j = 0; j < lines.length; j++) {
				String[] words = lines[j].split("\\s+");
				for (int k = 0; k < words.length; k++) {
					String filename = "p" + i + "k" + j + "w" + k+ ".png";
					generateImage(words[k], filename);
					System.out.println(StdDraw.mouseX() + " " + StdDraw.mouseY());
				}
			}
		}

	}

	public static void generateImage(String text, String filename) {
		createOriginal(text, filename);
	}

	public static void createOriginal(String text, String filename) {
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
}