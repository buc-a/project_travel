package com.example.project_travel_1.adapters;

import androidx.recyclerview.widget.DiffUtil;

import com.example.project_travel_1.objects.Memory;

import java.util.List;

public class ObjectDiffutilCallback extends DiffUtil.Callback {
    private final List<Memory> oldList;
    private final List<Memory> newList;

    public ObjectDiffutilCallback(List<Memory> oldList, List<Memory> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Memory oldMemory = oldList.get(oldItemPosition);
        Memory newMemory = newList.get(newItemPosition);

        return (oldMemory.getName().equals(newMemory.getName()));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Memory oldMemory = oldList.get(oldItemPosition);
        Memory newMemory = newList.get(newItemPosition);

        return (oldMemory.getPhoto_uri().equals(newMemory.getPhoto_uri())
        && oldMemory.getDescription().equals(newMemory.getDescription()));
    }
}
