package com.diotek.diodict.mean;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import com.diotek.diodict.dhwr.b2c.kor.DHWR;
import com.diotek.diodict.engine.EngineManager3rd;
import com.diotek.diodict.engine.ThemeColor;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

/* loaded from: classes.dex */
public class TagConverter {
    public static final int DSP_All_Mask = 127;
    public static final int DSP_Etc_Mask = 8;
    public static final int DSP_Example_Mask = 4;
    public static final int DSP_Frequency_Mask = 64;
    public static final int DSP_Idiom_Mask = 2;
    public static final int DSP_KeyField_ALL_Mask = 112;
    public static final int DSP_Keyword_Mask = 16;
    public static final int DSP_MeanField_All_Mask = 15;
    public static final int DSP_Meaning_Mask = 1;
    public static final int DSP_None_Mask = 0;
    public static final int DSP_Pronounce_Mask = 32;
    public static final int DSP_Quiz_Mask = 256;
    private static final int HashMap_ID = 0;
    private static final int HashMap_StartIndex = 1;
    public static final int LOADMEANING_FOR_QUIZ = 1;
    public static final int LOADMEANING_NORMAL = 0;
    public static final int advAddInfo = 12;
    public static final int advExample = 9;
    public static final int advFrequency = 0;
    public static final int advIdiom = 11;
    public static final int advNumbering1 = 1;
    public static final int advNumbering2 = 2;
    public static final int advNumbering3 = 3;
    public static final int advNumbering4 = 4;
    public static final int advPractical = 10;
    public static final int advSelectBg = 8;
    public static final int advSelectFg = 7;
    public static final int advSquareSpc = 6;
    public static final int advUsage = 5;
    public static final int baseChnInfo = 7;
    public static final int baseKeySearched = 2;
    public static final int baseKeyword = 0;
    public static final int baseKeywordBg = 1;
    public static final int baseMean = 4;
    public static final int baseMeanBg = 5;
    public static final int basePart = 8;
    public static final int basePronoun = 6;
    public static final int baseSeperateLine = 3;
    private int mAvailableMode;
    private LoadListener mClientFileLinkListener;
    private int mCurDspMode;
    private int mDstIndex;
    private EngineManager3rd mEngine;
    private boolean mIsBuildCtrlData;
    private boolean mIsLinkable;
    private boolean mIsStyleClose;
    private int mSrcIndex;
    private static int BUFFERSIZE_MEAN = 106496;
    private static int[] mTagDspMask = {0, 0, 0, 2, 4, 64, 0, 0, 0, 0, 16, 0, 1, 0, 8, 32, 8, 0, 0, 0, 0, 0, 0, 8, 8, 0};
    private static char[] mSymbolIndex = {193, 192, 57346, 57347, 57348, 57349, 197, 198, 652, 57351, 57352, 257, 225, 224, 226, 259, 227, 228, 57358, 57359, 57360, 57361, 57362, 57363, 57364, 230, 509, 57367, 601, 57369, 57370, 57371, 57372, 57373, 57374, 57375, 262, 268, 263, 57376, 57377, 57378, 231, 57380, 201, 200, 57383, 57384, 57385, 275, 233, 232, 234, 277, 235, 57390, 603, 57392, 57393, 57394, 643, 57396, 57397, 57398, 57399, 292, 57400, 57401, 626, 57403, 205, 204, 57406, 57407, 57408, 299, 237, 236, 238, 464, 239, 57414, 57415, 57416, 313, 57417, 57418, 57419, 324, 241, 57421, 211, 210, 57424, 57425, 57426, 57427, 333, 243, 242, 244, 466, 57431, 246, 57433, 248, 57435, 339, 57437, 57438, 57439, 596, 57441, 57442, 57443, 57444, 240, 952, 331, 340, 345, 57448, 346, 353, 218, 217, 57451, 220, 57453, 363, 250, 249, 251, 468, 252, 472, 476, 474, 57459, 57460, 650, 7810, 7811, 57462, 221, 7922, 253, 7923, 377, 658, 57466, 945, 946, 947, 952, 956, 960, 931, 955, 963, 968, 8304, 185, 178, 179, 8308, 8309, 8310, 8311, 8312, 8313, 57511, 57512, 57513, 57514, 57515, 57483, 57484, 57485, 57486, 57487, 57488, 57489, 57490, 57491, 57492, 57493, 57494, 57495, 57496, 57497, 57498, 57499, 57500, 57501, 57502, 57503, 57504, 57505, 57506, 57507, 57508, 57509, 57510, 57519, 57516, 57517, 57518, 720, 57473, 57474, 57475, 57476, 57477, 57478, 57479, 57480, 57481, 57482, 8224, '^', 711, 728, 730, 732, 714, 719, 812, 167, 183, 8230, 829, 8744, 8212, 8251, 12304, 12305, 9675, 9632, 594, 604, 691, 688, 7498, 57524, 57525, 7693, 7717, 7779, 7789, 7827, 269, 322, 367, 8322, 229, 8320, 8321, 8323, 8324, 8325, 8326, 8327, 8328, 8329, 328, 351, 919, 8211, 8315, 8711, 9643, 9744, 9830, 9838, 9839, 8730, 10004, 10007, 12314, 12315, 57526, 57527, 57528, 41042, 41092, 63556, 57530, 57531, 57532, 57533, 57542, 7424, 665, 7428, 7429, 7431, 57534, 610, 668, 618, 7434, 7435, 671, 7437, 628, 7439, 7448, 57535, 640, 57536, 7451, 7452, 7456, 7457, 57537, 655, 7458, 57538, 57539, 57540, 8225, 8776, 9415, 10022, 8319, 712, 716, 654, 676, 679, 57541, 8361, 650, 541, 8800, 8470, 10023, 9654, 9655, 700, 57394, 7869, 297, 361, 732, 57439, 57543, 57544, 57545, 61472, 61473, 61474, 61475, 61476, 61477, 61478, 61479, 61480, 61481, 61482, 61483, 61484, 61485, 61487, 8540, 57546, 57547, 8364, 189, 332, 8314, 8531, 9424, 9425, 9426, 11978, 15220, 15988, 16735, 17629, 17897, 19736, 19737, 19848, 256, 57548, 57549, 10145, 57552, 9672, 9733, 9827, 9824, 9829, 9837, 9671, 9758, 916, 915, 923, 937, 934, 928, 936, 8242, 8243, 8220, 8221, 8216, 8217, 967, 951, 953, 969, 966, 961, 964, 958, 950, 8594, 8596, 593, 8731, 8482, 8801, 920, 926, 933, 957, 8541, 8542, 57550, 57551, 65024, 65025, 65026, 65027, 65028, 65029, 65030, 65031, 12291, 948, 954, 64136, 261, 265, 279, 281, 283, 285, 293, 309, 337, 347, 349, 350, 352, 362, 371, 378, 380, 381, 382, 433, 487, 539, 551, 553, 917, 929, 959, 1195, 1265, 4578, 7716, 7723, 7735, 7747, 7749, 7751, 7753, 7770, 7771, 7776, 7778, 7788, 7826, 7828, 7841, 8145, 8153, 8226, 8317, 8318, 8331, 8336, 8339, 8554, 8555, 8644, 8724, 8706, 8715, 8802, 8804, 8805, 8807, 9437, 9613, 9767, 9776, 9777, 9778, 9779, 9780, 9781, 9782, 9783, 9792, 9831, 12828, 13192, 13193, 13198, 13200, 13201, 13202, 13203, 13208, 13210, 13215, 13219, 13224, 13225, 13234, 13235, 13239, 13240, 13245, 13246, 13247, 13252, 13253, 13256, 13258, 13263, 13270, 41053, 57767, 57987, 57989, 57992, 58009, 58250, 58280, 58476, 58480, 58726, 58954, 58959, 58963, 59479, 59553, 59559, 59561, 59562, 59570, 59773, 59787, 59793, 59839, 60017, 60049, 60135, 60684, 60815, 61064, 61068, 61077, 61780, 61948, 61952, 62273, 62292, 62536, 62775, 62778, 62780, 62800, 63463, 63561, 63562, 63564, 64890, 64942, 64943, 64985, 64986, 64987, 64988, 64990, 64991, 64992, 64993, 64994, 64995, 64996, 64997, 64998, 64999, 65000, 65001, 65002, 65003, 65004, 65005, 65006, 65007, 65008, 65009, 65010, 65011, 65012, 65013, 65014, 65015, 65016, 65017, 65018, 65019, 65020, 65021, 65022, 65023, 65078, 65441, 65442, 65443, 65444, 65445, 65446, 65447, 65448, 65449, 65450, 65451, 65452, 65453, 65454, 65455, 65456, 65457, 65458, 65459, 65460, 65461, 65462, 65463, 65464, 65465, 65466, 65467, 65468, 65469, 65470, 65471, 65472, 65473, 65474, 65475, 65476, 301, 323, 341, 365, 439, 477, 500, 602, 609, 632, 785, 941, 978, 988, 7728, 7742, 7747, 7764, 7765, 7810, 7923, 8050, 8219, 8258, 8501, 8994, 8995, 9768, 9774, 9782, 9838, 10016, 10017, 41042, 41092, 63561, 64257, 64865, 64866, 64867, 64872, 64873, 64874, 64875, 64876, 64877, 64878, 64879, 64880, 64881, 65136, 65140, 65141, 65142, 65143, 65144, 65145, 65146, 65147, 65148, 65149, 65150, 65151, 65152, 65153, 65154, 65155, 65156, 65157, 65158, 65159, 65160, 65161, 65162, 65163, 65165, 65166, 65167, 65168, 65169, 65170, 65171, 65172, 65173, 65174, 65175, 65176, 65177, 65178, 65179, 65180, 65181, 65192, 65193, 65194, 65195, 65196, 65197, 65198, 65270, 65273, 65274, 65275, 65276, 65277, 65279, 65038, 65039, 65040, 65041, 65042, 65043, 65044, 65045, 65046, 65047, 65048, 65049, 65050, 65051, 626, 771, 779, 780, 781, 782, 783, 784, 786, 787, 788, 789, 790, 791, 792, 807, 823, 824, 8255, 8356, 8593, 355, 64150, 64151, 64152, 64153, 64154, 64155, 64156, 64157, 64158, 64159, 629, 8532, 65052, 65411, 65413, 65414, 65415, 65416, 65417, 65418, 65419, 65420, 65421, 65422, 65423, 65424, 65426, 65427, 65428, 65429, 399, 400, 401, 504, 592, 697, 698, 776, 778, 833, 1008, 1141, 1475, 8118, 8358, 9474, 9475, 10229, 10230, 10521, 10522, 18081, 65439, 59811, 62581, 7290, 7291, 7351, 57577, 57578, 57581, 57582, 57585, 57586, 57587, 57588, 57589, 57590, 7685, 7745, 7777, 2112, 2113, 2114, 2115, 2116, 2117, 2118, 2119, 2120, 2121, 2122, 2123, 2124, 2125, 2126, 55695, 55696, 55697, 55698, 55699, 55700, 55701, 55702, 55703, 55704, 55705, 55706, 55707};
    private SpannableStringBuilder mSpannableBuilder = null;
    private SpannableStringBuilder mKeywordSpannableBuilder = new SpannableStringBuilder();
    protected SpannableStringBuilder mContentSpannableBuilder = new SpannableStringBuilder();
    private StringBuilder mStringBuilder = new StringBuilder();
    private String mSource = null;
    private boolean mIs2DepthStyle = false;
    private int mNumTableData = 0;
    private Stack<StyleStackItem> mStyleStack = new Stack<>();
    private ArrayList<DictPos> mDspIdxList = new ArrayList<>();
    private ArrayList<HashMap<Integer, Object>> mIDList = new ArrayList<>();
    private ArrayList<DictPos> mLinkIdxList = new ArrayList<>();
    int[] mBaseTheme = ThemeColor.baseTheme;
    int[] mAdvTheme = ThemeColor.advTheme;
    private int mCurrentTheme = 0;
    private final byte[] mTagLength = {1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2};
    private final byte[] mTagSubIndex = {0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 8};
    private TagHandler mTagRemover = new TagHandler() { // from class: com.diotek.diodict.mean.TagConverter.1
        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_Start(int tag) {
            TagConverter.this.StyleStack_Push(tag, -1, 15);
        }

        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_End(int tag) {
            StyleStackItem item = (StyleStackItem) TagConverter.this.mStyleStack.peek();
            if (item.tag != tag) {
                Log.d("TagConverter", "Tag Missing remover" + tag);
            }
        }
    };
    private TagHandler mTagHandler_Empty = new TagHandler() { // from class: com.diotek.diodict.mean.TagConverter.2
        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_Start(int tag) {
            if (!TagConverter.this.mStyleStack.isEmpty()) {
                int checkingTag = ((StyleStackItem) TagConverter.this.mStyleStack.peek()).tag;
                if (TagConverter.this.isEmptyTag(checkingTag)) {
                    TagConverter.this.mTagHandler[checkingTag].handleTag_End(checkingTag);
                    TagConverter.this.mStyleStack.pop();
                }
            }
        }

        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_End(int tag) {
        }
    };
    private TagHandler mTagHandler_DspMode = new TagHandler() { // from class: com.diotek.diodict.mean.TagConverter.3
        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_Start(int tag) {
            boolean linefeed = true;
            if (TagConverter.this.mSpannableBuilder != null && TagConverter.this.mSpannableBuilder.length() > 0 && TagConverter.this.mSpannableBuilder.charAt(TagConverter.this.mSpannableBuilder.length() - 1) == '(') {
                linefeed = false;
            }
            TagConverter.this.BlockTag_Start(tag, -1, linefeed);
        }

        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_End(int tag) {
            boolean linefeed = true;
            if (TagConverter.this.mSource != null && TagConverter.this.mSource.length() > TagConverter.this.mSrcIndex && TagConverter.this.mSource.charAt(TagConverter.this.mSrcIndex) == ')') {
                linefeed = false;
            }
            TagConverter.this.BlockTag_End(tag, linefeed);
        }
    };
    private TagHandler mTagHandler_B = new TagHandler() { // from class: com.diotek.diodict.mean.TagConverter.4
        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_Start(int tag) {
            TagConverter.this.StyleStack_Push(tag, -1, 15);
        }

        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_End(int tag) {
            StyleStackItem item = (StyleStackItem) TagConverter.this.mStyleStack.peek();
            if (item.tag == tag) {
                if (item.start != TagConverter.this.mDstIndex) {
                    TagConverter.this.mSpannableBuilder.setSpan(new StyleSpan(1), item.start, TagConverter.this.mDstIndex, 33);
                    return;
                }
                return;
            }
            Log.d("TagConverter", "Tag Missing B");
        }
    };
    private TagHandler mTagHandler_C = new TagHandler() { // from class: com.diotek.diodict.mean.TagConverter.5
        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_Start(int tag) {
            if ((tag & 255) == 1) {
                TagConverter.this.assertLineFeed();
                TagConverter.this.StyleStack_Push(tag, TagConverter.this.mAdvTheme[1], 15);
                return;
            }
            TagConverter.this.StyleStack_Push(tag, -1, 15);
        }

        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_End(int tag) {
            StyleStackItem item = (StyleStackItem) TagConverter.this.mStyleStack.peek();
            if (item.tag == tag) {
                if (item.start != TagConverter.this.mDstIndex && (tag & 255) == 1) {
                    TagConverter.this.mSpannableBuilder.setSpan(new ForegroundColorSpan(TagConverter.this.mAdvTheme[1]), item.start, TagConverter.this.mDstIndex, 33);
                    if (((StyleStackItem) TagConverter.this.mStyleStack.get(TagConverter.this.mStyleStack.size() - 2)).start == -1) {
                        ((StyleStackItem) TagConverter.this.mStyleStack.peek()).start = TagConverter.this.mDstIndex;
                        return;
                    }
                    return;
                }
                return;
            }
            Log.d("TagConverter", "Tag Missing B");
        }
    };
    private TagHandler mTagHandler_D = new TagHandler() { // from class: com.diotek.diodict.mean.TagConverter.6
        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_Start(int tag) {
            TagConverter.this.BlockTag_Start(tag, TagConverter.this.mAdvTheme[11]);
        }

        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_End(int tag) {
            TagConverter.this.BlockTag_End(tag);
            StyleStackItem item = (StyleStackItem) TagConverter.this.mStyleStack.peek();
            if (item.tag == tag) {
                if (item.start != TagConverter.this.mDstIndex) {
                    TagConverter.this.mSpannableBuilder.setSpan(new ForegroundColorSpan(TagConverter.this.mAdvTheme[11]), item.start, TagConverter.this.mDstIndex, 33);
                    return;
                }
                return;
            }
            Log.d("TagConverter", "Tag Missing D");
        }
    };
    private TagHandler mTagHandler_E = new TagHandler() { // from class: com.diotek.diodict.mean.TagConverter.7
        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_Start(int tag) {
            TagConverter.this.BlockTag_Start(tag, TagConverter.this.mAdvTheme[9], false);
        }

        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_End(int tag) {
            boolean linefeed = true;
            if (TagConverter.this.mSource != null && TagConverter.this.mSource.length() > TagConverter.this.mSrcIndex && TagConverter.this.mCurDspMode != 4 && (TagConverter.this.mSource.charAt(TagConverter.this.mSrcIndex) == ')' || TagConverter.this.mSource.charAt(TagConverter.this.mSrcIndex) == ',')) {
                linefeed = false;
            }
            TagConverter.this.BlockTag_End(tag, linefeed);
            StyleStackItem item = (StyleStackItem) TagConverter.this.mStyleStack.peek();
            if (item.tag == tag) {
                if (item.start != TagConverter.this.mDstIndex && TagConverter.this.mDstIndex != 0) {
                    TagConverter.this.mSpannableBuilder.setSpan(new ForegroundColorSpan(TagConverter.this.mAdvTheme[9]), item.start, TagConverter.this.mDstIndex, 33);
                    return;
                }
                return;
            }
            Log.d("TagConverter", "Tag Missing E");
        }
    };
    private TagHandler mTagHandler_F = new TagHandler() { // from class: com.diotek.diodict.mean.TagConverter.8
        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_Start(int tag) {
            TagConverter.this.mTagHandler_Empty.handleTag_Start(tag);
            TagConverter.this.StyleStack_Push(tag, TagConverter.this.mAdvTheme[0], 64);
        }

        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_End(int tag) {
            StyleStackItem item = (StyleStackItem) TagConverter.this.mStyleStack.peek();
            if (item.tag == tag) {
                if (item.start != TagConverter.this.mDstIndex && (TagConverter.this.mCurDspMode & 64) != 0) {
                    TagConverter.this.mSpannableBuilder.setSpan(new ForegroundColorSpan(TagConverter.this.mAdvTheme[0]), item.start, TagConverter.this.mDstIndex, 33);
                    return;
                }
                return;
            }
            Log.d("TagConverter", "Tag Missing F");
        }
    };
    private TagHandler mTagHandler_I = new TagHandler() { // from class: com.diotek.diodict.mean.TagConverter.9
        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_Start(int tag) {
            TagConverter.this.StyleStack_Push(tag, -1, 15);
        }

        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_End(int tag) {
            StyleStackItem item = (StyleStackItem) TagConverter.this.mStyleStack.peek();
            if (item.tag == tag && item.start != TagConverter.this.mDstIndex) {
                TagConverter.this.mSpannableBuilder.setSpan(new StyleSpan(2), item.start, TagConverter.this.mDstIndex, 33);
            }
        }
    };
    private TagHandler mTagHandler_K = new TagHandler() { // from class: com.diotek.diodict.mean.TagConverter.10
        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_Start(int tag) {
            TagConverter.this.mTagHandler_Empty.handleTag_Start(tag);
            TagConverter.this.StyleStack_Push(tag, TagConverter.this.mBaseTheme[0], 16);
        }

        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_End(int tag) {
            StyleStackItem item = (StyleStackItem) TagConverter.this.mStyleStack.peek();
            if (item.tag == tag) {
                if (item.start != TagConverter.this.mDstIndex && (TagConverter.this.mCurDspMode & 16) != 0) {
                    TagConverter.this.mSpannableBuilder.setSpan(new StyleSpan(1), item.start, TagConverter.this.mDstIndex, 33);
                    TagConverter.this.mSpannableBuilder.setSpan(new ForegroundColorSpan(TagConverter.this.mBaseTheme[0]), item.start, TagConverter.this.mDstIndex, 33);
                    return;
                }
                return;
            }
            Log.d("TagConverter", "Tag Missing K");
        }
    };
	private TagHandler mTagHandler_L = new TagHandler() { // from class: com.diotek.diodict.mean.TagConverter.11
		@Override // com.diotek.diodict.mean.TagConverter.TagHandler
		public void handleTag_Start(int tag) {
			switch (tag & 255) {
				case 3:
					TagConverter.this.WideBlockTag_Start(tag, -1);
					return;
				case 4:
					int end_index = TagConverter.this.mSource.indexOf("%l04", TagConverter.this.mSrcIndex);
					if (TagConverter.this.mIsBuildCtrlData) {
						int dstIndex = TagConverter.this.mDstIndex;
						if (TagConverter.this.mSource.charAt(end_index + 4) == '\n') {
							dstIndex++;
						}
						HashMap<Integer, Object> tItem = new HashMap<>();
						tItem.put(0, TagConverter.this.mSource.substring(TagConverter.this.mSrcIndex, end_index));
						tItem.put(1, Integer.valueOf(dstIndex));
						TagConverter.this.mIDList.add(tItem);
					}
					TagConverter.this.mSrcIndex = end_index + 4;
					return;
				case 5:
					if (TagConverter.this.mLinkIdxList.size() > 0 && ((DictPos) TagConverter.this.mLinkIdxList.get(TagConverter.this.mLinkIdxList.size() - 1)).end == TagConverter.this.mDstIndex - 1) {
						TagConverter.this.mSpannableBuilder.append((CharSequence) " ");
						TagConverter.access$908(TagConverter.this);
					}
					String url = null;
					int start = TagConverter.this.mDstIndex;
					int end_point = TagConverter.this.mSource.length();
					while (TagConverter.this.mSrcIndex > 0 && TagConverter.this.mSrcIndex < end_point) {
						if (TagConverter.this.mSource.codePointAt(TagConverter.this.mSrcIndex) == 37) {
							int inner_tag = TagConverter.this.getTagValue(TagConverter.this.mSource, TagConverter.this.mSrcIndex + 1);
							if (inner_tag == 2822) {
								TagConverter.access$812(TagConverter.this, 4);
								int end_index2 = TagConverter.this.mSource.indexOf("%l06", TagConverter.this.mSrcIndex);
								url = TagConverter.this.mSource.substring(TagConverter.this.mSrcIndex, end_index2);
								TagConverter.this.mSrcIndex = end_index2 + 4;
							} else if (inner_tag == 2821) {
								TagConverter.access$812(TagConverter.this, 4);
								TagConverter.this.mSpannableBuilder.append((CharSequence) TagConverter.this.mStringBuilder);
								TagConverter.this.mStringBuilder.delete(0, TagConverter.this.mStringBuilder.length());
								if (url != null && url.length() > 0 && TagConverter.this.mIsLinkable) {
									while (start < TagConverter.this.mDstIndex - 1 && TagConverter.this.mSpannableBuilder.charAt(start) == '\n') {
										start++;
									}
									TagConverter.this.mSpannableBuilder.setSpan(new LinkSpan(url), start, TagConverter.this.mDstIndex, 33);
									DictPos item = new DictPos();
									item.start = start;
									item.end = TagConverter.this.mDstIndex - 1;
									TagConverter.this.mLinkIdxList.add(item);
									return;
								}
								return;
							} else if (inner_tag > 0) {
								boolean isStart = TagConverter.this.mSource.codePointAt(TagConverter.this.mSrcIndex + 1) < 96;
								int value = (65280 & inner_tag) > 0 ? inner_tag >> 8 : inner_tag;
								TagConverter.access$812(TagConverter.this, TagConverter.this.mTagLength[value] + 1);
								TagConverter.this.mSpannableBuilder.append((CharSequence) TagConverter.this.mStringBuilder);
								TagConverter.this.mStringBuilder.delete(0, TagConverter.this.mStringBuilder.length());
								if (isStart) {
									TagConverter.this.mTagHandler[value].handleTag_Start(inner_tag);
								} else {
									TagConverter.this.mTagHandler[value].handleTag_End(inner_tag);
									TagConverter.this.StyleStack_Pop(tag);
								}
							}
						}
						TagConverter.this.mStringBuilder.appendCodePoint(TagConverter.this.mSource.codePointAt(TagConverter.access$808(TagConverter.this)));
						TagConverter.access$908(TagConverter.this);
					}
					return;
				case 6:
					Log.e("TagConverter", "File Link parsing error");
					return;
				case 7:
				default:
					TagConverter.this.StyleStack_Push(tag, -1, 15);
					return;
				case 8:
					TagConverter.this.BlockTag_Start(tag, -1);
					return;
				case 9:
					TagConverter.this.mSrcIndex = TagConverter.this.mSource.indexOf("%l09", TagConverter.this.mSrcIndex) + 4;
					return;
			}
		}
		
		@Override // com.diotek.diodict.mean.TagConverter.TagHandler
		public void handleTag_End(int tag) {
			StyleStackItem item = (StyleStackItem) TagConverter.this.mStyleStack.peek();
			if (item.tag == tag) {
				if (item.start != TagConverter.this.mDstIndex) {
					switch (tag & 255) {
						case 1:
							TagConverter.this.mSpannableBuilder.setSpan(new RelativeSizeSpan(0.8f), item.start, TagConverter.this.mDstIndex, 33);
							TagConverter.this.mSpannableBuilder.setSpan(new SubscriptSpan(), item.start, TagConverter.this.mDstIndex, 33);
							return;
						case 3:
							if (TagConverter.this.mIsStyleClose) {
								TagConverter.this.BlockTag_End(tag, false);
								return;
							} else {
								TagConverter.this.WideBlockTag_End(tag);
								return;
							}
						case 8:
							TagConverter.this.BlockTag_End(tag);
							return;
						default:
							return;
					}
				}
				return;
			}
			Log.d("TagConverter", "Tag Missing L");
		}
	};
    private TagHandler mTagHandler_M = new TagHandler() { // from class: com.diotek.diodict.mean.TagConverter.12
        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_Start(int tag) {
            TagConverter.this.mTagHandler_Empty.handleTag_Start(tag);
            if ((TagConverter.this.mCurDspMode & 15) == 0) {
                TagConverter.this.mSrcIndex = TagConverter.this.mSource.length();
                return;
            }
            TagConverter.this.mSpannableBuilder = TagConverter.this.mContentSpannableBuilder;
            if (TagConverter.this.mIsBuildCtrlData) {
                TagConverter.this.mSpannableBuilder.clear();
                TagConverter.this.mStringBuilder.delete(0, TagConverter.this.mStringBuilder.length());
                TagConverter.this.mDstIndex = 0;
                int end_point = TagConverter.this.mSource.length();
                while (TagConverter.this.mSrcIndex < end_point && TagConverter.this.mSource.codePointAt(TagConverter.this.mSrcIndex) == 10) {
                    TagConverter.access$808(TagConverter.this);
                }
            }
            TagConverter.this.StyleStack_Push(tag, -1, 1);
            if (TagConverter.this.mIsBuildCtrlData) {
                DictPos item = new DictPos();
                item.display_mask = 1;
                item.start = TagConverter.this.mDstIndex;
                TagConverter.this.mDspIdxList.add(item);
            }
        }

        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_End(int tag) {
            StyleStackItem item = (StyleStackItem) TagConverter.this.mStyleStack.peek();
            if (item.tag == tag) {
                if (TagConverter.this.mIsBuildCtrlData && !TagConverter.this.mIsStyleClose) {
                    if (((DictPos) TagConverter.this.mDspIdxList.get(TagConverter.this.mDspIdxList.size() - 1)).start == TagConverter.this.mDstIndex) {
                        TagConverter.this.mDspIdxList.remove(TagConverter.this.mDspIdxList.size() - 1);
                        return;
                    }
                    ((DictPos) TagConverter.this.mDspIdxList.get(TagConverter.this.mDspIdxList.size() - 1)).end = TagConverter.this.mDstIndex - 1;
                    return;
                }
                return;
            }
            Log.d("TagConverter", "Tag Missing M");
        }
    };
    private TagHandler mTagHandler_O = new TagHandler() { // from class: com.diotek.diodict.mean.TagConverter.13
        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_Start(int tag) {
            TagConverter.this.BlockTag_Start(tag, -16776961);
        }

        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_End(int tag) {
            TagConverter.this.BlockTag_End(tag);
            StyleStackItem item = (StyleStackItem) TagConverter.this.mStyleStack.peek();
            if (item.tag == tag) {
                if (item.start != TagConverter.this.mDstIndex) {
                    TagConverter.this.mSpannableBuilder.setSpan(new ForegroundColorSpan(TagConverter.this.mBaseTheme[4]), item.start, TagConverter.this.mDstIndex, 33);
                    return;
                }
                return;
            }
            Log.d("TagConverter", "Tag Missing O");
        }
    };
    private TagHandler mTagHandler_P = new TagHandler() { // from class: com.diotek.diodict.mean.TagConverter.14
        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_Start(int tag) {
            TagConverter.this.mTagHandler_Empty.handleTag_Start(tag);
            TagConverter.this.StyleStack_Push(tag, TagConverter.this.mBaseTheme[6], 32);
        }

        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_End(int tag) {
            StyleStackItem item = (StyleStackItem) TagConverter.this.mStyleStack.peek();
            if (item.tag == tag) {
                if (item.start != TagConverter.this.mDstIndex && (TagConverter.this.mCurDspMode & 32) != 0) {
                    TagConverter.this.mSpannableBuilder.setSpan(new ForegroundColorSpan(TagConverter.this.mBaseTheme[6]), item.start, TagConverter.this.mDstIndex, 33);
                    return;
                }
                return;
            }
            Log.d("TagConverter", "Tag Missing P");
        }
    };
    private TagHandler mTagHandler_S = new TagHandler() { // from class: com.diotek.diodict.mean.TagConverter.15
        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_Start(int tag) {
            TagConverter.this.StyleStack_Push(tag, -1, 15);
        }

        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_End(int tag) {
            StyleStackItem item = (StyleStackItem) TagConverter.this.mStyleStack.peek();
            if (item.tag == tag) {
                if (item.start != TagConverter.this.mDstIndex) {
                    TagConverter.this.mSpannableBuilder.setSpan(new RelativeSizeSpan(0.8f), item.start, TagConverter.this.mDstIndex, 33);
                    TagConverter.this.mSpannableBuilder.setSpan(new SuperscriptSpan(), item.start, TagConverter.this.mDstIndex, 33);
                    return;
                }
                return;
            }
            Log.d("TagConverter", "Tag Missing S");
        }
    };
    private TagHandler mTagHandler_T = new TagHandler() { // from class: com.diotek.diodict.mean.TagConverter.16
        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_Start(int tag) {
            TagConverter.this.StyleStack_Push(tag, TagConverter.this.mBaseTheme[8], 15);
        }

        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_End(int tag) {
            StyleStackItem item = (StyleStackItem) TagConverter.this.mStyleStack.peek();
            if (item.tag == tag) {
                if (item.start != TagConverter.this.mDstIndex) {
                    TagConverter.this.mSpannableBuilder.setSpan(new StyleSpan(1), item.start, TagConverter.this.mDstIndex, 33);
                    TagConverter.this.mSpannableBuilder.setSpan(new ForegroundColorSpan(TagConverter.this.mBaseTheme[8]), item.start, TagConverter.this.mDstIndex, 33);
                    return;
                }
                return;
            }
            Log.d("TagConverter", "Tag Missing T");
        }
    };
    private TagHandler mTagHandler_U = new TagHandler() { // from class: com.diotek.diodict.mean.TagConverter.17
        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_Start(int tag) {
            int end_point = TagConverter.this.mSource.length();
            int index = 0;
            while (true) {
                if (TagConverter.this.mSrcIndex >= end_point) {
                    break;
                } else if (TagConverter.this.mSource.codePointAt(TagConverter.this.mSrcIndex) != 63) {
                    index = ((index * 10) + TagConverter.this.mSource.codePointAt(TagConverter.this.mSrcIndex)) - 48;
                    TagConverter.access$808(TagConverter.this);
                } else {
                    TagConverter.access$808(TagConverter.this);
                    break;
                }
            }
            TagConverter.this.mStringBuilder.appendCodePoint(TagConverter.mSymbolIndex[index]);
            TagConverter.access$908(TagConverter.this);
        }

        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_End(int tag) {
        }
    };
    private TagHandler mTagHandler_W = new TagHandler() { // from class: com.diotek.diodict.mean.TagConverter.18
        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_Start(int tag) {
            if ((tag & 255) == 1) {
                TagConverter.this.BlockTag_Start(tag, -1);
            } else {
                int Color = -1;
                if ((tag & 255) == 3) {
                    Color = ThemeColor.w3color[0];
                }
                TagConverter.this.StyleStack_Push(tag, Color, 15);
            }
            switch (tag & 255) {
                case 7:
                    TagConverter.this.mNumTableData = 0;
                    return;
                case 8:
                case 9:
                    if (TagConverter.this.mNumTableData > 0) {
                        TagConverter.this.mStringBuilder.append(" | ");
                        TagConverter.access$912(TagConverter.this, 3);
                    }
                    TagConverter.access$2408(TagConverter.this);
                    return;
                default:
                    return;
            }
        }

        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_End(int tag) {
            StyleStackItem item = (StyleStackItem) TagConverter.this.mStyleStack.peek();
            if (item.tag == tag) {
                if (item.start != TagConverter.this.mDstIndex) {
                    switch (tag & 255) {
                        case 1:
                            TagConverter.this.BlockTag_End(tag);
                            return;
                        case 3:
                            TagConverter.this.mSpannableBuilder.setSpan(new ForegroundColorSpan(ThemeColor.w3color[0]), item.start, TagConverter.this.mDstIndex, 33);
                            if (!TagConverter.this.mStyleStack.isEmpty()) {
                                StyleStackItem curItem = (StyleStackItem) TagConverter.this.mStyleStack.pop();
                                StyleStackItem parentItem = (StyleStackItem) TagConverter.this.mStyleStack.peek();
                                if (parentItem != null) {
                                    int tag_index = TagConverter.this.getTagIndex(parentItem.tag);
                                    if (tag_index == 8) {
                                        TagConverter.this.mSpannableBuilder.setSpan(new StyleSpan(2), item.start, TagConverter.this.mDstIndex, 33);
                                    } else if (tag_index == 1) {
                                        TagConverter.this.mSpannableBuilder.setSpan(new StyleSpan(1), item.start, TagConverter.this.mDstIndex, 33);
                                    }
                                }
                                TagConverter.this.mStyleStack.push(curItem);
                                return;
                            }
                            return;
                        case 8:
                            TagConverter.this.mSpannableBuilder.setSpan(new StyleSpan(1), item.start, TagConverter.this.mDstIndex, 33);
                            return;
                        default:
                            return;
                    }
                }
                return;
            }
            Log.d("TagConverter", "Tag Missing W");
        }
    };
    private TagHandler mTagHandler_Z = new TagHandler() { // from class: com.diotek.diodict.mean.TagConverter.19
        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_Start(int tag) {
            switch (tag & 255) {
                case 1:
                    TagConverter.this.StyleStack_Push(tag, TagConverter.this.mBaseTheme[0], 15);
                    return;
                default:
                    TagConverter.this.StyleStack_Push(tag, -1, 15);
                    return;
            }
        }

        @Override // com.diotek.diodict.mean.TagConverter.TagHandler
        public void handleTag_End(int tag) {
            StyleStackItem item = (StyleStackItem) TagConverter.this.mStyleStack.peek();
            if (item.tag == tag) {
                if (item.start != TagConverter.this.mDstIndex) {
                    switch (tag & 255) {
                        case 1:
                            TagConverter.this.mSpannableBuilder.setSpan(new StyleSpan(1), item.start, TagConverter.this.mDstIndex, 33);
                            TagConverter.this.mSpannableBuilder.setSpan(new ForegroundColorSpan(TagConverter.this.mBaseTheme[0]), item.start, TagConverter.this.mDstIndex, 33);
                            return;
                        case 2:
                        case 4:
                        case 5:
                        case 7:
                        default:
                            return;
                        case 3:
                            TagConverter.this.mSpannableBuilder.setSpan(new StrikethroughSpan(), item.start, TagConverter.this.mDstIndex, 33);
                            return;
                        case 6:
                            TagConverter.this.mSpannableBuilder.setSpan(new RelativeSizeSpan(0.8f), item.start, TagConverter.this.mDstIndex, 33);
                            return;
                        case 8:
                            TagConverter.this.mSpannableBuilder.setSpan(new UnderlineSpan(), item.start, TagConverter.this.mDstIndex, 33);
                            return;
                    }
                }
                return;
            }
            Log.d("TagConverter", "Tag Missing Z");
        }
    };
    private TagHandler[] mTagHandler = {this.mTagRemover, this.mTagHandler_B, this.mTagHandler_C, this.mTagHandler_D, this.mTagHandler_E, this.mTagHandler_F, this.mTagRemover, this.mTagRemover, this.mTagHandler_I, this.mTagRemover, this.mTagHandler_K, this.mTagHandler_L, this.mTagHandler_M, this.mTagRemover, this.mTagHandler_O, this.mTagHandler_P, this.mTagHandler_DspMode, this.mTagRemover, this.mTagHandler_S, this.mTagHandler_T, this.mTagHandler_U, this.mTagRemover, this.mTagHandler_W, this.mTagHandler_DspMode, this.mTagHandler_DspMode, this.mTagHandler_Z};

