package com.vaja.util;


import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerTool {

    public static void main(String[] args) {
        TexturePacker.process("res/graphics_unpacked/tiles/red"
                            ,"res/graphics_unpacked/tiles_packed"
                            , "textures");
    }
}
