package com.example.apptest6;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.graphics.drawable.Drawable;
import android.hardware.SensorEvent;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	double SCREEN_SIZE_X, SCREEN_SIZE_Y;
	double MIDSCREEN_X, MIDSCREEN_Y;
	final int MAX_TURN_ROTATION = 45;
	final int MAX_SPEED_ROTATION = 25;
	MainActivity mainPointer;

	int carChoice;
	String playerName;

	GameThread gameThread;

	World world;
	int currentWorld;
	Camera camera;
	Player player;

	boolean gamePaused;

	float deviceAxisX;
	float deviceAxisY;
	float deviceAxisZ;

	boolean showDebugText = false; // shows variables in top left corner
	boolean screenTouched = false;
	int screenTouchX;
	int screenTouchY;

	static ImageData imageData;
	Drawable[] carImages;

	Random random = new Random();

	Paint bgPaint;
	Paint bgDesertPaint;
	Paint bgSnowPaint;
	Paint bgWaterPaint;
	Paint textPaint;
	Paint redPaint;
	Paint text2Paint;
	Paint greenPaint;
	Paint blackTransparentPaint;
	Paint greyPaint;
	Paint textBluePaint;

	Vibrator vib;

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
		loadImages(context);
		loadPaints();
	}

	public void setVib(Vibrator v) {
		vib = v;
	}

	public void setMainPointer(MainActivity a) {
		mainPointer = a;
	}

	public void loadPaints() {
		bgPaint = new Paint();
		bgPaint.setStyle(Paint.Style.FILL);
		bgPaint.setColor(Color.argb(255, 0, 90, 0));

		bgSnowPaint = new Paint();
		bgSnowPaint.setStyle(Paint.Style.FILL);
		bgSnowPaint.setColor(Color.argb(255, 230, 230, 230));

		bgDesertPaint = new Paint();
		bgDesertPaint.setStyle(Paint.Style.FILL);
		bgDesertPaint.setColor(Color.argb(255, 255, 204, 37));

		bgWaterPaint = new Paint();
		bgWaterPaint.setStyle(Paint.Style.FILL);
		bgWaterPaint.setColor(Color.argb(255, 28, 17, 174));

		redPaint = new Paint();
		redPaint.setStyle(Paint.Style.FILL);
		redPaint.setColor(Color.argb(255, 255, 0, 0));

		greenPaint = new Paint();
		greenPaint.setStyle(Paint.Style.FILL);
		greenPaint.setColor(Color.argb(255, 0, 255, 0));

		textPaint = new Paint();
		textPaint.setStyle(Paint.Style.STROKE);
		textPaint.setColor(Color.argb(255, 0, 0, 0));

		text2Paint = new Paint();
		text2Paint.setStyle(Paint.Style.FILL);
		text2Paint.setColor(Color.RED);
		text2Paint.setTextSize(20);

		textBluePaint = new Paint();
		textBluePaint.setStyle(Paint.Style.FILL);
		textBluePaint.setColor(Color.BLUE);
		textBluePaint.setTextSize(23);

		blackTransparentPaint = new Paint();
		blackTransparentPaint.setStyle(Paint.Style.FILL);
		blackTransparentPaint.setColor(Color.argb(100, 0, 0, 0));

		greyPaint = new Paint();
		greyPaint.setStyle(Paint.Style.FILL);
		greyPaint.setColor(Color.GRAY);
	}

	public void loadImages(Context context) {
		ImageData.imgCar1 = context.getResources().getDrawable(
				R.drawable.testcar);
		ImageData.imgCar2 = context.getResources().getDrawable(
				R.drawable.testcar2);
		ImageData.imgDxKitt = context.getResources().getDrawable(
				R.drawable.dx_kitt);
		ImageData.imgDxChevelle = context.getResources().getDrawable(
				R.drawable.dx_chevelle);
		ImageData.imgDxKnightRider = context.getResources().getDrawable(
				R.drawable.dx_knightrider);
		ImageData.imgDxLambo = context.getResources().getDrawable(
				R.drawable.dx_lambodiablo);
		ImageData.imgDxPlymouthFury = context.getResources().getDrawable(
				R.drawable.dx_plymouthfury);
		ImageData.imgDxAudiQuatro = context.getResources().getDrawable(
				R.drawable.dx_audiquatro);
		ImageData.imgDxCorvette = context.getResources().getDrawable(
				R.drawable.dx_corvette);
		ImageData.imgDxc = context.getResources().getDrawable(R.drawable.dxc);
		ImageData.imgCactus = context.getResources().getDrawable(
				R.drawable.cactus);
		ImageData.imgRoad4Lane2 = context.getResources().getDrawable(
				R.drawable.road4lane2);
		ImageData.imgFinishLine = context.getResources().getDrawable(
				R.drawable.finishline);

		ImageData.imgExplosion = context.getResources().getDrawable(
				R.drawable.explosion);
		ImageData.imgTree = context.getResources().getDrawable(R.drawable.tree);
		ImageData.imgTree2 = context.getResources().getDrawable(
				R.drawable.tree2r);
		ImageData.imgTreeSnow = context.getResources().getDrawable(
				R.drawable.treesnow);
		ImageData.imgRoad = context.getResources().getDrawable(R.drawable.road);
		ImageData.imgRoad4Lane = context.getResources().getDrawable(
				R.drawable.road4lane);

		carImages = new Drawable[10];
		carImages[0] = ImageData.imgDxKitt;
		carImages[1] = ImageData.imgDxCorvette;
		carImages[2] = ImageData.imgDxKnightRider;
		carImages[3] = ImageData.imgDxAudiQuatro;
		carImages[4] = ImageData.imgDxChevelle;
		carImages[5] = ImageData.imgDxPlymouthFury;
		carImages[6] = ImageData.imgDxLambo;
		carImages[7] = ImageData.imgCar1;
		carImages[8] = ImageData.imgCar2;
		carImages[9] = ImageData.imgDxc;
	}

	public GameThread getThread() {
		return gameThread;
	}

	public void setScreenSize(double w, double h) {
		SCREEN_SIZE_X = w;
		SCREEN_SIZE_Y = h;
		MIDSCREEN_X = SCREEN_SIZE_X / 2;
		MIDSCREEN_Y = SCREEN_SIZE_Y / 2;
	}

	public void setPlayerName(String n) {
		playerName = n;
	}

	public void setCarChoice(int s) {
		carChoice = s;
	}

	public void loadWorld(int n) {
		if (n == 0) {
			world = new World(bgPaint, 1390, 1775, 70000, 200,
					imageData.imgTree, imageData.imgRoad,
					imageData.imgRoad4Lane, carImages);
			world.addUnit((Unit) player.getVehicle());
			world.addRoad4Lane(1475, 100000, 270, 200);
			world.addTrafficLane(1480.0, 100000.0, 20, 200.0, 100.0, 68000.0,
					100000.0, 90.0);
			world.addTrafficLane(1540.0, 100000.0, 20, 200.0, 100.0, 68000.0,
					100000.0, 90.0);
			world.addTrafficLane(1605.0, 50000.0, 20, 200.0, 100.0, 68000.0,
					100000.0, 270.0);
			world.addTrafficLane(1668.0, 50000.0, 20, 200.0, 100.0, 68000.0,
					100000.0, 270.0);
			world.addTreeLine(1750, 100000, 270, 270, 150);
			world.addTreeLine(1400, 100000, 270, 270, 150);
		} else if (n == 1) {
			world = new World(bgSnowPaint, 1390, 1775, 65000, 220,
					imageData.imgTreeSnow, imageData.imgRoad,
					imageData.imgRoad4Lane, carImages);
			world.addUnit((Unit) player.getVehicle());

			world.addTrafficLane(1480.0, 100000.0, 45, 200.0, 100.0, 63000.0,
					100000.0, 90.0);
			world.addTrafficLane(1540.0, 100000.0, 45, 200.0, 100.0, 63000.0,
					100000.0, 90.0);
			world.addTrafficLane(1605.0, 50000.0, 36, 200.0, 100.0, 63000.0,
					100000.0, 270.0);
			world.addTrafficLane(1668.0, 50000.0, 36, 200.0, 100.0, 63000.0,
					100000.0, 270.0);
			world.addRoad4Lane(1475, 100000, 270, 180);
			world.addTreeLine(1750, 100000, 270, 130, 220);
			world.addTreeLine(1400, 100000, 270, 130, 220);
		} else if (n == 2) {
			world = new World(bgDesertPaint, 1390, 1650, 72000, 260,
					imageData.imgCactus, imageData.imgRoad,
					imageData.imgRoad4Lane, carImages);
			world.addUnit((Unit) player.getVehicle());
			world.addRoad(1475, 100000, 270, 180);
			world.addTrafficLane(1480.0, 100000.0, 45, 150.0, 85.0, 63000.0,
					100000.0, 90.0);
			world.addTrafficLane(1540.0, 100000.0, 55, 200.0, 100.0, 63000.0,
					100000.0, 270.0);
			world.addTreeLine(1630, 100000, 270, 200, 180);
			world.addTreeLine(1405, 100100, 270, 200, 180);
		} else if (n == 3) {
			world = new World(bgWaterPaint, 1475, 1687, 65000, 310,
					imageData.imgCactus, imageData.imgRoad,
					imageData.imgRoad4Lane, carImages);
			world.addUnit((Unit) player.getVehicle());
			world.addRoad4Lane(1475, 100000, 270, 180);
			world.addTrafficLane(1480.0, 100000.0, 37, 200.0, 100.0, 63000.0,
					100000.0, 90.0);
			world.addTrafficLane(1540.0, 100000.0, 37, 200.0, 100.0, 63000.0,
					100000.0, 90.0);
			world.addTrafficLane(1605.0, 50000.0, 42, 250.0, 120.0, 63000.0,
					100000.0, 270.0);
			world.addTrafficLane(1668.0, 50000.0, 42, 250.0, 120.0, 63000.0,
					100000.0, 270.0);
		} else if (n == 4) {
			world = new World(redPaint, 1475, 1687, 50000, 420,
					imageData.imgCactus, imageData.imgRoad,
					imageData.imgRoad4Lane, carImages);
			world.addUnit((Unit) player.getVehicle());
			world.addRoad4Lane(1475, 100000, 270, 260);
			world.addTrafficLane(1480.0, 100000.0, 80, 300.0, 150.0, 49000.0,
					100000.0, 90.0);
			world.addTrafficLane(1540.0, 100000.0, 80, 300.0, 150.0, 49000.0,
					100000.0, 90.0);
			world.addTrafficLane(1605.0, 50000.0, 90, 300.0, 150.0, 49000.0,
					100000.0, 270.0);
			world.addTrafficLane(1668.0, 50000.0, 90, 300.0, 150.0, 49000.0,
					100000.0, 270.0);
		}
	}

	public void newGame() {
		gameThread = new GameThread(getHolder());

		player = new Player(playerName, 100, loadPlayerCar(1400, 99900, 270, 0));
		currentWorld = 0;
		loadWorld(currentWorld);

		camera = new Camera(player.getVehicle(), SCREEN_SIZE_X, SCREEN_SIZE_Y,
				0, -70, 250, 200); // camera with y offset
		// camera = new Camera(playervehicle,SCREEN_SIZE_X,
		// SCREEN_SIZE_Y,0,0,200,200); //camera without y offset

		gamePaused = false;
		gameThread.setRunning(true);
		gameThread.start();

		Log.e("status", "newGame() ended");
	}

	public Vehicle loadPlayerCar(double startposx, double startposy,
			double ang, double startspeed) {
		// fix case - not constants anymore
		double STARTING_POSITION_X = startposx;
		double STARTING_POSITION_Y = startposy;
		double CAR_WIDTH, CAR_HEIGHT;
		double CAR_ANGLE;
		double TOP_SPEED;
		double ACCELERATION;
		double SLOWDOWN_RATE;
		double BRAKE_RATE;
		double TURN_RATE;
		double STARTING_SPEED;
		Drawable CAR_IMAGE;

		if (carChoice == 1) {
			CAR_WIDTH = 50;
			CAR_HEIGHT = 30;
			CAR_ANGLE = 270;
			TOP_SPEED = 520;
			ACCELERATION = 6.7;
			SLOWDOWN_RATE = 2;
			BRAKE_RATE = 7;
			TURN_RATE = 8.9;
			STARTING_SPEED = 0;
			CAR_IMAGE = imageData.imgCar1;
			return new Vehicle(true, true, STARTING_POSITION_X,
					STARTING_POSITION_Y, CAR_WIDTH, CAR_HEIGHT, CAR_ANGLE,
					TOP_SPEED, ACCELERATION, SLOWDOWN_RATE, BRAKE_RATE,
					TURN_RATE, STARTING_SPEED, CAR_IMAGE);
		} else if (carChoice == 2) {
			CAR_WIDTH = 50;
			CAR_HEIGHT = 30;
			CAR_ANGLE = 270;
			TOP_SPEED = 500;
			ACCELERATION = 7;
			SLOWDOWN_RATE = 2;
			BRAKE_RATE = 7;
			TURN_RATE = 9.1;
			STARTING_SPEED = 0;
			CAR_IMAGE = imageData.imgDxLambo;
			return new Vehicle(false, true, STARTING_POSITION_X,
					STARTING_POSITION_Y, CAR_WIDTH, CAR_HEIGHT, CAR_ANGLE,
					TOP_SPEED, ACCELERATION, SLOWDOWN_RATE, BRAKE_RATE,
					TURN_RATE, STARTING_SPEED, CAR_IMAGE);
		} else if (carChoice == 3) {
			CAR_WIDTH = 50;
			CAR_HEIGHT = 30;
			CAR_ANGLE = 270;
			TOP_SPEED = 490;
			ACCELERATION = 8;
			SLOWDOWN_RATE = 3;
			BRAKE_RATE = 8;
			TURN_RATE = 9.3;
			STARTING_SPEED = 0;
			CAR_IMAGE = imageData.imgDxAudiQuatro;
			return new Vehicle(false, true, STARTING_POSITION_X,
					STARTING_POSITION_Y, CAR_WIDTH, CAR_HEIGHT, CAR_ANGLE,
					TOP_SPEED, ACCELERATION, SLOWDOWN_RATE, BRAKE_RATE,
					TURN_RATE, STARTING_SPEED, CAR_IMAGE);
		} else
			return null;
	}

	public void setDeviceAxes(float x, float y, float z) {
		deviceAxisX = x;
		deviceAxisY = y;
		deviceAxisZ = z;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)// slow
	{
		Log.e("touch", "screen touched");
		screenTouched = true;
		screenTouchX = (int) event.getX();
		screenTouchY = (int) event.getY();
		if (gamePaused) {
			if (currentWorld == 4) {
				gameThread.setRunning(false);
				gameThread.stop();
				mainPointer.endGame();
			}
			if (player.alive) {
				player.resetHp();
				gameThread.resetElapsedTime();
				currentWorld++;
				loadWorld(currentWorld);
				player.getVehicle().setPosition(1550, 99950);
				player.getVehicle().setSpeed(0);
				player.getVehicle().setAngle(270);
				gamePaused = false;
			} else {
				gameThread.setRunning(false);
				gameThread.stop();
				mainPointer.endGame();
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.e("status", "surfaceCreated()");
		newGame();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	public class GameThread extends Thread {
		private SurfaceHolder surfaceHolder;
		private boolean running;
		double elapsedTime;
		boolean colliding = false; // put somewhere else

		public GameThread(SurfaceHolder holder) {
			surfaceHolder = holder;
			setName("GameThread");

			elapsedTime = 0;
			running = true;
		}

		public void setRunning(boolean run) {
			running = run;
		}

		@Override
		public void run() {
			Canvas canvas = null;
			long previousTime = System.currentTimeMillis();

			while (running)// GAME LOOP
			{
				if (!gamePaused) {
					try {
						canvas = surfaceHolder.lockCanvas(null);
						synchronized (surfaceHolder) {
							long currentTime = System.currentTimeMillis();
							double elapsedTimeMS = currentTime - previousTime;
							elapsedTime += elapsedTimeMS / 1000.00;

							int updateID = 0;
							if (updateID < 3) {
								Update(elapsedTimeMS, canvas, updateID);
								updateID++;
							} else {
								updateID = 0;
							}
							previousTime = currentTime;
						}
					} finally {
						if (canvas != null)
							surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
			}
		}

		void Update(double elapsedtimeMS, Canvas c, int upID) {
			double interval = elapsedtimeMS / 1000.0;
			c.drawRect(0, 0, c.getWidth(), c.getHeight(), world.getBgPaint());
			colliding = false;

			Vehicle playerVehicle = player.getVehicle();
			playerVehicle.turn(deviceAxisZ, MAX_TURN_ROTATION);
			playerVehicle.updateSpeed(deviceAxisY, MAX_SPEED_ROTATION);

			if (upID == 0) {
				ArrayList<Unit> units = world.getUnits();
				int unitCount = units.size();
				for (int i = 0; i < unitCount; i++) {
					Unit u = units.get(i);
					if (u.getUnitType() == (1)) // is a vehicle
					{
						Vehicle v = (Vehicle) u;
						v.applyMovement(interval);
						if (v == camera.getLockedUnit()) {
							camera.updateCamera();
						}
					}
					if (camera.isInView(u)) // /only draw unit if its close to
											// the camera
					{
						drawUnit(u, c);
						if (u.getCollides() && u != playerVehicle) {
							if (Math.abs(u.getX() - playerVehicle.getX()) < 70
									&& Math.abs(u.getY() - playerVehicle.getY()) < 70)// approx
																						// area
							{
								if (playerVehicle.collidingWith(u))// collision
																	// test
								{
									colliding = true;
									player.damage(1);
									vib.vibrate(100);
									if (player.getHp() <= 0)// /player dies
									{
										player.setAlive(false);
									}
								}
							}
						}
					}
				}
			}
			if (playerVehicle.getY() < world.getFinishY()) // /END LEVEL
			{
				if (currentWorld == 4) {
					double score = (world.getGivenTime() - elapsedTime)
							* ((double) player.getHp() / 100.0);
					player.addScore((int) score + 250);
					c.drawText("FINISH!", (float) (SCREEN_SIZE_X / 5), 160f,
							text2Paint);
					c.drawText("Time:    " + Math.round(elapsedTime),
							(float) (SCREEN_SIZE_X / 5), 180f, text2Paint);
					c.drawText("Level Score:    " + (int) score,
							(float) (SCREEN_SIZE_X / 5), 200f, text2Paint);
					c.drawText("Final Score:    " + player.getScore(),
							(float) (SCREEN_SIZE_X / 5), 220f, text2Paint);
					c.drawText("YOU WIN!", (float) (SCREEN_SIZE_X / 5), 240f,
							textBluePaint);
					if (player.score > getHighScore()) {
						c.drawText("New High Score!",
								(float) (SCREEN_SIZE_X / 5), 300f, bgSnowPaint);
						updateHighScore(playerName, player.getScore());
					}
					gamePaused = true;
				} else {
					double score = (world.getGivenTime() - elapsedTime)
							* ((double) player.getHp() / 100.0);
					player.addScore((int) score);
					drawRectangle(0, 0, (int) SCREEN_SIZE_X,
							(int) SCREEN_SIZE_Y, blackTransparentPaint, c);

					c.drawText("FINISH!", (float) (SCREEN_SIZE_X / 5), 160f,
							text2Paint);
					c.drawText("Time:    " + Math.round(elapsedTime),
							(float) (SCREEN_SIZE_X / 5), 180f, text2Paint);
					c.drawText("Level Score:    " + (int) score,
							(float) (SCREEN_SIZE_X / 5), 200f, text2Paint);
					c.drawText("Total Score:    " + player.getScore(),
							(float) (SCREEN_SIZE_X / 5), 220f, text2Paint);
					c.drawText("Touch Screen to Continue",
							(float) (SCREEN_SIZE_X / 5), 240f, text2Paint);

					gamePaused = true;
				}
			}

			// turn player if it too far to the side
			if (playerVehicle.getX() <= world.getBoundLeft()) {
				playerVehicle.setAngle(271);
				playerVehicle.setX(world.getBoundLeft());
			}
			if (playerVehicle.getX() >= world.getBoundRight()) {
				playerVehicle.setAngle(269);
				playerVehicle.setX(world.getBoundRight());
			}

			if (!player.getAlive()) {
				drawUnit(new Unit(false, false, playerVehicle.getX() - 15,
						playerVehicle.getY() - 15, 75, 75, 0,
						imageData.imgExplosion, 1), c);
				drawRectangle(0, 0, (int) SCREEN_SIZE_X, (int) SCREEN_SIZE_Y,
						blackTransparentPaint, c);
				c.drawText("YOU DIED!", (float) (SCREEN_SIZE_X / 5), 180f,
						text2Paint);
				c.drawText("Score:    " + player.getScore(),
						(float) (SCREEN_SIZE_X / 5), 200f, text2Paint);
				c.drawText("Touch Screen to Retry",
						(float) (SCREEN_SIZE_X / 5), 220f, text2Paint);
				if (player.score > getHighScore()) {
					c.drawText("New High Score!", (float) (SCREEN_SIZE_X / 5),
							240f, bgSnowPaint);
					updateHighScore(playerName, player.getScore());
				}

				gamePaused = true;
			}
			if (upID == 3) {
				int trafficLaneCount = world.getTrafficLanes().size();
				for (int i = 0; i < trafficLaneCount; i++) {
					world.getTrafficLanes().get(i).slowCheckAllVehicleBounds();
				}
			}
			// health bar
			drawRectangle((int) (SCREEN_SIZE_X - 120),
					(int) (SCREEN_SIZE_Y - 70), 100, 8, redPaint, c);
			drawRectangle((int) (SCREEN_SIZE_X - 120),
					(int) (SCREEN_SIZE_Y - 70), player.getHp(), 8, greenPaint,
					c);
			c.drawText("Health", (int) (SCREEN_SIZE_X - 120),
					(int) (SCREEN_SIZE_Y - 75), redPaint);

			// speedometer
			drawRectangle(15, (int) (SCREEN_SIZE_Y - 102), 80, 40,
					blackTransparentPaint, c);
			c.drawText("Speed", 40f, (float) (SCREEN_SIZE_Y - 90), greyPaint);
			double theta = Math
					.toRadians((int) ((playerVehicle.getSpeed() * 180) / playerVehicle
							.getTopSpeed()) + 180);
			c.drawLine(55f, (float) (SCREEN_SIZE_Y - 62),
					(float) (40 * Math.cos(theta)) + 55f,
					(float) (40 * Math.sin(theta))
							+ (float) (SCREEN_SIZE_Y - 62), redPaint);

			// distance meter
			int distanceToGo = (int) playerVehicle.getY() - world.getFinishY();
			drawRectangle(5, (int) (SCREEN_SIZE_Y / 2 + 103) - 75, 3, 150,
					greyPaint, c);
			drawRectangle(5, (int) (SCREEN_SIZE_Y / 2 + 103) - 75, 3,
					((150 * distanceToGo) / (100000 - world.getFinishY())),
					blackTransparentPaint, c);

			//
			c.drawText(playerName, 5f, 20f, text2Paint);
			c.drawText("" + player.getScore(), 5f, 40f, text2Paint);
			c.drawText(
					""
							+ (int) ((world.getGivenTime() - elapsedTime) * ((double) player
									.getHp() / 100.0)), 5f, 60f, text2Paint);

			if (showDebugText) {
				c.drawText(("speed:" + playerVehicle.getSpeed()), 5f, 53f,
						textPaint);
				c.drawText(("x:" + Math.round(playerVehicle.getX())), 5f, 63f,
						textPaint);
				c.drawText(("y:" + Math.round(playerVehicle.getY())), 5f, 73f,
						textPaint);
				c.drawText(
						("angle:" + Math.round(player.getVehicle().getAngle()) % 360),
						5f, 83f, textPaint);
				c.drawText("acc x:" + deviceAxisX, 5f, 93f, textPaint);
				c.drawText("acc y:" + deviceAxisY, 5f, 103f, textPaint);
				c.drawText("acc z:" + deviceAxisZ, 5f, 113f, textPaint);

				if (colliding) {
					c.drawText("COLLIDING", 5f, 73f, redPaint);
				} else {
					c.drawText("--", 5f, 73f, textPaint);
				}
			}
		}

		void updateHighScore(String n, int sc) {
			SharedPreferences settings = mainPointer.getSharedPreferences(
					"settings", 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("namehs", player.getName());
			editor.putInt("highscorehs", player.getScore());
			editor.commit();
			Log.e("hs", "high score updated");

		}

		int getHighScore() {
			SharedPreferences settings = mainPointer.getSharedPreferences(
					"settings", mainPointer.MODE_WORLD_READABLE);
			int hscore = settings.getInt("highscorehs", 0);
			return hscore;
		}

		void drawRotatedBitmap(double px, double py, double wid, double hgt,
				double ang, Drawable d, Canvas canv) {
			canv.save();
			canv.rotate((float) ang, (float) (px + (wid / 2)),
					(float) (py + (hgt / 2)));
			d.setBounds((int) px, (int) py, (int) (px + wid), (int) (py + hgt));
			d.draw(canv);
			canv.restore();
		}

		void drawUnit(Unit u, Canvas c) {
			drawRotatedBitmap(camera.getScreenCoordinateX(u.getX()),
					camera.getScreenCoordinateY(u.getY()), u.getWidth(),
					u.getHeight(), u.getAngle(), u.getImage(), c);
		}

		void drawRectangle(int x, int y, int wid, int hgt, Paint p, Canvas canv) {
			p.setAntiAlias(false);
			canv.drawRect(x, y, x + wid, y + hgt, p);
		}

		public void getAccelerometerStatus(SensorEvent e) {
			deviceAxisX = (int) e.values[0] - 270;
			deviceAxisY = (int) e.values[1];
			deviceAxisZ = (int) e.values[2];
		}

		public void resetElapsedTime() {
			elapsedTime = 0;
		}
	}
}
