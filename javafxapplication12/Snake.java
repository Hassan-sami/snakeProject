/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package javafxapplication12;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author Dell
 */
public interface Snake {
     void drawSnake(GraphicsContext gc);
     
     void moveSnake(int deltaX , int deltaY, Scene scene );
    
}
