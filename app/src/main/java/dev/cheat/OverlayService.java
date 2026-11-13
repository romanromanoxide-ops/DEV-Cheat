package dev.cheat;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OverlayService extends Service {
    private WindowManager windowManager;
    private View panel;
    private WindowManager.LayoutParams params;

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(18, 14, 18, 14);
        box.setBackgroundColor(0xEE1A1026);

        TextView heading = item("DEV  •  MENU", 18, 0xFFE8D9FF);
        box.addView(heading);
        box.addView(item("Статус: демонстрационный режим", 13, 0xFFB9A8CC));
        box.addView(item("Профиль: стандартный", 13, 0xFFB9A8CC));
        box.addView(item("Функции игры не изменяются", 12, 0xFF8F7BA5));

        TextView close = item("Закрыть ×", 14, 0xFFFFB6C8);
        close.setPadding(0, 16, 0, 4);
        close.setOnClickListener(v -> stopSelf());
        box.addView(close);

        panel = box;
        params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 30;
        params.y = 180;

        box.setOnTouchListener(new View.OnTouchListener() {
            private int startX, startY;
            private float touchX, touchY;
            @Override public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startX = params.x; startY = params.y;
                    touchX = event.getRawX(); touchY = event.getRawY();
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    params.x = startX + (int)(event.getRawX() - touchX);
                    params.y = startY + (int)(event.getRawY() - touchY);
                    windowManager.updateViewLayout(panel, params);
                    return true;
                }
                return event.getAction() == MotionEvent.ACTION_UP;
            }
        });

        windowManager.addView(panel, params);
    }

    private TextView item(String text, int size, int color) {
        TextView view = new TextView(this);
        view.setText(text);
        view.setTextSize(size);
        view.setTextColor(color);
        view.setPadding(4, 7, 4, 7);
        return view;
    }

    @Override public void onDestroy() {
        if (panel != null && windowManager != null) {
            try { windowManager.removeView(panel); } catch (Exception ignored) {}
            panel = null;
        }
        super.onDestroy();
    }

    @Override public IBinder onBind(Intent intent) { return null; }
}
