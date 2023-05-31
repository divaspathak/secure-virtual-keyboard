package com.example.kk;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipboardManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
@TargetApi(Build.VERSION_CODES.CUPCAKE)
@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class androidK extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    RelativeLayout rl1;
    public static int a=3;
    public static int b=25;

    public static String password="#group5";
    static EditText type_ll;
    private static final String ALGORITHM = "AES";
    private static  String KEY = "MySuperSecretKey";
    static EditText type_lr;
    static Button btnen;
    private KeyboardView keyboardView;
    private Keyboard keyboard;
    Boolean isCaps = false;
    static int enable=0;
    static int valid=0;
    static StringBuffer global=new StringBuffer();
    static StringBuffer sbb;
    static int validpassword=-1;
    int mode=1;
    RelativeLayout rl;
    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Add code to change attributes here
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Change attributes for landscape orientation
            Toast.makeText(getApplicationContext(),"changed",Toast.LENGTH_SHORT).show();
            // Set the window flags to adjust the keyboard height

            // Set the content view for the activity


        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Change attributes for portrait orientation
        }
    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateInputView() {
         rl1=(RelativeLayout)getLayoutInflater().inflate(R.layout.viewww,null);
         sbb=new StringBuffer();
         type_ll=(EditText) rl1.findViewById(R.id.type_l);
         type_lr=(EditText)rl1.findViewById(R.id.type_lr);
         btnen=(Button) rl1.findViewById(R.id.btnenable);
         btnen.setBackgroundColor(Color.parseColor("#157102"));
         type_ll.setBackgroundColor(Color.parseColor("#C6E14F"));



        btnen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validpassword == 1) {

                        if (enable == 0) {
                            enable = 1;
                            type_ll.setBackgroundColor(Color.parseColor("#EC9086"));

                            btnen.setBackgroundColor(Color.parseColor("#9C0404"));
                        } else {
                            enable = 0;
                            type_ll.setBackgroundColor(Color.parseColor("#C6E14F"));
                            btnen.setBackgroundColor(Color.parseColor("#157102"));

                        }

                }
            }
        });


        rl=new RelativeLayout(this);
        keyboardView = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard,null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);

        type_ll.setText("");

        rl.addView(keyboardView);
        rl.addView(rl1);
        return rl;
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

        InputConnection inputConnection = getCurrentInputConnection();
        playClick(primaryCode);
        switch (primaryCode){
           case Keyboard.KEYCODE_DELETE:
               global.delete(0,global.length());
                if(enable==0){
                        inputConnection.deleteSurroundingText(1,0);
                }else{
                    if(type_ll.getText().toString().equals("")){
                        inputConnection.deleteSurroundingText(1,0);
                    }else {
                        String temp = type_ll.getText().toString();
                        type_ll.setText(temp.substring(0, temp.length() - 1));
                    }
                }
                break;
            case Keyboard.KEYCODE_SHIFT:
                isCaps = !isCaps;
                keyboard.setShifted(isCaps);
                keyboardView.invalidateAllKeys();

            case Keyboard.KEYCODE_DONE:
                //encryption box will belong here
                String temp1=type_ll.getText().toString();

                Keyboard currentKeyboard = keyboardView.getKeyboard();
                List<Keyboard.Key> keys = currentKeyboard.getKeys();
//               keys.get(1).label.equals("OFF")
                if(global.toString().equals("#affine")){
                    Log.e("Affine", "onKey:Affine is enabled ",null);
                   type_ll.setText("");
                   sbb=new StringBuffer();
                   enable=1;
                    if(enable==0){
                        enable=1;
                        type_ll.setBackgroundColor(Color.parseColor("#EC9086"));

                        btnen.setBackgroundColor(Color.parseColor("#9C0404"));
                    }else{
                        enable=0;
                        type_ll.setBackgroundColor(Color.parseColor("#C6E14F"));
                        btnen.setBackgroundColor(Color.parseColor("#157102"));
                   }
               }else if(sbb.length()!=0 && sbb.toString().equals("#normal")){
                   type_ll.setText("");
                   sbb=new StringBuffer();
                   enable=0;
                    if(enable==0){
                        enable=1;
                        type_ll.setBackgroundColor(Color.parseColor("#EC9086"));

                        btnen.setBackgroundColor(Color.parseColor("#9C0404"));
                    }else{
                        enable=0;
                        type_ll.setBackgroundColor(Color.parseColor("#C6E14F"));
                        btnen.setBackgroundColor(Color.parseColor("#157102"));

                    }
               }else if(enable==0){
                    type_ll.setText("");
               }else {
                    String sb = null;
                    if(mode==1){
                        sb=encryptaffine(temp1,a,b);
                    }else {

                        try {
                            sb = encryptAES(temp1);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    inputConnection.commitText(sb, 0);
                    type_ll.setText("");
               }
               global.delete(0,global.length());

                //this will be the code for recieveng and translating
//                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                Log.e("clipboard", "onKey: \n"+clipboard.getText().toString(),null );

                inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER));

                break;

            default:

                char code = (char) primaryCode;

                if(Character.isLetter(code) && isCaps){
                    code = Character.toUpperCase(code);
                }else if(code==47){
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    String conve=clipboard.getText().toString();
                    String sb= null;
                    if(validpassword==1) {
                        if (mode == 1) {
                            sb = decryptaffine(conve, a, b);
                        } else if (mode == 2) {
                            try {
                                sb = decryptAES(conve);
                            } catch (Exception e) {
                                sb="";
                            }
                        }
                    }
                    type_lr.setText(sb);
                    break;
                }else if(code=='?'){
                    Log.e("Affine", ""+code,null);
                    if(global.toString().equals("#affine") && validpassword==1){
                        Toast.makeText(this,"Affine enabled ",Toast.LENGTH_SHORT).show();
                        Log.e("Affine", "onKey:Affine is enabled ",null);
                        type_ll.setText("");
                        sbb=new StringBuffer();
                        enable=1;
                        mode=1;
                        if(enable==1){
                            type_ll.setBackgroundColor(Color.parseColor("#EC9086"));
                             btnen.setBackgroundColor(Color.parseColor("#9C0404"));
                        }else{
                            type_ll.setBackgroundColor(Color.parseColor("#C6E14F"));
                            btnen.setBackgroundColor(Color.parseColor("#157102"));

                        }
                    }if(global.toString().equals("#aes") && validpassword==1){
                        Toast.makeText(this,"AES enabled ",Toast.LENGTH_SHORT).show();
                        Log.e("Affine", "onKey:AES is enabled ",null);
                        type_ll.setText("");
                        sbb=new StringBuffer();
                        enable=1;
                        mode=2;
                        if(enable==1){
                            type_ll.setBackgroundColor(Color.parseColor("#EC9086"));
                            btnen.setBackgroundColor(Color.parseColor("#9C0404"));
                        }else{
                            type_ll.setBackgroundColor(Color.parseColor("#C6E14F"));
                            btnen.setBackgroundColor(Color.parseColor("#157102"));

                        }
                    }else if(global.toString().equals("#normal")){
                        Log.e("Affine", "onKey:normal is enabled ",null);
                        type_ll.setText("");
                        sbb=new StringBuffer();
                        enable=0;
                        if(enable==1){
                            type_ll.setBackgroundColor(Color.parseColor("#EC9086"));
                            btnen.setBackgroundColor(Color.parseColor("#9C0404"));
                        }else{
                            type_ll.setBackgroundColor(Color.parseColor("#C6E14F"));
                            btnen.setBackgroundColor(Color.parseColor("#157102"));

                        }
                    }else if(global.substring(0,2).toString().equals("#.")){
                        StringBuffer sb=new StringBuffer();
                        String pww="";

                        for(int i=2;i<global.length();i++){
                            if(global.charAt(i)=='#'){
                                pww="#"+sb.toString();
                                sb=new StringBuffer();
                            }
                            sb.append(global.charAt(i));
                        }

                        if(password.equals(pww)) {
                            Toast.makeText(this," Password Changed "+sb,Toast.LENGTH_SHORT).show();
                            password = sb.toString();
                        }else{
                            Toast.makeText(this," Password Changed Failed "+password,Toast.LENGTH_SHORT).show();
                        }
                    }else if(global.toString().equals(password)){
                        Log.e("mm", "onKey: Password enable", null);
                        type_ll.setText("");
                        sbb=new StringBuffer();
                        validpassword=-1*validpassword;
                        if(validpassword==1)
                            Toast.makeText(this,"Keyboard enabled ",Toast.LENGTH_SHORT).show();
                        else {
                            enable=0;
                            valid=0;
                            setcolors();
                            Toast.makeText(this, "Keyboard disabled ", Toast.LENGTH_SHORT).show();
                        }
                    }
                    global.delete(0,global.length());
                }


                Log.e("Meow", "onKey: hello clicked" +String.valueOf(code) ,null );
                if(enable==0){
                    global.append(String.valueOf(code));
                    sbb.append(String.valueOf(code));
                    inputConnection.commitText(String.valueOf(code), 1);
                }else{
                    global.append(String.valueOf(code));
                    sbb.append(String.valueOf(code));
                    type_ll.append(""+String.valueOf(code));
                }

        }

    }
    public static void setcolors(){
        if(enable==1){
            type_ll.setBackgroundColor(Color.parseColor("#EC9086"));
            btnen.setBackgroundColor(Color.parseColor("#9C0404"));
        }else{
            type_ll.setBackgroundColor(Color.parseColor("#C6E14F"));
            btnen.setBackgroundColor(Color.parseColor("#157102"));

        }
    }
    public static String encryptaffine(String message, int a, int b) {
        String result = "";
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (Character.isLetter(c)) {
                c = (char) ((a * (c - 'a') + b) % 26 + 'a');
            }
            result += c;
        }
        return result;
    }

    public static String decryptaffine(String message, int a, int b) {
        String result = "";
        int aInverse = 0;
        for (int i = 0; i < 26; i++) {
            if ((a * i) % 26 == 1) {
                aInverse = i;
                break;
            }
        }
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (Character.isLetter(c)) {
                c = (char) (aInverse * (c - 'a' - b + 26) % 26 + 'a');
            }
            result += c;
        }
        return result;
    }

    public static String encryptAES(String plaintext) throws Exception {
        SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(encryptedBytes);
        }
        return plaintext;
    }

    public static String decryptAES(String ciphertext) throws Exception {
        SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            decodedBytes = Base64.getDecoder().decode(ciphertext);
        }
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    private void playClick(int i){

        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch(i){
            case 32:
                audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;

            case Keyboard.KEYCODE_DONE:
            case 10:
                audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;

            case Keyboard.KEYCODE_DELETE:
                audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;

            default:
                audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }

    }


    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}