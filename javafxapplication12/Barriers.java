/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxapplication12;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;

/**
 *
 * @author Dell
 */
public  class Barriers {

    public static List<Position> getBarriers() {
        return barriers;
    }
    private static  List<Position> barriers = new ArrayList();

    public static void setBarriers(List<Position> barriers) {
        Barriers.barriers = barriers;
    }
     
    public static void creaetBarriersWithMouse(Scene scene,int cellSize,Position food){
        
        
        
        scene.setOnMousePressed(event ->{
            
            MouseButton btn = event.getButton();
            if(btn == MouseButton.PRIMARY){

            int x = (int)event.getX()-(  (  (int)event.getX() )  % cellSize  );
            int y = (int)event.getY()-(  (  (int)event.getY() )  % cellSize  );
            Position barrier = new Position (x,y); 
            if(!(barriers.contains(barrier)|| barrier.equals(food))){
                
                System.out.println(x + "   "+ y);
                barriers.add(barrier);
                
                
            }
        }
            
        
        });
    }
    
    public static void removeBarriersWithMouse(Scene scene,int cellSize){
        
            scene.setOnMouseReleased(event->{
                MouseButton btn = event.getButton();
                if(btn == MouseButton.MIDDLE){

                    int x = (int)event.getX()-(  (  (int)event.getX() )  % cellSize  );
                    int y = (int)event.getY()-(  (  (int)event.getY() )  % cellSize  );
                    Position barrier = new Position (x,y); 
                    if(barriers.contains(barrier)){
                        barriers.remove(barrier);
                    }
                    
                }                
                
            
            
            });
        
    
    }
    
    
    
}
