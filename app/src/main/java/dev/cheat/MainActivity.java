package dev.cheat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final int REQUEST_OVERLAY = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(28, 28, 28, 28);
        root.setBackgroundColor(0xFF100B18);

        TextView title = new TextView(this);
        title.setText("DEV CHEAT");
        title.setTextColor(0xFFE8D9FF);
        title.setTextSize(28);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("Демонстрационное плавающее меню\nБез внедрения в игры и изменения их памяти");
        subtitle.setTextColor(0xFFB9A8CC);
        subtitle.setTextSize(14);
        subtitle.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(-1, -2);
        sp.setMargins(0, 18, 0, 28);
        root.addView(subtitle, sp);

        Button start = new Button(this);
        start.setText("Включить плавающее меню");
        root.addView(start, new LinearLayout.LayoutParams(-1, -2));

        Button stop = new Button(this);
        stop.setText("Выключить меню");
        root.addView(stop, new LinearLayout.LayoutParams(-1, -2));

        start.setOnClickListener(v -> {
            if (Settings.canDrawOverlays(this)) {
                startService(new Intent(this, OverlayService.class));
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_OVERLAY);
            }
        });

        stop.setOnClickListener(v -> stopService(new Intent(this, OverlayService.class)));
        setContentView(root);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OVERLAY && Settings.canDrawOverlays(this)) {
            startService(new Intent(this, OverlayService.class));
        }
    }
}
