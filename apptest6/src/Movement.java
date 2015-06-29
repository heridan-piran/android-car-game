package com.example.apptest6;

import android.util.Log;

public class Movement {
	double scalex, scaley;
	double velocityx, velocityy;

	public Movement() {
		scalex = 0;
		scaley = 0;
		velocityx = 0;
		velocityy = 0;
	}

	public void calculateVelocity(double ang, double spd) {
		double rad = ang * (Math.PI / 180);
		scalex = Math.cos(rad);
		scaley = Math.sin(rad);
		velocityx = scalex * spd;
		velocityy = scaley * spd;
	}

	public double getVelocityX() {
		return velocityx;
	}

	public double getVelocityY() {
		return velocityy;
	}
}
