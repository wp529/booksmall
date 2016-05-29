package com.pzhu.booksmall.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.pzhu.booksmall.R;
import com.pzhu.booksmall.Utils.DataUtils;
import com.pzhu.booksmall.Utils.UIUtils;

public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText mUserName, mPWD;
    private Button mLogin, mRegister,mLoginTourist;
    private CheckBox mRemember;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initUI();
        initData();
    }

    private void initUI() {
        mUserName = (EditText) findViewById(R.id.et_username);
        mPWD = (EditText) findViewById(R.id.et_pwd);
        mLogin = (Button) findViewById(R.id.btn_login);
        mRegister = (Button) findViewById(R.id.btn_register);
        mLoginTourist = (Button) findViewById(R.id.btn_login_tourist);
        mRemember = (CheckBox) findViewById(R.id.cb_remember);
    }

    private void initData() {
        SharedPreferences sp = DataUtils.getSharedPreferences(this);
        mUserName.setText(sp.getString("last_username",""));
        mPWD.setText(sp.getString("last_password",""));
        if(!TextUtils.isEmpty(sp.getString("last_password",""))){
            mUserName.setSelection(sp.getString("last_username","").length());
            mLogin.setEnabled(true);
            mLogin.setTextColor(Color.BLACK);
        }
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mLoginTourist.setOnClickListener(this);
        mPWD.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            username = mUserName.getText().toString().trim();
            password = mPWD.getText().toString().trim();
            if (!TextUtils.isEmpty(username)) {
                if (charSequence.toString().length() != 0) {
                    mLogin.setEnabled(true);
                    mLogin.setTextColor(Color.BLACK);
                } else {
                    mLogin.setEnabled(false);
                    mLogin.setTextColor(Color.GRAY);
                }
            } else {
                mLogin.setEnabled(false);
                mLogin.setTextColor(Color.GRAY);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    });
}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (loginResult()) {
                    //跳转到登录界面
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    //登录失败
                    UIUtils.makeToast(this, "账号或密码错误");
                }
                break;
            case R.id.btn_register:
                //跳转到注册界面
                startActivity(new Intent(this, RegisterActivity.class));
                mUserName.setText("");
                mUserName.requestFocus();
                mPWD.setText("");
                break;
            case R.id.btn_login_tourist:
                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("游客登录模式");
                dialog.setMessage("当前为游客登录模式,此登录模式下只能浏览商品,没有购物车等功能," +
                        "您确定以此模式进入?");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //游客登录模式下的界面
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("LoginMode","tourist");
                        startActivity(intent);
                        finish();
                    }
                });

                dialog.setNegativeButton("取消", null);
                dialog.show();
                break;
        }
    }

    private boolean loginResult() {
        boolean isLogin = false;
        SharedPreferences sp = DataUtils.getSharedPreferences(this);
        String loginPassword = sp.getString(mUserName.getText().toString(), "");
        if (loginPassword.equals(mPWD.getText().toString())) {
            if(mRemember.isChecked()){
                sp.edit().putString("last_username",mUserName.getText().toString()).commit();
                sp.edit().putString("last_password",loginPassword).commit();
            }else{
                sp.edit().putString("last_username","").commit();
                sp.edit().putString("last_password","").commit();
            }
            isLogin = true;
        }
        return isLogin;
    }
}
