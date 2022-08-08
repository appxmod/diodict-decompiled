package com.diotek.diodict.uitool;

import android.app.Activity;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.diotek.diodict3.phone.samsung.chn.R;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class CandidateBox {
    private static final int BASE_CANDI_INDEX = 0;
    private static final int CANDI_COUNT = 5;
    private static final int INPUT_WAIT_TIME = 2000;
    private AddStringToEdit mInterfaceAddStringToEdit;
    private LinearLayout mBox = null;
    private LinearLayout mWrapBox = null;
    private ArrayList<Button> mCandiList = new ArrayList<>();
    private String[] mCandiStringList = new String[5];
    private boolean isShow = false;
    public Handler mHandler = new Handler();
    private Runnable mAddStringToEditAndHide = new Runnable() { // from class: com.diotek.diodict.uitool.CandidateBox.2
        @Override // java.lang.Runnable
        public void run() {
            CandidateBox.this.mInterfaceAddStringToEdit.addString(CandidateBox.this.mCandiStringList[0]);
            CandidateBox.this.hide();
        }
    };
    private View.OnClickListener mBtnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.uitool.CandidateBox.3
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            int toAddCandiIndex = ((Integer) v.getTag()).intValue();
            CandidateBox.this.mInterfaceAddStringToEdit.addString(CandidateBox.this.mCandiStringList[toAddCandiIndex]);
            CandidateBox.this.hide();
        }
    };

    /* loaded from: classes.dex */
    public interface AddStringToEdit {
        void addString(String str);
    }

    public CandidateBox(AddStringToEdit addStringToEdit) {
        this.mInterfaceAddStringToEdit = null;
        this.mInterfaceAddStringToEdit = addStringToEdit;
        initCandiStringList();
    }

    public void Init(Activity activity) {
        int[] mButtonResIDList = {R.id.candi1, R.id.candi2, R.id.candi3, R.id.candi4, R.id.candi5};
        this.mBox = (LinearLayout) activity.findViewById(R.id.candidatebox);
        this.mWrapBox = (LinearLayout) activity.findViewById(R.id.candidatewraplayout);
        this.mCandiList.clear();
        for (int i = 0; i < 5; i++) {
            Button btn = (Button) activity.findViewById(mButtonResIDList[i]);
            btn.setOnClickListener(this.mBtnClickListener);
            btn.setTag(Integer.valueOf(i));
            this.mCandiList.add(btn);
        }
        this.mWrapBox.setOnTouchListener(new View.OnTouchListener() { // from class: com.diotek.diodict.uitool.CandidateBox.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View arg0, MotionEvent event) {
                if (event.getAction() == 0) {
                    CandidateBox.this.hide();
                    return false;
                }
                return false;
            }
        });
        for (int i2 = 0; i2 < 5; i2++) {
            this.mCandiList.get(i2).setText(this.mCandiStringList[i2]);
        }
        if (this.isShow) {
            show();
        } else {
            hide();
        }
    }

    public void setCandidate(String[] candList) {
        if (candList.length >= 5) {
            if (this.isShow) {
                this.mInterfaceAddStringToEdit.addString(this.mCandiStringList[0]);
            }
            this.mHandler.removeCallbacks(this.mAddStringToEditAndHide);
            this.mHandler.postDelayed(this.mAddStringToEditAndHide, 2000L);
            for (int i = 0; i < 5; i++) {
                this.mCandiList.get(i).setText(candList[i]);
                this.mCandiStringList[i] = candList[i];
            }
        }
    }

    public void show() {
        this.mBox.setVisibility(0);
        this.isShow = true;
    }

    public void hide() {
        this.mBox.setVisibility(8);
        this.isShow = false;
        initCandiStringList();
    }

    private void initCandiStringList() {
        for (int i = 0; i < 5; i++) {
            this.mCandiStringList[i] = "";
        }
    }
}
