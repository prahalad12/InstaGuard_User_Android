package com.instaguard.user.Models.GoogleDistance;

public class Legs  {

    Distances distance;
    Durations duration;

    public Distances getDistance() {
        return distance;
    }

    public void setDistance(Distances distance) {
        this.distance = distance;
    }

    public Durations getDuration() {
        return duration;
    }

    public void setDuration(Durations duration) {
        this.duration = duration;
    }

    public class Distances{
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        String text;
        double value;

    }

    public class Durations{
        String text;
        double value;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }

}