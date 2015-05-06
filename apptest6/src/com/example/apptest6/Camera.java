package com.example.apptest6;

public class Camera 
{
	double x,y;
	double viewwidth, viewheight;
	double upperbound, leftbound;
	double offsetx, offsety;
	double paddingx, paddingy;

	boolean lockedonunit;
	Unit lockedunit;

	public Camera(double px, double py, double w, double h, double padx, double pady)
	{
		x = px;
		y = py;
		viewwidth = w;
		viewheight = h;
		lockedonunit = false;
		lockedunit = null;
		offsetx = 0;
		offsety = 0;
		paddingx = padx;
		paddingy = pady;
	}
	public Camera(Unit lockedun,double w, double h, int offx, int offy, double padx, double pady)
	{
		x = lockedun.getX();
		y = lockedun.getY();
		viewwidth = w;
		viewheight = h;
		lockedonunit = true;
		lockedunit = lockedun;
		offsetx = offx;
		offsety = offy;
		paddingx = padx;
		paddingy = pady;
	}



	public void updateCamera(double px, double py)
	{
		x = px + offsetx;
		y = py + offsety;
		calculateBounds();
	}
	public void updateCamera(Unit u)
	{
		x = u.getX() + (u.getWidth() / 2) + offsetx;
		y = u.getY() + (u.getHeight() / 2) + offsety;
		calculateBounds();
	}
	public void updateCamera()
	{
		x = lockedunit.getX() + (lockedunit.getWidth() / 2) + offsetx;
		y = lockedunit.getY() + (lockedunit.getHeight() / 2) + offsety;
		calculateBounds();
	} 

	public boolean isInView(Unit u)
	{
	
		double ux = u.getX();
		double uy = u.getY();

		double leftviewbound = leftbound - paddingx;
		double rightviewbound = leftbound + viewwidth + paddingx;

		if (ux > leftviewbound && ux < rightviewbound) // within x view
		{
			double upviewbound = upperbound - paddingy;
			double downviewbound = upperbound + viewheight + paddingy;
			
			if(uy > upviewbound && uy < downviewbound)
			{
				return true;
			}
			else
			{
				return false;
			}
			
			
		}
		else
		{
			return false;
		}

	}

	void calculateBounds()
	{
		leftbound = x - (viewwidth / 2) + offsetx;
		upperbound = y - (viewheight / 2) + offsety;
	}

	double getScreenCoordinateX(double worldposx)
	{
		return worldposx - leftbound;
	}
	double getScreenCoordinateY(double worldposy)
	{
		return worldposy - upperbound;
	}

	public boolean getIsLockedOnUnit(){return lockedonunit;}
	public Unit getLockedUnit(){return lockedunit;}
	public double getX(){return x;}
	public double getY(){return y;}
	public double getLeftBound() { return leftbound; }
	public double getRightBound() { return leftbound + viewwidth; }
	public double getUpperBound() { return upperbound; }
	public double getLowerBound() { return upperbound + viewheight; }




}
