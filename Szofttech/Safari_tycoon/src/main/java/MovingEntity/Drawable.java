/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package MovingEntity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 *
 * @author Levente
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS,
    include = JsonTypeInfo.As.PROPERTY,
    property = "@class"
)
public interface Drawable {
    public int[] getHitBoxParams(int x, int y, int tileSize);
    public int[] getDrawImageParams(int x, int y, int tileSize);
    public int[] getRangeBoxParams(int x, int y, int tileSize);
    public Point getCoordinate();
    public int getHp();
    public BufferedImage getIcon();
}
