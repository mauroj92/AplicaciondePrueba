package com.example.maurojuarez.aplicaciondeprueba;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Activity extends AppCompatActivity {

    private EditText edt_correo, edt_CUIT, edt_monto;

    private SeekBar skb_cantidad_dias;
    private TextView txv_cantidad_dias, txv_rendimiento;
    private Button btn_plazo;

    private TextView txv_mensaje;
    int plazo = 0, dias;
    double monto = 0, techo1, techo2;
    double tasa1, tasa2, tasa3, tasa4, tasa5, tasa6;

    double rendimiento = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        inicializarVariables();

        skb_cantidad_dias.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            //EVENTOS
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                plazo = i;
                //Mostrar los días seleccionados
                txv_cantidad_dias.setText(plazo + " días de inversión");
                //Calcular el rendimiento
                try {
                    //monto = Double.parseDouble(edt_monto.getText().toString());
                    rendimiento = calculoInteres(monto,plazo);
                } catch (Exception e) {
                    rendimiento = 0;
                }

                txv_rendimiento.setText("Rendimiento: $ " + String.format("%.2f",rendimiento));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btn_plazo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje;
                //Validación de los EditText correo y CUIT
                if (       edt_correo.getText().toString().matches("")
                        || edt_CUIT.getText().toString().matches("")
                        || edt_monto.getText().toString().matches(""))
                {
                    mensaje = getString(R.string.mensajeIncorrecto);
                    txv_mensaje.setTextColor(getResources().getColor(R.color.colorError,null));
                }
                else {
                    Log.v("rendimiento",String.valueOf(rendimiento + monto));
                    mensaje = getString(R.string.mensajeCorrecto, rendimiento + monto);
                    txv_mensaje.setTextColor(getResources().getColor(R.color.colorCorrecto,null));
                }

                txv_mensaje.setText(mensaje);
                txv_mensaje.setVisibility(View.VISIBLE);
            }
        });

        edt_monto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    monto = Double.parseDouble(edt_monto.getText().toString());
                    rendimiento = calculoInteres(monto,plazo);
                } catch (Exception e) {
                    rendimiento = 0;
                }

                txv_rendimiento.setText("Rendimiento: $" + String.format("%.2f",rendimiento));
            }
        });

    }

    private void inicializarVariables() {
        edt_correo = (EditText) findViewById(R.id.edt_correo);
        edt_CUIT = (EditText) findViewById(R.id.edt_CUIT);
        edt_monto = (EditText) findViewById(R.id.edt_monto);

        txv_cantidad_dias = (TextView) findViewById(R.id.txv_cantidad_dias);
        skb_cantidad_dias = (SeekBar) findViewById(R.id.skb_cantidad_dias);
        txv_rendimiento = (TextView) findViewById(R.id.txv_rendimiento);

        txv_mensaje = (TextView) findViewById(R.id.txv_mensaje);

        btn_plazo = (Button) findViewById(R.id.btn_plazo);

        //Mostrar 0 días seleccionados
        txv_cantidad_dias.setText("0 días de inversión");

        techo1 = Double.parseDouble(getResources().getString(R.string.techo1));
        techo2 = Double.parseDouble(getResources().getString(R.string.techo2));

        dias = Integer.parseInt(getResources().getString(R.string.dias));

        tasa1 = Double.parseDouble((getResources().getString(R.string.tasa1)));
        tasa2 = Double.parseDouble((getResources().getString(R.string.tasa2)));
        tasa3 = Double.parseDouble((getResources().getString(R.string.tasa3)));
        tasa4 = Double.parseDouble((getResources().getString(R.string.tasa4)));
        tasa5 = Double.parseDouble((getResources().getString(R.string.tasa5)));
        tasa6 = Double.parseDouble((getResources().getString(R.string.tasa6)));
    }

    private double calculoInteres(double monto, int plazo) {
        double rendimiento = 0;
        double tasa = 0;

        if (plazo < 30) {
            if (monto >= 0 && monto < techo1) {
                tasa = tasa1;
            }
            else if (monto >= techo1 && monto < techo2) {
                tasa = tasa3;
            }
            else if (monto >= techo2) {
                tasa = tasa5;
            }
        }
        else {
            if (monto >= 0 && monto < techo1) {
                tasa = tasa2;
            }
            else if (monto >= techo1 && monto < techo2) {
                tasa = tasa4;
            }
            else if (monto >= techo2) {
                tasa = tasa6;
            }
        }

        rendimiento = monto * (Math.pow(( 1 + tasa/100.0), plazo/360.0) - 1);

        return rendimiento;
    }

}
