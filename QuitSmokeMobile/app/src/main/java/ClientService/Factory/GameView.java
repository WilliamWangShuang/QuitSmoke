package clientservice.factory;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;

import clientservice.QuitSmokeClientUtils;
import clientservice.entities.Boom;
import clientservice.entities.Enemy;
import clientservice.entities.Friend;
import clientservice.entities.Player;
import clientservice.entities.Star;

public class GameView extends SurfaceView implements Runnable {
    volatile boolean playing;
    private Thread gameThread = null;
    private Player player;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Enemy[] enemies;
    //created a reference of the class Friend
    private Friend friend;
    private int enemyCount = 1;
    private ArrayList<Star> stars = new ArrayList<>();
    //defining a boom object to display blast
    private Boom boom;
    //a screenX holder
    int screenX;
    int screenY;
    //to count the number of Misses
    int countMisses;
    //indicator that the enemy has just entered the game screen
    boolean[] flag ;
    //an indicator if the game is Over
    private boolean isGameOver ;
    //the score holder
    int score;
    //the high Scores Holder
    int highScore[] = new int[4];
    //Shared Prefernces to store the High Scores
    SharedPreferences sharedPreferences;
    Context context;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;
        countMisses = 0;
        this.context = context;
        isGameOver = false;
        player = new Player(context, screenX, screenY);

        surfaceHolder = getHolder();
        paint = new Paint();

        int starNums = 100;
        for (int i = 0; i < starNums; i++) {
            Star s = new Star(screenX, screenY);
            stars.add(s);
        }
        // add player, enenmies, friend and boom.
        resetRolesOnScreen();

        //setting the score to 0 initially
        score = 0;
        sharedPreferences = context.getSharedPreferences("high score",Context.MODE_PRIVATE);
        //initializing the array high scores with the previous values
        highScore[0] = sharedPreferences.getInt("score1",0);
        highScore[1] = sharedPreferences.getInt("score2",0);
        highScore[2] = sharedPreferences.getInt("score3",0);
        highScore[3] = sharedPreferences.getInt("score4",0);
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        //incrementing score as time passes
        score++;
        player.update();

        //setting boom outside the screen
        boom.setX(0 - boom.getBitmap().getWidth());
        boom.setY(0 - boom.getBitmap().getHeight());

        for (Star s : stars) {
            s.update(player.getSpeed());
        }

        for (int i = 0; i < enemyCount; i++) {
            enemies[i].update(player.getSpeed());
            //setting the flag true when the enemy just enters the screen
            if(enemies[i].getX()== screenX){
                flag[i] = true;
            }

            //if collision occurrs with player
            if (Rect.intersects(player.getDetectCollision(), enemies[i].getDetectCollision())) {
                Log.d("QuitSmokeDebug", "======collision=======");
                //displaying boom at that location
                boom.setX(enemies[i].getX());
                boom.setY(enemies[i].getY());
                enemies[i].setX(0 - enemies[i].getBitmap().getWidth());
            } else{
                //if the enemy has just entered
                if(flag[i]){
                    //if player's x coordinate is more than the enemies's x coordinate.i.e. enemy has just passed across the player
                    if(player.getDetectCollision().left >= enemies[i].getDetectCollision().right){
                        //increment countMisses
                        countMisses++;
                        Log.d("QuitSmokeDebug", "======countMisses=======" + countMisses);
                        //setting the flag false so that the else part is executed only when new enemy enters the screen
                        flag[i] = false;
                        //if no of Misses is equal to 3, then game is over.
                        if(countMisses == 3){
                            //setting playing false to stop the game.
                            playing = false;
                            isGameOver = true;
                            Log.d("QuitSmokeDebug", "======Enemies exceed 3=======");
                        }
                    }
                }
            }
        }

        //updating the friend ships coordinates
        friend.update(player.getSpeed());
        //checking for a collision between player and a friend
        if(Rect.intersects(player.getDetectCollision(),friend.getDetectCollision())){

            //displaying the boom at the collision
            boom.setX(friend.getX());
            boom.setY(friend.getY());
            //setting playing false to stop the game
            playing = false;
            //setting the isGameOver true as the game is over
            isGameOver = true;
            Log.d("QuitSmokeDebug", "======Hit Friend=======");

            //Assigning the scores to the highscore integer array
            for (int i = 0; i < 4; i++){
                if (highScore[i] < score){
                    final int finalI = i;
                    highScore[i] = score;
                    break;
                }
            }

            //storing the scores through shared Preferences
            SharedPreferences.Editor e = sharedPreferences.edit();
            for(int i = 0; i < 4; i++){
                int j = i + 1;
                e.putInt("score" + j, highScore[i]);
            }
            e.apply();
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            paint.setColor(Color.WHITE);
            paint.setTextSize(20);

            for (Star s : stars) {
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }

            paint.setTextSize(30);
            canvas.drawText("Score:" + score,screenX / 2,50,paint);

            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);

            for (int i = 0; i < enemyCount; i++) {
                canvas.drawBitmap(
                        enemies[i].getBitmap(),
                        enemies[i].getX(),
                        enemies[i].getY(),
                        paint
                );
            }

            //drawing boom image
            canvas.drawBitmap(
                    boom.getBitmap(),
                    boom.getX(),
                    boom.getY(),
                    paint
            );

            //drawing friends image
            canvas.drawBitmap(

                    friend.getBitmap(),
                    friend.getX(),
                    friend.getY(),
                    paint
            );

            //draw game Over when the game is over
            if(isGameOver){
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);

                int yPos=(int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Game Over",canvas.getWidth()/2,yPos,paint);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.d("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(e));
        }
    }

    public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
    }

    // reset game context when restart game
    public void reset() {
        isGameOver = false;
        score = 0;
        countMisses = 0;

        // add player, enenmies, friend and boom.
        resetRolesOnScreen();
    }

    private void resetRolesOnScreen() {
        enemies = new Enemy[enemyCount];
        flag = new boolean[enemyCount];
        for (int i = 0; i < enemyCount; i++) {
            enemies[i] = new Enemy(context, screenX, screenY);
            flag[i] = false;
        }
        //initializing boom object
        boom = new Boom(context);
        //initializing the Friend class object
        friend = new Friend(context, screenX, screenY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float y = motionEvent.getY();

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_MOVE:
                player.setBoosting(y);
                break;
        }
        return true;
    }

}