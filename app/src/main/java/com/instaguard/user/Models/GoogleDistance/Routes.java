package com.instaguard.user.Models.GoogleDistance;

import java.util.ArrayList;

public class Routes extends Legs{

    ArrayList<Legs> legs = null;

    public ArrayList<Legs> getLegs() {
        return legs;
    }

    public void setLegs(ArrayList<Legs> legs) {
        this.legs = legs;
    }
}