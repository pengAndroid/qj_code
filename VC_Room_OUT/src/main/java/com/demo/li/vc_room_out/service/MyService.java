package com.demo.li.vc_room_out.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.demo.li.vc_room_out.IOutRoomAidlInterface;
import com.demo.li.vc_room_out.library.Global;
import com.demo.li.vc_room_out.library.SettingHepler;
import com.demo.li.vc_room_out.library.entity.CallOff;
import com.demo.li.vc_room_out.library.entity.CallRequest;
import com.demo.li.vc_room_out.library.entity.CallRequestResult;
import com.demo.li.vc_room_out.sockethelper.VisitSender;



public class MyService extends Service {

    private static final String TAG = "MyService";


    public MyService() {
        
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
      return iOutRoomAidlInterface;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    
    private final IOutRoomAidlInterface.Stub iOutRoomAidlInterface = new IOutRoomAidlInterface.Stub() {

        @Override
        public void sendCallOf(String session, String deviceIP, String deviceNumber, boolean isAutoCallOff) throws RemoteException {
            CallOff callOff = new CallOff();
            callOff.session = session;
            callOff.deviceIP = deviceIP;
            callOff.deviceNumber = deviceNumber;
            callOff.isAutoCallOff = isAutoCallOff;
            VisitSender.sendCallOff(callOff);
        }

        @Override
        public void sendCallRequest(String session) throws RemoteException {
            CallRequest callRequest = new CallRequest();
            callRequest.session = session;
            VisitSender.sendCallRequest(callRequest);
        }
        @Override
        public void setIsCalling(boolean isCalling) throws RemoteException{
            Global.isVisitCallMultiProcessToShowActivity = isCalling;
        }

        @Override
        public void sendCallRequestResult(int status, String session, String message) throws RemoteException {
            CallRequestResult requestResult = new CallRequestResult();
            requestResult.session = session;
            requestResult.status = status;
            requestResult.message = message;
            VisitSender.sendCallRequestResult(requestResult);
        }
        @Override
        public int getVideoMode()throws RemoteException {
           return SettingHepler.getVideoMode();
        }
    };

}
