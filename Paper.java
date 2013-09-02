import org.newdawn.slick.*;

public class Paper {
	Word[][][] text;
	int currentPage = 0;

	public static final float SPACE = 10.0f;
	public static final float LINE = 50.0f;
	public static final float ACTIVATE_DISTANCE = 50.0f;

	public Paper(String src, GameContainer container) {

		// read in text source file that corresponds to the PNGs
		In in = new In(src);
		String source = in.readAll();
		// create a Word object for each word in the paper,
		// store in 3-dimensional array
		String[] paragraphs = source.split("<p>");
		text = new Word[paragraphs.length][][];
		for (int i = 0; i < paragraphs.length; i++) {
			String[] lines = paragraphs[i].split("<br>");
			text[i] = new Word[lines.length][];
			for (int j = 0; j < lines.length; j++) {
				String[] words = lines[j].split("\\s+");
				text[i][j] = new Word[words.length];
				for (int k = 0; k < words.length; k++) {
					String filename = "p" + i + "k" + j + "w" + k+ ".png";
					text[i][j][k] = new Word(words[k], filename);
				}
			}
		}
		// calculate positions of all words while calculating
		// the width and height of the paragraph's bounds to
		// center the paragraph by later
		for (int i = 0; i < text.length; i++) {
			float height = 0.0f;
			float width = 0.0f;
			float x = 0.0f;
			float y = 0.0f;

			Word[][] paragraph = text[i];
			for (int j = 0; j < paragraph.length; j++) {

				Word[] line = paragraph[j];
				for (int k = 0; k < line.length; k++) {
					Word word = line[k];
					word.setPos(x, y);
					x += word.getWidth() + SPACE;
					if (x > width) width = x;
				}
				x = 0.0f;
				height = y;
				y += LINE;
			}
			// shift paragraph to center
			float shiftX = (container.getWidth()-width)/2.0f;
			float shiftY = (container.getHeight()-height)/2.0f;

			for (int j = 0; j < paragraph.length; j++) {
				Word[] line = paragraph[j];
				for (int k = 0; k < line.length; k++) {
					Word word = line[k];
					word.shift(shiftX, shiftY);

				}
			}
		}

	}



	public void update(GameContainer container, int delta) {
		Input input = container.getInput();
		for (Word[] line : text[currentPage]) {
			for (Word word : line) {
				float mx = (float)input.getMouseX();
				float my = (float)input.getMouseY();
				float cx = word.getCenterX();
				float cy = word.getCenterY();
				float dist = (float)Math.sqrt((mx-cx)*(mx-cx)+(my-cy)*(my-cy));
				if (dist <= ACTIVATE_DISTANCE)
					word.activate(true);
				word.update(container, delta);
			}
		}
	}

	public void render(GameContainer container, Graphics g) {
		Word[][] paragraph = text[currentPage];
		for (Word[] line : paragraph) {
			for (Word word : line) {
				word.render(container, g);
			}
		}
	}

}