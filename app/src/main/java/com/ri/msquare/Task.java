package com.ri.msquare;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Suresh on 31-10-2016.
 */

public class Task implements Parcelable {

    String id;
    String name;
    String status;
    String startDate;
    String endDate;
    String totalHours;
    String currentHours;

    public String getCurrentHours() {
        return currentHours;
    }

    public Task setCurrentHours(String currentHours) {
        this.currentHours = currentHours;
        return this;
    }

    public String getEndDate() {
        return endDate;
    }

    public Task setEndDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getId() {
        return id;
    }

    public Task setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Task setName(String name) {
        this.name = name;
        return this;
    }

    public String getStartDate() {
        return startDate;
    }

    public Task setStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Task setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getTotalHours() {
        return totalHours;
    }

    public Task setTotalHours(String totalHours) {
        this.totalHours = totalHours;
        return this;
    }

    @Override
    public String toString() {
        return "Task{" +
                "endDate='" + endDate + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", startDate='" + startDate + '\'' +
                ", totalHours=" + totalHours +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.status);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeString(this.totalHours);
        dest.writeString(this.currentHours);
    }

    public Task() {
    }

    protected Task(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.status = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.totalHours = in.readString();
        this.currentHours = in.readString();
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
