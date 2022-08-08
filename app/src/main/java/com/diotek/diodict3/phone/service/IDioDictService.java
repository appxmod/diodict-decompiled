package com.diotek.diodict3.phone.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.Map;

/* loaded from: classes.dex */
public interface IDioDictService extends IInterface {
    String getFontPath() throws RemoteException;

    int getMeanTextColor() throws RemoteException;

    /* renamed from: getMeaning */
    Map mo2getMeaning(int i, String str, int i2) throws RemoteException;

    /* renamed from: getWordList */
    Map mo3getWordList(int i, String str, boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IDioDictService {
        private static final String DESCRIPTOR = "com.diotek.diodict3.phone.service.IDioDictService";
        static final int TRANSACTION_getFontPath = 3;
        static final int TRANSACTION_getMeanTextColor = 4;
        static final int TRANSACTION_getMeaning = 2;
        static final int TRANSACTION_getWordList = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDioDictService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IDioDictService)) {
                return (IDioDictService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    boolean _arg2 = data.readInt() != 0;
                    Map _result = getWordList(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeMap(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    String _arg12 = data.readString();
                    int _arg22 = data.readInt();
                    Map _result2 = getMeaning(_arg02, _arg12, _arg22);
                    reply.writeNoException();
                    reply.writeMap(_result2);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _result3 = getFontPath();
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _result4 = getMeanTextColor();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IDioDictService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.diotek.diodict3.phone.service.IDioDictService
            /* renamed from: getWordList */
            public Map mo3getWordList(int dicType, String inputWord, boolean isGetWordList) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(dicType);
                    _data.writeString(inputWord);
                    if (!isGetWordList) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    Map _result = _reply.readHashMap(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.diotek.diodict3.phone.service.IDioDictService
            /* renamed from: getMeaning */
            public Map mo2getMeaning(int dicType, String keyword, int suid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(dicType);
                    _data.writeString(keyword);
                    _data.writeInt(suid);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    Map _result = _reply.readHashMap(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.diotek.diodict3.phone.service.IDioDictService
            public String getFontPath() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.diotek.diodict3.phone.service.IDioDictService
            public int getMeanTextColor() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
