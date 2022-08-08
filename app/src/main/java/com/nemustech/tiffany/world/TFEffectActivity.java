package com.nemustech.tiffany.world;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import com.nemustech.tiffany.world.ITFServiceFrom;
import com.nemustech.tiffany.world.ITFServiceTo;
import com.nemustech.tiffany.world.TFEffect;

/* loaded from: classes.dex */
public class TFEffectActivity {
    public static final String BITMAP_FILENAME = "capBitmap.png";
    public static final String CAPTURE_PACKAGE = "com.nemustech.tiffany.world.keyPackage";
    public static final String EFFECT_ID = "com.nemustech.tiffany.world.keyEffect";
    private static final String TAG = "TFEffectActivity";
    private Activity mActivityFrom;
    private Activity mActivityTo;
    private Bitmap mBmpTo;
    private int mEffectID;
    private Intent mIntentFrom;
    private ServiceConnection mTFConnectionFrom = new ServiceConnection() { // from class: com.nemustech.tiffany.world.TFEffectActivity.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            TFEffectActivity.this.mTFServiceFrom = ITFServiceFrom.Stub.asInterface(service);
            Bitmap bmpCapture = TFEffectActivity.this.captureBitmap(TFEffectActivity.this.mActivityFrom);
            if (bmpCapture != null && TFUtils.saveBitmapToFile(TFEffectActivity.this.mActivityFrom, TFEffectActivity.BITMAP_FILENAME, bmpCapture, Bitmap.CompressFormat.PNG, 1)) {
                TFEffectActivity.this.mIntentFrom.putExtra(TFEffectActivity.EFFECT_ID, TFEffectActivity.this.mEffectID);
                TFEffectActivity.this.mIntentFrom.putExtra(TFEffectActivity.CAPTURE_PACKAGE, TFEffectActivity.this.mActivityFrom.getPackageName());
                TFEffectActivity.this.setupServiceFrom(TFEffectActivity.this.mActivityFrom);
            }
            try {
                TFEffectActivity.this.mTFServiceFrom.setPathFrom(TFEffectActivity.this.mActivityFrom.getPackageName(), TFEffectActivity.BITMAP_FILENAME);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            final Handler handler = new Handler();
            handler.post(new Runnable() { // from class: com.nemustech.tiffany.world.TFEffectActivity.2.1
                @Override // java.lang.Runnable
                public void run() {
                    boolean isBlock = false;
                    try {
                        isBlock = TFEffectActivity.this.mTFServiceFrom.isBlock();
                    } catch (RemoteException e2) {
                        e2.printStackTrace();
                    }
                    if (isBlock) {
                        TFEffectActivity.this.mActivityFrom.startActivity(TFEffectActivity.this.mIntentFrom);
                        TFEffectActivity.this.mActivityFrom.overridePendingTransition(0, 0);
                        TFEffectActivity.this.mActivityFrom.unbindService(TFEffectActivity.this.mTFConnectionFrom);
                        return;
                    }
                    handler.post(this);
                }
            });
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName className) {
            TFEffectActivity.this.mTFServiceFrom = null;
        }
    };
    private ServiceConnection mTFConnectionTo = new ServiceConnection() { // from class: com.nemustech.tiffany.world.TFEffectActivity.3
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            TFEffectActivity.this.mTFServiceTo = ITFServiceTo.Stub.asInterface(service);
            TFEffectActivity.this.startEffect(TFEffectActivity.this.mActivityTo);
            TFEffectActivity.this.mActivityTo.unbindService(TFEffectActivity.this.mTFConnectionTo);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName className) {
            TFEffectActivity.this.mTFServiceTo = null;
        }
    };
    private TFEffect.AnimationEventListener mEffectAnimationListener = new TFEffect.AnimationEventListener() { // from class: com.nemustech.tiffany.world.TFEffectActivity.4
        @Override // com.nemustech.tiffany.world.TFEffect.AnimationEventListener
        public void onAnimationStart(int EffectKind, boolean bReversed) {
            try {
                TFEffectActivity.this.mTFServiceTo.showBlockWindow(false);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override // com.nemustech.tiffany.world.TFEffect.AnimationEventListener
        public void onAnimationEnd(int EffectKind, boolean bReversed) {
        }
    };
    private ITFServiceFrom mTFServiceFrom = null;
    private ITFServiceTo mTFServiceTo = null;

    public void startActivityFrom(Activity activity, Intent intent, int effectID) {
        setupServiceFrom(activity);
        this.mActivityFrom = activity;
        this.mIntentFrom = intent;
        this.mEffectID = effectID;
    }

    public void startActivityTo(Activity activity) {
        this.mActivityTo = activity;
        final Handler handler = new Handler();
        handler.post(new Runnable() { // from class: com.nemustech.tiffany.world.TFEffectActivity.1
            @Override // java.lang.Runnable
            public void run() {
                TFEffectActivity.this.mBmpTo = TFEffectActivity.this.captureBitmap(TFEffectActivity.this.mActivityTo);
                if (TFEffectActivity.this.mBmpTo != null) {
                    TFEffectActivity.this.setupServiceTo(TFEffectActivity.this.mActivityTo);
                } else {
                    handler.post(this);
                }
            }
        });
    }

    public void closeEffectFrom(Activity activity) {
        if (this.mTFServiceFrom != null) {
            try {
                if (this.mTFServiceFrom.isBound()) {
                    activity.unbindService(this.mTFConnectionFrom);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startEffect(Activity activity) {
        TFEffect effect = new TFEffect(activity);
        effect.setAnimationEventListener(this.mEffectAnimationListener);
        Intent intent = activity.getIntent();
        int effectID = intent.getIntExtra(EFFECT_ID, -1);
        String packageFrom = intent.getStringExtra(CAPTURE_PACKAGE);
        Bitmap bmpFrom = TFUtils.loadBitmapFromFile(packageFrom, BITMAP_FILENAME);
        if (bmpFrom != null) {
            effect.addView(null, bmpFrom, 0);
        }
        if (this.mBmpTo != null) {
            effect.addView(null, this.mBmpTo, 1);
        }
        effect.showEffect(effectID, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setupServiceFrom(Activity activity) {
        return activity.bindService(new Intent(ITFServiceFrom.class.getName()), this.mTFConnectionFrom, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setupServiceTo(Activity activity) {
        return activity.bindService(new Intent(ITFServiceTo.class.getName()), this.mTFConnectionTo, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bitmap captureBitmap(Activity activity) {
        View v = activity.getWindow().getDecorView();
        v.setDrawingCacheEnabled(true);
        Bitmap b = v.getDrawingCache();
        if (b != null) {
            return cutStatusBarFromBitmap(activity, b);
        }
        return b;
    }

    protected Bitmap cutStatusBarFromBitmap(Activity a, Bitmap bitmap) {
        int yOffset = TFUtils.getStatusBarHeight(a);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap cut = Bitmap.createBitmap(bitmap, 0, yOffset, w, h - yOffset, (Matrix) null, false);
        return cut;
    }
}
