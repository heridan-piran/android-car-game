package com.example.apptest6;

public class Player
{

	String name;
	int hp; 
	int level;	
	int score;
	boolean alive;
	Vehicle vehicle;
	
	
	public Player(String n, int h, Vehicle v)
	{
		alive = true;
		vehicle = v;
		name = n;
		hp = h;
		level = 0;
	}
	public Vehicle getVehicle(){return vehicle;}
	public int getLevel(){return level;}
	public int getHp(){return hp;}
	public String getName(){return name;}
	public int getScore(){return score;}
	
	public void damage(int amt){hp -= amt;}
	public void repair(int amt){hp += amt;}
	public void addScore(int amt){score += amt;}
	public void advanceLevel(){level++;}
	public void resetHp(){hp = 100;}

	public void setVehicle(Vehicle v){vehicle = v;}
	public boolean getAlive(){return alive;}
	public void setAlive(boolean b){alive = b;}
	
}
