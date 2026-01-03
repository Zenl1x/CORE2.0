package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Подсистема клешни робота
 * Управляет всеми аспектами работы клешни:
 * - Выдвижение/втягивание
 * - Подъём/опускание
 * - Поворот
 * - Захват/отпускание
 */
public class ClawSubsystem {
    
    private final DcMotor extendingMotor;
    private final DcMotor liftingMotor;
    
    private final Servo gripServo;
    private final Servo liftServo;
    private final Servo rotateServo;
    
    private boolean isLifted = false;
    
    public ClawSubsystem(HardwareMap hardwareMap) {
        // Инициализация моторов
        extendingMotor = hardwareMap.get(DcMotor.class, RobotConfig.CLAW_EXTENDING_MOTOR);
        liftingMotor = hardwareMap.get(DcMotor.class, RobotConfig.LIFTING_MOTOR);
        
        // Настройка подъёмного мотора
        liftingMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftingMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftingMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        // Инициализация сервоприводов
        gripServo = hardwareMap.get(Servo.class, RobotConfig.CLAW_GRIP_SERVO);
        liftServo = hardwareMap.get(Servo.class, RobotConfig.CLAW_LIFT_SERVO);
        rotateServo = hardwareMap.get(Servo.class, RobotConfig.CLAW_ROTATE_SERVO);
        
        // Установка начальных позиций
        gripServo.setPosition(RobotConfig.CLAW_GRIP_OPEN);
        liftServo.setPosition(RobotConfig.CLAW_LIFT_DOWN);
        rotateServo.setPosition(RobotConfig.CLAW_ROTATE_DEFAULT);
    }
    
    // ==================== ВЫДВИЖЕНИЕ КЛЕШНИ ====================
    
    /**
     * Выдвинуть клешню с заданной мощностью
     * @param power - мощность (0.0 до 1.0)
     */
    public void extend(double power) {
        extendingMotor.setPower(Math.abs(power));
    }
    
    /**
     * Втянуть клешню с заданной мощностью
     * @param power - мощность (0.0 до 1.0)
     */
    public void retract(double power) {
        extendingMotor.setPower(-Math.abs(power));
    }
    
    /**
     * Остановить выдвижение/втягивание
     */
    public void stopExtending() {
        extendingMotor.setPower(0);
    }
    
    // ==================== ПОДЪЁМ КЛЕШНИ ====================
    
    /**
     * Поднять клешню в верхнее положение
     */
    public void liftUp() {
        if (!isLifted) {
            liftServo.setPosition(RobotConfig.CLAW_LIFT_UP);
            setLiftingPosition(RobotConfig.LIFTING_UP_POSITION, RobotConfig.LIFTING_UP_POWER);
            isLifted = true;
        }
    }
    
    /**
     * Опустить клешню в нижнее положение
     */
    public void liftDown() {
        if (isLifted) {
            liftServo.setPosition(RobotConfig.CLAW_LIFT_DOWN);
            setLiftingPosition(RobotConfig.LIFTING_DOWN_POSITION, RobotConfig.LIFTING_DOWN_POWER);
            isLifted = false;
        }
    }
    
    /**
     * Установить среднее положение подъёма
     */
    public void liftMiddle() {
        liftServo.setPosition(RobotConfig.CLAW_LIFT_MIDDLE);
    }
    
    /**
     * Установить нижнее положение сервопривода
     */
    public void liftServoDown() {
        liftServo.setPosition(RobotConfig.CLAW_LIFT_DOWN);
    }
    
    /**
     * Установить целевую позицию для подъёмного мотора
     */
    private void setLiftingPosition(int targetPosition, double power) {
        liftingMotor.setTargetPosition(targetPosition);
        liftingMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftingMotor.setPower(power);
    }
    
    // ==================== ПОВОРОТ КЛЕШНИ ====================
    
    /**
     * Установить клешню в положение по умолчанию (0°)
     */
    public void rotateDefault() {
        rotateServo.setPosition(RobotConfig.CLAW_ROTATE_DEFAULT);
    }
    
    /**
     * Повернуть клешню на 45°
     */
    public void rotate45() {
        rotateServo.setPosition(RobotConfig.CLAW_ROTATE_45);
    }
    
    /**
     * Повернуть клешню на 90°
     */
    public void rotate90() {
        rotateServo.setPosition(RobotConfig.CLAW_ROTATE_90);
    }
    
    /**
     * Повернуть клешню на 180°
     */
    public void rotate180() {
        rotateServo.setPosition(RobotConfig.CLAW_ROTATE_180);
    }
    
    // ==================== ЗАХВАТ ====================
    
    /**
     * Открыть клешню
     */
    public void openGrip() {
        gripServo.setPosition(RobotConfig.CLAW_GRIP_OPEN);
    }
    
    /**
     * Закрыть клешню (захватить)
     */
    public void closeGrip() {
        gripServo.setPosition(RobotConfig.CLAW_GRIP_CLOSED);
    }
    
    // ==================== АВТОМАТИЗАЦИЯ ====================
    
    /**
     * Проверка условия для автоматического захвата
     * Если клешня поднята и достигла определённой высоты, автоматически открываем захват
     */
    public boolean shouldAutoGrip() {
        return liftServo.getPosition() == RobotConfig.CLAW_LIFT_UP 
            && liftingMotor.getCurrentPosition() < -790;
    }
    
    // ==================== ГЕТТЕРЫ ДЛЯ ТЕЛЕМЕТРИИ ====================
    
    public boolean isLifted() {
        return isLifted;
    }
    
    public int getLiftingPosition() {
        return liftingMotor.getCurrentPosition();
    }
    
    public double getGripPosition() {
        return gripServo.getPosition();
    }
    
    public double getLiftPosition() {
        return liftServo.getPosition();
    }
    
    public double getRotatePosition() {
        return rotateServo.getPosition();
    }
    
    public boolean isLiftingBusy() {
        return liftingMotor.isBusy();
    }
}
