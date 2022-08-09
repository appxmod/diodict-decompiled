package com.nemustech.tiffany.world;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import com.nemustech.tiffany.world.ITFServiceFrom;
import com.nemustech.tiffany.world.ITFServiceTo;

/* loaded from: classes.dex */
public class TFService extends Service {
    private static final int HIDE_IMAGE_VIEW_MSG = 3;
    private static final int SET_IMAGE_MSG = 1;
    private static final int SHOW_IMAGE_VIEW_MSG = 2;
    private static final String TAG = "TFService";
    private static ImageView mImageView;
    private static boolean mSetView = false;
    private String mFromFileName;
    private String mFromPackageName;
    private WindowManager mWindowMgr;
    private final Handler mHandler = new Handler() { // from class: com.nemustech.tiffany.world.TFService.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Bitmap bmp = TFUtils.loadBitmapFromFile(TFService.this.mFromPackageName, TFService.this.mFromFileName);
                    if (bmp != null) {
                        TFService.mImageView.setBackgroundDrawable(new BitmapDrawable(bmp));
                        return;
                    }
                    return;
                case 2:
                    TFService.mImageView.setVisibility(View.VISIBLE);
                    return;
                case 3:
                    TFService.mImageView.setVisibility(View.INVISIBLE);
                    return;
                default:
                    super.handleMessage(msg);
                    return;
            }
        }
    };
    private final ITFServiceFrom.Stub mTiffanyBinderFrom = new ITFServiceFrom.Stub() { // from class: com.nemustech.tiffany.world.TFService.2
        @Override // com.nemustech.tiffany.world.ITFServiceFrom
        public void setPathFrom(String packageName, String fileName) throws RemoteException {
            TFService.this.mFromPackageName = packageName;
            TFService.this.mFromFileName = fileName;
            TFService.this.mHandler.sendEmptyMessage(1);
            TFService.this.mHandler.sendEmptyMessage(2);
        }

        @Override // com.nemustech.tiffany.world.ITFServiceFrom
        public boolean isBlock() throws RemoteException {
            return TFService.mImageView.getVisibility() == 0;
        }

        @Override // com.nemustech.tiffany.world.ITFServiceFrom
        public boolean isBound() throws RemoteException {
            return TFService.this.mIsBoundFrom;
        }
    };
    private final ITFServiceTo.Stub mTiffanyBinderTo = new ITFServiceTo.Stub() { // from class: com.nemustech.tiffany.world.TFService.3
        @Override // com.nemustech.tiffany.world.ITFServiceTo
        public void showBlockWindow(boolean show) throws RemoteException {
            if (show) {
                TFService.this.mHandler.sendEmptyMessage(2);
            } else {
                TFService.this.mHandler.sendEmptyMessage(3);
            }
        }

        @Override // com.nemustech.tiffany.world.ITFServiceTo
        public boolean isBound() throws RemoteException {
            return TFService.this.mIsBoundTo;
        }
    };
    private boolean mIsBoundFrom = false;
    private boolean mIsBoundTo = false;

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        if (ITFServiceFrom.class.getName().equals(intent.getAction())) {
            this.mIsBoundFrom = true;
            return this.mTiffanyBinderFrom;
        } else if (ITFServiceTo.class.getName().equals(intent.getAction())) {
            this.mIsBoundTo = true;
            return this.mTiffanyBinderTo;
        } else {
            return null;
        }
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        if (ITFServiceFrom.class.getName().equals(intent.getAction())) {
            this.mIsBoundFrom = false;
        }
        if (ITFServiceTo.class.getName().equals(intent.getAction())) {
            this.mIsBoundTo = false;
        }
        return super.onUnbind(intent);
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        if (!mSetView) {
            this.mWindowMgr = (WindowManager) getSystemService("window");
            mImageView = new ImageView(this);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams(-2, -2, 2005, 0, -1);
            lp.setTitle("TiffanyWindow");
            lp.x = 0;
            lp.y = 0;
            lp.width = this.mWindowMgr.getDefaultDisplay().getWidth();
            lp.height = this.mWindowMgr.getDefaultDisplay().getHeight() - TFUtils.getStatusBarHeight(this);
            this.mWindowMgr.addView(mImageView, lp);
            mImageView.setVisibility(View.INVISIBLE);
            mSetView = true;
        }
    }
}
