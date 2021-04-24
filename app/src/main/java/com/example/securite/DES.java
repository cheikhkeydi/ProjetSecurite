package com.example.securite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class DES extends AppCompatActivity {


    EditText plainText, keyText;
    TextView cryptedText;

    //les tableaux pour les permutations
    final int[] IP = { 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29,
            21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7 };
    final int[] IP_1 = { 40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62,
            30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10,
            50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25 };
    final int[] E = { 32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17,
            18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1 };
    final int[] P = { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19,
            13, 30, 6, 22, 11, 4, 25 };
    final int[] Pc1 = { 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3,
            60, 52, 44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28,
            20, 12, 4 };

    final int[] Pc2 = { 14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41,
            52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32 };
    final int[][] S1 = { { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
            { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
            { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
            { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 } };
    final int[][] S2 = { { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
            { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
            { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
            { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } };
    final int[][] S3 = { { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
            { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
            { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
            { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 } };
    final int[][] S4 = { { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
            { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
            { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
            { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 } };
    final int[][] S5 = { { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
            { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
            { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
            { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 } };
    final int[][] S6 = { { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
            { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
            { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
            { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 } };
    final int[][] S7 = { { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
            { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
            { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
            { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 } };
    final int[][] S8 = { { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
            { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
            { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
            { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 }, };

    final int[][][] Boite_S = { S1, S2, S3, S4, S5, S6, S7, S8 };
    int compteur = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_e_s);

        plainText = findViewById(R.id.textClair);
        cryptedText = findViewById(R.id.textCrypted);
        keyText = findViewById(R.id.textkey);



    }

    public void encodeDES(View v){
        //Récupération des champs de la page
        String message = plainText.getText().toString();
        String cle = keyText.getText().toString();

        String res = "";
        String mes_permut;
        String mes = testFormatMes(message);
        String[] TabMes = longMessage(mes);
        String[] Ki = makeKi(cle);
        for (int m = 0; m < TabMes.length; m++) {
            mes_permut = permutation_initial(TabMes[m]);
            String[][] GiDj = iteration(mes_permut, Ki);
            Log.d("","\n\n");

            res = res + permutation_finale(GiDj[0], GiDj[1]);
        }

        //return res; // + " code en Hex: " + getHex(res)
        cryptedText.setText(res);
    }

    public void decodeDES(View v){
        //Récupération des champs de la page
        String message = plainText.getText().toString();
        String cle = keyText.getText().toString();

        String res = "";
        String mes_permut;

        String mes = testFormatMes(message);
        String[] TabMes = longMessage(mes);
        String[] Ki = makeKi(cle);
        for (int m = 0; m < TabMes.length; m++) {
            mes_permut = permutation_initial(TabMes[m]);
            String[][] GiDj = iterationDecode(mes_permut, Ki);
            Log.d("", "\n\n");

            res = res + permutation_finale(GiDj[0], GiDj[1]);
        }

        cryptedText.setText(res);
        //return res;
    }

    public String getHex(String e) {
        String res = "";
        int index = 0;
        for (int i = 0; i < e.length(); i++) {
            if (e.charAt(i) < 128) {
                res = res + Integer.toHexString((int) e.charAt(i));
            } else {
                index = ExtendedAscii.getIndexAsciiExtended(e.charAt(i));
                res = res + Integer.toHexString(index);
            }
        }

        return res;
    }

    // Fonction qui transforme un char en sa valeur en hexadecimal
    public String charToHex(char c) {
        String res;
        switch (c) {
            case '0':
                res = "0000";
                break;
            case '1':
                res = "0001";
                break;
            case '2':
                res = "0010";
                break;
            case '3':
                res = "0011";
                break;
            case '4':
                res = "0100";
                break;
            case '5':
                res = "0101";
                break;
            case '6':
                res = "0110";
                break;
            case '7':
                res = "0111";
                break;
            case '8':
                res = "1000";
                break;
            case '9':
                res = "1001";
                break;
            case 'a':
                res = "1010";
                break;
            case 'b':
                res = "1011";
                break;
            case 'c':
                res = "1100";
                break;
            case 'd':
                res = "1101";
                break;
            case 'e':
                res = "1110";
                break;
            case 'f':
                res = "1111";
                break;
            default:
                res = null;
        }
        return res;
    }

    // fonction qui transforme un String Binaire en sa valeur en hexadecimal
    public char binaire_to_Hex_char(String s) {
        char res;
        if (s.length() > 4 || s.length() < 4) {
            return (char) 0;
        }
        switch (s) {
            case "0000":
                res = '0';
                break;
            case "0001":
                res = '1';
                break;
            case "0010":
                res = '2';
                break;
            case "0011":
                res = '3';
                break;
            case "0100":
                res = '4';
                break;
            case "0101":
                res = '5';
                break;
            case "0110":
                res = '6';
                break;
            case "0111":
                res = '7';
                break;
            case "1000":
                res = '8';
                break;
            case "1001":
                res = '9';
                break;
            case "1010":
                res = 'a';
                break;
            case "1011":
                res = 'b';
                break;
            case "1100":
                res = 'c';
                break;
            case "1101":
                res = 'd';
                break;
            case "1110":
                res = 'e';
                break;
            case "1111":
                res = 'f';
                break;
            default:
                res = (char) 0;
        }
        return res;
    }

    // fonction verifie le format du message
    public int format_Mes(String cle) {
        int format = 0;

        //format hexadecimale
        if (cle.startsWith("\\x")) {
            format = 0;

            //format binaire
        } else if (cle.startsWith("-")) {
            format = 1;

            //format normal
        } else {
            format = 2;
        }

        return format;
    }

    // fonction qui transforme le message en binaire selon le format
    public String testFormatMes(String message) {
        int format = format_Mes(message);
        String res = "";
        String tmp = "";
        switch (format) {
            case 0:
                res = mesBinaire(message.substring(2)); // si le format est en hexa on passe en binaire en enlevant \x au debut
                break;
            case 1:
                res = message.substring(1); // si le format est binaire on enlève le -
                break;
            case 2:
                tmp = getHex(message);// si il est en lettre normal on passe en hexadecimal
                res = mesBinaire(tmp); // puis on transforme en binaire
                break;

        }
        return res;
    }

    // fonction qui transforme le message en binaire selon le format
    public String testFormatKey(String key) {
        int format = format_Mes(key);
        String res = "";
        switch (format) {
            case 0:
                res = key.substring(2); // si le format est en hexa on laisse comme il est mais on enlève \x
                break;
            case 1:
                res = key;
                break;
            case 2:
                res = getHex(key);// si il est en lettre normal on passe en hexadecimal
                break;
        }
        return res;
    }

    // fonction pour transformer un hex en decimal
    public int hex_to_decimal(String hex) {
        int a = Integer.parseInt(hex, 16);
        return a;
    }

    public String hex_to_Message(String s) {
        String res = "";
        String message = "";
        int start = 0;
        int end = 4;
        for (int i = 0; i < s.length(); i += 4) { // on recupère le message en morceau de 4 bits
            if (end < s.length()) {
                res = res + binaire_to_Hex_char(s.substring(start, end));// on le transforme en hex
                start = end;
                end += 4;
            } else {
                res = res + binaire_to_Hex_char(s.substring(start));
            }
        }
        start = 0;
        end = 2;
        for (int i = 0; i < res.length(); i += 2) { // on recupère les codes hexa 2 par 2
            if (end < res.length()) {
                if ((hex_to_decimal(res.substring(start, end)) >= 0 && hex_to_decimal(res.substring(start, end)) < 32)
                        || hex_to_decimal(res.substring(start, end)) == 127 || hex_to_decimal(res.substring(start, end)) == 255) {
                    message = message + "\\x" + hex_to_decimal(res.substring(start, end));
                }
                message = message + ExtendedAscii.getAscii(hex_to_decimal(res.substring(start, end))); // et on les retransforme en symbole du
                // code ascii
                start = end;
                end += 2;
            } else {
                if ((hex_to_decimal(res.substring(start, end)) >= 0 && hex_to_decimal(res.substring(start, end)) < 32)
                        || hex_to_decimal(res.substring(start, end)) == 127 || hex_to_decimal(res.substring(start, end)) == 255) {
                    message = message + "\\x" + hex_to_decimal(res.substring(start, end));
                } else {
                    message = message + ExtendedAscii.getAscii(hex_to_decimal(res.substring(start)));// étendu
                }
            }
        }

        return message;
    }

    // validation de la si elle fait bien 64 bits sinon on ajoute des 0 a la
    public String valideKey(String key) {
        // fin
        String res = "";
        Log.d("", "");
        String cle = testFormatKey(key);
        if (!(cle.startsWith("-"))) {
            for (int i = 0; i < cle.length(); i++) {
                res = res + charToHex(cle.charAt(i));
            }
        } else {
            res = cle;
        }

        if (res.length() < 64) {
            while (res.length() < 64) {
                res += "0";
            }
        }
        if (res.length() > 64) {
            res = null;
        }

        return res;
    }

    // fonction permettant a faire le decalage circulaire vers la gauche
    public String permutationCirculaireG(String ci, int n) {
        String n1 = ci.substring(0, n);// il recupère un String de la longeur du decalage
        String n2 = ci.substring(n, ci.length());// il rescupère le reste du String de depart
        String res = n2 + n1; // rajoute n1 a la fin de n2

        return res;
    }

    // fonction qui crée les différent ci avec la permutation selon le i utilisé si
    // i=1,2,9,ou 16 decalage de 1 sinon de 2 par rapport a ci-1
    public String[] permuCi(String pc) {
        String[] Ci = new String[17];
        String c0 = pc.substring(0, 28);
        Ci[0] = c0;
        for (int i = 1; i < 17; i++) {

            if (i == 1 || i == 2 || i == 9 || i == 16) {

                Ci[i] = permutationCirculaireG(Ci[i - 1], 1);
            } else {
                Ci[i] = permutationCirculaireG(Ci[i - 1], 2);
            }
        }
        return Ci;
    }

    // fonction qui crée les différent di avec la permutation selon le i utilisé si
    // i=1,2,9,ou 16 decalage de 1 sinon de 2 par rapport a di-1
    public String[] permuDi(String pc) {
        String[] Di = new String[17];
        String d0 = pc.substring(28, 56);
        Di[0] = d0;
        for (int i = 1; i < 17; i++) {
            if (i == 1 || i == 2 || i == 9 || i == 16) {

                Di[i] = permutationCirculaireG(Di[i - 1], 1);
            } else {
                Di[i] = permutationCirculaireG(Di[i - 1], 2);
            }
        }
        return Di;
    }

    // après avoir avoir creer les cidi on les passe dans la table PC-2
    public String diversi(String cle) {
        String res = "";
        for (int i = 0; i < 48; i++) {
            res = res + cle.charAt(Pc2[i] - 1);
        }

        return res;
    }

    public String[] makeKi(String cle) {
        // Phase 1 Prepartion -Diversification de de la cle
        String[] Ki = new String[17]; // tableau contenant tout les ki (les différentes cles diversifier de k1 à k16)
        String pc_1;
        String key = valideKey(cle);
        pc_1 = prepa(key); // Pc_1 est la cle apres son passage dans le tableau PC_1
        String[] Ci = permuCi(pc_1); // Creation des Ci par rapport a pc1
        String[] Di = permuDi(pc_1);// Creation des Di par rapport a pc1

        for (int i = 1; i < 17; i++) {
            Ki[i] = diversi(Ci[i] + Di[i]); // Creation des différentes cles ki avec Ci et Di
        }

        return Ki;
    }

    public String prepa(String cle) { // phase 1 : préparation de la clé en la passant dans PC-1
        String res = "";
        for (int i = 0; i < 56; i++) {
            res = res + cle.charAt(Pc1[i] - 1);
        }
        return res;
    }

    public String mesBinaire(String mes) { // fonction de transformation d'un message hex en binaire
        String res = "";
        String message = mes.toLowerCase();
        for (int i = 0; i < message.length(); i++) {
            res = res + charToHex(message.charAt(i));
        }
        return res;
    }

    // fonction qui renvoie un tableau du message séparer en block de 64 bits
    public String[] longMessage(String message) {
        String res = message;
        String[] e = {};
        Log.d("", "Le Message en binaire avant transformation en block :  " + res);
        if (res.length() < 64) {
            while (res.length() < 64) {
                res += "0";
            }
            e = new String[1];
            e[0] = res;
        }
        if (res.length() > 64) {
            if (res.length() % 64 != 0) {
                int ligne = 0;
                e = new String[(res.length() / 64) + 1];
                for (int i = 0; i < e.length; i++) {
                    e[i] = "";
                }
                for (int i = 0; i < res.length(); i++) {
                    if (e[ligne].length() < 64) {
                        e[ligne] += res.charAt(i);
                    } else {
                        ligne++;
                        e[ligne] += res.charAt(i);
                    }
                }
                if (e[ligne].length() < 64) {
                    while (e[ligne].length() < 64) {
                        e[ligne] += "0";
                    }
                }
            } else {
                int ligne = 0;
                e = new String[(res.length() / 64)];
                for (int i = 0; i < e.length; i++) {
                    e[i] = "";
                }
                for (int i = 0; i < res.length(); i++) {
                    if (e[ligne].length() < 64) {
                        e[ligne] += res.charAt(i);
                    } else {
                        ligne++;
                        e[ligne] += res.charAt(i);
                    }
                }
                if (e[ligne].length() < 64) {
                    while (e[ligne].length() < 64) {
                        e[ligne] += "0";
                    }
                }
            }
        }

        if (res.length() == 64) {
            e = new String[1];
            e[0] = res;
        }
        return e;
    }

    // Phase 2 : Permuation initiale : passage du block de 64 bits du message dans IP
    public String permutation_initial(String message_block) {
        String Y = "";
        for (int i = 0; i < IP.length; i++) {
            Y = Y + message_block.charAt(IP[i] - 1);
        }
        return Y;
    }

    // Phase 3: Itération - création des tableaux de GiDi
    public String[][] iteration(String y, String[] k) {
        String g0 = y.substring(0, 32);
        String d0 = y.substring(32, 64);
        String[][] res = new String[2][];
        String[] Gi = new String[17];
        String[] Dj = new String[17];
        Gi[0] = g0;
        Dj[0] = d0;
        for (int i = 1; i < 17; i++) {
            Gi[i] = Dj[i - 1];
            Dj[i] = XOR(Gi[i - 1], f(Dj[i - 1], k[i]));
            Log.d("", "G  :  " + Gi[i]);
            Log.d("", "D  :  " + Dj[i]);
        }
        res[0] = Gi;
        res[1] = Dj;

        return res;
    }

    // Phase 3 :Itération pour le décodage
    public String[][] iterationDecode(String y, String[] k) {
        String g0 = y.substring(0, 32);
        String d0 = y.substring(32, 64);
        String[][] res = new String[2][];
        String[] Gi = new String[17];
        String[] Dj = new String[17];
        Gi[0] = g0;
        Dj[0] = d0;
        for (int i = 1; i < 17; i++) {
            Gi[i] = Dj[i - 1];
            Dj[i] = XOR(Gi[i - 1], f(Dj[i - 1], k[17 - i])); // le changement par rapport au codage au lieu d'avoir ki on a
            // k17-i
            Log.d("", "G  :  " + Gi[i]);
            Log.d("", "D  :  " + Dj[i]);
        }
        res[0] = Gi;
        res[1] = Dj;

        return res;
    }

    // fonction ou exclusif
    public String XOR(String g, String f) {
        String res = "";
        if (g.length() == f.length()) {
            for (int i = 0; i < g.length(); i++) {
                if (g.charAt(i) == f.charAt(i)) { // si on a 00 et 11 alors on rajoute 0
                    res = res + '0';
                } else { // sinon 1
                    res = res + '1';
                }
            }
        } else {
            return null;
        }
        return res;
    }

    // fonction pour transformé le binaire en décimal
    public int binary_to_decimal(String e) {
        int dec = 0;
        int size_e = e.length();
        int bit;

        for (int i = size_e, j = 0; i > 0; i--, j++) {
            bit = Character.getNumericValue(e.charAt(i - 1));
            dec += bit * Math.pow(2, j);
        }

        return dec;
    }

    // Phase 3 : itération : la transformation local
    public String[] tranfoLocal(String[] bi, int[][][] s) {
        String[] res = new String[8];
        String l, c, tmp;
        int size, ligne, colonne;

        for (int i = 0; i < 8; i++) {
            size = bi[i].length();
            l = "" + bi[i].charAt(0) + bi[i].charAt(size - 1);
            c = bi[i].substring(1, size - 1);
            ligne = binary_to_decimal(l);
            colonne = binary_to_decimal(c);
            tmp = Integer.toBinaryString(s[i][ligne][colonne]);
            if (tmp.length() < 4) {
                while (tmp.length() < 4) {
                    tmp = "0" + tmp;
                }
            }
            res[i] = tmp;
        }

        Log.d("", "");
        int com = 1;
        for (String str : res) {
            Log.d("", "s" + com + " : " + str);
            Log.d("", "  , ");
            com++;
        }
        Log.d("", "");

        return res;
    }

    // passage des ci
    public String permut(String[] t) {

        String res = "", c = "";
        for (int i = 0; i < t.length; i++) {
            c += t[i];
        }
        Log.d("", "B =    " + c);
        for (int i = 0; i < P.length; i++) {
            res = res + c.charAt(P[i] - 1);

        }

        Log.d("", "P(B)=    " + res);

        return res;
    }

    // Phase 3 : itération : la fonction de confusion
    public String f(String d, String k) {
        String res = "";
        String expansion_d = "";
        String B = "";
        String[] Bi = new String[8];

        Log.d("", "---------------------------------------------------------------------");
        Log.d("", "K :   " + k);
        for (int i = 0; i < E.length; i++) {
            expansion_d = expansion_d + d.charAt(E[i] - 1);
        }
        Log.d("", "E(R) " + compteur + " :    " + expansion_d);
        compteur++;
        B = XOR(expansion_d, k);
        Log.d("", "A = E(R) + K = " + B);
        int start = 0;
        int end = 6;
        for (int j = 0; j < 8; j++) {
            Bi[j] = B.substring(start, end);
            start = end;
            end += 6;
        }
        String[] Ci = tranfoLocal(Bi, Boite_S);

        res = permut(Ci);

        return res;
    }

    // Phase 4 Permutation final passage de G16D16 dans
    public String permutation_finale(String[] g, String[] d) {
        // IP-1
        String res = "";
        String tmp = d[g.length - 1] + g[d.length - 1];

        for (int i = 0; i < IP_1.length; i++) {
            res = res + tmp.charAt(IP_1[i] - 1);

        }
        return res;
    }

    //Termine l'activité
    public void endActivity(View v){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

}