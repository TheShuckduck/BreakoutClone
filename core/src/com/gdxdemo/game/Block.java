package com.gdxdemo.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Block implements CollidesWith{
    int x, y, width, height;
    public Block(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public static void createBlocks() {
    }

    public void draw(ShapeRenderer shape) {
        shape.rect(x, y, width, height);
    }

    @Override
    public void handleCollision(Ball ball) {
        if (ball.x + ball.radius > x && ball.x - ball.radius < x + width
                && ball.y + ball.radius > y && ball.y - ball.radius < y + height) {
            ball.reverseY();
        }
    }
}