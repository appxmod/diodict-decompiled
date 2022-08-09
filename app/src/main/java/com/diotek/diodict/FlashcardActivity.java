package com.diotek.diodict;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.diotek.diodict.database.DioDictDatabase;
import com.diotek.diodict.database.DioDictDatabaseInfo;
import com.diotek.diodict.dependency.Dependency;
import com.diotek.diodict.engine.DictInfo;
import com.diotek.diodict.uitool.BaseActivity;
import com.diotek.diodict.uitool.CommonUtils;
import com.diotek.diodict.uitool.PageGridView;
import com.diotek.diodict.uitool.TextImageButton;
import com.diotek.diodict3.phone.samsung.chn.R;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class FlashcardActivity extends BaseActivity {
    static final int DIALOG_EDIT = 1;
    static final int DIALOG_MAKE = 0;
    ArrayList<HashMap<String, Object>> mFlashcardItems = new ArrayList<>();
    private String mInputWordbookName = null;
    private Button mBtnMakeWordbook = null;
    private TextImageButton mBtnMakeWordbookOk = null;
    private TextImageButton mBtnMakeWordbookCancel = null;
    private TextImageButton mBtnEditWordbookOk = null;
    private TextImageButton mBtnEditWordbookCancel = null;
    private Button mBtnDelWordbook = null;
    private Button mBtnEditWordbook = null;
    private EditText mEdittextWordbookName = null;
    private EditText mEdittextEditWordbookName = null;
    private TextView mInputWordBookName = null;
    private RadioButton mCard1 = null;
    private RadioButton mCard2 = null;
    private RadioButton mCard3 = null;
    private Dialog mWordbookDialog = null;
    private Context mContext = null;
    private String mWordbookName = "";
    private ImageButton mClearBtn = null;
    private int mFlashcardMode = 0;
    private int mCurrentGridView = 0;
    private int mCurrentMaxPage = 1;
    private int mWordbookType = 1;
    PageGridView mPageGridView = null;
    private int mOrientation = 0;
    private Bundle mBundle = null;
    private String mReturnActivity = null;
    private String mBackupCardName = null;
    private boolean mIsCreate = true;
    private int currWordbookDialogId = -1;
    View.OnClickListener mBtnEditWordbookOkOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardActivity.3
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            FlashcardActivity.this.runBtnEditWordbookOk(v);
            FlashcardActivity.this.mPageGridView.notifyFlashcardList(FlashcardActivity.this.mFlashcardItems, FlashcardActivity.this.mCurrentMaxPage, FlashcardActivity.this.mOrientation);
        }
    };
    View.OnClickListener mBtnEditWordbookCancelOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardActivity.4
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            FlashcardActivity.this.runBtnEditWordbookCancel();
        }
    };
    View.OnClickListener mBtnEditWordbookOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardActivity.5
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (!v.isSelected()) {
                v.setSelected(true);
            } else {
                v.setSelected(false);
            }
            FlashcardActivity.this.mBtnDelWordbook.setSelected(false);
            FlashcardActivity.this.runBtnEditWordbook(v);
        }
    };
    View.OnClickListener mBtnDelWordbookOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardActivity.6
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (!v.isSelected()) {
                v.setSelected(true);
            } else {
                v.setSelected(false);
            }
            FlashcardActivity.this.mBtnEditWordbook.setSelected(false);
            FlashcardActivity.this.runBtnDelWordbook(v);
        }
    };
    PageGridView.PageGridViewOnClickListener mWordbookFolderGridViewOnItemClickListener = new PageGridView.PageGridViewOnClickListener() { // from class: com.diotek.diodict.FlashcardActivity.7
        @Override // com.diotek.diodict.uitool.PageGridView.PageGridViewOnClickListener
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            switch (FlashcardActivity.this.mFlashcardMode) {
                case 1:
                    FlashcardActivity.this.runFlashcardFolderGridViewEdit(position);
                    return;
                case 2:
                    FlashcardActivity.this.runFlashcardFolderGridViewDelete(position);
                    return;
                default:
                    FlashcardActivity.this.runFlashcardFolderGridViewIdle(position);
                    return;
            }
        }

        @Override // com.diotek.diodict.uitool.PageGridView.PageGridViewOnClickListener
        public void onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
            if (FlashcardActivity.this.mFlashcardMode != 2) {
                FlashcardActivity.this.setFlashcardMode(1);
                FlashcardActivity.this.runFlashcardFolderGridViewEdit(position);
                FlashcardActivity.this.setFlashcardMode(0);
            }
        }
    };
    View.OnClickListener mBtnMakeWordbookOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardActivity.8
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            FlashcardActivity.this.makeWordbook();
        }
    };
    View.OnClickListener mBtnMakeWordbookOkOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardActivity.9
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            FlashcardActivity.this.runBtnMakeWordbookOk(v);
        }
    };
    View.OnClickListener mBtnMakeWordbookCancelOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardActivity.10
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            FlashcardActivity.this.runBtnMakeWordbookCancel(v);
        }
    };
    CompoundButton.OnCheckedChangeListener mCard1OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.FlashcardActivity.11
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                FlashcardActivity.this.mWordbookType = 1;
            }
        }
    };
    CompoundButton.OnCheckedChangeListener mCard2OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.FlashcardActivity.12
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                FlashcardActivity.this.mWordbookType = 2;
            }
        }
    };
    CompoundButton.OnCheckedChangeListener mCard3OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.diotek.diodict.FlashcardActivity.13
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                FlashcardActivity.this.mWordbookType = 3;
            }
        }
    };
    View.OnClickListener mEditClearBtnOnClickListener = new View.OnClickListener() { // from class: com.diotek.diodict.FlashcardActivity.14
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (FlashcardActivity.this.mInputWordBookName != null) {
                if (FlashcardActivity.this.mEdittextWordbookName != null) {
                    FlashcardActivity.this.mEdittextWordbookName.setText("");
                } else if (FlashcardActivity.this.mEdittextEditWordbookName != null) {
                    FlashcardActivity.this.mEdittextEditWordbookName.setText("");
                }
                FlashcardActivity.this.mInputWordBookName.setVisibility(View.VISIBLE);
            }
        }
    };
    TextWatcher mWordbookEditWatcher = new TextWatcher() { // from class: com.diotek.diodict.FlashcardActivity.15
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            FlashcardActivity.this.hideEditMessage();
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable s) {
            FlashcardActivity.this.mBackupCardName = s.toString();
            if (FlashcardActivity.this.mBackupCardName.length() == 0 && FlashcardActivity.this.mInputWordBookName != null) {
                FlashcardActivity.this.mInputWordBookName.setVisibility(View.VISIBLE);
            }
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };
    View.OnFocusChangeListener mCard1OnFocusChangeListener = new View.OnFocusChangeListener() { // from class: com.diotek.diodict.FlashcardActivity.16
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View card, boolean isFocused) {
            if (isFocused) {
                ((RadioButton) card).setChecked(true);
            }
        }
    };
    View.OnFocusChangeListener mCard2OnFocusChangeListener = new View.OnFocusChangeListener() { // from class: com.diotek.diodict.FlashcardActivity.17
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View card, boolean isFocused) {
            if (isFocused) {
                ((RadioButton) card).setChecked(true);
            }
        }
    };
    View.OnFocusChangeListener mCard3OnFocusChangeListener = new View.OnFocusChangeListener() { // from class: com.diotek.diodict.FlashcardActivity.18
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View card, boolean isFocused) {
            if (isFocused) {
                ((RadioButton) card).setChecked(true);
            }
        }
    };

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < 11) {
            requestWindowFeature(7);
        }
        this.mCurrentMenuId = R.id.menu_flashcard;
        if (super.onCreateActivity(savedInstanceState)) {
            if (Dependency.isContainTTS()) {
                setVolumeControlStream(3);
            }
            setContentView(R.layout.flashcard_layout);
            initActivity();
            Intent intent = getIntent();
            String enteringmode = intent.getStringExtra(DictInfo.INTENT_FLASHCARD_ENTERING_MODE);
            int index = intent.getIntExtra(DictInfo.INTENT_FLASHCARD_FOLER_ID, -1);
            if (index != -1) {
                scrollGridView(index);
            }
            if (enteringmode == null) {
                this.mReturnActivity = null;
            } else {
                this.mReturnActivity = enteringmode;
            }
        }
    }

    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
	public void onResume() {
        updateWordbookFolderItems();
        super.onResume();
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int id) {
        this.currWordbookDialogId = id;
        switch (id) {
            case 0:
                return createMakeWordbookDialog();
            case 1:
                return createEditWordbookDialog(this.mBundle);
            default:
                return super.onCreateDialog(id);
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onDetachedFromWindow() {
        Log.d("diodict", " flipper Err");
        try {
            super.onDetachedFromWindow();
        } catch (IllegalArgumentException e) {
        }
    }

    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        this.mIsCreate = false;
        if (this.mWordbookDialog != null && this.mWordbookDialog.isShowing()) {
            switch (this.currWordbookDialogId) {
                case 0:
                    removeDialog(0);
                    showDialog(0);
                    if (this.mEdittextWordbookName != null) {
                        this.mEdittextWordbookName = (EditText) this.mWordbookDialog.findViewById(R.id.edittext_makewordbookname);
                        this.mEdittextWordbookName.setText(this.mBackupCardName);
                        this.mEdittextWordbookName.addTextChangedListener(this.mWordbookEditWatcher);
                    }
                    ImageButton clearBtn = (ImageButton) this.mWordbookDialog.findViewById(R.id.edit_clearbtn);
                    if (clearBtn != null) {
                        clearBtn.setOnClickListener(this.mEditClearBtnOnClickListener);
                        break;
                    }
                    break;
                case 1:
                    removeDialog(1);
                    showDialog(1);
                    if (this.mEdittextEditWordbookName != null) {
                        this.mEdittextEditWordbookName = (EditText) this.mWordbookDialog.findViewById(R.id.edittext_editwordbookname);
                        this.mEdittextEditWordbookName.setText(this.mBackupCardName);
                        this.mEdittextEditWordbookName.addTextChangedListener(this.mWordbookEditWatcher);
                    }
                    ImageButton clearBtn2 = (ImageButton) this.mWordbookDialog.findViewById(R.id.edit_clearbtn);
                    if (clearBtn2 != null) {
                        clearBtn2.setOnClickListener(this.mEditClearBtnOnClickListener);
                        break;
                    }
                    break;
            }
        }
        initActivity();
        if (isVisiableView(this.mBtnMakeWordbook)) {
            this.mBtnMakeWordbook.requestFocus();
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                if (this.mWordbookDialog != null && this.mWordbookDialog.isShowing()) {
                    switch (this.currWordbookDialogId) {
                        case 0:
                        case 1:
                            removeDialog(this.currWordbookDialogId);
                            this.mWordbookDialog = null;
                            return true;
                    }
                }
                if (this.mFlashcardMode != 0 && this.mFlashcardItems.size() > 2) {
                    setFlashcardMode(0);
                    return true;
                }
                runSearchBtn(true);
                break;
            case 21:
                if (this.mClearBtn != null && this.mClearBtn.isFocusable() && this.mEdittextWordbookName != null) {
                    this.mEdittextWordbookName.requestFocus();
                    break;
                }
                break;
            case 22:
                if (this.mEdittextWordbookName != null && this.mEdittextWordbookName.isFocused() && this.mClearBtn != null) {
                    this.mClearBtn.requestFocus();
                    break;
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override // com.diotek.diodict.uitool.BaseActivity, android.app.Activity
	public void onDestroy() {
        super.onDestroy();
    }

    private void initActivity() {
        backupParameters();
        if (!this.mIsCreate) {
            setContentView(R.layout.flashcard_layout);
        }
        prepareTitleLayout(R.string.title_flashcard, this.mIsCreate);
        this.mOrientation = getResources().getConfiguration().orientation;
        prepareContentLayout();
        if (this.mIsCreate || this.mFlashcardMode != 0) {
            restoreState();
        }
        if (this.mWordbookDialog != null && this.mWordbookDialog.isShowing() && this.mBackupCardName != null) {
            if (this.mEdittextWordbookName != null) {
                this.mEdittextWordbookName.setText(this.mBackupCardName);
                this.mEdittextWordbookName.setSelection(this.mBackupCardName.length());
                if (this.mWordbookDialog.isShowing()) {
                    ((TextView) this.mWordbookDialog.findViewById(R.id.makeview_title)).setText(R.string.new_wordbook);
                    ((TextImageButton) this.mWordbookDialog.findViewById(R.id.button_makewordbook_ok)).setText(R.string.ok);
                    ((TextImageButton) this.mWordbookDialog.findViewById(R.id.button_makewordbook_cancel)).setText(R.string.cancel);
                }
            } else if (this.mEdittextEditWordbookName != null) {
                this.mEdittextEditWordbookName.setText(this.mBackupCardName);
                this.mEdittextEditWordbookName.setSelection(this.mBackupCardName.length());
                if (this.mWordbookDialog.isShowing()) {
                    ((TextView) this.mWordbookDialog.findViewById(R.id.editview_title)).setText(R.string.edit_flashcard);
                    ((TextImageButton) this.mWordbookDialog.findViewById(R.id.button_editwordbook_ok)).setText(R.string.ok);
                    ((TextImageButton) this.mWordbookDialog.findViewById(R.id.button_editwordbook_cancel)).setText(R.string.cancel);
                }
            }
        }
    }

    public void prepareContentLayout() {
        prepareWordbookFolderEditBtn();
        prepareWordbookFolderGridView();
    }

    public void prepareWordbookFolderEditBtn() {
        this.mBtnMakeWordbook = (Button) findViewById(R.id.bt_makeflashcard);
        this.mBtnDelWordbook = (Button) findViewById(R.id.bt_deletewordbook);
        this.mBtnEditWordbook = (Button) findViewById(R.id.bt_editflashcard);
        this.mBtnMakeWordbook.setOnClickListener(this.mBtnMakeWordbookOnClickListener);
        this.mBtnDelWordbook.setOnClickListener(this.mBtnDelWordbookOnClickListener);
        this.mBtnEditWordbook.setOnClickListener(this.mBtnEditWordbookOnClickListener);
    }

    public void prepareWordbookFolderGridView() {
        ViewFlipper layout = (ViewFlipper) findViewById(R.id.gridviewGrouplayout);
        RadioGroup pageDotLayout = (RadioGroup) findViewById(R.id.pageBarLayout);
        this.mPageGridView = new PageGridView(this, layout, pageDotLayout);
        this.mPageGridView.setOnItemClickListener(this.mWordbookFolderGridViewOnItemClickListener);
        updateWordbookFolderItems();
        this.mCurrentMaxPage = this.mPageGridView.createFlashcardPageGridView(this.mFlashcardItems, getCountPerPage(), this.mOrientation);
    }

    public void runBtnEditWordbookOk(View v) {
        if (runEditWordbookOk(String.valueOf(v.getTag()))) {
            Toast.makeText(this, getResources().getString(R.string.editWordbook), 0).show();
            updateWordbookFolderItems();
            setFlashcardMode(0);
            removeDialog(1);
        }
    }

    public void runBtnEditWordbookCancel() {
        removeDialog(1);
    }

    public void runBtnEditWordbook(View v) {
        if (this.mFlashcardMode == 1) {
            setFlashcardMode(0);
        } else {
            setFlashcardMode(1);
        }
    }

    public void runBtnDelWordbook(View v) {
        if (this.mFlashcardMode == 2) {
            setFlashcardMode(0);
        } else {
            setFlashcardMode(2);
        }
    }

    public void runBtnMakeWordbookOk(View v) {
        if (runMakeWordbookOK()) {
            updateWordbookFolderItems();
            this.mCurrentMaxPage = this.mPageGridView.notifyFlashcardList(this.mFlashcardItems, this.mCurrentMaxPage, this.mOrientation);
            this.mPageGridView.setPageView(this.mCurrentMaxPage - 1);
            removeDialog(0);
            setFlashcardMode(0);
        }
    }

    public void runBtnMakeWordbookCancel(View v) {
        removeDialog(0);
    }

    public void runFlashcardFolderGridViewIdle(int nPos) {
        HashMap<String, Object> tFlashcardRow;
        int nIndex = nPos + (this.mPageGridView.getCurrentPage() * getCountPerPage());
        if (this.mFlashcardItems != null && nIndex < this.mFlashcardItems.size() && (tFlashcardRow = this.mFlashcardItems.get(nIndex)) != null) {
            Integer nWordbookFolderId = (Integer) tFlashcardRow.get(DictInfo.ListItem_WordbookFolderId);
            Integer nWordCount = (Integer) tFlashcardRow.get(DictInfo.ListItem_WordCount);
            String WordbookName = (String) tFlashcardRow.get(DictInfo.ListItem_WordbookName);
            if (nWordCount.intValue() < 1) {
                Toast.makeText(this, getResources().getString(R.string.noItemInWordbook), 0).show();
                return;
            }
            finish();
            Intent intent = new Intent();
            if (this.mReturnActivity != null) {
                if (this.mReturnActivity.contains("DictationActivity")) {
                    intent.setClass(this, DictationActivity.class);
                } else if (this.mReturnActivity.contains("StudyActivity")) {
                    intent.setClass(this, StudyActivity.class);
                }
            } else {
                intent.setClass(this, FlashcardItemActivity.class);
            }
            intent.putExtra(DictInfo.INTENT_WORDBOOKFOLDERID, nWordbookFolderId);
            intent.putExtra(DictInfo.INTENT_WORDCOUNT, nWordCount);
            intent.putExtra(DictInfo.INTENT_WORDBOOKNAME, WordbookName);
            startActivity(intent);
        }
    }

    public void runFlashcardFolderGridViewDelete(int nPos) {
        this.mContext = this;
        HashMap<String, Object> item = this.mFlashcardItems.get((this.mPageGridView.getCurrentPage() * getCountPerPage()) + nPos);
        String message = getResources().getString(R.string.dialog_delete_selectedwordbook);
        this.mWordbookName = item.get(DictInfo.ListItem_WordbookName).toString();
        new AlertDialog.Builder(this).setMessage(message.replaceAll("%s", this.mWordbookName)).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.diotek.diodict.FlashcardActivity.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: com.diotek.diodict.FlashcardActivity.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                DioDictDatabase.deleteWordbookFolder(FlashcardActivity.this.mContext, FlashcardActivity.this.mWordbookName);
                Toast.makeText(FlashcardActivity.this.mContext, FlashcardActivity.this.getResources().getString(R.string.deleteWordbook), 0).show();
                FlashcardActivity.this.updateWordbookFolderItems();
                FlashcardActivity.this.mCurrentMaxPage = FlashcardActivity.this.mPageGridView.notifyFlashcardList(FlashcardActivity.this.mFlashcardItems, FlashcardActivity.this.mCurrentMaxPage, FlashcardActivity.this.mOrientation);
            }
        }).setCancelable(true).show();
    }

    public void runFlashcardFolderGridViewEdit(int nPos) {
        int index = nPos + (this.mPageGridView.getCurrentPage() * getCountPerPage());
        if (index < this.mFlashcardItems.size()) {
            HashMap<String, Object> flashcardRow = this.mFlashcardItems.get(index);
            this.mBundle = new Bundle();
            this.mBundle.putString(DioDictDatabaseInfo.DB_COLUMN_NAME, flashcardRow.get(DictInfo.ListItem_WordbookName).toString());
            this.mBundle.putInt(DioDictDatabaseInfo.DB_COLUMN_FOLDERTYPE, Integer.parseInt(flashcardRow.get(DictInfo.ListItem_WordbookFolderType).toString()));
            removeDialog(0);
            showDialog(1);
            if (this.mEdittextEditWordbookName != null) {
                this.mEdittextEditWordbookName = (EditText) this.mWordbookDialog.findViewById(R.id.edittext_editwordbookname);
                this.mEdittextEditWordbookName.addTextChangedListener(this.mWordbookEditWatcher);
            }
            this.mClearBtn = (ImageButton) this.mWordbookDialog.findViewById(R.id.edit_clearbtn);
            if (this.mClearBtn != null) {
                this.mClearBtn.setOnClickListener(this.mEditClearBtnOnClickListener);
            }
            this.mWordbookDialog.show();
        }
    }

    public void updateWordbookFolderItems() {
        this.mFlashcardItems.clear();
        addDefaultFolders();
        Cursor tCursor = DioDictDatabase.getWordbookFolderCursor(this);
        if (tCursor == null) {
            setFlashcardMode(3);
            return;
        }
        this.mFlashcardItems.size();
        do {
            int i = tCursor.getPosition();
            HashMap<String, Object> mFlashcardRow = new HashMap<>();
            mFlashcardRow.put(DictInfo.ListItem_WordbookName, tCursor.getString(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_NAME)));
            mFlashcardRow.put(DictInfo.ListItem_WordCount, Integer.valueOf(DioDictDatabase.getWordbookItemCount(this, tCursor.getInt(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_ID)))));
            mFlashcardRow.put(DictInfo.ListItem_WordbookFolderId, Integer.valueOf(tCursor.getInt(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_ID))));
            mFlashcardRow.put(DictInfo.ListItem_WordbookFolderType, Integer.valueOf(tCursor.getInt(tCursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_FOLDERTYPE))));
            addRowToFlashcardArrayList(i, mFlashcardRow);
        } while (tCursor.moveToNext());
        tCursor.close();
    }

    public void addRowToFlashcardArrayList(int CursorPos, HashMap<String, Object> row) {
        this.mFlashcardItems.add(row);
    }

    private void addDefaultFolders() {
        Cursor cursor = DioDictDatabase.getMemoCursor(this, 2);
        addDefaultFolderFlashcardRow(true, cursor);
        if (cursor != null) {
            cursor.close();
        }
        Cursor cursor2 = DioDictDatabase.getMarkerCursor(this, 2);
        addDefaultFolderFlashcardRow(false, cursor2);
        if (cursor2 != null) {
            cursor2.close();
        }
    }

    private void addDefaultFolderFlashcardRow(boolean bMemo, Cursor cursor) {
        int nCount = 0;
        int nFolderId = -1;
        if (cursor != null) {
            nCount = cursor.getCount();
            nFolderId = cursor.getInt(cursor.getColumnIndex(DioDictDatabaseInfo.DB_COLUMN_ID));
        }
        String wordbookName = DioDictDatabaseInfo.FOLDERNAME_MARKER;
        if (bMemo) {
            wordbookName = DioDictDatabaseInfo.FOLDERNAME_MEMO;
        }
        HashMap<String, Object> flashcardRow = new HashMap<>();
        flashcardRow.put(DictInfo.ListItem_WordbookName, wordbookName);
        flashcardRow.put(DictInfo.ListItem_WordCount, Integer.valueOf(nCount));
        flashcardRow.put(DictInfo.ListItem_WordbookFolderId, Integer.valueOf(nFolderId));
        flashcardRow.put(DictInfo.ListItem_WordbookFolderType, 1);
        addRowToFlashcardArrayList(this.mFlashcardItems.size(), flashcardRow);
    }

    public void createCommonDialog(int nLayout, int selectedCard) {
        this.mWordbookType = 1;
        this.mWordbookDialog = new Dialog(this);
        this.mWordbookDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.mWordbookDialog.requestWindowFeature(1);
        this.mWordbookDialog.setContentView(nLayout);
        this.mCard1 = (RadioButton) this.mWordbookDialog.findViewById(R.id.card1);
        this.mCard2 = (RadioButton) this.mWordbookDialog.findViewById(R.id.card2);
        this.mCard3 = (RadioButton) this.mWordbookDialog.findViewById(R.id.card3);
        this.mCard1.setOnCheckedChangeListener(this.mCard1OnCheckedChangeListener);
        this.mCard2.setOnCheckedChangeListener(this.mCard2OnCheckedChangeListener);
        this.mCard3.setOnCheckedChangeListener(this.mCard3OnCheckedChangeListener);
        this.mCard1.setOnFocusChangeListener(this.mCard1OnFocusChangeListener);
        this.mCard2.setOnFocusChangeListener(this.mCard2OnFocusChangeListener);
        this.mCard3.setOnFocusChangeListener(this.mCard3OnFocusChangeListener);
        switch (selectedCard) {
            case 1:
                this.mCard1.setChecked(true);
                break;
            case 2:
                this.mCard2.setChecked(true);
                break;
            case 3:
                this.mCard3.setChecked(true);
                break;
        }
        this.mWordbookType = selectedCard;
    }

    public Dialog createMakeWordbookDialog() {
        if (this.mWordbookDialog != null) {
            removeDialog(0);
            this.mWordbookDialog = null;
        }
        createCommonDialog(R.layout.flashcard_makedialog_layout, 1);
        if (this.mWordbookDialog == null) {
            return null;
        }
        this.mEdittextEditWordbookName = null;
        this.mCard1.setChecked(true);
        EditText tv = (EditText) this.mWordbookDialog.findViewById(R.id.edittext_makewordbookname);
        String string = getDefaultWordbookName();
        tv.setText(string);
        tv.setSelection(string.length());
        this.mBtnMakeWordbookOk = (TextImageButton) this.mWordbookDialog.findViewById(R.id.button_makewordbook_ok);
        this.mBtnMakeWordbookOk.setOnClickListener(this.mBtnMakeWordbookOkOnClickListener);
        this.mBtnMakeWordbookOk.setText(R.string.ok);
        this.mBtnMakeWordbookCancel = (TextImageButton) this.mWordbookDialog.findViewById(R.id.button_makewordbook_cancel);
        this.mBtnMakeWordbookCancel.setOnClickListener(this.mBtnMakeWordbookCancelOnClickListener);
        this.mBtnMakeWordbookCancel.setText(R.string.cancel);
        this.mInputWordBookName = (TextView) this.mWordbookDialog.findViewById(R.id.editview_editwordbook);
        if (CommonUtils.isLowResolutionDevice(this)) {
            this.mBtnMakeWordbookOk.setTextSize((int) (CommonUtils.getDeviceDensity(this) * 14.0f));
            this.mBtnMakeWordbookCancel.setTextSize((int) (CommonUtils.getDeviceDensity(this) * 14.0f));
        }
        if (this.mEdittextWordbookName == null) {
            this.mEdittextWordbookName = (EditText) this.mWordbookDialog.findViewById(R.id.edittext_makewordbookname);
            this.mBackupCardName = this.mEdittextWordbookName.getText().toString();
        }
        return this.mWordbookDialog;
    }

    public Dialog createEditWordbookDialog(Bundle db) {
        if (db == null) {
            return null;
        }
        if (this.mWordbookDialog != null) {
            removeDialog(1);
            this.mWordbookDialog = null;
        }
        createCommonDialog(R.layout.flashcard_editdialog_layout, db.getInt(DioDictDatabaseInfo.DB_COLUMN_FOLDERTYPE));
        if (this.mWordbookDialog == null) {
            return null;
        }
        this.mWordbookDialog.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.diotek.diodict.FlashcardActivity.19
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == 4) {
                    FlashcardActivity.this.runBtnEditWordbookCancel();
                    return true;
                }
                return false;
            }
        });
        this.mEdittextWordbookName = null;
        EditText tv = (EditText) this.mWordbookDialog.findViewById(R.id.edittext_editwordbookname);
        String string = db.getString(DioDictDatabaseInfo.DB_COLUMN_NAME);
        tv.setText(string);
        tv.setSelection(string.length());
        this.mBtnEditWordbookOk = (TextImageButton) this.mWordbookDialog.findViewById(R.id.button_editwordbook_ok);
        this.mBtnEditWordbookOk.setOnClickListener(this.mBtnEditWordbookOkOnClickListener);
        this.mBtnEditWordbookOk.setTag(db.getString(DioDictDatabaseInfo.DB_COLUMN_NAME));
        this.mBtnEditWordbookOk.setText(R.string.ok);
        this.mBtnEditWordbookCancel = (TextImageButton) this.mWordbookDialog.findViewById(R.id.button_editwordbook_cancel);
        this.mBtnEditWordbookCancel.setOnClickListener(this.mBtnEditWordbookCancelOnClickListener);
        this.mBtnEditWordbookCancel.setText(R.string.cancel);
        if (CommonUtils.isLowResolutionDevice(this)) {
            this.mBtnEditWordbookOk.setTextSize((int) (CommonUtils.getDeviceDensity(this) * 14.0f));
            this.mBtnEditWordbookCancel.setTextSize((int) (CommonUtils.getDeviceDensity(this) * 14.0f));
        }
        this.mInputWordBookName = (TextView) this.mWordbookDialog.findViewById(R.id.editview_editwordbook);
        if (this.mEdittextEditWordbookName == null) {
            this.mEdittextEditWordbookName = (EditText) this.mWordbookDialog.findViewById(R.id.edittext_editwordbookname);
            if (this.mEdittextEditWordbookName != null) {
                this.mBackupCardName = this.mEdittextEditWordbookName.getText().toString();
            }
        }
        return this.mWordbookDialog;
    }

    public boolean runEditWordbookOk(String name) {
        if (this.mEdittextEditWordbookName == null) {
            this.mEdittextEditWordbookName = (EditText) this.mWordbookDialog.findViewById(R.id.edittext_editwordbookname);
        }
        this.mInputWordbookName = this.mEdittextEditWordbookName.getText().toString();
        if (this.mInputWordbookName.equals("")) {
            Toast.makeText(this, (int) R.string.input_wordbookname, 0).show();
            return false;
        } else if (DioDictDatabase.editWordbookFolder(this, name, this.mInputWordbookName, this.mWordbookType) == 2) {
            Toast.makeText(this, getResources().getString(R.string.alreadyExistWordbook), 0).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean runMakeWordbookOK() {
        if (DioDictDatabase.getWordbookFolderCount(this) >= 40) {
            Toast.makeText(this, (int) R.string.alreadyMaxWordbook, 0).show();
            return false;
        }
        if (this.mEdittextWordbookName == null) {
            this.mEdittextWordbookName = (EditText) this.mWordbookDialog.findViewById(R.id.edittext_makewordbookname);
        }
        this.mInputWordbookName = this.mEdittextWordbookName.getText().toString();
        this.mInputWordbookName = CommonUtils.checkSpaceInWBName(this.mInputWordbookName);
        if (this.mInputWordbookName == null || this.mInputWordbookName.equals("")) {
            Toast.makeText(this, (int) R.string.input_wordbookname, 0).show();
            return false;
        } else if (DioDictDatabase.addWordbookFolder(this, this.mInputWordbookName, this.mWordbookType) == 2) {
            Toast.makeText(this, getResources().getString(R.string.alreadyExistWordbook), 0).show();
            return false;
        } else {
            return true;
        }
    }

    public void makeWordbook() {
        int nCount = DioDictDatabase.getWordbookFolderCount(this);
        if (nCount >= 40) {
            Toast.makeText(this, (int) R.string.alreadyMaxWordbook, 0).show();
            return;
        }
        removeDialog(1);
        showDialog(0);
        if (this.mEdittextWordbookName != null) {
            this.mEdittextWordbookName = (EditText) this.mWordbookDialog.findViewById(R.id.edittext_makewordbookname);
            this.mEdittextWordbookName.addTextChangedListener(this.mWordbookEditWatcher);
        }
        ImageButton clearBtn = (ImageButton) this.mWordbookDialog.findViewById(R.id.edit_clearbtn);
        if (clearBtn != null) {
            clearBtn.setOnClickListener(this.mEditClearBtnOnClickListener);
        }
        this.mWordbookDialog.show();
    }

    public String getDefaultWordbookName() {
        String DefaultName = getResources().getString(R.string.default_wordbookname);
        String RetName = null;
        for (int i = 1; i <= 40; i++) {
            RetName = DefaultName + i;
            if (!DioDictDatabase.existWordbookFolder(this, RetName)) {
                break;
            }
        }
        return RetName;
    }

    public void setFlashcardMode(int nMode) {
        if (nMode != 3) {
            this.mFlashcardMode = nMode;
        }
        switch (nMode) {
            case 0:
                this.mBtnEditWordbook.setSelected(false);
                this.mBtnDelWordbook.setSelected(false);
                this.mBtnEditWordbook.setEnabled(true);
                this.mBtnDelWordbook.setEnabled(true);
                break;
            case 1:
                this.mBtnEditWordbook.setSelected(true);
                this.mBtnDelWordbook.setSelected(false);
                break;
            case 2:
                this.mBtnEditWordbook.setSelected(false);
                this.mBtnDelWordbook.setSelected(true);
                break;
            case 3:
                this.mBtnEditWordbook.setSelected(false);
                this.mBtnDelWordbook.setSelected(false);
                this.mBtnEditWordbook.setEnabled(false);
                this.mBtnDelWordbook.setEnabled(false);
                this.mFlashcardMode = 0;
                break;
        }
        this.mPageGridView.setGridViewAdapter(this.mFlashcardItems, getCountPerPage(), this.mFlashcardMode);
    }

    private void backupParameters() {
        if (this.mPageGridView != null) {
            this.mCurrentGridView = this.mPageGridView.getCurrentPage();
        }
    }

    private void restoreState() {
        setFlashcardMode(this.mFlashcardMode);
        if (this.mFlashcardMode == 2) {
            this.mBtnDelWordbook.setSelected(true);
        } else if (this.mFlashcardMode == 1) {
            this.mBtnEditWordbook.setSelected(true);
        }
        this.mPageGridView.setPageView(this.mCurrentGridView);
    }

    private int getCountPerPage() {
        return getResources().getInteger(R.integer.flashcard_count_perpage);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideEditMessage() {
        if (this.mInputWordBookName != null) {
            this.mInputWordBookName.setVisibility(View.GONE);
        }
    }

    public void scrollGridView(int index) {
        int pagePos = index / getCountPerPage();
        this.mPageGridView.setPageView(pagePos);
    }
}