    /* loaded from: classes.dex */
    public interface LoadListener {
        void OnLoad(String str, int i, String str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface TagHandler {
        void handleTag_End(int i);

        void handleTag_Start(int i);
    }

    static /* synthetic */ int access$2408(TagConverter x0) {
        int i = x0.mNumTableData;
        x0.mNumTableData = i + 1;
        return i;
    }

    static /* synthetic */ int access$808(TagConverter x0) {
        int i = x0.mSrcIndex;
        x0.mSrcIndex = i + 1;
        return i;
    }

    static /* synthetic */ int access$812(TagConverter x0, int x1) {
        int i = x0.mSrcIndex + x1;
        x0.mSrcIndex = i;
        return i;
    }

    static /* synthetic */ int access$908(TagConverter x0) {
        int i = x0.mDstIndex;
        x0.mDstIndex = i + 1;
        return i;
    }

    static /* synthetic */ int access$912(TagConverter x0, int x1) {
        int i = x0.mDstIndex + x1;
        x0.mDstIndex = i;
        return i;
    }
	
	private static void access$802(com.diotek.diodict.mean.TagConverter tagConverter, int i) {
		tagConverter.mSrcIndex = i;
	}
	
	public TagConverter(Context context, EngineManager3rd engine, int nTheme, LoadListener loadListener) {
        boolean z = true;
        this.mEngine = engine;
        this.mClientFileLinkListener = loadListener;
        this.mIsLinkable = this.mClientFileLinkListener == null ? false : z;
        setTheme(nTheme);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class StyleStackItem {
        int dspMode;
        int f_Color;
        int start;
        int tag;

        private StyleStackItem() {
        }
    }

    /* loaded from: classes.dex */
    public class DictPos {
        public int display_mask;
        public int end;
        public int start;

        public DictPos() {
        }
    }

    /* loaded from: classes.dex */
    public class LinkSpan extends ClickableSpan {
        private String mURL;

        LinkSpan(String url) {
            this.mURL = url;
        }

        private int convertString2Integer(String str) {
            int result = Integer.parseInt(str);
            return result;
        }

        @Override // android.text.style.ClickableSpan
        public void onClick(View widget) {
            int suid;
            if (TagConverter.this.mClientFileLinkListener != null && this.mURL != null && this.mURL.length() != 0) {
                int sep_index = this.mURL.indexOf(DHWR.DLANG_THAI_SIGN, 0);
                int sep_index2 = sep_index + 1;
                String keyword = this.mURL.substring(0, sep_index);
                int next_sep_index = this.mURL.indexOf(DHWR.DLANG_THAI_SIGN, sep_index2);
                if (next_sep_index == -1) {
                    suid = convertString2Integer(this.mURL.substring(sep_index2, this.mURL.length()));
                } else {
                    suid = convertString2Integer(this.mURL.substring(sep_index2, next_sep_index));
                }
                String ref_id = null;
                if (next_sep_index > 0) {
                    ref_id = this.mURL.substring(next_sep_index + 1, this.mURL.length());
                }
                TagConverter.this.mClientFileLinkListener.OnLoad(keyword, suid, ref_id);
            }
        }
    }

    public boolean loadMeaning(int dicType, String keyword, int suid, int nMode) {
        this.mSource = this.mEngine.getMeaning(keyword, suid, dicType, 0, BUFFERSIZE_MEAN, true, true, nMode);
        this.mCurDspMode = 127;
        if (this.mSource == null || this.mSource.length() == 0) {
            return false;
        }
        this.mIsBuildCtrlData = true;
        Convert_EngineContent_Spanned();
        this.mIsBuildCtrlData = false;
        return true;
    }

    public boolean loadMeaningWithMode(int dicType, String keyword, int suid, int dispMode) {
        this.mSource = this.mEngine.getMeaning(keyword, suid, dicType, dispMode, BUFFERSIZE_MEAN, true, true, 0);
        this.mCurDspMode = dispMode;
        this.mAvailableMode = dispMode;
        if (this.mSource.length() == 0) {
            return false;
        }
        this.mDspIdxList.clear();
        this.mIDList.clear();
        Convert_EngineContent_Spanned();
        return true;
    }

    public boolean loadMeaningBySource(int dicType, String source) {
        this.mSource = source;
        this.mCurDspMode = 127;
        if (this.mSource.length() == 0) {
            return false;
        }
        this.mIsBuildCtrlData = true;
        Convert_EngineContent_Spanned();
        this.mIsBuildCtrlData = false;
        return true;
    }

    public Spanned getKeyFieldSpan() {
        return this.mKeywordSpannableBuilder;
    }

    public Spanned getMeanFieldSpan() {
        return this.mContentSpannableBuilder;
    }

    public boolean updateDispalyMode(int displayMode) {
        if ((this.mAvailableMode & displayMode) == 0) {
            return false;
        }
        this.mCurDspMode = displayMode;
        Convert_EngineContent_Spanned();
        return true;
    }

    public int getFragmentOffset(String ref_id) {
        if (ref_id == null || ref_id.length() == 0) {
            return 0;
        }
        int size = this.mIDList.size();
        int idx = 0;
        while (idx < size) {
            int idx2 = idx + 1;
            HashMap<Integer, Object> item = this.mIDList.get(idx);
            if (ref_id.equals(item.get(0))) {
                int goFragmentOffset = ((Integer) item.get(1)).intValue();
                return goFragmentOffset;
            }
            idx = idx2;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isEmptyTag(int tag) {
        switch (tag) {
            case 5:
            case 10:
            case 12:
            case 15:
                return true;
            default:
                return false;
        }
    }

    public void clearSpannableBuilder() {
        if ((this.mCurDspMode & 112) != 0) {
            this.mKeywordSpannableBuilder.clear();
        }
        if ((this.mCurDspMode & 15) != 0) {
            this.mContentSpannableBuilder.clear();
        }
    }

    private void Convert_EngineContent_Spanned() {
		try {
			Convert_EngineContent_Spanned_real();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void Convert_EngineContent_Spanned_real() {
		int tag;
		this.mSpannableBuilder = this.mKeywordSpannableBuilder;
		if (this.mSource == null || this.mSource.length() == 0) {
			clearSpannableBuilder();
			return;
		}
		this.mSrcIndex = 0;
		this.mDstIndex = this.mSrcIndex;
		if (this.mIsBuildCtrlData) {
			this.mAvailableMode = 0;
			this.mDspIdxList.clear();
			this.mIDList.clear();
		}
		int endPoint = this.mSource.length();
		this.mIsStyleClose = false;
		if ((this.mCurDspMode & 112) == 0 && endPoint > this.mSrcIndex) {
			this.mSrcIndex = this.mSource.indexOf("%M", this.mSrcIndex);
			this.mSpannableBuilder = this.mContentSpannableBuilder;
		}
		while (this.mSrcIndex < endPoint && this.mSource.codePointAt(this.mSrcIndex) == 10) {
			addLeftIntent();
			this.mSrcIndex++;
		}
		clearSpannableBuilder();
		this.mStringBuilder.delete(0, this.mStringBuilder.length());
		this.mLinkIdxList.clear();
		while (this.mSrcIndex < endPoint) {
			if (this.mSource.length() > this.mSrcIndex && this.mSource.codePointAt(this.mSrcIndex) == 37 && (tag = getTagValue(this.mSource, this.mSrcIndex + 1)) > -1 && this.mSrcIndex < endPoint) {
				int value = this.mSource.codePointAt(this.mSrcIndex + 1);
				if (value > 64 && value < 91) {
					int value2 = value - 65;
					this.mSrcIndex++;
					this.mSrcIndex += this.mTagLength[value2];
					if (mTagDspMask[value2] != 0 || this.mStyleStack.isEmpty() || (this.mStyleStack.peek().dspMode & this.mCurDspMode) != 0) {
						try {
							this.mSpannableBuilder.append((CharSequence) this.mStringBuilder);
						} catch (Exception e) {
							e.printStackTrace();
						}
						this.mStringBuilder.delete(0, this.mStringBuilder.length());
						this.mTagHandler[value2].handleTag_Start(tag);
					}
				} else if (value > 96 && value < 123) {
					int value3 = value - 97;
					this.mSrcIndex++;
					this.mSrcIndex += this.mTagLength[value3];
					if ((this.mStyleStack.peek().dspMode & this.mCurDspMode) != 0 || mTagDspMask[value3] != 0) {
						try {
							this.mSpannableBuilder.append((CharSequence) this.mStringBuilder);
						} catch (Exception e2) {
							e2.printStackTrace();
						}
						this.mStringBuilder.delete(0, this.mStringBuilder.length());
						this.mTagHandler[value3].handleTag_End(tag);
						StyleStack_Pop(tag);
					}
				}
			} else {
				if (this.mSrcIndex >= 0 && this.mSrcIndex < endPoint && this.mSource.length() > this.mSrcIndex && this.mSource.codePointAt(this.mSrcIndex) == 10) {
					addLeftIntent();
					if (this.mStringBuilder.length() > 0) {
						if (this.mStringBuilder.charAt(this.mStringBuilder.length() - 1) == '\n') {
							this.mSrcIndex++;
						}
					} else if (this.mDstIndex > 0 && this.mSpannableBuilder.charAt(this.mSpannableBuilder.length() - 1) == '\n') {
						this.mSrcIndex++;
					}
				}
				if (!this.mStyleStack.empty() && (this.mStyleStack.peek().dspMode & this.mCurDspMode) > 0) {
					if (this.mSource.length() > this.mSrcIndex && this.mSrcIndex < endPoint) {
						this.mStringBuilder.appendCodePoint(this.mSource.codePointAt(this.mSrcIndex));
					}
					this.mDstIndex++;
				}
				this.mSrcIndex++;
			}
		}
		if (this.mStringBuilder.length() > 0) {
			try {
				this.mSpannableBuilder.append((CharSequence) this.mStringBuilder);
			} catch (Exception e3) {
				e3.printStackTrace();
			}
			this.mStringBuilder.delete(0, this.mStringBuilder.length());
		}
		while (!this.mStyleStack.isEmpty()) {
			try {
				int tag2 = this.mStyleStack.peek().tag;
				this.mTagHandler[getTagIndex(tag2)].handleTag_End(tag2);
				StyleStack_Pop(tag2);
				if (tag2 != 12) {
					MSG.l(2, "[TagConverter]Stack Sync Error" + tag2);
					MSG.l(2, "[TagConverter]" + this.mKeywordSpannableBuilder.toString());
				}
			} catch (EmptyStackException e4) {
				e4.printStackTrace();
			}
		}
		if (this.mIsBuildCtrlData) {
			int size = this.mDspIdxList.size();
			for (int idx = 0; idx < size; idx++) {
				this.mAvailableMode = this.mDspIdxList.get(idx).display_mask | this.mAvailableMode;
			}
		}
		addLeftIntent();
		
	}
	
	public void addLeftIntent() {
	}

    public int getAvailableDispMode() {
        return this.mAvailableMode;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public ArrayList<DictPos> convert_Partial_Index(int dspMode, int start, int end) {
        ArrayList<DictPos> tPosList = new ArrayList<>();
        int idx = 0;
        int current_start = 0;
        if ((this.mAvailableMode & dspMode) != 0) {
            switch (dspMode) {
                case 15:
                    DictPos tPosItem = new DictPos();
                    tPosItem.start = start;
                    tPosItem.end = end;
                    tPosList.add(tPosItem);
                    break;
                default:
                    while (idx < this.mDspIdxList.size()) {
                        int idx2 = idx + 1;
                        DictPos pItem = this.mDspIdxList.get(idx);
                        if (pItem.display_mask == dspMode) {
                            int tStart = pItem.start;
                            if (tStart > end) {
                                break;
                            } else {
                                int tEnd = pItem.end;
                                int current_end = ((current_start + tEnd) - tStart) + 1;
                                if (tEnd < start) {
                                    current_start = current_end;
                                    idx = idx2;
                                } else {
                                    DictPos tPos = new DictPos();
                                    if (start < tStart) {
                                        tPos.start = current_start;
                                    } else {
                                        tPos.start = (current_start + start) - tStart;
                                    }
                                    if (end > tEnd) {
                                        tPos.end = current_end;
                                        start = tEnd + 1;
                                    } else {
                                        tPos.end = (end - tStart) + current_start;
                                    }
                                    tPosList.add(tPos);
                                    current_start = current_end;
                                    idx = idx2;
                                }
                            }
                        } else {
                            idx = idx2;
                        }
                    }
                    break;
            }
        }
        return tPosList;
    }

    public ArrayList<DictPos> getLinkIndexList() {
        return this.mLinkIdxList;
    }

    public DictPos isLinkOffset(int off) {
        for (int i = 0; i < this.mLinkIdxList.size(); i++) {
            DictPos pos = this.mLinkIdxList.get(i);
            if (pos.start <= off && off <= pos.end) {
                return pos;
            }
        }
        return null;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public ArrayList<DictPos> convert_Total_Index(int dspMode, int start, int end) {
        ArrayList<DictPos> tPosList = new ArrayList<>();
        int idx = 0;
        int current_start = 0;
        if ((this.mAvailableMode & dspMode) == 0) {
            Log.e("TagConverter", "convert_Total_Index() Wrong display mode");
        } else {
            switch (dspMode) {
                case 15:
                    DictPos tPosItem = new DictPos();
                    tPosItem.start = start;
                    tPosItem.end = end;
                    tPosList.add(tPosItem);
                    break;
                default:
                    while (idx < this.mDspIdxList.size()) {
                        int idx2 = idx + 1;
                        DictPos pItem = this.mDspIdxList.get(idx);
                        if (pItem.display_mask == dspMode) {
                            int current_end = ((pItem.end + current_start) - pItem.start) + 1;
                            if (start > current_end) {
                                current_start = current_end;
                                idx = idx2;
                            } else {
                                DictPos tPos = new DictPos();
                                tPos.start = (pItem.start + start) - current_start;
                                if (end < current_end) {
                                    tPos.end = (pItem.start + end) - current_start;
                                } else {
                                    tPos.end = (pItem.start + current_end) - current_start;
                                    start = current_end;
                                }
                                tPosList.add(tPos);
                                if (end < current_end) {
                                    break;
                                } else {
                                    current_start = current_end;
                                    idx = idx2;
                                }
                            }
                        } else {
                            idx = idx2;
                        }
                    }
                    break;
            }
        }
        return tPosList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTagValue(String source, int index) {
        int tag;
        int tag2 = source.codePointAt(index);
        if (tag2 > 64 && tag2 < 91) {
            tag = tag2 - 65;
        } else if (tag2 <= 96 || tag2 >= 123 || this.mStyleStack.isEmpty()) {
            return -1;
        } else {
            tag = tag2 - 97;
        }
        int validIndex = this.mTagSubIndex[tag];
        if (validIndex == 0) {
            return tag;
        }
        int len = this.mTagLength[tag] - 1;
        int subIndex = 0;
        int index2 = index + 1;
        while (true) {
            int len2 = len;
            int index3 = index2;
            len = len2 - 1;
            if (len2 > 0) {
                int value = source.codePointAt(index3);
                if (value < 48 || value > 57) {
                    break;
                }
                index2 = index3 + 1;
                subIndex = ((subIndex * 10) + source.codePointAt(index3)) - 48;
            } else {
                int tag3 = (tag << 8) | subIndex;
                if (subIndex > validIndex) {
                    return -1;
                }
                return tag3;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void StyleStack_Push(int tag, int f_Color, int dspMode) {
        StyleStackItem tItem = new StyleStackItem();
        tItem.tag = tag;
        if (this.mStyleStack.isEmpty()) {
            tItem.dspMode = dspMode;
            tItem.f_Color = f_Color;
        } else {
            StyleStackItem topItem = this.mStyleStack.peek();
            if ((this.mCurDspMode & topItem.dspMode) > 0) {
                int tag_index = getTagIndex(topItem.tag);
                StyleEnd(topItem, f_Color);
                tItem.dspMode = dspMode != 15 ? dspMode : topItem.dspMode;
                tItem.f_Color = f_Color != -1 ? f_Color : topItem.f_Color;
                if (tag_index == 8 || tag_index == 1) {
                    StyleStackItem topItem2 = this.mStyleStack.pop();
                    StyleStackItem parentTopItem = this.mStyleStack.peek();
                    if (parentTopItem != null && getTagIndex(parentTopItem.tag) == 19) {
                        this.mIs2DepthStyle = true;
                        StyleEnd(parentTopItem, f_Color);
                        if (dspMode == 15) {
                            dspMode = topItem2.dspMode;
                        }
                        tItem.dspMode = dspMode;
                        if (f_Color == -1) {
                            f_Color = topItem2.f_Color;
                        }
                        tItem.f_Color = f_Color;
                    }
                    this.mStyleStack.push(topItem2);
                }
            } else {
                if (dspMode == 15) {
                    dspMode = topItem.dspMode;
                }
                tItem.dspMode = dspMode;
                if (f_Color == -1) {
                    f_Color = topItem.f_Color;
                }
                tItem.f_Color = f_Color;
            }
        }
        tItem.start = this.mDstIndex;
        this.mStyleStack.push(tItem);
    }

    private void StyleEnd(StyleStackItem topItem, int f_Color) {
        if (f_Color != -1 && topItem.f_Color != f_Color && topItem.start != -1) {
            int tag_index = getTagIndex(topItem.tag);
            this.mIsStyleClose = true;
            this.mTagHandler[tag_index].handleTag_End(topItem.tag);
            this.mIsStyleClose = false;
            topItem.start = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void StyleStack_Pop(int tag) {
        if (this.mStyleStack.isEmpty()) {
            Log.d("TagConverter", "StyleStack_Pop error " + tag);
            return;
        }
        this.mStyleStack.pop();
        if (!this.mStyleStack.isEmpty() && this.mStyleStack.peek().start == -1) {
            this.mStyleStack.peek().start = this.mDstIndex;
            if (this.mIs2DepthStyle) {
                this.mIs2DepthStyle = false;
                StyleStackItem tempItem = this.mStyleStack.pop();
                if (!this.mStyleStack.isEmpty() && this.mStyleStack.peek().start == -1) {
                    this.mStyleStack.peek().start = this.mDstIndex;
                }
                this.mStyleStack.push(tempItem);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void assertLineFeed() {
        if (this.mDstIndex > 0 && this.mSpannableBuilder.charAt(this.mDstIndex - 1) != '\n') {
            this.mSpannableBuilder.append('\n');
            this.mDstIndex++;
        }
        int len = this.mSource.length();
        while (this.mSrcIndex < len) {
            if (this.mSource.codePointAt(this.mSrcIndex) == 10 || this.mSource.codePointAt(this.mSrcIndex) == 32) {
                this.mSrcIndex++;
            } else {
                return;
            }
        }
    }

    private void assertWideLineFeed() {
        if (this.mDstIndex > 0 && this.mSpannableBuilder.charAt(this.mDstIndex - 1) == '\n') {
            this.mSpannableBuilder.append('\n');
            this.mDstIndex++;
        } else if (this.mDstIndex > 0 && this.mSpannableBuilder.charAt(this.mDstIndex - 1) != '\n') {
            this.mSpannableBuilder.append('\n');
            this.mSpannableBuilder.append('\n');
            this.mDstIndex += 2;
        }
        int len = this.mSource.length();
        while (this.mSrcIndex < len) {
            if (this.mSource.codePointAt(this.mSrcIndex) == 10 || this.mSource.codePointAt(this.mSrcIndex) == 32) {
                this.mSrcIndex++;
            } else {
                return;
            }
        }
    }

    public void BlockTag_Start(int tag, int f_Color) {
        BlockTag_Start(tag, f_Color, true);
    }

    public void WideBlockTag_Start(int tag, int f_Color) {
        assertWideLineFeed();
        BlockTag_Start(tag, f_Color, false);
    }

    public void BlockTag_Start(int tag, int f_Color, boolean assertLinefeed) {
        if (assertLinefeed) {
            assertLineFeed();
        }
        int value = tag > 255 ? tag >> 8 : tag;
        int curDspMask = mTagDspMask[value];
        if (tag == 2824 || tag == 2819) {
            curDspMask = 1;
        }
        if (!this.mStyleStack.isEmpty()) {
            switch (this.mStyleStack.peek().dspMode) {
                case 1:
                    break;
                case 2:
                    if (curDspMask == 4) {
                        curDspMask = 2;
                        break;
                    }
                    break;
                default:
                    curDspMask = this.mStyleStack.peek().dspMode;
                    break;
            }
        }
        StyleStack_Push(tag, f_Color, curDspMask);
        DspIdxList_Add(curDspMask, true);
    }

    public void BlockTag_End(int tag) {
        BlockTag_End(tag, true);
    }

    public void WideBlockTag_End(int tag) {
        assertWideLineFeed();
        BlockTag_End(tag, false);
    }

    public void BlockTag_End(int tag, boolean assertLinefeed) {
        if (assertLinefeed) {
            assertLineFeed();
        }
        if (!this.mIsStyleClose) {
            DspIdxList_Add(0, false);
        }
    }

    public void DspIdxList_Add(int dsp_mode, boolean is_start) {
        if (this.mIsBuildCtrlData) {
            if (is_start) {
                if (this.mDspIdxList.get(this.mDspIdxList.size() - 1).start == this.mDstIndex) {
                    if (this.mDspIdxList.size() > 1 && this.mDspIdxList.get(this.mDspIdxList.size() - 2).display_mask == dsp_mode) {
                        this.mDspIdxList.remove(this.mDspIdxList.size() - 1);
                    } else {
                        this.mDspIdxList.get(this.mDspIdxList.size() - 1).display_mask = dsp_mode;
                    }
                } else if (this.mDspIdxList.get(this.mDspIdxList.size() - 1).display_mask != dsp_mode) {
                    this.mDspIdxList.get(this.mDspIdxList.size() - 1).end = this.mDstIndex - 1;
                    DictPos item = new DictPos();
                    item.display_mask = dsp_mode;
                    item.start = this.mDstIndex;
                    this.mDspIdxList.add(item);
                }
            } else {
				int v1 = this.mDspIdxList.size() - 1<0?-100 : this.mDspIdxList.get(this.mDspIdxList.size() - 1).display_mask;
				int v2 = this.mStyleStack.size() - 2<0?-100 :  this.mStyleStack.get(this.mStyleStack.size() - 2).dspMode;
				if (v1 != v2 && v1!=-100 && v2!=-100) {
					this.mDspIdxList.get(this.mDspIdxList.size() - 1).end = this.mDstIndex - 1;
					DictPos item2 = new DictPos();
					item2.display_mask = this.mStyleStack.get(this.mStyleStack.size() - 2).dspMode;
					item2.start = this.mDstIndex;
					this.mDspIdxList.add(item2);
				}
			}
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTagIndex(int tag) {
        if (tag > 255) {
            int tempTag = tag >> 8;
            return tempTag;
        }
        return tag;
    }

    public int setTheme(int theme) {
        this.mCurrentTheme = theme;
        switch (theme) {
            case 1:
                this.mBaseTheme = ThemeColor.baseTheme2;
                this.mAdvTheme = ThemeColor.advTheme2;
                break;
            case 2:
                this.mBaseTheme = ThemeColor.baseTheme3;
                this.mAdvTheme = ThemeColor.advTheme3;
                break;
            default:
                this.mBaseTheme = ThemeColor.baseTheme;
                this.mAdvTheme = ThemeColor.advTheme;
                break;
        }
        int nMeanColor = this.mBaseTheme[4];
        return nMeanColor;
    }

    public int getMeanColor() {
        return this.mBaseTheme[4];
    }

    public int getTheme() {
        if (this.mCurrentTheme >= 3 || this.mCurrentTheme <= 0) {
            return 0;
        }
        return this.mCurrentTheme;
    }
}
