package com.example.apptest6;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.Log;

public class World {
	final int ROAD_WIDTH = 135;
	final int ROAD_LENGTH = 200;
	final int ROAD4_WIDTH = 245;
	final int ROAD4_LENGTH = 200;

	int givenTime;
	int boundLeft, boundRight;
	int finishY;
	ArrayList<Unit> units;
	ArrayList<TrafficLane> trafficLanes;
	int unitCount;
	int vehicleCount;

	Drawable treeImage;
	Drawable roadImage;
	Drawable road4Image;
	Drawable grassImage;
	Drawable sidewalkImage;

	Drawable carImages[];
	Paint bgPaint;

	Random random = new Random();

	public World(Paint bgp, int sx, int sy, int fY, int timegiven,
			Drawable treeimg, Drawable roadimg, Drawable road4img,
			Drawable[] cars) {
		bgPaint = bgp;
		finishY = fY;
		givenTime = timegiven;
		carImages = cars;
		unitCount = 0;
		boundLeft = sx;
		boundRight = sy;
		units = new ArrayList<Unit>();
		trafficLanes = new ArrayList<TrafficLane>();
		treeImage = treeimg;
		roadImage = roadimg;
		road4Image = road4img;
	}

	public void addUnit(Unit u) {
		if (u.getUnitType() == 1)// add to end of list
		{
			units.add(units.size(), u);
		}

		else // add to beginning
		{
			units.add(0, u);
		}
	}

	public void removeUnit(Unit u) {
		units.remove(u);
	}

	public void addTree(int x, int y) {
		Unit tree = new Unit(true, false, x, y, 45, 75, 0, treeImage);
		// tree.setUnitType(1); //temporary
		addUnit(tree);
	}

	public void addTrees(int areax, int areay, int areawid, int areahgt,
			int count)// adds area of trees
	{
		Random random = new Random();
		for (int i = 0; i < count; i++) {
			addTree(areax + random.nextInt(areawid),
					areay + random.nextInt(areahgt));
		}
	}

	public void addTreeLine(int x, int y, double ang, int spacing, int length) {

		for (int c = 1; c < length; c++) {
			double radians = ang * (Math.PI / 180);
			addTree((int) (x + (c * SolveforX(radians, spacing))),
					(int) (y + (c * SolveforY(radians, spacing))));
		}

	}

	public void addRoad4Lane(double px, double py, double ang, int length) {
		for (int c = 1; c <= length; c++) {
			double radians = ang * (Math.PI / 180);
			addUnit(new Unit(false, false, px
					+ (c * SolveforX(radians, ROAD4_LENGTH)), py
					+ (c * SolveforY(radians, ROAD4_LENGTH)),
					(double) ROAD4_WIDTH, ROAD4_LENGTH + 3, ang - 90,
					road4Image));
		}
	}

	public void addRoad(double px, double py, double ang, int length) {
		for (int c = 1; c <= length; c++) {
			double radians = ang * (Math.PI / 180);
			addUnit(new Unit(false, false, px
					+ (c * SolveforX(radians, ROAD_LENGTH)), py
					+ (c * SolveforY(radians, ROAD_LENGTH)),
					(double) ROAD_WIDTH, ROAD_LENGTH + 1, ang - 90, roadImage));
		}
	}

	public void addTrafficLane(double px, double py, int numcar, double maxspd,
			double minspd, double tb, double bb, double ang) {
		trafficLanes.add(new TrafficLane(px, py, numcar, maxspd, minspd, tb,
				bb, ang));
	}

	double SolveforY(double ang, double hyp) {
		return Math.sin(ang) * hyp;
	}

	double SolveforX(double ang, double hyp) {
		return Math.cos(ang) * hyp;
	}

	public int getBoundLeft() {
		return boundLeft;
	}

	public int getBoundRight() {
		return boundRight;
	}

	public ArrayList<Unit> getUnits() {
		return units;
	}

	public int getSizeX() {
		return boundLeft;
	}

	public int getSizeY() {
		return boundRight;
	}

	public ArrayList<TrafficLane> getTrafficLanes() {
		return trafficLanes;
	}

	public int getFinishY() {
		return finishY;
	}

	public Paint getBgPaint() {
		return bgPaint;
	}

	public int getGivenTime() {
		return givenTime;
	}

	public class TrafficLane {
		double posX;
		double posY;
		int numberOfCars;
		double maximumSpeed;
		double minimumSpeed;
		double topBound;
		double bottomBound;
		double angle;
		ArrayList<Vehicle> laneVehicles;

		TrafficLane(double xx, double yy, int numcar, double maxspd,
				double minspd, double tb, double bb, double ang) {
			posX = xx;
			posY = yy;
			numberOfCars = numcar;
			maximumSpeed = maxspd;
			minimumSpeed = minspd;
			topBound = tb;
			bottomBound = bb;
			angle = ang;
			laneVehicles = new ArrayList<Vehicle>();
			for (int i = 0; i < numberOfCars; i++) {
				addRandomVehicle();
			}
		}

		void addRandomVehicle() {
			double y = bottomBound
					- (random
							.nextInt((int) ((Math.abs(bottomBound - topBound)))));
			double denom = Math.tan(Math.toRadians(360 - angle));
			double x = posX + ((bottomBound - y) / denom);
			double topspeed = minimumSpeed
					+ random.nextInt((int) (maximumSpeed - minimumSpeed));
			Drawable image = carImages[random.nextInt(carImages.length)];
			Vehicle newvehicle = new Vehicle(true, true, x, y, 50, 30, angle,
					topspeed, 1, 1, 1, 1, topspeed, image);
			addVehicle(newvehicle);
		}

		void addVehicle(Vehicle v) {
			laneVehicles.add(v);
			units.add(v);
		}

		void checkVehicleBounds(Vehicle v) {
			if (v.getY() < topBound) {
				v.setY(bottomBound - 5);
				v.setX(posX);
			} else if (v.getY() > bottomBound) {
				v.setY(topBound + 5);
				// v.setX((bottomBound - topBound) /
				// Math.tan(Math.toRadians(angle)));
			}
		}

		int slowCheckIndex = 0;

		void slowCheckAllVehicleBounds() // checks 1 vehicle per update rather
											// than all per update. this is
											// faster
		{
			checkVehicleBounds(laneVehicles.get(slowCheckIndex));
			slowCheckIndex++;
			if (slowCheckIndex > laneVehicles.size() - 1)
				slowCheckIndex = 0;

		}

		void checkAllVehicleBounds() {
			int count = laneVehicles.size();
			for (int i = 0; i < count; i++) {
				checkVehicleBounds(laneVehicles.get(i));
			}
		}

	}

}
