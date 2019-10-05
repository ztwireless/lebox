package com.mgc.letobox.happy.circle.adapter;

public interface IDataAdapter<DATA> {
    void notifyDataChanged(DATA var1, boolean var2);

    DATA getData();

    boolean isEmpty();
}