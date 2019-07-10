package com.demo.li.vc_room_out.library;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketServer
{
    private Thread thread;
    private ServerSocket serverSocket;
    private int port = 8080;
    private List<SocketClient> socketClients;
    private int restartInterval = 5000;

    private static SocketServer instance;

    public static SocketServer getInstance()
    {
	if (instance == null)
	{
	    instance = new SocketServer();
	}
	return instance;
    }

    private SocketServer()
    {
	socketClients = new ArrayList<SocketClient>();
    }

    public SocketServer setPort(int port)
    {
	this.port = port;
	return this;
    }

    public SocketServer setRestartInterval(int restartInterval)
    {
	this.restartInterval = restartInterval;
	return this;
    }

    public List<SocketClient> getSocketClients()
    {
        return socketClients;
    }

    public void start()
    {
	thread = new Thread(new SocketServerRunnable());
	thread.start();
    }

    public void stop()
    {
	try
	{
	    if (thread != null)
	    {
		thread.interrupt();
		serverSocket.close();
		serverSocket = null;
		thread = null;
		socketClients.clear();
	    }
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
    }

    public void restart()
    {
	stop();
	start();
    }

    private class SocketServerRunnable implements Runnable
    {
	public void run()
	{
	    try
	    {
		Thread.sleep(restartInterval);
		serverSocket = new ServerSocket(port);
		while (!thread.isInterrupted())
		{
		    Socket socket = serverSocket.accept();
		    SocketClient client = new SocketClient(socket);
		    client.start();

		    // ��Ӵ˿ͻ��˵��ͻ��˼���
		    socketClients.add(client);
		}
	    }
	    catch (Exception e)
	    {
		e.printStackTrace();
	    }
	    finally
	    {
		restart();
	    }
	}
    }
}
