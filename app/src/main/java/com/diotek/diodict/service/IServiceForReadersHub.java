package com.diotek.diodict.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IServiceForReadersHub extends IInterface {
    boolean SetSearchList(String str, int i) throws RemoteException;

    boolean changeDicType(int i) throws RemoteException;

    String getFontPath() throws RemoteException;

    String getKeyword() throws RemoteException;

    int getKeywordPosition() throws RemoteException;

    int getSuid() throws RemoteException;

    int[] getSuidList() throws RemoteException;

    List<String> getWordList() throws RemoteException;

    String getWordMean(String str, int i) throws RemoteException;

    String getWordMeanWithSUID(int i, int i2) throws RemoteException;

    boolean isDBExactMatching() throws RemoteException;

    boolean isDBExisting() throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IServiceForReadersHub {
        private static final String DESCRIPTOR = "com.diotek.diodict.service.IServiceForReadersHub";
        static final int TRANSACTION_SetSearchList = 6;
        static final int TRANSACTION_changeDicType = 1;
        static final int TRANSACTION_getFontPath = 11;
        static final int TRANSACTION_getKeyword = 4;
        static final int TRANSACTION_getKeywordPosition = 9;
        static final int TRANSACTION_getSuid = 5;
        static final int TRANSACTION_getSuidList = 8;
        static final int TRANSACTION_getWordList = 7;
        static final int TRANSACTION_getWordMean = 2;
        static final int TRANSACTION_getWordMeanWithSUID = 3;
        static final int TRANSACTION_isDBExactMatching = 12;
        static final int TRANSACTION_isDBExisting = 10;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IServiceForReadersHub asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IServiceForReadersHub)) {
                return (IServiceForReadersHub) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    boolean _result = changeDicType(_arg0);
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    int _arg1 = data.readInt();
                    String _result2 = getWordMean(_arg02, _arg1);
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    int _arg12 = data.readInt();
                    String _result3 = getWordMeanWithSUID(_arg03, _arg12);
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _result4 = getKeyword();
                    reply.writeNoException();
                    reply.writeString(_result4);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _result5 = getSuid();
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    int _arg13 = data.readInt();
                    boolean _result6 = SetSearchList(_arg04, _arg13);
                    reply.writeNoException();
                    if (_result6) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _result7 = getWordList();
                    reply.writeNoException();
                    reply.writeStringList(_result7);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _result8 = getSuidList();
                    reply.writeNoException();
                    reply.writeIntArray(_result8);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _result9 = getKeywordPosition();
                    reply.writeNoException();
                    reply.writeInt(_result9);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result10 = isDBExisting();
                    reply.writeNoException();
                    if (_result10) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _result11 = getFontPath();
                    reply.writeNoException();
                    reply.writeString(_result11);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result12 = isDBExactMatching();
                    reply.writeNoException();
                    if (_result12) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IServiceForReadersHub {
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

            @Override // com.diotek.diodict.service.IServiceForReadersHub
            public boolean changeDicType(int dicType) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(dicType);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.diotek.diodict.service.IServiceForReadersHub
            public String getWordMean(String word, int dicType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(word);
                    _data.writeInt(dicType);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.diotek.diodict.service.IServiceForReadersHub
            public String getWordMeanWithSUID(int suid, int dicType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(suid);
                    _data.writeInt(dicType);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.diotek.diodict.service.IServiceForReadersHub
            public String getKeyword() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.diotek.diodict.service.IServiceForReadersHub
            public int getSuid() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.diotek.diodict.service.IServiceForReadersHub
            public boolean SetSearchList(String word, int dicType) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(word);
                    _data.writeInt(dicType);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.diotek.diodict.service.IServiceForReadersHub
            public List<String> getWordList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.diotek.diodict.service.IServiceForReadersHub
            public int[] getSuidList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.diotek.diodict.service.IServiceForReadersHub
            public int getKeywordPosition() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.diotek.diodict.service.IServiceForReadersHub
            public boolean isDBExisting() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.diotek.diodict.service.IServiceForReadersHub
            public String getFontPath() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.diotek.diodict.service.IServiceForReadersHub
            public boolean isDBExactMatching() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
