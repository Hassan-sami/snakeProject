/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package javafxapplication12;

import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author Dell
 */
public interface Food {
    void drawFood(GraphicsContext gc);
    
    
    Position generateFood();
}
