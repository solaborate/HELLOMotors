# TOP_PATH refers to the project root dir
TOP_PATH := $(call my-dir)
APP_ABI := x86 x86_64 armeabi-v7a arm64-v8a

# Build "hello_motors" library
include $(CLEAR_VARS)
LOCAL_PATH := $(TOP_PATH)/motors
LOCAL_MODULE    := hello_motors
LOCAL_SRC_FILES := SerialPort.c
LOCAL_LDLIBS    := -llog
LOCAL_C_INCLUDES := $(LOCAL_PATH)
include $(BUILD_SHARED_LIBRARY)