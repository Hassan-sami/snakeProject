/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxapplication12;

/**
 *
 * @author Dell
 */
public class Position {
    
        private  int x;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
        private  int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            if (this == o) return true;            
            Position position = (Position) o;
            return x== position.x && y == position.y;
        }

        @Override
        public int hashCode() {
            return 31 * Double.hashCode(x) + 7 * Double.hashCode(y) ;
        }
    
}
