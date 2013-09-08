import org.newdawn.slick.*;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Font;


public class BastionPaper extends BasicGame {

	public Paper paper;

	public BastionPaper() {
		super("Bastion Paper");
	}

	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new BastionPaper());
			// set fullscreen of native resolution
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = (int)screenSize.getWidth();
			int height = (int)screenSize.getHeight();
			app.setDisplayMode(width, height, true);
			app.setDisplayMode((int)(width/1.2), (int)(height/1.2), false);
			app.setTargetFrameRate(120);
			app.start();
		}
		catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		paper = new Paper("source.txt", container);
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		Input input = container.getInput();

		paper.update(container, delta);
		if (input.isKeyPressed(Input.KEY_RIGHT))
			paper.turnPage();
	}

	public void render(GameContainer container, Graphics g) throws SlickException {
		paper.render(container, g);
	}

}