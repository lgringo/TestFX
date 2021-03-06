/*
 * Copyright 2013-2014 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Licence for the specific language governing permissions
 * and limitations under the Licence.
 */
package org.loadui.testfx.robots.impl;

import javafx.scene.input.KeyCode;
import org.loadui.testfx.robots.KeyboardRobot;
import org.loadui.testfx.robots.TypeRobot;
import org.loadui.testfx.utils.KeyCodeUtils;
import org.loadui.testfx.utils.keymaps.KeyChar;
import org.loadui.testfx.utils.keymaps.KeyCharMap;

public class TypeRobotImpl implements TypeRobot {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private KeyboardRobot keyboardRobot;
    private KeyCharMap localKeyCharMap;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public TypeRobotImpl(KeyboardRobot keyboardRobot) {
        this(keyboardRobot,null);
    }

    public TypeRobotImpl(KeyboardRobot keyboardRobot, String keyboardLayoutName) {
        this.keyboardRobot = keyboardRobot;
        if(keyboardLayoutName == null){
            localKeyCharMap = KeyCharMap.getDefault();
        }else{
            localKeyCharMap = KeyCharMap.getInstance(keyboardLayoutName);
        }
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void type(KeyCode... keys) {
        keyboardRobot.press(keys);
        keyboardRobot.release(keys);
    }

    @Override
    public void type(char character) {
        if ( localKeyCharMap != null ) {
            KeyChar keyChar = localKeyCharMap.getKeyChar(character);
            if ( keyChar != null ) {
                type(keyChar);
            } else {
                fallbackType(character);
            }
        } else {
            fallbackType(character);
        }
    }

    @Override
    public void type(String text) {
        for (int index = 0; index < text.length(); index++) {
            type(text.charAt(index));
            waitBetweenCharacters(25);
        }
    }

    @Override
    public void erase(int characters) {
        for (int index = 0; index < characters; index++) {
            type(KeyCode.BACK_SPACE);
        }
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    /* Old method that does not use KeyChar */
    private void fallbackType(
        final char character)
    {
        KeyCode keyCode = KeyCodeUtils.findKeyCode(character);
        if ( isNotUpperCase(character) ) {
            typeLowerCase(keyCode);
        } else {
            typeUpperCase(keyCode);
        }
    }

    private boolean isNotUpperCase(char character) {
        return !Character.isUpperCase(character);
    }

    private void typeLowerCase(KeyCode keyCode) {
        type(keyCode);
    }

    private void typeUpperCase(KeyCode keyCode) {
        keyboardRobot.press(KeyCode.SHIFT);
        type(keyCode);
        keyboardRobot.release(KeyCode.SHIFT);
    }

    private void type(
        final KeyChar keyChar)
    {
        KeyCode[] modifiers = keyChar.getModifiersKeyCodes();
        for (KeyCode modifier : modifiers) {
            keyboardRobot.press(modifier);
        }
        type(keyChar.getKeyCode());
        for (KeyCode modifier : modifiers) {
            keyboardRobot.release(modifier);
        }
        if ( keyChar.isDeadKey() ) {
            modifiers = keyChar.getExtraModifiersKeyCodes();
            for (KeyCode modifier : modifiers) {
                keyboardRobot.press(modifier);
            }
            type(keyChar.getExtraKeyCode());
            for (KeyCode modifier : modifiers) {
                keyboardRobot.release(modifier);
            }
        }
    }

    private void waitBetweenCharacters(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException ignore) {}
    }
}
