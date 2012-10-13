import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;

public class PingClient
{
  public static void main(String[] args)
    throws Exception
  {
    int i = 1000; int j = 0;
    double[] rtt = new double[10];
    double averageTime = 0.0D; double max = 0.0; double min = 100000.0;
    if (args.length != 2) {
      System.out.println("Required arguments: host port");
      return;
    }
    InetAddress localInetAddress = InetAddress.getByName(args[0]);
    int port = Integer.parseInt(args[1]);

    byte[] arrayOfByte = new byte[1024];

    DatagramSocket socket = new DatagramSocket();
    socket.setSoTimeout(i);

    for (int m = 0; m < 10; m++)
    {
      Calendar localCalendar = Calendar.getInstance();
      localCalendar.setTimeInMillis(System.currentTimeMillis());
      Date localDate1 = localCalendar.getTime();

      String str = "PING " + Integer.toString(m) + " " + localDate1.toString() + "\r\n";
      arrayOfByte = str.getBytes();

      DatagramPacket recieve1 = new DatagramPacket(arrayOfByte, arrayOfByte.length, localInetAddress, port);

      socket.send(recieve1);

      DatagramPacket recieve2 = new DatagramPacket(new byte[1024], 1024);
      try
      {
        socket.receive(recieve2);
        localCalendar.setTimeInMillis(System.currentTimeMillis());
        Date localDate2 = localCalendar.getTime();
        rtt[m] = (localDate2.getTime() - localDate1.getTime());
        System.out.println("Reply from " + localInetAddress.toString() + ": bytes: " + arrayOfByte.length + " Time: " + rtt[m] + "ms");
        j++;
      }
      catch (InterruptedIOException localInterruptedIOException)
      {
        System.out.println("Request timed out.");
      }
      Thread.sleep(i);
    }
    for (int m = 0; m < 10; m++)
    {
      if (rtt[m] == 0.0D)
        continue;
      averageTime += rtt[m];
      if (rtt[m] > max)
        max = rtt[m];
      if (rtt[m] < min) {
        min = rtt[m];
      }
    }
    averageTime /= j;
    System.out.println("Average Time: " + averageTime + "ms");
    System.out.println("Max Time: " + max + "ms");
    System.out.println("Min Time: " + min + "ms");
  }
}
