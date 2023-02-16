package com.robertsrewards.ball;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Objects;

public class Paddle implements CollidesWith {
    float x, y, width, height;

    public Paddle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public void update() {
        x = Gdx.input.getX() - width / 2;
        y = 0;
        //y = Gdx.graphics.getHeight() - Gdx.input.getY();  This has the paddle follow my mouse vertically,
        //  I have turned it off to lock it to the bottom.
        //  The above part is REALLY weird.  I needed to
        //  subtract the mouse position from the height since the cursor position returned by Gdx.input.getY()
        //  is counted from the top of the screen while drawing the paddle is from the bottom.
        if (x > Gdx.graphics.getWidth()) {
            x = x + width;
        }
    }
    public void draw(ShapeRenderer shape) {
        shape.rect(x,y, width, height);
    }

    @Override
    public void handleCollision(Ball ball) {
        if (ball.x + ball.radius > x && ball.x - ball.radius < x + width && ball.y + ball.radius > y
                && ball.y - ball.radius < y + height) {
            ball.reverseY();
        }
    }

    public void reset() {
        x = 0;
        y = 0;
    }
}

