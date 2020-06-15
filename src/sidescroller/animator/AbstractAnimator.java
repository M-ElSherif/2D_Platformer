package sidescroller.animator;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sidescroller.entity.FpsCounter;
import sidescroller.entity.Grid;
import sidescroller.entity.property.Drawable;
import sidescroller.scene.MapSceneInterface;
import utility.Tuple;

public abstract class AbstractAnimator extends javafx.animation.AnimationTimer implements AnimatorInterface {

	protected MapSceneInterface map;
	protected Tuple mouse;
	private Canvas canvas;
	private FpsCounter fps;
	private Grid grid;

	public AbstractAnimator() {
		this.mouse = new Tuple();
		this.fps = new FpsCounter(10, 25);
		Drawable<?> fpsSprite = this.fps.getDrawable();
		fpsSprite.setFill(Color.BLACK).setStroke(Color.WHITE).setWidth(1);
	}

	@Override
	public void clearAndFill(GraphicsContext gc, Color background) {
		gc.setFill(background);
		gc.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
	
	@Override
	public void handle(long now) {
		// get the grpahic context object
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		// if we can draw FPS in the map, calculate the fps
		if (map.getDrawFPS() ) {
			fps.calculateFPS(now);
		}
		
		// Call handle method and insert graphics context gc in argument
		this.handle(gc, now);
		
		// if map provides a grid
		if (map.getDrawGrid()) {
			// if grid is null, create one
			if (grid == null) {
				this.grid = new Grid(map.getGridCount(), canvas.getWidth(), canvas.getHeight());
				Drawable<?> gridSprite = this.grid.getDrawable();
				gridSprite.setStroke(Color.BLACK)
				.setWidth(1)
				.setScale(map.getScale())
				.setTileSize(map.getGridSize());
			}
			this.grid.getDrawable().draw(gc);
		}
		
		if (map.getDrawFPS()) {
			this.fps.getDrawable().draw(gc);
		}
		
	}
	
	@Override
	public void handle(GraphicsContext gc, long now) {
	}
	
	@Override
	public void setMapScene(MapSceneInterface map) {
		this.map = map;
	}
	
	@Override
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

}
