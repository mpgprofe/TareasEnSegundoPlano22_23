package com.example.tareasensegundoplano22_23;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    Button buttonStart, buttonStop, buttonStart2, buttonStop2, buttonDescontar;
    TextView textViewCrono, textViewCrono2;
    ProgressBar progressBar;

    int contador=0;
    Thread hilo=null;
    boolean hiloActivo = true;
    boolean crono2ON = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonStart = findViewById(R.id.buttonStart);
        buttonStop = findViewById(R.id.buttonStop);
        textViewCrono = findViewById(R.id.textViewCrono);
        buttonStart2 = findViewById(R.id.buttonStart2);
        buttonStop2 = findViewById(R.id.buttonStop2);
        textViewCrono2 = findViewById(R.id.textViewCrono2);
        progressBar = findViewById(R.id.progressBar);
        buttonDescontar = findViewById(R.id.buttonDescontar);

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
                                    Thread.sleep(100);
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

        buttonStart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MiCronometro mc = new MiCronometro(0, textViewCrono2);
                crono2ON = true;
                mc.execute();
            }
        });

        buttonStop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crono2ON = false;
            }
        });
        buttonDescontar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MiCuentaAtras cuentaAtras = new MiCuentaAtras(progressBar);
                //Así se ejecuta en serie: cuentaAtras.execute();
                cuentaAtras.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }

    private class MiCronometro extends AsyncTask<String, String, String>{
        int miContador;
        TextView miTextView;
        MiCronometro(int inicio, TextView tv){
            miContador = inicio;
            miTextView = tv;

        }
        @Override
        protected void onProgressUpdate(String... values) {
            miTextView.setText(values[0]);
        }

        @Override
        protected String doInBackground(String... strings) {
            while(crono2ON){
                int minutos = miContador /60;
                int segundos = miContador %60;
                String salida = String.format("%02d:%02d", minutos, segundos);
                publishProgress(salida);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                miContador++;
            }
            return null;

        }
    }

    class MiCuentaAtras extends AsyncTask<String, String,String>{
        ProgressBar miProgressBar;
        MiCuentaAtras(ProgressBar pb){
            miProgressBar = pb;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            miProgressBar.setProgress(miProgressBar.getProgress()-1);
        }

        @Override
        protected void onPostExecute(String s) {

            buttonStart2.callOnClick();
        }

        @Override
        protected String doInBackground(String... strings) {
            while(miProgressBar.getProgress()>0){
                publishProgress();
                try{
                    Thread.sleep(200);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

}