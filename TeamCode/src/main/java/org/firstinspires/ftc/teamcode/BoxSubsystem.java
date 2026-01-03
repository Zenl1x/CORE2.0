package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Подсистема управления коробкой
 * Простая система открытия/закрытия коробки через сервопривод
 */
public class BoxSubsystem {
    
    private final Servo boxServo;
    private boolean isOpen = false;
    
    public BoxSubsystem(HardwareMap hardwareMap) {
        boxServo = hardwareMap.get(Servo.class, RobotConfig.BOX_SERVO);
        boxServo.setPosition(RobotConfig.BOX_CLOSED);
    }
    
    /**
     * Открыть коробку
     */
    public void open() {
        boxServo.setPosition(RobotConfig.BOX_OPEN);
        isOpen = true;
    }
    
    /**
     * Закрыть коробку
     */
    public void close() {
        boxServo.setPosition(RobotConfig.BOX_CLOSED);
        isOpen = false;
    }
    
    /**
     * Переключить состояние коробки
     */
    public void toggle() {
        if (isOpen) {
            close();
        } else {
            open();
        }
    }
    
    // Геттеры для телеметрии
    public boolean isOpen() {
        return isOpen;
    }
    
    public double getPosition() {
        return boxServo.getPosition();
    }
}
