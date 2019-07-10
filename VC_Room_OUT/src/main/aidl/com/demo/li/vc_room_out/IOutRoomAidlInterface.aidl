// IOutRoomAidlInterface.aidl
package com.demo.li.vc_room_out;

// Declare any non-default types here with import statements

interface IOutRoomAidlInterface {
    void sendCallOf (String session,String deviceIP,String deviceNumber,boolean isAutoCallOff);//视频挂掉指令
      void sendCallRequest(String session);
      void setIsCalling(boolean isCalling);
      void sendCallRequestResult(int status,String session,String message);
      int getVideoMode();
}
