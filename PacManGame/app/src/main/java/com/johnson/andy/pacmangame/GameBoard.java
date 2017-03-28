package com.johnson.andy.pacmangame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * Created by AndyComp on 11/19/2016.
 */

public class GameBoard extends View {

    int[][] boardLevelOne;
    int[][] boardLevelTwo;
    int[][] currentBoard;
    final double ghostSpeedLevel1 = .125;
    final double ghostSpeedLevel2 = .25;
    double currentSpeed;
    int currentDirection;
    int currentLevel;
    int cakeCount;
    Paint wall, notWall2, notWall, circle, pac, ghost, currentWall;
    RectF square = new RectF();
    float pacX, pacY;
    float[] gX;
    float[] gY;
    int[] gD = new int[8];

    MediaPlayer mp;
    boolean hitGhost = false;

    int oldPacDir = 3;
    int numGhostsLevelOne;
    int numGhostsLevelTwo;
    int currentGhostCount;

    public GameBoard(Context context) {
        super(context);
        setFields();
    }

    public GameBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFields();
    }

    public GameBoard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFields();
    }

    public void setFields() {
        pacX = 0;
        pacY = 0;
        setColors();
        setBoardLevel1();
        currentBoard = boardLevelOne;
        cakeCount = getCakes();

    }

    public void setColors() {
        circle = new Paint();
        circle.setStyle(Paint.Style.FILL);
        circle.setColor(Color.CYAN);

        notWall = new Paint();
        notWall.setStyle(Paint.Style.FILL);
        notWall.setColor(Color.BLUE);

        notWall2 = new Paint();
        notWall2.setStyle(Paint.Style.FILL);
        notWall2.setColor(Color.MAGENTA);

        wall = new Paint();
        wall.setStyle(Paint.Style.FILL);
        wall.setColor(Color.BLACK);

        pac = new Paint();
        pac.setStyle(Paint.Style.FILL);
        pac.setColor(Color.GREEN);

        ghost = new Paint();
        ghost.setStyle(Paint.Style.FILL);
        ghost.setColor(Color.WHITE);


    }

    public void update(int direction) {
        movePac(direction, pacX, pacY);
        for (int i = 0; i < currentGhostCount; i++) {
            moveGhost(gX[i], gY[i], i + 1);
        }

        // invalidate();
    }

    public void goLevelTwo() {
        setBoardLevel2();
        currentBoard = boardLevelTwo;
        cakeCount = getCakes();
        invalidate();
    }

    public void moveGhost(float X, float Y, int ghostNum) {
        if (gD[ghostNum - 1] == 0)
            gD[ghostNum - 1] = randomNumberBetween(1, 4);

        int direction = randomNumberBetween(1, 4);
        if (gD[ghostNum - 1] != direction) {
            int wholeX = (int) (X * 10) % 10;
            int wholeY = (int) (Y * 10) % 10;
            if (wholeX != 0 || wholeY != 0)
                direction = gD[ghostNum - 1];
        }


        if (direction == 1) // UP
        {
            if (Y - currentSpeed >= 0) {
                if ((Y * 10) % 10 == 0) // whole number
                {
                    int indexY = (int) Y;
                    if (indexY - 1 >= 0) {
                        if (currentBoard[indexY - 1][(int) X] != 0) {
                            Y -= currentSpeed;
                        } else {
                            moveGhost(X, Y, ghostNum);
                            return;
                        }
                    }

                } else {
                    Y -= currentSpeed;
                }
            } else {
                moveGhost(X, Y, ghostNum);
                return;
            }
        } else if (direction == 2) // LEFT
        {
            if (X - currentSpeed >= 0) {

                if ((X * 10) % 10 == 0) // whole number
                {
                    int indexX = (int) X;
                    if (indexX - 1 >= 0) {
                        if (currentBoard[(int) Y][indexX - 1] != 0) {
                            X -= currentSpeed;
                        } else {
                            moveGhost(X, Y, ghostNum);
                            return;
                        }
                    }
                } else {
                    X -= currentSpeed;
                }
            } else {
                moveGhost(X, Y, ghostNum);
                return;
            }

        } else if (direction == 3) // RIGHT
        {
            if (X + currentSpeed <= 13) {
                if ((X * 10) % 10 == 0) // whole number
                {
                    int indexX = (int) X;
                    if (indexX + 1 <= 13) {
                        if (currentBoard[(int) Y][indexX + 1] != 0) {
                            X += currentSpeed;
                        } else {
                            moveGhost(X, Y, ghostNum);
                            return;
                        }
                    }
                } else {
                    X += currentSpeed;
                }
            } else {
                moveGhost(X, Y, ghostNum);
                return;
            }
        } else if (direction == 4)// DOWN
        {
            if (Y + currentSpeed <= 13) {
                if ((Y * 10) % 10 == 0) // whole number
                {
                    int indexY = (int) Y;
                    if (indexY + 1 <= 13) {
                        if (currentBoard[indexY + 1][(int) X] != 0) {
                            Y += currentSpeed;
                        } else {
                            moveGhost(X, Y, ghostNum);
                            return;
                        }
                    }
                } else {
                    Y += currentSpeed;
                }
            } else {
                moveGhost(X, Y, ghostNum);
                return;
            }
        }
        gX[ghostNum - 1] = X;
        gY[ghostNum - 1] = Y;
        gD[ghostNum - 1] = direction;
        invalidate();

    }


    public void movePac(int direction, float X, float Y) {

        if (oldPacDir != direction) {
            System.out.println("New direction");
            int wholeX = (int) (X * 10) % 10;
            int wholeY = (int) (Y * 10) % 10;
            if (wholeX != 0 || wholeY != 0)
                direction = oldPacDir;
        }
        if (direction == 1) // UP
        {
            if (Y - .25 >= 0) {
                if ((Y * 10) % 10 == 0) // whole number
                {
                    int indexY = (int) Y;
                    if (indexY - 1 >= 0) {
                        if (currentBoard[indexY - 1][(int) X] != 0) {
                            Y -= .25;
                        }
                    }

                } else {
                    Y -= .25;
                }
            }
        } else if (direction == 2) // LEFT
        {
            if (X - .25 >= 0) {

                if ((X * 10) % 10 == 0) // whole number
                {
                    int indexX = (int) X;
                    if (indexX - 1 >= 0) {
                        if (currentBoard[(int) Y][indexX - 1] != 0) {
                            X -= .25;
                        }
                    }
                } else {
                    X -= .25;
                }
            }

        } else if (direction == 3) // RIGHT
        {
            if (X + .25 <= 13) {
                if ((X * 10) % 10 == 0) // whole number
                {
                    int indexX = (int) X;
                    if (indexX + 1 <= 13) {
                        if (currentBoard[(int) Y][indexX + 1] != 0) {
                            X += .25;
                        }
                    }
                } else {
                    X += .25;
                }
            }
        } else if (direction == 4)// DOWN
        {
            if (Y + .25 <= 13) {
                if ((Y * 10) % 10 == 0) // whole number
                {
                    int indexY = (int) Y;
                    if (indexY + 1 <= 13) {
                        if (currentBoard[indexY + 1][(int) X] != 0) {
                            Y += .25;
                        }
                    }
                } else {
                    Y += .25;
                }
            }
        }
        pacX = X;
        pacY = Y;
        oldPacDir = direction;
        currentDirection = direction;
        invalidate();

    }

    public int[][] getBoardLevelOne() {
        return boardLevelOne;
    }


    public void setBoardLevel1() {
        boardLevelOne = new int[][]
                {
                        {2, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1},
                        {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1},
                        {1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1},
                        {1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0},
                        {1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0},
                        {1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1},
                        {1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0},
                        {0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0},
                        {1, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0},
                        {1, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1},
                        {1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1},
                        {1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1},
                        {1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1},
                        {1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1},
                };

        float[] gX1 = {7, 0, 13, 5};
        gX = new float[gX1.length];
        gX = gX1;
        float[] gY1 = {0, 13, 1, 5};
        gY = new float[gY1.length];
        gY = gY1;
        pacX = pacY = 0;
        currentWall = notWall;
        currentLevel = 1;
        numGhostsLevelOne = 2;
        currentSpeed = ghostSpeedLevel1;
        currentGhostCount = numGhostsLevelOne;
    }

    public void setBoardLevel2() {
        boardLevelTwo = new int[14][14];
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 14; j++) {
                if (j == 0 || j == 13 || i == 0 || i == 13 || i % 2 == 1) {
                    boardLevelTwo[i][j] = 1;
                } else
                    boardLevelTwo[i][j] = 0;
            }
        }
        boardLevelTwo[0][0] = 2;
        boardLevelTwo[12][4] = 1;
        boardLevelTwo[12][9] = 1;
        boardLevelTwo[10][6] = 1;
        boardLevelTwo[8][4] = 1;
        boardLevelTwo[8][9] = 1;
        boardLevelTwo[6][7] = 1;
        boardLevelTwo[4][4] = 1;
        boardLevelTwo[4][9] = 1;
        boardLevelTwo[2][6] = 1;

        float[] gX1 = {0, 4, 13, 13, 6, 6, 13, 9};
        gX = new float[gX1.length];
        gX = gX1;
        float[] gY1 = {13, 5, 9, 0, 2, 10, 13, 3};
        gY = new float[gY1.length];
        gY = gY1;

        pacX = 0;
        pacY = 0;
        currentLevel = 2;
        currentWall = notWall2;
        currentSpeed = ghostSpeedLevel2;
        currentGhostCount = numGhostsLevelTwo;
    }

    public void update(int pacX, int pacY) {
        this.pacX = pacX;
        this.pacY = pacY;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {

        int offSet = getMeasuredWidth() / 14;
        for (int y = 0; y < 14; y++) {
            for (int x = 0; x < 14; x++) {
                square.set(x * offSet, y * offSet, x * offSet + offSet, y * offSet + offSet);

                // draw blue spot
                if (currentBoard[y][x] == 1) {
                    canvas.drawRect(square, currentWall);

                    canvas.drawCircle(square.left + offSet / 2, square.top + offSet / 2, 5, circle);
                }

                // draw black spot
                else if (currentBoard[y][x] == 0) {
                    canvas.drawRect(square, wall);
                }
                // draw the cake
                else if (currentBoard[y][x] == 2) {
                    canvas.drawRect(square, currentWall);
                }

            }
        }

        canvas.drawCircle(pacX * offSet + offSet / 2, pacY * offSet + offSet / 2, 15, pac);
        Path mouth = new Path();

        float X = pacX * offSet + offSet / 2;
        float Y = pacY * offSet + offSet / 2;

        int testX = (int) (pacX * 100) % 100;
        int testY = (int) (pacY * 100) % 100;
        boolean drawMouth = false;

        mouth.moveTo(X, Y);
        if (currentDirection == 1) // UP
        {
            if (testY >= 50 && testY <= 75)
                drawMouth = true;

            mouth.lineTo(X - 12, Y - 17);
            mouth.lineTo(X + 12, Y - 17);
        } else if (currentDirection == 2) // LEFT
        {
            if (testX >= 50 && testX <= 75)
                drawMouth = true;
            mouth.lineTo(X - 17, Y - 12);
            mouth.lineTo(X - 17, Y + 12);
        } else if (currentDirection == 3) // RIGHT
        {
            if (testX >= 50 && testX <= 75)
                drawMouth = true;
            mouth.lineTo(X + 17, Y - 12);
            mouth.lineTo(X + 17, Y + 12);
        } else if (currentDirection == 4) // DOWN
        {
            if (testY >= 50 && testY <= 75)
                drawMouth = true;
            mouth.lineTo(X - 12, Y + 17);
            mouth.lineTo(X + 12, Y + 17);
        }
        mouth.close();

        if (drawMouth)
            canvas.drawPath(mouth, currentWall);

        int cakeX = -1;
        int cakeY = -1;

        // if whole number
        if ((pacX * 10) % 10 == 0 && (pacY * 10) % 10 == 0) {
            cakeX = (int) pacX;
            cakeY = (int) pacY;

            if (currentBoard[cakeY][cakeX] == 1) {
                currentBoard[cakeY][cakeX] = 2;
                // play cake sound
                cakeSound();
                cakeCount--;
            }
        }


        for (int i = 0; i < currentGhostCount; i++) {
            canvas.drawCircle(gX[i] * offSet + offSet / 2, gY[i] * offSet + offSet / 2, 15, ghost);

            if (gX[i] == pacX && gY[i] == pacY) {
                hitGhost = true;
            } else if (gX[i] + currentSpeed == pacX && gY[i] == pacY) {
                hitGhost = true;
            } else if (gX[i] - currentSpeed == pacX && gY[i] == pacY) {
                hitGhost = true;
            } else if (gY[i] + currentSpeed == pacY && gX[i] == pacX) {
                hitGhost = true;
            } else if (gY[i] - currentSpeed == pacY && gX[i] == pacX) {
                hitGhost = true;
            }
        }

    }

    public void cheat() {
        for (int x = 0; x < 14; x++) {
            for (int y = 0; y < 14; y++) {
                if (currentBoard[y][x] == 1) {
                    currentBoard[y][x] = 2;
                }
            }
        }
        currentBoard[0][2] = 1;
        cakeCount = 1;
    }


    public int getCakeCount() {
        return cakeCount;
    }


    public int getCakes() {
        int count = 0;
        for (int x = 0; x < 14; x++) {
            for (int y = 0; y < 14; y++) {
                if (currentBoard[y][x] == 1) {
                    count++;
                }
            }
        }
        return count;
    }

    public int randomNumberBetween(int min, int max) {
        {
            Random rand = new Random();
            int randomInt = rand.nextInt((max - min + 1)) + min;
            return (randomInt);
        }
    }

    public int getCurrentLevel() {
        return currentLevel;
    }


    public boolean hitGhost() {
        return hitGhost;
    }

    public void restart() {
        if (currentLevel == 1) {
            setBoardLevel1();
            currentBoard = boardLevelOne;
        } else {
            setBoardLevel2();
            currentBoard = boardLevelTwo;
        }
        cakeCount = getCakes();
        hitGhost = false;
        invalidate();
    }

    public void updateGhosts(int numGhosts, int addGhosts) {
        numGhostsLevelOne = numGhosts;
        numGhostsLevelTwo = numGhosts + addGhosts;
        if (currentLevel == 1) {
            currentGhostCount = numGhostsLevelOne;
        } else
            currentGhostCount = numGhostsLevelTwo;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("state", super.onSaveInstanceState());
        bundle.putFloatArray("gx", gX);
        bundle.putFloatArray("gy", gY);
        bundle.putIntArray("gd", gD);
        bundle.putIntArray("board", convertBoardTo1DArray());
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            state = bundle.getParcelable("state");
            gX = bundle.getFloatArray("gx");
            gY = bundle.getFloatArray("gy");
            gD = bundle.getIntArray("gd");
            convertBoardTo2DArray(bundle.getIntArray("board"));

        }
        super.onRestoreInstanceState(state);
    }

    public int[] convertBoardTo1DArray() {
        int[] newArray = new int[196];

        int k = 0;
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 14; j++) {
                newArray[k] = currentBoard[i][j];
                k++;
            }
        }

        return newArray;
    }

    public void convertBoardTo2DArray(int[] board) {
        int k = 0;
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 14; j++) {
                currentBoard[i][j] = board[k];
                k++;
            }
        }
    }

    public void cakeSound() {
        if (mp != null) {
            mp.pause();
            mp.seekTo(0);
            mp.start();
        } else {
            mp = MediaPlayer.create(getContext(), R.raw.eat);
            mp.setVolume(0.9f, 0.9f);
            mp.start();
        }
    }
}
