package com.example.pingpong;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity { //클래스를 상속받아 MainActivity 클래스를 작성한다.

    @Override
    protected void onCreate(Bundle savedInstanceState) { //onCreat 메소드의 매개변수는 이전 실행 상태를 전달한다.
        super.onCreate(savedInstanceState); //상위클래스의 onCreat를 호출한다.
        MySurfaceView view = new MySurfaceView(this); //view는 MySurfaceView로 선언한다.
        setContentView(view); //view를 띄운다.
    }

    @Override
    protected void onPause(){ //다른 Activity가 활성화 되었을 때 호출한다.
        super.onPause();
    }

    protected void onSaveInstanceState(Bundle outState){ //Activity가 종료될 때 데이터가 저장된다.
        super.onSaveInstanceState(outState);
    }
}
