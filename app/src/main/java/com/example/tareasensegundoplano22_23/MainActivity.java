package com.example.tareasensegundoplano22_23;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button buttonStart, buttonStop;
    TextView textViewCrono;
    int contador=0;
    Thread hilo=null;
    boolean hiloActivo = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonStart = findViewById(R.id.buttonStart);
        buttonStop = findViewById(R.id.buttonStop);
        textViewCrono = findViewById(R.id.textViewCrono);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hilo==null) {
                    hiloActivo = true;
                    hilo = new Thread() {
                        @Override
                        public void run() {

                            while (hiloActivo) {
                                int minutos = contador / 60;
                                int segundos = contador % 60;

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //textViewCrono.setText(minutos + ":" + segundos);
                                        textViewCrono.setText(String.format("%02d:%02d", minutos, segundos));
                                    }
                                });

                                Log.d("CRONO", minutos + ":" + segundos);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                contador++;

                            }
                        }
                    };
                    hilo.start();
                }


            }
        });
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiloActivo = false;
                hilo =null;
                Log.d("CRONO", "Crono parado");
            }
        });


    }
}