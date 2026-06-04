package com.datastruct;

//Class untuk menyimpan informasi terkait lift yang telah dipilih
public class LiftCall {
    private int floor;
    private Direction direction;
    private Lift pointedLift;

    public LiftCall(int floor, Direction direction, Lift pointedLift) {
        this.floor = floor;
        this.direction = direction;
        this.pointedLift = pointedLift;
    }

    public int getFloor(){
        return floor;
    }

    public Direction getDirection(){
        return direction;
    }

    public Lift getPointedLift() {
        return pointedLift;
    }
}
