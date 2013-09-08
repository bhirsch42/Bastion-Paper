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

	public void advancePage() {
		currentPage++;
		if (currentPage > text.length-1)
			currentPage = text.length-1;
	}

	public int numPages() {
		return text.length;
	}

	private boolean turningPage = false;

	public void turnPage() {
		turningPage = true;
	}

	private float pageTurnStep = 0;
	private boolean haveAdvancedPage = false;

	public void update(GameContainer container, int delta) {
		Input input = container.getInput();
		// drop in words around cursor
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

		// turn page
		if (turningPage) {
			pageTurnStep += delta; //!!!
			if (pageTurnStep < 1000.0f) {
				this.setShift(-(pageTurnStep/1000.0f)*container.getWidth(), 0.0f);
			}
			else if (!haveAdvancedPage) {
				advancePage();
				haveAdvancedPage = true;
				setShift((float)container.getWidth(), 0.0f);
			}
			if (pageTurnStep >= 1000.0f && pageTurnStep < 2000.0f) {
				float p = pageTurnStep - 1000.0f;
				this.setShift(((1000.0f-p)/1000.0f)*container.getWidth(), 0.0f);
			}
			else if (pageTurnStep >= 2000.0f) {
				this.setShift(0.0f, 0.0f);
				pageTurnStep = 0;
				turningPage = false;
			}

		}
	}

	public void shift(float x, float y) {
		for (Word[] line : text[currentPage]) {
			for (Word word : line) {
				word.translate(x, y);
			}
		}
	}

	public void setShift(float x, float y) {
		for (Word[] line : text[currentPage]) {
			for (Word word : line) {
				word.setTranslation(x, y);
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