package com.example.securite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class vigenere extends AppCompatActivity {


    EditText plainText,  keyText;
    TextView cryptedText;

    String text, result, key;

    boolean encode;

    //indice de la clée lors du parcourt
    int i_key = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vigenere);

        plainText = findViewById(R.id.textClair);
        cryptedText = findViewById(R.id.textCrypted);
        keyText = findViewById(R.id.textkey);
    }


    public void encodeVigenere(View v){
        encode = true;
        vigenere();
        //on affiche le résultat
        cryptedText.setText(result);
    }

    public void decodeVigenere(View v){
        encode = false;
        vigenere();
        //on affiche le résultat
        cryptedText.setText(result);
    }

    protected void vigenere(){
        //On récupère le texte ainsi que la clée
        text = plainText.getText().toString();
        key = keyText.getText().toString();
        result = "";

        //on récupère les lettres courantes
        int val_lettre;
        int indice_key;

        //Si le texte est écrit en héxadecimale
        String nb_hexa = "";

        //on parcourt toutes les lettres du message
        for(int i_text=0; i_text<text.length(); i_text++){
            val_lettre = text.charAt(i_text);
            //Si la lettre récupérée est notée en hexadecimale
            if((char)val_lettre == '\\'){
                //on recupere la valeur hexadecimale
                nb_hexa += (char) (text.charAt(i_text+2));
                nb_hexa += (char) (text.charAt(i_text+3));

                //on convertit la lettre en int
                val_lettre = Integer.parseInt(nb_hexa, 16);
                //on incrémente i
                i_text = i_text + 3;
                //on réinitialise nb_hexa si on en lit d'autre
                nb_hexa = "";
            }

            //Si la lettre récupérée au dans la table ASCII étendue
            if(val_lettre > 127)
                val_lettre = ExtendedAscii.getIndexAsciiExtended(val_lettre);

            indice_key = key.charAt(i_key % key.length());
            //on récupère le décalage que l'on doit faire si la lettre de la clée courante n'est pas dans la table Ascii normale
            indice_key = getIndiceKey(indice_key);

            //on effectue le décalage de la lettre du message par rapport à la lettre de la clée
            //on encode
            if(encode)
                val_lettre = ((val_lettre + indice_key) % 256);
            else {
                //on décode
                val_lettre = val_lettre - indice_key;
                if(val_lettre < 0){
                    val_lettre = 256 + val_lettre;
                    val_lettre = ExtendedAscii.getIndexAsciiExtended(val_lettre);
                }
            }

            //on l'ajoute au result
            if((val_lettre >= 0 && val_lettre < 32) || val_lettre == 127 || val_lettre == 255 || (val_lettre == 32 && encode)){
                //le if/else sert permet que toutes les notations hexadecimales sont de la forme \x..
                if(val_lettre < 16)
                    result += "\\x0" + Integer.toHexString(val_lettre);
                else
                    result += "\\x" + Integer.toHexString(val_lettre);

            } else {
                //nous pouvons représenter la lettre ou l'espace normalement
                result += ExtendedAscii.getAscii(val_lettre);
            }

        }
    }

    protected int getIndiceKey(int val){
        //Si le caractère de la clée est écrit en hexadécimale
        if((char)val == '\\'){
            String temp = "";
            temp += (char) key.charAt(i_key+2);
            temp += (char) key.charAt(i_key+3);

            //on convertit la lettre en int
            val = Integer.parseInt(temp, 16);
            //on incrémente suffisament i_key
            i_key = i_key + 3;
        }

        //Si le caractère est dans la table ASCII étendue
        if(val > 127)
            val = ExtendedAscii.getIndexAsciiExtended(val);

        //pour que le prochain passage dans la boucle, i_key désigne le bon caractère
        i_key++;
        return(val);
    }

    //Termine l'activité
    public void endActivity(View v){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }



}