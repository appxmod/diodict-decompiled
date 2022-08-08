package com.nemustech.tiffany.world;

import android.util.Log;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.StringTokenizer;

/* loaded from: classes.dex */
public class TFLua {
    public static final int LUA_GCCOLLECT = 2;
    public static final int LUA_GCCOUNT = 3;
    public static final int LUA_GCCOUNTB = 4;
    public static final int LUA_GCRESTART = 1;
    public static final int LUA_GCSETPAUSE = 6;
    public static final int LUA_GCSETSTEPMUL = 7;
    public static final int LUA_GCSTEP = 5;
    public static final int LUA_GCSTOP = 0;
    private static final String TAG = "TFLua";
    public final int mL = n_newLua();

    public static native synchronized void cm_dump_stack(int i);

    public static native synchronized Object cm_invoke_proxy(int i, int i2, Method method, Object[] objArr) throws Throwable;

    public static native synchronized void cm_unref_proxy(int i, int i2);

    public static native synchronized int lua_gc(int i, int i2, int i3);

    private static native synchronized void n_addObject(int i, String str, Object obj);

    private static native synchronized void n_closeLua(int i);

    private static native synchronized int n_newLua();

    private static native synchronized void n_removeObject(int i, String str);

    private static native synchronized int n_runString(int i, String str);

    protected void finalize() throws Throwable {
        super.finalize();
        Log.i(TAG, "finalized " + toString());
        n_closeLua(this.mL);
    }

    public int runString(String code) {
        return n_runString(this.mL, code);
    }

    public void addObject(String name, Object o) {
        n_addObject(this.mL, name, o);
    }

    public void removeObject(String name) {
        n_removeObject(this.mL, name);
    }

    public void addTFObject(String name, TFObject o) {
        addObject(name, o);
    }

    public void addTFWorld(String name, TFWorld world) {
        addObject(name, world);
    }

    /* loaded from: classes.dex */
    public static class Invocation implements InvocationHandler {
        public final int mL;
        public final int mRef;

        public Invocation(int L, int ref) {
            this.mL = L;
            this.mRef = ref;
        }

        protected void finalize() throws Throwable {
            super.finalize();
            TFLua.cm_unref_proxy(this.mL, this.mRef);
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return TFLua.cm_invoke_proxy(this.mL, this.mRef, method, args);
        }
    }

    public static synchronized Object createProxy(int L, int ref, String interfaces) throws Throwable {
        Object newProxyInstance;
        synchronized (TFLua.class) {
            String clspaths = interfaces.replace('/', '.');
            StringTokenizer st = new StringTokenizer(clspaths, ", ");
            int count = st.countTokens();
            Class<?>[] classes = new Class[count];
            for (int i = 0; i < count; i++) {
                classes[i] = Class.forName(st.nextToken());
            }
            Invocation i2 = new Invocation(L, ref);
            newProxyInstance = Proxy.newProxyInstance(i2.getClass().getClassLoader(), classes, i2);
        }
        return newProxyInstance;
    }

    static {
        System.loadLibrary("tflua");
    }
}
