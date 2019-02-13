package com.solaborate.hellomotors;


import java.io.File;
import java.io.OutputStream;

/**
 * Created by Kushtrim Pacaj on 13/02/19.
 * kushtrimpacaj@gmail.com
 */

@SuppressWarnings("WeakerAccess")
public class CameraMotorInteractor {

    private SerialPort mSerialPort;
    private OutputStream mOutputStream;


    public void init() {
        try {
            mSerialPort = new SerialPort(new File("/dev/ttyS1"), 9600, 0);
            mOutputStream = mSerialPort.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void destroy() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }


    public void sendCommand(MotorMovement motorMovement) {
        if (motorMovement == null) return;
        byte[] moveCameraCommand = packageCommand(motorMovement.getByteCommand());

        try {
            mOutputStream.write(moveCameraCommand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static byte[] packageCommand(byte command) {

        byte HEAD_DATA_PACKAGE = (byte) 0xAD;
        byte END_DATA_PACKAGE = (byte) 0xBF;

        byte[] data = new byte[6];
        data[0] = HEAD_DATA_PACKAGE;
        data[1] = (byte) 0x04;
        data[2] = command;
        data[3] = (byte) 0x0a;
        data[4] = (byte) (byteToInt(data[1]) + byteToInt(data[2]) + byteToInt(data[3]));
        data[5] = END_DATA_PACKAGE;
        return data;
    }

    private static int byteToInt(byte b) {
        return b & 0xFF;
    }


}
