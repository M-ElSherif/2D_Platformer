package sidescroller.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import javafx.scene.canvas.Canvas;
import sidescroller.entity.GenericEntity;
import sidescroller.entity.property.Entity;
import sidescroller.entity.property.HitBox;
import sidescroller.entity.sprite.BackgroundSprite;
import sidescroller.entity.sprite.ItemSprite;
import sidescroller.entity.sprite.LandSprite;
import sidescroller.entity.sprite.PlatformSprite;
import sidescroller.entity.sprite.SpriteFactory;
import sidescroller.entity.sprite.TreeSprite;
import sidescroller.entity.sprite.tile.Tile;
import utility.Tuple;

public class MapBuilder implements MapBuilderInterface {

	private Tuple rowColCount;
	private Tuple dimension;
	private double scale;
	private Canvas canvas;
	private Entity background;
	private List<Entity> landMass;
	private List<Entity> other;

	protected MapBuilder() {
		this.landMass = new ArrayList<>();
		this.other = new ArrayList<Entity>();
	}

	public static MapBuilder createBuilder() {
		return new MapBuilder();
	}

	@Override
	public MapBuilder setCanvas(Canvas canvas) {
		this.canvas = canvas;
		return this;
	}

	@Override
	public MapBuilder setGrid(Tuple rowColCount, Tuple dimension) {
		this.rowColCount = rowColCount;
		this.dimension = dimension;
		return this;
	}

	@Override
	public MapBuilder setGridScale(double scale) {
		this.scale = scale;
		return this;
	}

	@Override
	public MapBuilder buildBackground(BiFunction<Integer, Integer, Tile> callback) {
		BackgroundSprite backGroundSprite = SpriteFactory.get("Background");
		backGroundSprite.init(scale, dimension, Tuple.pair(0, 0));
		backGroundSprite.createSnapshot(canvas, rowColCount, callback);
		HitBox hitBox = HitBox.build(0, 0, scale * dimension.x() * rowColCount.y(), scale * dimension.y() * rowColCount.x());
		this.background = new GenericEntity(backGroundSprite, hitBox);
		return this;
	}

	@Override
	public MapBuilder buildLandMass(int rowPos, int colPos, int rowConut, int colCount) {
		LandSprite landSprite = SpriteFactory.get("Land");
		landSprite.init(scale, dimension, Tuple.pair(colPos, rowPos));
		landSprite.createSnapshot(canvas, rowConut, colCount);
		HitBox hitBox = HitBox.build(colPos * dimension.x() * scale, rowPos * dimension.y() * scale, scale * dimension.x() * colCount, scale * dimension.y() * rowConut);
		this.landMass.add(new GenericEntity(landSprite, hitBox));
		return this;
	}

	@Override
	public MapBuilder buildTree(int rowPos, int colPos, Tile tile) {
		TreeSprite treeSprite = SpriteFactory.get("Tree");
		treeSprite.init(scale, dimension, Tuple.pair(colPos, rowPos));
		treeSprite.createSnapshot(canvas, tile);
		this.other.add(new GenericEntity(treeSprite, null)); // TODO is hitbox null here
		return this;
	}

	@Override
	public MapBuilder buildPlatform(int rowPos, int colPos, int length, Tile tile) {
		PlatformSprite platformSprite = SpriteFactory.get("Platform");
		platformSprite.init(scale, dimension, Tuple.pair(colPos, rowPos));
		platformSprite.createSnapshot(canvas, tile, length);
		HitBox hitBox = HitBox.build((colPos + .5) * dimension.x() * scale, rowPos * dimension.y() * scale,
				scale * dimension.x() * (length - 1), scale * dimension.y() / 2);
		this.other.add(new GenericEntity(platformSprite, hitBox));
		return this;
	}
	
	@Override
	public MapBuilder buildItem(int rowPos, int colPos, Tile tile) {
		ItemSprite itemSprite = SpriteFactory.get("Item");
		itemSprite.init(scale, dimension, Tuple.pair(colPos, rowPos));
		itemSprite.createSnapshot(canvas, tile);
		this.other.add(new GenericEntity(itemSprite, null)); // TODO is hitbox null here
		return this;
	}
	

	@Override
	public Entity getBackground() {
		return this.background;
	}

	@Override
	public List<Entity> getEntities(List<Entity> list) {
		if (list == null) {
			throw new NullPointerException();
		} else {
				list.addAll(this.landMass);
				list.addAll(this.other);
			return list;
		}
	}



}
