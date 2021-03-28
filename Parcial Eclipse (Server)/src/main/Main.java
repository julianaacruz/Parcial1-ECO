package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.Gson;

import model.Recordatorio;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

public class Main extends PApplet{
	
	private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    
    private ArrayList <PVector> pos;
    private ArrayList <String> recs;
    private PVector v = new PVector(-200,-200);
    private String vistaText = "", vistaColor = "";
    

	public static void main(String[] args) {
		PApplet.main("main.Main");
	}
	
	public void settings() {
		size(1000,600);
	}
	
	public void setup() {
		pos = new ArrayList<PVector>();
		recs = new ArrayList<String>();

		initServer();
		rectMode(CORNER);
		//String[] fontList = PFont.list();
		//printArray(fontList);
		PFont myFont = createFont("Futura-Bold", 22);
		  textFont(myFont);
	}
	
	public void draw () {
		background(222,229,232);
		fill(51, 58, 65);
		textSize(22);
		text("Mis recordatorios",50, 50);
		fill(51, 58, 65,65);
		textSize(12);
		text("Ubique recordatorios en la posición que desee desde su celular.",50, 80);
		
		
		
		for(int i=0; i<pos.size(); i++) { 
			PVector p = pos.get(i);
			String r = recs.get(i);
			String[] rec = r.split(",");
			String t = rec[0];
			String c = rec[1];
			fill(255);
			rect(p.x, p.y, 150, 70, 7);
			fill(51, 58, 65);
			textAlign(LEFT);
			text(t,p.x+7, p.y+16, 135, 54);
			
			if (c.equals("verde")) {
				fill(61, 207, 122);
			}else if (c.equals("amarillo")) {
				fill(242, 215, 79);
			}else if (c.equals("rojo")) {
				fill(242, 101, 57);
			}
			rect(p.x, p.y, 150, 10, 7,7,0,0);
		}
		
		noStroke();
		fill(255);
		rect(v.x, v.y, 150, 70, 7);
		fill(51, 58, 65);
		textAlign(LEFT);
		text(vistaText,v.x+7, v.y+16, 136, 54);
		
		if (vistaColor.equals("verde")) {
			fill(61, 207, 122);
		}else if (vistaColor.equals("amarillo")) {
			fill(242, 215, 79);
		}else if (vistaColor.equals("rojo")) {
			fill(242, 101, 57);
		}
		rect(v.x, v.y, 150, 10, 7,7,0,0);

	}
	

	public void initServer() {
		new Thread(
				()->{
					try {
					
						InetAddress inetAddress = InetAddress.getLocalHost();
						String ip = inetAddress.getHostAddress();
						System.out.println(ip);
						
						boolean conectado = inetAddress.isReachable(1000);
						System.out.println("Conectado: " + conectado);


						ServerSocket server = new ServerSocket(5000);
						System.out.println("Esperando conexión en el canal 5000...");
						socket = server.accept();
						
						System.out.println("Cliente conectado");
						
						InputStream is = socket.getInputStream();
						InputStreamReader isr = new InputStreamReader(is);
						reader = new BufferedReader(isr);
						
						OutputStream os = socket.getOutputStream();
                        OutputStreamWriter osw = new OutputStreamWriter(os);
                        writer = new BufferedWriter(osw);

						
						 while(true){
	                            System.out.println("Esperando mensaje...");
	                            String line = reader.readLine();
	                            System.out.println("Recibido: " + line);
	                            
	                            Gson gson = new Gson();
	                            Recordatorio [] objs = gson.fromJson(line, Recordatorio[].class);
	                            
	                            for (int i = 0; i<objs.length ; i++ ) {
	                            Recordatorio obj = objs[i];
	                            
	                            String tipo = obj.getTipo();
	                             
	                            if (tipo.equals("confirmado")) {
		                        String r = obj.getTexto()+","+ obj.getImportancia();
		                        //System.out.println(r);
		                        recs.add(r);
		                        //System.out.println("added");
	                            PVector p = new PVector(obj.getX(), obj.getY());
	                            v = new PVector(-200, -200);
		                        vistaText = "";
		                        vistaColor = "";
	                            pos.add(p);
	                            }else if (tipo.equals("vista")){
		                        v = new PVector(obj.getX(), obj.getY());
		                        vistaText = obj.getTexto();
		                        vistaColor = obj.getImportancia();
;
	                            }

	                            	}
	                        }

					}catch(IOException e) {
					e.printStackTrace();
					}
				}
				).start();
	}
	 
	public void sendMessage(String msg){
	        new Thread(
	                ()->{
	                    try{
	                        writer.write(msg+"/n");
	                        writer.flush();
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                }
	                ).start();
	    }
}
