package com.example.parcial1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.parcial1.model.Recordatorio;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button buttonRojo, buttonVerde, buttonAmarillo, vistaButton, confirmarButton;
    EditText recordatorioText, posX, posY;

    private String importancia;

    // Variables TCP
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    ArrayList<Recordatorio> grupo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referenciar
        buttonRojo = findViewById(R.id.buttonRojo);
        buttonVerde = findViewById(R.id.buttonVerde);
        buttonAmarillo = findViewById(R.id.buttonAmarillo);
        vistaButton = findViewById(R.id.vistaButton);
        confirmarButton = findViewById(R.id.confirmarButton);
        recordatorioText = findViewById(R.id.recordatorioText);
        posX = findViewById(R.id.posX);
        posY = findViewById(R.id.posY);
        System.out.println("<<<");
        initClient();

        buttonVerde.setOnClickListener(
                (v) -> {
                    importancia = "verde";
                }
        );

        buttonAmarillo.setOnClickListener(
                (v) -> {
                    importancia = "amarillo";
                }
        );
        buttonRojo.setOnClickListener(
                (v) -> {
                    importancia = "rojo";
                }
        );

        vistaButton.setOnClickListener(
                (v) -> {
                    Gson gson = new Gson();

                    String texto = recordatorioText.getText().toString();
                    int x = Integer.parseInt(posX.getText().toString());
                    int y = Integer.parseInt(posY.getText().toString());

                    for (int i = 0; i < grupo.size(); i++) {
                        Recordatorio obj = grupo.get(i);
                        String tipo = obj.getTipo();
                        if (tipo.equals("vista")) {
                            grupo.remove(i);
                        }

                    }


                    grupo.add(new Recordatorio(x, y, importancia, texto, "vista"));
                    String json = gson.toJson(grupo);
                    sendMessage(json);


                }
        );

        confirmarButton.setOnClickListener(
                (v) -> {
                    Gson gson = new Gson();

                    String texto = recordatorioText.getText().toString();
                    int x = Integer.parseInt(posX.getText().toString());
                    int y = Integer.parseInt(posY.getText().toString());
                    for (int i = 0; i < grupo.size(); i++) {
                        Recordatorio obj = grupo.get(i);
                        String tipo = obj.getTipo();
                        if (tipo.equals("vista")) {
                            grupo.remove(i);
                        }

                    }

                    grupo.add(new Recordatorio(x, y, importancia, texto, "confirmado"));
                    String json = gson.toJson(grupo);
                    sendMessage(json);


                }
        );


    }

    public void initClient() {
        new Thread(
                () -> {
                    try {
                        //192.168.1.2
                        System.out.println("Conectando con el server...<<<<");
                        socket = new Socket("10.0.2.2", 5000); //Con emulador 10.0.2.2
                        System.out.println("Conectados<<<<");

                        InputStream is = socket.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        reader = new BufferedReader(isr);

                        OutputStream os = socket.getOutputStream();
                        OutputStreamWriter osw = new OutputStreamWriter(os);
                        writer = new BufferedWriter(osw);

                        while (true) {
                            System.out.println("Esperando...<<<");
                            String line = reader.readLine();
                            System.out.println("<<< Recibido: " + line);

                        }

                    } catch (UnknownHostException e) {
                        e.printStackTrace();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

        ).start();
    }

    public void sendMessage(String msg) {
        new Thread(
                () -> {
                    try {
                        writer.write(msg + "\n");
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }


}