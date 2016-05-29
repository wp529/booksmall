package com.pzhu.booksmall.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import com.pzhu.booksmall.R;
import com.pzhu.booksmall.Utils.DataUtils;
import com.pzhu.booksmall.Utils.UIUtils;

public class RegisterActivity extends Activity {

    private EditText mRegisterUsername,mRegisterPassword;
    private Button mRegisterButton;
    private String username;
    private String password;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        mRegisterUsername = (EditText) findViewById(R.id.et_register_username);
        mRegisterPassword = (EditText) findViewById(R.id.et_register_pwd);
        mRegisterButton = (Button) findViewById(R.id.btn_reg_register);
        sp = DataUtils.getSharedPreferences(RegisterActivity.this);

        mRegisterPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                username = mRegisterUsername.getText().toString().trim();
                if (!TextUtils.isEmpty(username)) {
                    if (charSequence.toString().length() != 0) {
                        mRegisterButton.setEnabled(true);
                        mRegisterButton.setTextColor(Color.BLACK);
                    } else {
                        mRegisterButton.setEnabled(false);
                        mRegisterButton.setTextColor(Color.GRAY);
                    }
                } else {
                    mRegisterButton.setEnabled(false);
                    mRegisterButton.setTextColor(Color.GRAY);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                password = mRegisterPassword.getText().toString().trim();
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = mRegisterUsername.getText().toString().trim();
                password = mRegisterPassword.getText().toString().trim();
                if(TextUtils.isEmpty(username)){
                    UIUtils.makeToast(RegisterActivity.this,"用户名不能为空");
                    return;
                }else if(TextUtils.isEmpty(password)){
                    UIUtils.makeToast(RegisterActivity.this,"密码不能为空");
                    return;
                }
                boolean haved;
                haved = !TextUtils.isEmpty(sp.getString(username,""));
                if(haved){
                    UIUtils.makeToast(RegisterActivity.this,"用户名已存在");
                }else{
                    sp.edit().putString(username,password).commit();
                    UIUtils.makeToast(RegisterActivity.this,"注册成功");
                    finish();
                }
            }
        });
    }
}
