package com.example.pingpong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//움직이는 공을 나타내는 클래스
class Ball{
    int x, y, xInc = 1, yInc = 1; //x,y : 현재 위치를 나타냄 / xInc와 yInc : 한 번에 움직이는 거리
    int diameter; //공의 반지름
    static int WIDTH = 1080, HEIGHT = 1920; //움직이는 공간의 크기

    public Ball(int d){ //Ball 생성자
        this.diameter = d; // 객체 d에 반지름을 할당한다.

        x = (int) (Math.random() * (WIDTH - d) + 3); //볼의 위치를 랜덤하게 설정
        y = (int) (Math.random() * (HEIGHT - d) + 3);

        xInc = (int) (Math.random() * 5 + 1); //한 번에 움직이는 거리도 랜덤하게 설정
        yInc = (int) (Math.random() * 5 + 1);
    }
    //여기서 공을 그린다.
    public void paint(Canvas g) { //paint 생성자가 Canvas의 객체 g를 생성한다.
        Paint paint = new Paint(); //piant를 선언한다.
        //벽에 부딪히면 반사하게 한다.
        if(x < 0|| x> (WIDTH - diameter))
            xInc = -xInc; //부호를 반대로 만들어서 공이 반대로 움직이게 한다.
        if(y < 0|| y> (HEIGHT - diameter))
            yInc = -yInc;

        x += xInc; //볼의 좌표를 갱신한다.
        y += yInc;

        //볼을 화면에 그린다.
        paint.setColor(Color.RED); //색은 빨강
        g.drawCircle(x, y, diameter, paint); //x, y, diameter의 정보를 통해 그린다.
    }
}

//서피스 뷰 정의
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    //MySurfaceView 클래스는 SurfaceView 클래스를 상속받아 SurfaceHolder.Callback 인터페이스를 구현한다.
    public Ball basket[] = new Ball[10]; //ball의 배열을 선언한다. -> 메모리가 잡힌다.
    private MyThread thread; //스레드 참조 변수를 선언한다.

    public MySurfaceView(Context context){ //MySurfaceView 생성자가 객체 context를 생성한다.
        super(context); //객체의 정보를 상위클래스로 전달한다.

        SurfaceHolder holder = getHolder(); //서피스 뷰의 홀더를 얻는다.
        holder.addCallback(this); //이 자체에서 콜백을 받을 수 있도록 콜백 메소드를 처리한다.

        thread = new MyThread(holder); //스레드를 생성한다.

        // Ball 객체를 생성하여서 배열에 넣는다.
        for (int i = 0; i < 10; i++) //10개의 볼을 생성한다.
            basket[i] = new Ball(20); //diametter를 20으로 한다.
    }

    public MyThread getThread() { // 생성자 스레드를 생성한다.
        return thread; //스레드 값을 반환한다.
    }

    public void surfaceCreated(SurfaceHolder holder){ //서피스뷰가 생성된다.
        thread.setRunning(true); //스레드를 시작한다.
        thread.start(); //스레드를 실행한다.
    }

    public void surfaceDestroyed(SurfaceHolder holder){ //서피스뷰가 소멸된다.
        boolean retry = true; //다시 그릴지 retry를 선언한다.

        thread.setRunning(false); //스레드를 중지시킨다.
        while (retry){ //retry의 결과에 따라
            try{
                thread.join(); //메인 스레드와 합친다.
                retry = false;
            } catch (InterruptedException e){ //아무것도 처리하지 않는다.
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
        //서피스뷰를 변경한다.
        //Ball.WIDTH = width;
        //Ball.HEIGHT = height;

    }
    //스레드를 내부 클래스로 정의한다.
    public class MyThread extends Thread{ //Thread를 상속받아 MyThread를 작성한다.
        private boolean mRun = false; //run이 한번만 실행한다.
        private SurfaceHolder mSurfaceHolder; //SurfaceHolder이 선언한다.

        public MyThread(SurfaceHolder surfaceHolder){ //생성자를 호출한다.
            mSurfaceHolder = surfaceHolder; //내부 변수에 선언한다.
        }

        @Override
        //실제 그리는 메소드를 만든다.
        //holder에 있는 서페이스를 사용 가능하다.
        public void run() { //run하면 실행한다. 반복루프
            while (mRun) { //run에 따라서
                Canvas c = null; //canvas를 초기화한다.
                try { //run이 시도되면
                    c = mSurfaceHolder.lockCanvas(null); //lock걸고 쓴다. 캔버스를 얻는다.
                    c.drawColor(Color.BLACK); //캔버스의 배경을 지운다.
                    synchronized (mSurfaceHolder) { //동기화한다.
                        for (Ball b : basket) { //basket의 모든 원소를 그린다.
                            b.paint(c); //캔버스를 그린다.
                        }
                    }
                } finally { //10번 돌고 빠져나가면
                    if (c != null){ //끝나면
                        mSurfaceHolder.unlockCanvasAndPost(c); //캔버스의 로킹을 푼다.
                    }
                }
            }
        }
            public void setRunning(boolean b){ //run이 ture를 주면 계속 스레드가 반복하고 실행된다.
                mRun = b; //mRun은 b로 선언한다.
            }
    }
}
