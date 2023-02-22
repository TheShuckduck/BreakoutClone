package com.gdxdemo.game;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

public class BlockCreator {
    private ArrayList<Block> blocks = new ArrayList<>();

    public ArrayList<Block> createBlocks() {
        int blockWidth = 55;
        int blockHeight = 20;
        int x = 0;
        int y = Gdx.graphics.getHeight() / 2;
        while (y < Gdx.graphics.getHeight()) {
            while (x < Gdx.graphics.getWidth()) {
                if (x + blockWidth <= Gdx.graphics.getWidth()) {
                    blocks.add(new Block(x, y, blockWidth, blockHeight));
                    x += blockWidth + 10;
                } else {
                    break;
                }
            }
            x = 0;
            y += blockHeight + 10;
        }
        return blocks;
    }
}
