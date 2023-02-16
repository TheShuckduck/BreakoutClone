package com.robertsrewards.ball;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Ball {
    int x, y, radius, xSpeed, ySpeed;
    Color color = Color.WHITE;
    public Ball(int x, int y, int radius, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }
    public void update() {
        x += xSpeed;
        y += ySpeed;
        if (x < radius || x > Gdx.graphics.getWidth() - radius) {
            xSpeed = -xSpeed;
        }
        if (y < radius || y > Gdx.graphics.getHeight() - radius) {
            ySpeed = -ySpeed;
        }
    }

    public void reset() {
        x = 150;
        y = 200;
        xSpeed = 7;
        ySpeed = 3;
    }
    public void draw(ShapeRenderer shape) {
        shape.setColor(color);
        shape.circle(x,y, radius);
    }

    public void checkCollision(Paddle paddle) {
        if (collidesWith(paddle)) {
            color = Color.GREEN;
            ySpeed = - ySpeed;
        }
        else {
            color = Color.WHITE;
        }
    }

    public boolean collidesWith(Paddle paddle) {
        return x + radius > paddle.x
                && x - radius < paddle.x + paddle.width
                && y + radius > paddle.y
                && y - radius < paddle.y + paddle.height;
    }

    public boolean collidesWith(Block block) {
        return x + radius > block.x
                && x - radius < block.x + block.width
                && y + radius > block.y
                && y - radius < block.y + block.height;
    }

    public void reverseY() {
        ySpeed = - ySpeed;
    }
}

