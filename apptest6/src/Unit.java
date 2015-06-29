package com.example.apptest6;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;

class Unit {
	int unittype; // general, vehicle, background, road
	// int drawlayer;
	Drawable image;

	protected double angle;
	protected double x, y, width, height;
	boolean moves;
	boolean collides;

	// protected double speed;
	// Movement movement;

	public Unit() {
	}

	public Unit(boolean col, boolean movs, double px, double py, double wid,
			double hgt, double ang, Drawable img) {
		collides = col;
		moves = movs;
		x = px;
		y = py;
		width = wid;
		height = hgt;
		image = img;
		angle = ang;
		unittype = 0;
	}

	public Unit(boolean col, boolean movs, double px, double py, double wid,
			double hgt, double ang, Drawable img, int utype) {
		collides = col;
		moves = movs;
		x = px;
		y = py;
		width = wid;
		height = hgt;
		image = img;
		angle = ang;
		unittype = utype;
	}

	public boolean collidingWith(Unit u) {
		return collidingWith(u.getX(), u.getY(), u.getWidth(), u.getHeight(),
				u.getAngle(), u);
	}

	public boolean collidingWith(double x, double y, double w, double h,
			double a, Unit u) {
		ArrayList<Point> rectangleAxis = new ArrayList<Point>();
		rectangleAxis.add(subtractVectors(getUpperRightCorner(),
				getUpperLeftCorner()));
		rectangleAxis.add(subtractVectors(getUpperRightCorner(),
				getLowerRightCorner()));
		rectangleAxis.add(subtractVectors(u.getUpperLeftCorner(),
				u.getLowerLeftCorner()));
		rectangleAxis.add(subtractVectors(u.getUpperLeftCorner(),
				u.getUpperRightCorner()));

		for (Point axis : rectangleAxis) {
			if (!isAxisCollision(u, axis)) {
				return false;
			}
		}

		return true;
	}

	private boolean isAxisCollision(Unit unit, Point aAxis) {
		ArrayList<Integer> otherUnitScalars = new ArrayList<Integer>();
		otherUnitScalars.add(generateScalar(unit.getUpperLeftCorner(), aAxis));
		otherUnitScalars.add(generateScalar(unit.getUpperRightCorner(), aAxis));
		otherUnitScalars.add(generateScalar(unit.getLowerLeftCorner(), aAxis));
		otherUnitScalars.add(generateScalar(unit.getLowerRightCorner(), aAxis));

		ArrayList<Integer> thisUnitScalars = new ArrayList<Integer>();
		thisUnitScalars.add(generateScalar(getUpperLeftCorner(), aAxis));
		thisUnitScalars.add(generateScalar(getUpperRightCorner(), aAxis));
		thisUnitScalars.add(generateScalar(getLowerLeftCorner(), aAxis));
		thisUnitScalars.add(generateScalar(getLowerRightCorner(), aAxis));

		int otherUnitMinimum = getMinimum(otherUnitScalars);
		int otherUnitMaximum = getMaximum(otherUnitScalars);
		int thisUnitMinimum = getMinimum(thisUnitScalars);
		int thisUnitMaximum = getMaximum(thisUnitScalars);

		if (thisUnitMinimum <= otherUnitMaximum
				&& thisUnitMaximum >= otherUnitMaximum) {
			return true;
		} else if (otherUnitMinimum <= thisUnitMaximum
				&& otherUnitMaximum >= thisUnitMaximum) {
			return true;
		}
		return false;
	}

	private int generateScalar(Point theRectangleCorner, Point theAxis) {
		// projvU = ((u * v) / (v^2)) * v
		double numerator = (theRectangleCorner.x * theAxis.x)
				+ (theRectangleCorner.y * theAxis.y);
		double denominator = (theAxis.x * theAxis.x) + (theAxis.y * theAxis.y);
		double result = numerator / denominator;
		Point proj = new Point((int) (result * theAxis.x),
				(int) (result * theAxis.y));

		double scalar = (theAxis.x * proj.x) + (theAxis.y * proj.y);
		return (int) scalar;
	}

	private Point rotatePoint(Point thePoint, Point theOrigin,
			double theRotation) {
		Point aTranslatedPoint = new Point();
		aTranslatedPoint.x = (int) (theOrigin.x + (thePoint.x - theOrigin.x)
				* Math.cos(theRotation) - (thePoint.y - theOrigin.y)
				* Math.sin(theRotation));
		aTranslatedPoint.y = (int) (theOrigin.y + (thePoint.y - theOrigin.y)
				* Math.cos(theRotation) + (thePoint.x - theOrigin.x)
				* Math.sin(theRotation));
		return aTranslatedPoint;
	}

	public Point getUpperLeftCorner() {
		Point aUpperLeft = new Point((int) x, (int) y);
		aUpperLeft = rotatePoint(aUpperLeft,
				addVectors(aUpperLeft, getOrigin()), Math.toRadians(angle));
		return aUpperLeft;
	}

	public Point getUpperRightCorner() {
		Point aUpperRight = new Point((int) x + (int) width, (int) y);
		aUpperRight = rotatePoint(
				aUpperRight,
				addVectors(aUpperRight, new Point((int) -getOriginX(),
						(int) getOriginY())), Math.toRadians(angle));
		return aUpperRight;
	}

	public Point getLowerLeftCorner() {
		Point aLowerLeft = new Point((int) x, (int) y + (int) height);
		aLowerLeft = rotatePoint(
				aLowerLeft,
				addVectors(aLowerLeft, new Point((int) getOriginX(),
						(int) -getOriginY())), Math.toRadians(angle));
		return aLowerLeft;
	}

	public Point getLowerRightCorner() {
		Point aLowerRight = new Point((int) x + (int) width, (int) y
				+ (int) height);
		aLowerRight = rotatePoint(
				aLowerRight,
				addVectors(aLowerRight, new Point((int) -getOriginX(),
						(int) -getOriginY())), Math.toRadians(angle));
		return aLowerRight;
	}

	private Point addVectors(Point a, Point b) {
		return new Point(a.x + b.x, a.y + b.y);
	}

	private Point subtractVectors(Point a, Point b) {
		return new Point(a.x - b.x, a.y - b.y);
	}

	private int getMaximum(ArrayList<Integer> a) {
		int max = a.get(0);
		for (int i = 0; i < 4; i++) {
			if (a.get(i) > max) {
				max = a.get(i);
			}
		}
		return max;
	}

	private int getMinimum(ArrayList<Integer> a) {
		int min = a.get(0);
		for (int i = 0; i < 4; i++) {
			if (a.get(i) < min) {
				min = a.get(i);
			}
		}
		return min;
	}

	public double getOriginX() {
		return width / 2;
	}

	public double getOriginY() {
		return height / 2;
	}

	Point getOrigin() {
		return new Point((int) width / 2, (int) height / 2);
	}

	Point getGlobalOrigin() {
		return new Point((int) (x + getOriginX()), (int) (y + getOriginY()));
	}

	protected void setUnitType(int s) {
		unittype = s;
	}

	public boolean getCollides() {
		return collides;
	}

	public int getUnitType() {
		return unittype;
	}

	public double getY() {
		return y;
	}

	public double getX() {
		return x;
	}

	public double getAngle() {
		return angle;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public boolean getMoves() {
		return moves;
	}

	public Drawable getImage() {
		return image;
	}

	public void setX(double xx) {
		x = xx;
	}

	public void setY(double yy) {
		y = yy;
	}

	public void setPosition(double xx, double yy) {
		x = xx;
		y = yy;
	}

	public void setAngle(double a) {
		angle = a;
	}

}
