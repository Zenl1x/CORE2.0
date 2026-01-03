package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Детектор цвета
 * Работа с цветовым сенсором для определения объектов
 */
public class ColorDetector {
    
    private final ColorSensor colorSensor;
    
    public ColorDetector(HardwareMap hardwareMap) {
        colorSensor = hardwareMap.get(ColorSensor.class, RobotConfig.COLOR_SENSOR);
    }
    
    /**
     * Проверка, обнаружен ли какой-либо цвет выше порогового значения
     * @return true если любой из каналов RGB выше порога
     */
    public boolean isColorDetected() {
        int red = colorSensor.red();
        int green = colorSensor.green();
        int blue = colorSensor.blue();
        
        return red > RobotConfig.COLOR_DETECTION_THRESHOLD 
            || green > RobotConfig.COLOR_DETECTION_THRESHOLD 
            || blue > RobotConfig.COLOR_DETECTION_THRESHOLD;
    }
    
    /**
     * Получить доминирующий цвет
     * @return "RED", "GREEN", "BLUE" или "NONE"
     */
    public String getDominantColor() {
        int red = colorSensor.red();
        int green = colorSensor.green();
        int blue = colorSensor.blue();
        
        int max = Math.max(red, Math.max(green, blue));
        
        if (max < RobotConfig.COLOR_DETECTION_THRESHOLD) {
            return "NONE";
        }
        
        if (red == max) return "RED";
        if (green == max) return "GREEN";
        return "BLUE";
    }
    
    // Геттеры для сырых значений (для телеметрии)
    public int getRed() {
        return colorSensor.red();
    }
    
    public int getGreen() {
        return colorSensor.green();
    }
    
    public int getBlue() {
        return colorSensor.blue();
    }
}
