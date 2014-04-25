package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import utils.MathUtils;

public class SpaceMouseAdapter extends MouseAdapter {
	private boolean isDragging = false;
	private int dragStartX = 0;
	private int dragStartY = 0;
	private int dragEndX = 0;
	private int dragEndY = 0;
	private int offsetY = 0;
	private int offsetX = 0;

	private double dx = 0;
	private double dy = 0;

	private static final double FRICTION = 0.95;

	public int getOffsetX() {
		if (isDragging) {
			return offsetX + dragStartX - dragEndX;
		}
		
		return offsetX;
	}

	public void setOffset(double x, double y) {
		offsetX = MathUtils.makeInt(x);
		offsetY = MathUtils.makeInt(y);
	}

	public int getOffsetY() {
		if (isDragging) {
			return offsetY + dragStartY - dragEndY;
		}
		return offsetY;
	}

	public void updateScreenMomentum() {
		if(!isDragging) {
			offsetX += MathUtils.makeInt(dx);
			offsetY += MathUtils.makeInt(dy);

			dx *= FRICTION;
			dy *= FRICTION;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		dragStartX = e.getX();
		dragStartY = e.getY();
		// also do end so that this gets set even when we don't drag
		dragEndX = e.getX();
		dragEndY = e.getY();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		isDragging = true;

		dx = dragEndX - e.getX();
		dy = dragEndY - e.getY();

		dragEndX = e.getX();
		dragEndY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		offsetX += dragStartX - dragEndX;
		offsetY += dragStartY - dragEndY;

		isDragging = false;
	}
}
