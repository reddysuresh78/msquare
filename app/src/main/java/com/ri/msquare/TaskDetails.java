package com.ri.msquare;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Suresh on 31-10-2016.
 */

public class TaskDetails implements Parcelable {

    String id;
    String name;
    String status;
    String startDate;
    String endDate;
    float totalHours;
    float currentHours;

    public float getCurrentHours() {
        return currentHours;
    }

    public TaskDetails setCurrentHours(float currentHours) {
        this.currentHours = currentHours;
        return this;
    }

    public String getEndDate() {
        return endDate;
    }

    public TaskDetails setEndDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getId() {
        return id;
    }

    public TaskDetails setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TaskDetails setName(String name) {
        this.name = name;
        return this;
    }

    public String getStartDate() {
        return startDate;
    }

    public TaskDetails setStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public TaskDetails setStatus(String status) {
        this.status = status;
        return this;
    }

    public float getTotalHours() {
        return totalHours;
    }

    public TaskDetails setTotalHours(float totalHours) {
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
        dest.writeFloat(this.totalHours);
        dest.writeFloat(this.currentHours);
    }

    public TaskDetails() {
    }

    protected TaskDetails(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.status = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.totalHours = in.readFloat();
        this.currentHours = in.readFloat();
    }

    public static final Creator<TaskDetails> CREATOR = new Creator<TaskDetails>() {
        @Override
        public TaskDetails createFromParcel(Parcel source) {
            return new TaskDetails(source);
        }

        @Override
        public TaskDetails[] newArray(int size) {
            return new TaskDetails[size];
        }
    };
}
