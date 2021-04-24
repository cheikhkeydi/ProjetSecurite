package com.example.securite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class Hill extends AppCompatActivity {
    EditText A, B, C, D;
    EditText textClair;
    TextView textcode;


    String text, result;

    //Les constantes nécessaires
    int[][] matrice = new int[2][2];

    private static final int MODULO = 256;

    //on met l'indice de parcours en attribut pour qu'on puisse l'utiliser et le modifier dans une fonction différente
    //de laquelle on fait le parcours
    int indice;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hill);

        textClair= findViewById(R.id.textClair);
        textcode = findViewById(R.id.textCrypted);
        A = findViewById(R.id.valKeyA);
        B = findViewById(R.id.valKeyB);
        C = findViewById(R.id.valKeyC);
        D = findViewById(R.id.valKeyD);



    }


    //chiffrement du message
    public void encodeHill(View v){
        // test du determinant

        System.out.println("  Chiffrement");

        if(recupMat() == -1)
            return;

        System.out.println("Determinant = " + recupMat());

        //On récupère le message à crypter
        text = textClair.getText().toString();
        result = "";

        System.out.println("Matrice : ");
        System.out.print("a = "+ matrice[0][0] + ", ");
        System.out.print("b = "+ matrice[0][1] + ", ");
        System.out.print("c = "+ matrice[1][0] + ", ");
        System.out.println("d = "+ matrice[1][1] + ", ");
        //on lance Hill
        hill(true);
    }

    //dechiffrement  du message
    public void decodeHill(View v){
        System.out.println(" Dechiffrement");
        // test du determinant

        int determinant;
        determinant = recupMat();
        if(determinant == -1)
            return;

        System.out.println("Determinant = " + determinant);

        //  calcul dela nouvelle matrice
        //si le determinant est negatif, on lui ajoute 256 pour qu'il devient positif tout en restant % 256
        while(determinant < 0){
            determinant += MODULO;
        }
        //calcul de l'inverse du determinant
        int inverse = getInverse(determinant, MODULO);
        System.out.println("Inverse % 256 = " + inverse);
        //on modifie la matrice
        int constA = matrice[0][0];
        int constB = matrice[0][1];
        int constC = matrice[1][0];
        int constD = matrice[1][1];

        matrice[0][0] = constD * inverse;
        matrice[0][1] = constB * inverse * -1;
        matrice[1][0] = constC * inverse * -1;
        matrice[1][1] = constA * inverse;

        System.out.println("New matrice : ");
        System.out.print("a = "+ matrice[0][0] + ", ");
        System.out.print("b = "+ matrice[0][1] + ", ");
        System.out.print("c = "+ matrice[1][0] + ", ");
        System.out.println("d = "+ matrice[1][1] + ", ");

        //On récupère le message à décrypter
        text = textClair.getText().toString();
        result = "";

        //on lance hill
        hill(false);
    }

    //cryptage et decryptage de Hill
    public void hill(boolean encode){

        //si on encode, on prévoit des variables pour compléter le dernier bloc si il manque un caractère
        char[] rare = {'q', 'w', 'z', 'k', 'j', 'x'};
        int nb_alea;
        //pour avoir un nombre aléatoire
        Random random = new Random();

        //recuperation des caractere 2 par 2 caractères du message en bloc de 2
        int letter1, letter2;
        int y1, y2;
        for(indice=0; indice<text.length(); indice +=1){

            //on récupère le premier caractère
            letter1 = recupLettre();
            indice++; //on passe au caractère suivant

            //on récupère le deuxième si il est présent
            if(indice < text.length()){
                letter2 = recupLettre();

                //Sinon si on est en encodage, on ajoute une lettre rare dans letter2
            } else if(encode) {
                nb_alea = random.nextInt(rare.length);
                letter2 = rare[nb_alea];
                System.out.println("letter2 sera random");

                //Sinon on génère une erreur, on est en decryptage => il manque une lettre
            } else {
                errorCryptage(3);
                return;
            }

            //Si les caractères sont au-dessus de 127, on les ramène dans la bonne norme
            if(letter1 > 127)
                letter1 = ExtendedAscii.getIndexAsciiExtended(letter1);
            if(letter2 > 127)
                letter2 = ExtendedAscii.getIndexAsciiExtended(letter2);

            System.out.println("Lettre 1 = " + letter1 + " soit : " + (char)letter1);
            System.out.println("Lettre 2 = È" + letter2 + " soit : " + (char)letter2);

            //On effectue les combinaisons linéaires
            y1 = matrice[0][0] * letter1 + matrice[0][1] * letter2;
            y2 = matrice[1][0] * letter1 + matrice[1][1] * letter2;

            System.out.print("y1 = a*letter1 + b*letter2 ==> ");
            System.out.println(matrice[0][0] + " * " + letter1 + " + " + matrice[0][1] + " * "+ letter2 + " = " + y1);

            System.out.print("y2 = c*letter1 + d*letter2 ==> ");
            System.out.println(matrice[1][0] + " * " + letter2 + " + " + matrice[1][1] + " * "+ letter2 + " = " + y2);

            //Si les valeurs sont négatives
            while(y1 < 0)
                y1 += MODULO;

            while(y2 < 0)
                y2 += MODULO;

            //on les passes dans un modulo 256
            letter1 = y1 % MODULO;
            letter2 = y2 % MODULO;

            System.out.println("letter1 après le modulo 256 = " + letter1 + " soit " + (char)letter1);
            System.out.println("letter2 après le modulo 256 = " + letter2 + " soit " + (char)letter2);

            //On doit maintenant ré écire les caractères dans la chaîne de réponse
            writeLetter(letter1, encode);
            writeLetter(letter2, encode);
        }

        //Le message a été complètement encoder -> on peut l'afficher
        textcode.setText(result);
    }

    //Récupère la lettre à l'indice donnée, convertit en int si écrit en hexadecimal
    protected int recupLettre(){
        int letter = text.charAt(indice);
        String nb_hexa = "";
        //Si le premier caractère est écrit en hexadécimale, on le re écrit dans le bon format
        if(letter == '\\'){
            nb_hexa += (char) (text.charAt(indice+2));
            nb_hexa += (char) (text.charAt(indice+3));

            // => conversion en int
            letter = Integer.parseInt(nb_hexa, 16);
            // on incrémente i pour récupérer le prochain caractère
            indice = indice + 3;
        }
        return(letter);
    }

    //Ecrit la lettre dans la chaîne réponse dans le bon format
    protected void writeLetter(int letter, boolean encode){
        if((letter >= 0 && letter < 32) || letter == 127 || letter == 255 || (letter == 32 && encode)){
            if(letter < 16)
                result += "\\x0" + Integer.toHexString(letter);
            else
                result += "\\x" + Integer.toHexString(letter);
            //on l'écrit normalement
        } else {
            result += ExtendedAscii.getAscii(letter);
        }
    }

    //on récupère les clés en vérifiants qu'elles sont bien des int
    protected int recupMat(){
        String a, b, c, d;
        a = A.getText().toString();
        b = B.getText().toString();
        c = C.getText().toString();
        d = D.getText().toString();

        //on les convertit en int
        try{
            matrice[0][0] = Integer.parseInt(a);    //a
            matrice[0][1] = Integer.parseInt(b);    //b
            matrice[1][0] = Integer.parseInt(c);    //c
            matrice[1][1] = Integer.parseInt(d);    //d

        }catch (Exception e){
            errorCryptage(1);
            return(-1);
        }

        //test de validite de la matrice
        int constA = matrice[0][0];
        int constB = matrice[0][1];
        int constC = matrice[1][0];
        int constD = matrice[1][1];

        // verifier que les deux nombres soient premiers entre eux
        int val = (constA*constD - constB*constC);
        if(pgcd(val, MODULO) != 1){
            errorCryptage(2);
            return(-1);
        }

        //on retourne le déterminant
        return(val);
    }

    //fonction qui retourne le pgcd entre a et b
    // ==> on s'en sert pour voir si la matrice est possible
    protected int pgcd(int a, int b){
        int r = 0;
        while(b!=0)
        {
            r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    //fonction qui calcule l'inverse du déterminant
    protected int getInverse(int determinant, int modulo){
        //Calcul de manière intuitive
        int n = 0;
        int i = 1;
        while((i % determinant) != 0){
            n += 1;
            i = (1 + modulo * n);
        }
        return(i/determinant);
    }

    //Gère l'affichage lorsque qu'il y a une erreur de saisie des clés
    protected void errorCryptage(int error){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setCancelable(true);

        //une ou plusieurs clés ne sont pas des int
        if(error == 1){
            builder1.setMessage("Les clés doivent être des nombres");
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        //matrice non conforme
        if(error == 2){
            builder1.setMessage("Matrice non conforme car (ad-bc) et 256 ne sont pas premiers entre eux, pgcd != 1");
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        //manque un caractère dans le texte
        if(error == 3){
            builder1.setMessage("Il y a une erreur dans le texte");
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    //Termine l'activité
    public void endActivity(View v){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }



}