package com.pavlyuchuk.findpath.model;

import java.util.ArrayList;
import java.util.List;

public class Route
{
    private int id;
    private List<Stop> route = new ArrayList<Stop>();

    public Route()
    {

    }

    public final int getId()
    {
        return id;
    }

    public final void setId(int id)
    {
        this.id = id;
    }

    public final List<Stop> getRoute()
    {
        return route;
    }

    public final void setRoute(List<Stop> route)
    {
        this.route = route;
    }

}
