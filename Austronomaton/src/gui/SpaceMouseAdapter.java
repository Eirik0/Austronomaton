package gui;

import java.awt.event.*;

import utils.MathUtils;

public class SpaceMouseAdapter extends MouseAdapter {
	private final double FRICTION = 0.95;

	private boolean isDragging = false;

	private int dragStartX = 0;
	private int dragStartY = 0;

	private int dragEndX = 0;
	private int dragEndY = 0;

	// The offset represents how far we are from the origin
	private int offsetY = 0;
	private int offsetX = 0;

	private double dx = 0;
	private double dy = 0;

	Austronomaton astro;

	SpaceMouseAdapter(Austronomaton astro) {
		this.astro = astro;
	}

	public int getOffsetX() {
		if (isDragging) {
			return offsetX + dragStartX - dragEndX;
		}

		return offsetX;
	}

	public int getOffsetY() {
		if (isDragging) {
			return offsetY + dragStartY - dragEndY;
		}
		return offsetY;
	}

	public void updateScreenMomentum() {
		if (!isDragging) {
			offsetX += MathUtils.makeInt(dx);
			offsetY += MathUtils.makeInt(dy);

			dx *= FRICTION;
			dy *= FRICTION;
		}
	}

	public void stopDragging() {
		offsetX += dragStartX - dragEndX;
		offsetY += dragStartY - dragEndY;

		dragStartX = 0;
		dragStartY = 0;

		dragEndX = 0;
		dragEndY = 0;

		isDragging = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!astro.isPaused) {
			dragStartX = e.getX();
			dragStartY = e.getY();
			// also do end so that this gets set even when we don't drag
			dragEndX = e.getX();
			dragEndY = e.getY();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (astro.isPaused) {
			astro.handleMouseReleased();
		} else {
			stopDragging();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		astro.setMouseXY(e.getX(), e.getY());

		if (!astro.isPaused) {
			isDragging = true;

			dx = dragEndX - e.getX();
			dy = dragEndY - e.getY();

			dragEndX = e.getX();
			dragEndY = e.getY();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		astro.setMouseXY(e.getX(), e.getY());
	}

}
