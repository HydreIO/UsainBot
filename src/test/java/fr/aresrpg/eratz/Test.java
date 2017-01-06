package fr.aresrpg.eratz;

import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.dofus.util.*;
import fr.aresrpg.dofus.util.Pathfinding.Node;

import java.util.Arrays;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Test extends Application {

	private final int width = 15;
	private final int height = 17;
	private final int cellsW = 479;
	private final int range = 12;
	private final int player = 92;
	Cell[] cells = new Cell[cellsW];

	@Override
	public void start(Stage stage) throws Exception {
		DofusMapView v = new DofusMapView();
		stage.setTitle("Hello World");
		/*
		 * stage.setScene(new Scene(v));
		 * //GDM|7554|0905131019|
		 * 50504d4065244a7a6c345c75562c5a3068215e395c487e75612532422d3330543730787e402a4a3776774657233a3e79303f3b3074742e227121657831584a253242406f786e6e226b5d6569236f30605c652252452f465524253235674e3a7d2e6c683a722933796b396c3c74332671347c2067753b40547a775c3d6f233e533241285c4050717e77482d4b7a3628362e433141682d655a694d283b4d6d4544532e7c305c76483767725e48
		 * DofusMap map = Maps.loadMap(
		 * SwfVariableExtractor.extractVariable(Maps.downloadMap(7554 , "0905131019")),
		 * "50504d4065244a7a6c345c75562c5a3068215e395c487e75612532422d3330543730787e402a4a3776774657233a3e79303f3b3074742e227121657831584a253242406f786e6e226b5d6569236f30605c652252452f465524253235674e3a7d2e6c683a722933796b396c3c74332671347c2067753b40547a775c3d6f233e533241285c4050717e77482d4b7a3628362e433141682d655a694d283b4d6d4544532e7c305c76483767725e48"
		 * );
		 * v.setMap(map);
		 * stage.setResizable(true);
		 * stage.show();
		 * System.out.println("Map " + map.getHeight() + " " + map.getWidth() + " " + map.getCells().length);
		 */
		for (int i = 0; i < cells.length; i++) {
			cells[i] = new Cell(false, false);
			if (Maps.distanceManathan(i, player, width, height) > range)
				cells[i].accesible = false;
		}
		cells[player] = new Cell(true, false);
		cells[93] = new Cell(false, true);
		cells[94] = new Cell(false, true);
		int ox = Maps.getXRotated(player, width, height);
		int oy = Maps.getYRotated(player, width, height);
		castShadow(Maps.getXRotated(93, width, height), Maps.getYRotated(93, width, height), ox, oy);
		castShadow(Maps.getXRotated(94, width, height), Maps.getYRotated(94, width, height), ox, oy);
		Canvas canvas1 = new Canvas();
		Canvas canvas2 = new Canvas();
		canvas1.setWidth(1200);
		canvas2.setWidth(1200);
		canvas1.setHeight(1200);
		canvas2.setHeight(1200);
		drawCell(canvas1.getGraphicsContext2D(), canvas2.getGraphicsContext2D(), cells);
		//drawRay(canvas1.getGraphicsContext2D());
		Group r = new Group();
		r.getChildren().add(canvas1);
		r.getChildren().add(canvas2);
		stage.setScene(new Scene(r));
		stage.show();
	}

	private void drawCell(GraphicsContext gc, GraphicsContext gid, Cell[] cells) {
		int leftUpCorner = 0; // left up corner
		int leftUpCornerX = Maps.getXRotated(leftUpCorner, width, height);
		int leftUpCornerY = Maps.getYRotated(leftUpCorner, width, height) + 1;
		leftUpCorner = Maps.getIdRotated(leftUpCornerX, leftUpCornerY, width, height);

		int rightUpCorner = width - 1;// right up corner
		int rightUpCornerX = Maps.getXRotated(rightUpCorner, width, height) - 1;
		int rightUpCornerY = Maps.getYRotated(rightUpCorner, width, height);
		rightUpCorner = Maps.getIdRotated(rightUpCornerX, rightUpCornerY, width, height);

		int rightDownCorner = cellsW - 1; // right down corner
		int rightDownCornerX = Maps.getXRotated(rightDownCorner, width, height);
		int rightDownCornerY = Maps.getYRotated(rightDownCorner, width, height) - 1;
		rightDownCorner = Maps.getIdRotated(rightDownCornerX, rightDownCornerY, width, height);

		int leftDownCorner = (cellsW - 1) - (width - 1); // left down corner
		int leftDownCornerX = Maps.getXRotated(leftDownCorner, width, height) + 1; // +1 car on veut la case d'a coté
		int leftDownCornerY = Maps.getYRotated(leftDownCorner, width, height);
		leftDownCorner = Maps.getIdRotated(leftDownCornerX, leftDownCornerY, width, height);

		int[] leftupnode = Arrays.stream(Pathfinding.getNeighbors(new Node(leftUpCornerX, leftUpCornerY))).filter(n -> Maps.isInMapRotated(n.getX(), n.getY(), width, height))
				.mapToInt(n -> Maps.getIdRotated(n.getX(), n.getY(), width, height)).toArray();
		int[] rightupnode = Arrays.stream(Pathfinding.getNeighbors(new Node(rightUpCornerX, rightUpCornerY))).filter(n -> {
			System.out.println("is in map " + n.getX() + "," + n.getY() + " = " + Maps.isInMapRotated(n.getX(), n.getY(), width, height));
			return Maps.isInMapRotated(n.getX(), n.getY(), width, height);
		})
				.mapToInt(n -> Maps.getIdRotated(n.getX(), n.getY(), width, height)).toArray();
		int[] rightdownnode = Arrays.stream(Pathfinding.getNeighbors(new Node(rightDownCornerX, rightDownCornerY))).filter(n -> Maps.isInMapRotated(n.getX(), n.getY(), width, height))
				.mapToInt(n -> Maps.getIdRotated(n.getX(), n.getY(), width, height)).toArray();
		int[] leftdownnode = Arrays.stream(Pathfinding.getNeighbors(new Node(leftDownCornerX, leftDownCornerY))).filter(n -> Maps.isInMapRotated(n.getX(), n.getY(), width, height))
				.mapToInt(n -> Maps.getIdRotated(n.getX(), n.getY(), width, height)).toArray();

		for (int i = 0; i < cells.length; i++) {
			Cell c = cells[i];
			if (c.obstacle)
				gc.setFill(Color.BLUEVIOLET);
			else if (c.player)
				gc.setFill(Color.AQUA);
			else if (c.accesible)
				gc.setFill(Color.GREEN);
			else
				gc.setFill(Color.LIGHTGRAY);
			int xe = Maps.getX(i, width);
			int ye = Maps.getY(i, width);

			//	if (i == leftUpCorner || ArrayUtils.contains(i, leftupnode)) gc.setFill(Color.RED);
			if (i == rightUpCorner || ArrayUtils.contains(i, rightupnode)) gc.setFill(Color.YELLOW);
			//	if (i == rightDownCorner || ArrayUtils.contains(i, rightdownnode)) gc.setFill(Color.BLUE);
			//	if (i == leftDownCorner || ArrayUtils.contains(i, leftdownnode)) gc.setFill(Color.BROWN);

			System.out.println(xe + " " + ye);
			System.out.println(i + " " + Maps.getIdRotated(xe, ye, width, height));
			double xp = xe * 25;
			double yp = ye * 25;
			//gc.fillRect(xp, yp, 29, 29);

			gc.fillPolygon(new double[] { xp, xp + 25, xp, xp - 25 },
					new double[] { yp + 25, yp, yp - 25, yp }, 4);

			//gid.fillText(i + "", xp, yp + gc.getFont().getSize() / 3);
			gid.fillText(Maps.getXRotated(i, width, height) + "," + Maps.getYRotated(i, width, height) + "", xp, yp + 16);
			gid.setFill(Color.RED);
			gid.fillText(i + "", xp, yp + 32);
			gid.setFill(Color.BLACK);

		}
	}

	private int getNeighborsTeleporters(int w, int h, int cellid, int cellX, int cellY) {
		Node[] neighbors = Pathfinding.getNeighbors(new Node(cellX, cellY));
		for (Node n : neighbors) {
			if (!Maps.isInMapRotated(n.getX(), n.getY(), w, h)) continue;
			int idRotated = Maps.getIdRotated(n.getX(), n.getY(), w, h);
		}

		return cellid;
	}

	void castShadow(int px, int py, int ox, int oy) {
		int x = px - ox;
		int y = py - oy;

		/** Absolute Values **/
		int ax = x;
		if (ax < 0) ax = -ax;

		int ay = y;
		if (ay < 0) ay = -ay;

		int flipX = 0;
		if (x != 0) flipX = x / ax;

		int flipY = 0;
		if (y != 0) flipY = y / ay;

		/** Min Max Slopes **/
		double slope1 = getSlope((ax - 0.5), (ay + 0.5));
		double slope2 = getSlope((ax + 0.5), (ay - 0.5));

		System.out.println("Slopes : " + slope1 + " " + slope2);

		boolean flag = true;

		for (int cy = ay; cy < range; cy++) {
			for (int cx = ax; cx < range; cx++) {
				if ((cx != x || cy != y) && (cx * cx + cy * cy) <= range * range) {
					/** Skip main cell */
					double slope = getSlope(cx, cy);

					if ((slope > slope1) && (slope < slope2 || (slope2 < 0 && cx > x))) {
						if (!flag) {
							ax = cx;
							flag = true;
						}

						mirrorHide(cx, cy, ox, oy, flipX, flipY);
					} else {
						if (flag) {
							break;
						}
					}

				}
			}
			if (flag) {
				flag = false;
			} else {
				break;
			}

		}
	}

	void mirrorHide(int cx, int cy, int ox, int oy, int flipX, int flipY) {
		if (flipX >= 0) {
			mirrorHide(cx, cy, ox, oy, flipY);
		}
		if (flipX <= 0) {
			mirrorHide(-cx, cy, ox, oy, flipY);
		}
	}

	void mirrorHide(int cx, int cy, int ox, int oy, int flipY) {
		if (flipY >= 0) {
			shadow(cx + ox, cy + oy);
		}
		if (flipY <= 0) {
			shadow(cx + ox, -cy + oy);
		}
	}

	Cell getCell(int x, int y) {
		if (x > height + width || y > height + width)
			return null;
		int id = Maps.getIdRotated(x, y, width, height);
		if (Maps.isInMapRotated(x, y, width, height))
			return cells[id];
		else
			return null;
	}

	void shadow(int x, int y) {
		Cell cell = getCell(x, y);
		if (cell != null)
			cell.accesible = false;
	}

	double getSlope(double a, double b) {
		if (b == 0) { return 99; }
		return a / b;
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	public class Cell {
		boolean player;
		boolean obstacle;
		boolean accesible = true;

		public Cell(boolean player, boolean obstacle) {
			this.player = player;
			this.obstacle = obstacle;
		}
	}

}