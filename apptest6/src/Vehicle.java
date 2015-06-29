package com.example.apptest6;

import android.graphics.drawable.Drawable;
import android.util.Log;

class Vehicle extends Unit {
	Movement movement;
	double turnspeed;
	double speed;
	double topspeed;
	double acceleration;
	double slowdownrate;
	double brakerate;

	boolean usingbrake;
	boolean inreverse;

	public Vehicle(boolean col, boolean movs, double px, double py, double w,
			double h, double ang, double topsp, double accel, double sdrate,
			double brkrate, double turn, double spd, Drawable img)

	{
		super(col, movs, px, py, w, h, ang, img);
		super.setUnitType(1);
		slowdownrate = sdrate;
		topspeed = topsp;
		turnspeed = turn;
		acceleration = accel;
		brakerate = brkrate;
		inreverse = false;
		usingbrake = false;
		movement = new Movement();
		speed = spd;
	}

	public void applyMovement(double ang, double spd) {

		movement.calculateVelocity(ang, spd);
		x += movement.getVelocityX();
		y += movement.getVelocityY();
	}

	public void applyMovement(double interval) {

		movement.calculateVelocity(angle, speed);
		x += movement.getVelocityX() * interval;
		y += movement.getVelocityY() * interval;
	}

	public void turn(float axisrotation, int maxrotate) {
		if (Math.abs(axisrotation) > 5) {
			if (Math.abs(speed) > 10) {
				if (axisrotation > maxrotate) {
					axisrotation = maxrotate;
				}
				angle += (axisrotation / maxrotate) * turnspeed;
			}
		}
	}

	public void turnRight(double interval) {
		angle += turnspeed * interval;
	}

	public void turnLeft(double interval) {
		angle -= turnspeed * interval;
	}

	public void updateSpeed(float axisrotation, int maxrotate) {
		if (Math.abs(axisrotation) > 0) {
			if (axisrotation > maxrotate) {
				axisrotation = maxrotate;
			}

			if (axisrotation >= 0) {
				speed += (axisrotation / maxrotate) * acceleration;
				if (speed > topspeed) {
					speed = topspeed;
				}
			} else if (axisrotation < -40) // reverse
			{
				speed += (axisrotation / maxrotate) * brakerate;
				if (speed < -1 * (topspeed / 5)) {
					speed = -1 * (topspeed / 5);
				}
			} else if (axisrotation < -15) // brake
			{
				speed += (axisrotation / maxrotate) * brakerate;
				if (speed < 0) {
					speed = 0;
				}
			}

		}
	}

	public void accelerate() {
		speed += acceleration;
		if (speed > topspeed) {
			speed = topspeed;
		}
	}

	public void decelerate() {
		speed -= slowdownrate;
		if (speed < 0) {
			speed = 0;
		}
	}

	public void brake() {
		speed -= brakerate;
		if (speed < 0) {
			speed = 0;
		}
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double s) {
		speed = s;
	}

	public double getTopSpeed() {
		return topspeed;
	}

}
