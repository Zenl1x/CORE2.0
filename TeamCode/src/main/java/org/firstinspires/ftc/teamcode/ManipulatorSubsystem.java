package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Подсистема телескопического манипулятора
 * Управляет выдвижением/втягиванием телескопических моторов
 */
public class ManipulatorSubsystem {
    
    private final DcMotor telescopicLeft;
    private final DcMotor telescopicRight;
    
    private boolean isExtended = false;
    
    public ManipulatorSubsystem(HardwareMap hardwareMap) {
        // Инициализация моторов
        telescopicLeft = hardwareMap.get(DcMotor.class, RobotConfig.TELESCOPIC_LEFT_MOTOR);
        telescopicRight = hardwareMap.get(DcMotor.class, RobotConfig.TELESCOPIC_RIGHT_MOTOR);
        
        // Сброс и настройка энкодеров
        telescopicLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        telescopicRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        telescopicLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        telescopicRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    
    /**
     * Выдвинуть телескопические моторы
     */
    public void extend() {
        if (!isExtended) {
            setPosition(RobotConfig.TELESCOPIC_EXTENDED, RobotConfig.TELESCOPIC_EXTEND_POWER);
            isExtended = true;
        }
    }
    
    /**
     * Втянуть телескопические моторы
     */
    public void retract() {
        if (isExtended) {
            setPosition(RobotConfig.TELESCOPIC_RETRACTED, RobotConfig.TELESCOPIC_RETRACT_POWER);
            isExtended = false;
        }
    }
    
    /**
     * Установить целевую позицию для обоих моторов
     */
    private void setPosition(int targetPosition, double power) {
        telescopicLeft.setTargetPosition(targetPosition);
        telescopicRight.setTargetPosition(targetPosition);
        
        telescopicLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        telescopicRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        telescopicLeft.setPower(power);
        telescopicRight.setPower(power);
    }
    
    /**
     * Проверка, достигли ли моторы целевой позиции
     */
    public boolean isAtTarget() {
        return !telescopicLeft.isBusy() && !telescopicRight.isBusy();
    }
    
    /**
     * Остановка моторов
     */
    public void stop() {
        telescopicLeft.setPower(0);
        telescopicRight.setPower(0);
    }
    
    // Геттеры для телеметрии
    public boolean isExtended() {
        return isExtended;
    }
    
    public int getLeftPosition() {
        return telescopicLeft.getCurrentPosition();
    }
    
    public int getRightPosition() {
        return telescopicRight.getCurrentPosition();
    }
}
