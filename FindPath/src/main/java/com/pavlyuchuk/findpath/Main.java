package com.pavlyuchuk.findpath;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import com.pavlyuchuk.findpath.model.Bus;
import com.pavlyuchuk.findpath.model.Direction;
import com.pavlyuchuk.findpath.model.Schedule;
import com.pavlyuchuk.findpath.model.Stop;
import com.pavlyuchuk.findpath.model.Trip;

public class Main
{

    public static void main(String[] args)
    {
        Set<String> routeNames = new HashSet<>();

        Map<Integer, Stop> stops = new HashMap<>();
        Map<Integer, Direction> directions = new HashMap<>();

        List<Bus> buses = new ArrayList<>();

        try (Scanner sc = new Scanner(new File("rasp.csv"));)
        {
            String cLine;
            String[] splitString;
            Integer routeId = 1;
            int countTravels;

            String busName;
            String directionsName;
            String stopName;
            String day;
            Direction direction = new Direction();
            Integer stopId;

            while (sc.hasNextLine())
            {
                cLine = sc.nextLine();
                splitString = cLine.split(";");

                countTravels = (splitString.length - 1) - 4;

                ArrayList<Calendar[]> travelMatrix = new ArrayList<>();

                busName = splitString[0];
                day = splitString[1];
                directionsName = splitString[2];
                stopName = splitString[3];
                stopId = Integer.parseInt(splitString[4]);
                boolean busExist = false;

                if (!routeNames.contains(directionsName))
                {
                    direction = new Direction(routeId, directionsName);
                    directions.put(routeId, direction);
                    routeNames.add(directionsName);
                    routeId++;
                }
                else
                {
                    for (Direction directionIterator : directions.values())
                    {
                        if (directionsName.equals(directionIterator.getName()))
                        {
                            direction = directionIterator;
                        }
                    }
                }

                if (stops.get(stopId) == null)
                {
                    stops.put(stopId, new Stop(stopId, stopName));
                }

                Bus bus = null;

                for (Bus bus1 : buses)
                {
                    if (busName.equals(bus1.getNumber()) && directionsName.equals(bus1.getDirection().getName()))
                    {
                        bus = bus1;
                        busExist = true;
                        break;
                    }
                }

                if (!busExist)
                {
                    bus = new Bus();
                    bus.setNumber(busName);
                    bus.setDirection(direction);
                    bus.getRoute().getRoute().add(stops.get(stopId));
                }

                Calendar[] travels = new Calendar[countTravels];

                for (int i = 5; i < splitString.length; i++)
                {
                    if (splitString[i].equals("null"))
                    {
                        travels[i - 5] = null;
                    }
                    else
                    {
                        String[] splitTime = splitString[i].split(":");
                        travels[i - 5] = Calendar.getInstance();
                        travels[i - 5].set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTime[0]));
                        travels[i - 5].set(Calendar.MINUTE, Integer.parseInt(splitTime[1]));
                    }
                }

                travelMatrix.add(travels);

                while ((cLine = sc.nextLine()).length() != 0)
                {
                    splitString = cLine.split(";");

                    if (!busExist)
                    {
                        stopName = splitString[3];
                        stopId = Integer.parseInt(splitString[4]);

                        if (stops.get(stopId) == null)
                        {
                            stops.put(stopId, new Stop(stopId, stopName));
                        }

                        bus.getRoute().getRoute().add(stops.get(stopId));
                    }
                    travels = new Calendar[countTravels];

                    for (int i = 5; i < splitString.length; i++)
                    {
                        if (splitString[i].equals("null"))
                        {
                            travels[i - 5] = null;
                        }
                        else
                        {
                            String[] splitTime = splitString[i].split(":");
                            travels[i - 5] = Calendar.getInstance();
                            travels[i - 5].set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTime[0]));
                            travels[i - 5].set(Calendar.MINUTE, Integer.parseInt(splitTime[1]));
                        }
                    }
                    travelMatrix.add(travels);
                }
                List<Trip> trips = new ArrayList<Trip>();
                for (int j = 0; j < countTravels; j++)
                {
                    Trip trip = new Trip();
                    for (int i = 0; i < travelMatrix.size(); i++)
                    {
                        trip.getTrip().add(travelMatrix.get(i)[j]);
                        /*
                         * if (travelMatrix.get(i)[j] != null) {
                         * System.out.print(travelMatrix.get(i)[j].get(Calendar.HOUR_OF_DAY) + ":" +
                         * travelMatrix.get(i)[j].get(Calendar.MINUTE) + " "); } else { System.out.print("null "); }
                         */
                    }
                    trips.add(trip);
                    // System.out.println();
                }
                // System.out.println();
                
                if(day.equals("Р"))
                {
                    bus.getSchedule().setMonday(trips);
                    bus.getSchedule().setTuesday(trips);
                    bus.getSchedule().setWednesday(trips);
                    bus.getSchedule().setThursday(trips);
                    bus.getSchedule().setFriday(trips);
                }
                
                if(day.equals("В"))
                {
                    bus.getSchedule().setSunday(trips);
                    bus.getSchedule().setSaturday(trips);
                }
                
                if(day.equals("Р,В"))
                {
                    bus.getSchedule().setMonday(trips);
                    bus.getSchedule().setTuesday(trips);
                    bus.getSchedule().setWednesday(trips);
                    bus.getSchedule().setThursday(trips);
                    bus.getSchedule().setFriday(trips);
                    bus.getSchedule().setSunday(trips);
                    bus.getSchedule().setSaturday(trips);
                }

                if (!busExist)
                {
                    buses.add(bus);
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        Scanner sc = new Scanner(System.in);
        sc.next();
        
        for (Bus bus2 : buses)
        {
            System.out.println(bus2.getNumber() + " " + bus2.getDirection().getName());
          /*  for (Bus bus2 : buses)
            {
                
            }*/
        }
        /*
         * for (Stop stop : stops.values()) { System.out.println(stop.getId() + " " + stop.getName()); } for (Direction
         * direction : directions.values()) { System.out.println(direction.getId() + " " + direction.getName()); } for
         * (Bus bus : buses) { for (Stop stop : bus.getRoute().getRoute()) { System.out.println(bus.getNumber() + " " +
         * bus.getDirection().getId() + " " + stop.getName() + " " + stop.getId()); } }
         */
    }
}
