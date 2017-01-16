package com.quascenta.QBlueLogger;

/**
 * Created by AKSHAY on 1/16/2017.
 */

public interface OnOperationListener {

        public abstract void processOperationResult(byte[] mBufferDataCmd);
        public abstract void processOperationNotCompleted();
    }

