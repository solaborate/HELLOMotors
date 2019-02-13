package com.solaborate.hellomotors;

/**
 * Created by Kushtrim Pacaj on 13/02/19.
 * kushtrimpacaj@gmail.com
 */

public enum MotorMovement {
    UP((byte) 0x02),
    LEFT((byte) 0x03),
    RIGHT((byte) 0x04),
    DOWN((byte) 0x05);

    private byte command;

    MotorMovement(byte command) {
        this.command = command;
    }

    public byte getByteCommand() {
        return command;
    }
}
