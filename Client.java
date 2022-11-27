import java.net.*;

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

import java.io.*;

public class Client extends JFrame
{
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    
    //
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    public JTextField messageInput=new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);//HERE YOU CAN CHANGE FONT AND THEIR SIZE


    public Client(){
        try {
            System.out.println("Sending Request To Server");
            String Name;
            Name =JOptionPane.showInputDialog("Enter Server IP Address:");

            socket =new Socket(Name,7778);//ip address(127.0.0.1) of server and port number(7777)
            System.out.println("Connection Done");

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out=new PrintWriter(socket.getOutputStream());

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
        this.setTitle("Client Message [END]");//Title of GUI Window
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
        heading.setForeground(Color.green);//its all About The Heading Of The Gui Window

        messageArea.setEditable(false);
        messageArea.setBackground(Color.PINK);
        messageArea.setForeground(Color.BLACK);// it set forground color of all chatting on client side 
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        messageInput.setForeground(Color.MAGENTA);
        //frame LAyout

        this.setLayout(new BorderLayout());
        
        //adding components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);//this is for scroll bar
        
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);//It SETS message typing box location on GUI





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
                      
                      //String a ="\u001b";
                      //String b="\u001B";

                      messageArea.append("Me        ::"+contentToSend +"\n");
                      
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
    {//Read The Data
        Runnable r1 =()->{
            System.out.println("Reader Started.");
           
           try{
            while(!socket.isClosed())
            {
              
                    
                    String msg=br.readLine();
                    if(msg.equals("exit"))
                    {
                        System.out.println("ServerTerminate The Chat..");
                        JOptionPane.showMessageDialog(this,"Server Terminate The Chat..");
                        messageInput.setEnabled(false);
                        socket.close();//used to stop chatting when Server Types the exit message.
                        break;
                    }
                    //System.out.println("Server -->  "+ msg);//it shows Who Messages
                    messageArea.append("Client2 ::"+msg+"\n"); 

                    
               
            }
        }catch(Exception e)
        {
            System.out.println("Connection Closed");
        }
        };
        new Thread(r1).start();

    }
    public void startWriting()
    {
        //Thread, It takes Data From The User And Send To The Server
        Runnable r2 = ()->{
            System.out.println("Writer Started");
        
        try{
            while(!socket.isClosed())
        {
            
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String content = br.readLine();
                out.println(content);
                out.flush();


                if(content.equals("exit"))
                {
                    socket.close();//used to stop chatting when Client types The EXit Message
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


public static void main(String[] args){
    System.out.println("User Is Now Online ");
    new Client();
}
}