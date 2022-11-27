
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.*;
import java.io.*;
public class Server extends JFrame{


    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;


    //

    private JLabel heading = new JLabel("Client2 Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);



    //constructor
    public Server(){
        try {
            server= new ServerSocket(7778);
            System.out.println("Server is Ready to Accept the Connection");
            System.out.println("Waiting...");
            socket=server.accept(); //accepting connection from the client 

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //socket input stream, InputStreamReader get the data in the byte and sent to the  BufferdStreamReader for Read The Data


            out=new PrintWriter(socket.getOutputStream());//It is the Mediam To Send And Recieve The Data , We Can Send The Data Unidirectional
            createGui();
            handleEvents();
            startReading();
            //startWriting();
        } catch (Exception e) {
           e.printStackTrace();
        }

       
    }
    public void createGui()
    {
        this.setTitle("Client2 Message [END]");//Title of GUI Window
        this.setSize(600,700);
        this.setLocationRelativeTo(null);//It takes Windows To the center
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //code for components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setIcon(new ImageIcon("AppIcon.png"));//Icon

        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        heading.setForeground(Color.green);

        messageArea.setBackground(Color.pink);
        messageArea.setForeground(Color.BLACK);
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        //frame LAyout

        this.setLayout(new BorderLayout());
        
        //adding components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);//this is for scroll bar
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);





        this.setVisible(true);
    }


    public void handleEvents()
    {
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                  //System.out.println("key Released " + e.getKeyCode());
                  if(e.getKeyCode()==10)//10 is key code of Enter Button
                  {
                      String contentToSend =messageInput.getText();
                      messageArea.append("Me      ::"+contentToSend+"\n");
                      out.println(contentToSend);
                      out.flush();
                      messageInput.setText(" ");
                      messageInput.requestFocus();
                  }
                
            }

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
             
            }
            
        });
    }

    
    public void startReading()
    {
        //Thread Runnable r1 read the data
        Runnable r1 = ()->{
            System.out.println("Reader Started...");

            try{
            while(!socket.isClosed())
            {
               
                    String msg=br.readLine();
                    if(msg.equals("exit"))
                    {
                        System.out.println("Client Terminate The Chat...");
                        JOptionPane.showMessageDialog(this,"Client Terminate The Chat..");
                        messageInput.setEnabled(false);
                        socket.close();//used to terminate the chat when client type the exit message
                        break;
                    }
                    messageArea.append("Client ::"+msg+"\n"); 
                    //System.out.println("Client --> " + msg);//it shows Who Messages 
                    
                
            }
            System.out.println("Connection Closed");

        }catch(Exception e)
        {
            System.out.println("Connection Closed");
        }
        };
        new Thread(r1).start();

    }

    public void startWriting(){
        //Thread It takes Data from the user and sent it to the Client
        Runnable r2=()->{
            System.out.println("Writter Started..");
            
            try{
            while(!socket.isClosed())
            {
                
                    BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
                    String content=br.readLine();

                    out.println(content);
                    out.flush();
                    
                    if(content.equals("exit"))
                    {
                        socket.close();//used to close the chat when Server type the exit message
                        break;
                        
                    }
               

            }
           
        }catch(Exception e)
        {
            System.out.println("Connection Closed");
        }
        };
        new Thread(r2).start();


    }
    public static void main(String[] args)
    {
        System.out.println("Server Going To Start..");
        new Server();
    }
}